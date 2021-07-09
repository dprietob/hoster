package com.hoster.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Server
{
    private static final String STATUS_COMMAND = "service apache2 status";

    public static void restart(String cmd)
    {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isActive()
    {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(STATUS_COMMAND);

            InputStream is = pr.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder scOutput = new StringBuilder();

            while ((line = br.readLine()) != null) {
                scOutput.append(line).append("\n");
            }

            if (scOutput.toString().contains("apache2.service")) {
                if (scOutput.toString().contains("Active: active (running)")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}