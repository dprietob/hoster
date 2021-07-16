package com.hoster.files;

import com.hoster.gui.listeners.ConsoleListener;

import java.io.File;
import java.io.IOException;

public class ConfigFile
{
    protected ConsoleListener consoleListener;

    public void addConsoleListener(ConsoleListener listener)
    {
        consoleListener = listener;
    }

    public boolean deleteCurrentFile(String fileName)
    {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public boolean createNewFile(String fileName)
    {
        try {
            File file = new File(fileName);
            return file.createNewFile();
        } catch (IOException e) {
            consoleListener.onConsoleError(e.getMessage());
        }
        return false;
    }
}
