package com.haifisch.server.datamanagement;

import com.haifisch.server.utils.Serialize;
import com.haifisch.server.utils.RandomString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Collections;

public class LocalStorage {
    private static LocalStorage instance;
    private String storageRoot;

    public static LocalStorage getInstance(String folder) {
        if (instance == null)
            instance = new LocalStorage(folder);
        return instance;
    }

    private LocalStorage(String folder) {
        initialize(folder);
    }

    private boolean initialize(String folder) {

        if (folder.length() != 0) {
            try {
                Files.write(Paths.get(folder + "test.txt"), Collections.singletonList("testtest"));
                this.storageRoot = folder;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                //program root
                Files.write(Paths.get("test.txt"), Collections.singletonList("testtest"));
                this.storageRoot = "";
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    public boolean storeObject(Object obj) {
        try {
            Files.write(
                    Paths.get(storageRoot + "/obj/" + new RandomString(8).nextString()), //path and object name
                    Serialize.serialize(obj)); //object data
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Object[] retrieveObjects() {
        File folder = new File(storageRoot + "/obj/");
        File[] files = folder.listFiles();
        Object[] objects = new Object[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                objects[i] = Serialize.deserialize(Files.readAllBytes(files[i].toPath()));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

    public File readFile(String filename) {
        try {
            return new File(filename);
        } catch (Exception e) {
            return null;
        }

    }
    
    public boolean writeFile(String filename, Iterable toBeWriten){
    	try {
            Files.write(
                    Paths.get(storageRoot +filename), //path and file name
                    toBeWriten, StandardCharsets.UTF_8 , StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    
    }


}
