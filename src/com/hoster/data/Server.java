package com.hoster.data;

public class Server
{
    public static boolean restart(String cmd)
    {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isActive()
    {
        return true;
//        String STATE_PREFIX = "STATE              : ";
//
//        String s = executeCommand("sc query \"" + service + "\"");
//// check that the temp string contains the status prefix
//        int ix = s.indexOf(STATE_PREFIX);
//        if (ix >= 0) {
//            // compare status number to one of the states
//            String stateStr = s.substring(ix + STATE_PREFIX.length(), ix + STATE_PREFIX.length() + 1);
//            int state = Integer.parseInt(stateStr);
//            switch (state) {
//                case (1): // service stopped
//                    break;
//                case (4): // service started
//                    break;
//            }
//        }
    }
}
