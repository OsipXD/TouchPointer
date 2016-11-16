package ru.endlesscode.touchpointer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by OsipXD on 16.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
class RootExecutor {
    private static Process suProcess;

    static boolean requestRoot() {
        try {
            suProcess = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    static void exec(String command) {
        DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
        InputStream is = suProcess.getInputStream();
        try {
            dos.writeBytes(command + "\n");
            dos.flush();
            dos.writeBytes("echo -n 0\n");
            dos.flush();
            is.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
