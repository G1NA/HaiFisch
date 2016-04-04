package com.haifisch.server.datamanagement;

import com.haifisch.server.utils.Serializer;
import com.haifisch.server.utils.RandomString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;


public class LocalStorage {

    private static LocalStorage instance;
    private String storageRoot;

    /**
     * Get the instance of the local storage singleton
     *
     * @param folder The folder to be considered as root(optional)
     * @return The isntance of the local storage object
     */
    public static LocalStorage getInstance(String folder) {
        if (instance == null)
            instance = new LocalStorage(folder);
        return instance;
    }

    /**
     * LocalStorage constructor
     *
     * @param folder optional root folder request
     */
    private LocalStorage(String folder) {
        initialize(folder);
    }

    /**
     * Initialize the object with the given root, if none then program root
     *
     * @param folder root folder
     * @return true for success
     */
    private boolean initialize(String folder) {

        if (folder.length() != 0) {
            try {
                Files.write(Paths.get(folder + "test.txt"), Collections.singletonList("testtest"));
                this.storageRoot = folder;
                Files.delete(Paths.get(folder + "test.txt"));
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
                Files.delete(Paths.get(folder + "test.txt"));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    /**
     * Store a serializable object
     * TODO thorough testing before being used for state save
     *
     * @param obj the object to be stored
     * @return true for success
     */
    public boolean storeObject(Object obj) {
        try {
            Files.write(
                    Paths.get(storageRoot + "/obj/" + new RandomString(8).nextString()), //path and object name
                    Serializer.serialize(obj)); //object data
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Retrieve all the objects stored, if there are any
     * TODO thorough testing before being used for state recovery
     *
     * @return the objects
     */
    public Object[] retrieveObjects() {
        File folder = new File(storageRoot + "/obj/");
        File[] files = folder.listFiles();
        Object[] objects = new Object[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                objects[i] = Serializer.deserialize(Files.readAllBytes(files[i].toPath()));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

    /**
     * Read a file given the filename
     *
     * @param filename self explanatory
     * @return the File
     */
    public File readFile(String filename) {
        try {
            return new File(filename);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Write string data to a file
     *
     * @param filename   self explanatory
     * @param toBeWriten the data to be written
     * @return true for success
     */
    public boolean writeFile(String filename, Iterable<? extends CharSequence> toBeWriten) {
        try {
            Files.write(
                    Paths.get(storageRoot + filename), //path and file name
                    toBeWriten, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }

    }


}
