package com.ayg.rdbms.utils;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class OperatorUtil {

    CSVReader reader;
    int index, conditionVal;
    String value;
    public OperatorUtil (CSVReader r, int i, int c, String v) {
        reader = r;
        index = i;
        conditionVal = c;
        value = v;
    }

    public ArrayList<Integer> lessThanEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();

        String[] nextLine;

        if(conditionVal == -1){
            return null;
        }
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return rowsAffected;
            if (tableVal <= conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }

    public ArrayList<Integer> moreThanEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;

        if(conditionVal == -1){
            return null;
        }

        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return rowsAffected;
            if (tableVal >= conditionVal) {
                rowsAffected.add(i);
            }
        }
        return rowsAffected;
    }

    public ArrayList<Integer> notEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();

        String[] nextLine;

        if(conditionVal == -1){
            for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
                String tableVal;
                if(index != -1)
                    tableVal = nextLine[index];
                else
                    return rowsAffected;
                if (!Objects.equals(tableVal, value)) {
                    rowsAffected.add(i);
                }
            }
        }else{
            for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
                int tableVal;
                if(index != -1)
                    tableVal = Integer.parseInt(nextLine[index]);
                else
                    return rowsAffected;
                if (tableVal != conditionVal) {
                    rowsAffected.add(i);
                }
            }
        }


        return rowsAffected;
    }

    public ArrayList<Integer> equalTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;

        if(conditionVal == -1){
            for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
                String tableVal;
                if(index != -1)
                    tableVal = nextLine[index];
                else
                    return rowsAffected;
                if (Objects.equals(tableVal, value)) {
                    rowsAffected.add(i);
                }
            }
        }else{
            for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
                int tableVal;
                if(index != -1)
                    tableVal = Integer.parseInt(nextLine[index]);
                else
                    return rowsAffected;
                if (tableVal == conditionVal) {
                    rowsAffected.add(i);
                }
            }
        }


        return rowsAffected;
    }

    public ArrayList<Integer> lessThan() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;

        if(conditionVal == -1){
            return null;
        }

        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return rowsAffected;
            if (tableVal < conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }

    public ArrayList<Integer> moreThan() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;

        if(conditionVal == -1){
            return null;
        }

        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return rowsAffected;
            if (tableVal > conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }


}
