package com.dms.utils;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class OperatorUtil {

    CSVReader reader;
    int index, conditionVal;
    public OperatorUtil (CSVReader r, int i, int c) {
        reader = r;
        index = i;
        conditionVal = c;
    }

    public ArrayList<Integer> lessThanEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();

        String[] nextLine;
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return null;
            if (tableVal <= conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }

    public ArrayList<Integer> moreThanEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return null;
            if (tableVal >= conditionVal) {
                rowsAffected.add(i);
            }
        }
        return rowsAffected;
    }

    public ArrayList<Integer> notEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return null;
            if (tableVal != conditionVal) {
                rowsAffected.add(i);
            }
        }
        return rowsAffected;
    }

    public ArrayList<Integer> equalTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return null;
            if (tableVal == conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }

    public ArrayList<Integer> lessThan() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return null;
            if (tableVal < conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }

    public ArrayList<Integer> moreThan() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        String[] nextLine;
        for (int i = 1; (nextLine = reader.readNext()) != null; i++) {
            int tableVal;
            if(index != -1)
                tableVal = Integer.parseInt(nextLine[index]);
            else
                return null;
            if (tableVal > conditionVal) {
                rowsAffected.add(i);
            }
        }

        return rowsAffected;
    }


}
