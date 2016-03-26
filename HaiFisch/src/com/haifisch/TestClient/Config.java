package com.haifisch.TestClient;

import com.haifisch.server.NetworkTools.ConnectionAcknowledge;
import com.haifisch.server.datamanagement.LocalStorage;
import com.haifisch.server.utils.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Config {
    private static Config ourInstance = new Config();
    private static String data;

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        LocalStorage.getInstance();
    }

    public HashMap<ConnectionAcknowledge, Point[]> getMapperData() {
        File file = LocalStorage.getInstance().readFile("config.cfg");
        HashMap<ConnectionAcknowledge, Point[]> map = new HashMap<>();
        try {
            String text = "";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                text += line;
                line = reader.readLine();
            }
            data = text;
            String[] splitted = text.split("!-");
            for (String token : splitted) {
                String[] split = token.split(";");

                if (split[0].contains("MapperName:")) {
                    ConnectionAcknowledge connectionAcknowledge =
                            new ConnectionAcknowledge(0, split[0].substring(split[0].indexOf(':')),
                                    Integer.getInteger(split[1].substring(split[1].indexOf(':'))));
                    Point[] points = new Point[2];
                    String[] len = split[2].substring(split[2].indexOf(":")).split(",");
                    points[0] = new Point(Double.parseDouble(len[0]), Double.parseDouble(len[1]));
                    len = split[3].substring(split[3].indexOf(":")).split(",");
                    points[1] = new Point(Double.parseDouble(len[0]), Double.parseDouble(len[1]));
                    map.put(connectionAcknowledge, points);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ConnectionAcknowledge getReducerData() {
        File file = LocalStorage.getInstance().readFile("config.cfg");
        try {
            String[] splitted = data.split("!-");
            for (String token : splitted) {
                String[] split = token.split(";");

                if (split[0].contains("ReducerName:")) {
                    return new ConnectionAcknowledge(0, split[0].substring(split[0].indexOf(':')),
                            Integer.getInteger(split[1].substring(split[1].indexOf(':'))));
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
