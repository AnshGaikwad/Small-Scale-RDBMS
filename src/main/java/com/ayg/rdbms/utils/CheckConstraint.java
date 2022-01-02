package com.ayg.rdbms.utils;

import java.util.Objects;

public class CheckConstraint {

    String attribute, operator, value;
    public CheckConstraint (String a, String o, String v) {
        attribute = a;
        operator = o;
        value = v;
    }

    public boolean lessThanEqualTo() {
        int attributeInt = Integer.parseInt(attribute);
        int valueInt = Integer.parseInt(value);
        return attributeInt <= valueInt;
    }

    public boolean moreThanEqualTo() {
        int attributeInt = Integer.parseInt(attribute);
        int valueInt = Integer.parseInt(value);
        return attributeInt >= valueInt;
    }

    public boolean notEqualTo() {
        return !Objects.equals(attribute, value);
    }

    public boolean equalTo() {
        return Objects.equals(attribute, value);
    }

    public boolean lessThan() {
        int attributeInt = Integer.parseInt(attribute);
        int valueInt = Integer.parseInt(value);
        return attributeInt < valueInt;
    }

    public boolean moreThan() {
        int attributeInt = Integer.parseInt(attribute);
        int valueInt = Integer.parseInt(value);
        return attributeInt > valueInt;
    }

}
