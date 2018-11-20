package utils;

import com.opencsv.CSVReader;
import log.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by yuao on 22/9/17.
 */
public class CSVHelper {

    public static List<String> getStringList(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
        } catch (FileNotFoundException ex) {
            LoggerFactory.getLogger().error(String.format("File not found, path = %s", path));
        } catch (UnsupportedEncodingException ex) {
            LoggerFactory.getLogger().error(String.format("Encoding error, path = %s", path));
        }
        if (reader == null) {
            return null;
        }
        List<String> resultList = new ArrayList<>();

        String[] strArr = null;
        try {
            while ((strArr = reader.readNext()) != null) {
                if (strArr[0].isEmpty()) {
                    continue;
                }
                resultList.add(strArr[0]);
            }
            reader.close();
        } catch (IOException ex) {
            LoggerFactory.getLogger().error(String.format("IO error, path = %s", path));
        }
        return resultList;
    }

    public static Map<String, Set<String>> getStdQAnswer(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
        } catch (FileNotFoundException ex) {
            LoggerFactory.getLogger().error(String.format("File not found, path = %s", path));
        } catch (UnsupportedEncodingException ex) {
            LoggerFactory.getLogger().error(String.format("Encoding error, path = %s", path));
        }

        if (reader == null) {
            return null;
        }

        Map<String, Set<String>> resultMap = new HashMap<>();

        String[] strArr = null;
        try {
            while ((strArr = reader.readNext()) != null) {
                Set<String> answers = new HashSet<>();
                if (resultMap.containsKey(strArr[1])) {
                    answers = resultMap.get(strArr[1]);
                }
                answers.add(strArr[0]);
                resultMap.put(strArr[1], answers);
            }
            reader.close();
        } catch (IOException ex) {
            LoggerFactory.getLogger().error(String.format("IO error, path = %s", path));
        }
        return resultMap;
    }

    public static Set<String> getElement(String path) {
        CSVReader reader = null;

        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

        } catch (FileNotFoundException e) {
            LoggerFactory.getLogger().error(String.format("File not found, path = %s", path));
        } catch (UnsupportedEncodingException ex) {
            LoggerFactory.getLogger().error(String.format("Encoding error, path = %s", path));
        }
        if (reader == null) {
            return null;
        }

        Set<String> resultSet = new HashSet<>();

        String[] strArr;

        try {
            while ((strArr = reader.readNext()) != null) {
                resultSet.add(strArr[0]);
            }
            reader.close();
        } catch (IOException e) {
            LoggerFactory.getLogger().error(String.format("IO error, path = %s", path));
        }
        return resultSet;
    }
}
