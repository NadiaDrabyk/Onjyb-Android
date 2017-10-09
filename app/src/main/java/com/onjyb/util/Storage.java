package com.onjyb.util;

import android.util.Log;
import java.io.File;

public class Storage {
    public static void verifyDirPath(String dir) {
        try {
            File f_dir = new File(dir);
            if (!f_dir.exists()) {
                f_dir.mkdirs();
            }
        } catch (Exception e) {
            Log.v("verifyDirPath", "error : ");
            e.printStackTrace();
            Log.e("directories not verify", e.toString());
        }
    }
}
