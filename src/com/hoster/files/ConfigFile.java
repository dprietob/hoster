package com.hoster.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class ConfigFile
{
    public Map<String, String> load(String fileName, Map<String, String> initialConfig, String separator)
    {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String[] line = myReader.nextLine().split(separator);
                    if (line.length == 2) {
                        initialConfig.put(line[0].trim(), line[1].trim());
                    }
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return initialConfig;
    }

    public boolean save(String fileName, String header, String separator, Map<String, String> config)
    {
        if (deleteCurrentFile(fileName)) {
            if (createNewFile(fileName)) {
                return writeFile(fileName, header, separator, config);
            }
        }
        return false;
    }

    private boolean deleteCurrentFile(String fileName)
    {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    private boolean createNewFile(String fileName)
    {
        try {
            File file = new File(fileName);
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean writeFile(String fileName, String header, String separator, Map<String, String> config)
    {
        try {
            if (!config.isEmpty()) {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("# " + header + " \n");
                for (Map.Entry<String, String> entry : config.entrySet()) {
                    fileWriter.write(entry.getKey() + separator + entry.getValue() + "\n");
                }
                fileWriter.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
