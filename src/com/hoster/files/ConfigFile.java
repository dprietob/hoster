package com.hoster.files;

import java.io.File;
import java.io.IOException;

public class ConfigFile
{
    public static boolean deleteCurrentFile(String fileName)
    {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public static boolean createNewFile(String fileName)
    {
        try {
            File file = new File(fileName);
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
