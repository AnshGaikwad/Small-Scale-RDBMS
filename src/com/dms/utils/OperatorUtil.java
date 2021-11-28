package com.dms.utils;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.ArrayList;

public class OperatorUtil {

    CSVReader reader;
    int index, conditionVal;
    ArrayList<Integer> rowsAffected = new ArrayList<>();
    public OperatorUtil (CSVReader r, int i, int c) {
        reader = r;
        index = i;
        conditionVal = c;
    }

    public ArrayList<Integer> lessThanEqualTo() throws IOException {
        ArrayList<Integer> rowsAffected = new ArrayList<>();

        String[] nextLine;
        for (int i = 0; (nextLine = reader.readNext()) != null; i++) {
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
        String[] nextLine;
        for (int i = 0; (nextLine = reader.readNext()) != null; i++) {
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
        String[] nextLine;
        for (int i = 0; (nextLine = reader.readNext()) != null; i++) {
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
        String[] nextLine;
        for (int i = 0; (nextLine = reader.readNext()) != null; i++) {
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
        String[] nextLine;
        for (int i = 0; (nextLine = reader.readNext()) != null; i++) {
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
        String[] nextLine;
        for (int i = 0; (nextLine = reader.readNext()) != null; i++) {
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
