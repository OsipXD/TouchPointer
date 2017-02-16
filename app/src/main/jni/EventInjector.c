/*
 * Android Event Injector 
 *
 * Copyright (c) 2013 by Radu Motisan , radu.motisan@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * For more information on the GPL, please go to:
 * http://www.gnu.org/copyleft/gpl.html
 *
 */

#include <string.h>
#include <stdint.h>
#include <jni.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <dirent.h>
#include <time.h>
#include <errno.h>

#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/inotify.h>
#include <sys/limits.h>
#include <sys/poll.h>

#include <linux/fb.h>
#include <linux/kd.h>
#include <linux/input.h>

#include <android/log.h>
#define TAG "EventInjector::JNI"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#include "EventInjector.h"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
 *Debug tools
 */
int g_debug = 0;

void debug(char* szFormat, ...) {
	if (g_debug == 0) {
		return;
	}

	char szBuffer[4096];
	const size_t NUMCHARS = sizeof(szBuffer) / sizeof(szBuffer[0]);
	const int LASTCHAR = NUMCHARS - 1;
	va_list pArgs;
	va_start(pArgs, szFormat);
	vsnprintf(szBuffer, NUMCHARS - 1, szFormat, pArgs);
	va_end(pArgs);
	szBuffer[LASTCHAR] = '\0';

	LOGD("%s", szBuffer);
}

jint Java_ru_endlesscode_touchpointer_injector_Native_enableDebug(JNIEnv* env, jobject thiz, jint enable) {
	g_debug = enable;

	return g_debug;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	debug("eventinterceptor native lib loaded.");

	return JNI_VERSION_1_2; //1_2 1_4
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
	debug("eventinterceptor native lib unloaded.");
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

static struct typedev {
	struct pollfd ufds;
	char* device_path;
	char* device_name;
	uint16_t id;
}* devices = NULL;

struct pollfd* ufds;
static int devicesCount;
struct input_event event;

const char* device_path = "/dev/input";

static int get_index(int id) {
	if (devices == NULL) {
		return -1;
	}

    int i;
    for (i = 0; i < devicesCount; i++) {
    	if (devices[i].id == id) {
            return i;
    	}
    }

    return -1;
}

static int scan_dir(const char* dirname) {
	devicesCount = 0;
	char devname[PATH_MAX], *filename;
	DIR* dir;
	struct dirent* entry;

	dir = opendir(dirname);
	if (dir == NULL) {
		return -1;
	}

	strcpy(devname, dirname);
	filename = devname + strlen(devname);
	*filename++ = '/';
	while ((entry = readdir(dir))) {
		if (entry->d_name[0] == '.' && (entry->d_name[1] == '\0' || (entry->d_name[1] == '.' && entry->d_name[2] == '\0'))) {
			continue;
		}

		strcpy(filename, entry->d_name);
		debug("scan_dir:prepare to open:%s", devname);

		struct typedev* new_devices = realloc(devices, sizeof(devices[0]) * (devicesCount + 1));
		if (new_devices == NULL) {
			debug("out of memory");
			return -1;
		}
		devices = new_devices;

		struct pollfd* new_ufds = realloc(ufds, sizeof(ufds[0]) * (devicesCount + 1));
		if (new_ufds == NULL) {
			debug("out of memory");
			return -1;
		}
		ufds = new_ufds;

		ufds[devicesCount].events = POLLIN;
		devices[devicesCount].ufds.events = POLLIN;
		devices[devicesCount].id = devicesCount;
		devices[devicesCount].device_path = strdup(devname);

		devicesCount++;
	}

	closedir(dir);

	return 0;
}

static int open_device(int id) {
	int index = get_index(id);
    if (index == -1) {
    	return -1;
    }

	debug("open_device prep to open");
	char* device = devices[index].device_path;

	debug("open_device call %s", device);
	
	int fd = open(device, O_RDWR);
	if (fd < 0) {
		devices[index].ufds.fd = -1;
		devices[index].device_name = NULL;
		debug("could not open %s, %s", device, strerror(errno));

		return -1;
	}

	char name[80];

	devices[index].ufds.fd = fd;
	ufds[index].fd = fd;

	name[sizeof(name) - 1] = '\0';
	if (ioctl(fd, EVIOCGNAME(sizeof(name) - 1), &name) < 1) {
		debug("could not get device name for %s, %s", device, strerror(errno));
		name[0] = '\0';
	}

	debug("Device %d: %s: %s", id, device, name);
	devices[index].device_name = strdup(name);

	return 0;
}

int remove_device(int id) {
	int index = get_index(id);
	if (index == -1) {
		return -1;
	}

	int count = devicesCount - index - 1;
	debug("remove device %d", id);
	free(devices[index].device_path);
	free(devices[index].device_name);

	memmove(&devices[index], &devices[index + 1], sizeof(devices[0]) * (count));
	devicesCount--;

	return 0;
}

jint Java_ru_endlesscode_touchpointer_injector_Native_sendEvent(JNIEnv* env, jobject thiz, jint id, uint16_t type, uint16_t code, int32_t value) {
	int index = get_index(id);
	if (index == -1 || devices[index].ufds.fd == -1) {
		return -1;
	}

	int fd = devices[index].ufds.fd;
	debug("SendEvent call (%d, %d, %d, %d)", fd, type, code, value);

	struct input_event event;
	ssize_t ret;
    int version;

    if(fd < 0) {
        debug("could not open %s, %s", devices[index].device_name, strerror(errno));
        return 1;
    }

    if (ioctl(fd, EVIOCGVERSION, &version)) {
        debug("could not get driver version for %s, %s", devices[index].device_name, strerror(errno));
        return 1;
    }

	memset(&event, 0, sizeof(event));
	event.type = type;
	event.code = code;
	event.value = value;

	ret = write(fd, &event, sizeof(event));
	if(ret < (ssize_t) sizeof(event)) {
        debug("write event failed, %s", strerror(errno));
        return -1;
    }

	return 0;
}

jint Java_ru_endlesscode_touchpointer_injector_Native_scanFiles(JNIEnv* env, jobject thiz) {
	int device_count = scan_dir(device_path);
	if (device_count < 0) {
		debug("scan dir failed for %s:", device_path);
		return -1;
	}

	return devicesCount;
}

jstring Java_ru_endlesscode_touchpointer_injector_Native_getPath(JNIEnv* env, jobject thiz, jint id) {
    int index = get_index(id);
    if (index == -1) {
    	return NULL;
    }

	return (*env)->NewStringUTF(env, devices[index].device_path);
}

jstring Java_ru_endlesscode_touchpointer_injector_Native_getName(JNIEnv* env, jobject thiz, jint id) {
    int index = get_index(id);
	if (index == -1 || devices[index].device_name == NULL) {
		return NULL;
	} else {
		return (*env)->NewStringUTF(env, devices[index].device_name);
	}
}

jint Java_ru_endlesscode_touchpointer_injector_Native_openDevice(JNIEnv* env, jobject thiz, jint id) {
	return open_device(id);
}

jint Java_ru_endlesscode_touchpointer_injector_Native_removeDevice(JNIEnv* env, jobject thiz, jint id) {
	return remove_device(id);
}

jint Java_ru_endlesscode_touchpointer_injector_Native_pollDevice(JNIEnv* env, jobject thiz, jint id) {
	int index = get_index(id);
	if (index == -1 || devices[index].ufds.fd == -1) {
		return -1;
	}

	int pollres = poll(ufds, devicesCount, -1);
	if (ufds[index].revents) {
		if (ufds[index].revents & POLLIN) {
			int res = read(ufds[index].fd, &event, sizeof(event));
			if (res < (int)sizeof(event)) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	return -1;
}

jint Java_ru_endlesscode_touchpointer_injector_Native_getType(JNIEnv* env, jobject thiz) {
	return event.type;
}

jint Java_ru_endlesscode_touchpointer_injector_Native_getCode(JNIEnv* env, jobject thiz) {
	return event.code;
}

jint Java_ru_endlesscode_touchpointer_injector_Native_getValue(JNIEnv* env, jobject thiz) {
	return event.value;
}
