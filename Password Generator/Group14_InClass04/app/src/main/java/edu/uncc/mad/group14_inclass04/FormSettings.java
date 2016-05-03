// Assignment In Class 04
// FormSettings.java
// Brad LaMotte
// ACHYUTA RAM
// Jesus Garcia

package edu.uncc.mad.group14_inclass04;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bradlamotte on 2/8/16.
 */
public class FormSettings {
    final static Integer PW_LENGTH_8_12 = 0;
    final static Integer PW_LENGTH_13_17 = 1;
    final static Integer PW_LENGTH_18_22 = 2;

    private Integer pwLength;
    private Boolean numbers;
    private Boolean upperCase;
    private Boolean lowerCase;
    private Boolean special;
    private ArrayList<String> errors;

    public FormSettings(Integer pwLength, Boolean numbers, Boolean upperCase, Boolean lowerCase, Boolean special) {
        this.pwLength = pwLength;
        this.numbers = numbers;
        this.upperCase = upperCase;
        this.lowerCase = lowerCase;
        this.special = special;
        this.errors = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "FormSettings{" +
                "pwLength=" + pwLength +
                ", numbers=" + numbers +
                ", upperCase=" + upperCase +
                ", lowerCase=" + lowerCase +
                ", special=" + special +
                '}';
    }

    public Integer getPwLength() {
        if(pwLength > 0){
            return (pwLength-1);
        } else{
            return pwLength;
        }
    }

    public Boolean getNumbers() {
        return numbers;
    }

    public Boolean getUpperCase() {
        return upperCase;
    }

    public Boolean getLowerCase() {
        return lowerCase;
    }

    public Boolean getSpecial() {
        return special;
    }

    public Boolean valid(){
        return (haveLength() && haveOption());
    }

    private Boolean haveLength(){
        if(pwLength == 0){
            errors.add("Please select a length");
            return false;
        } else {
            return true;
        }
    }

    private Boolean haveOption(){
        if(!numbers && !upperCase && !lowerCase && !special){
            errors.add("Please choose an option checkbox");
            return false;
        } else {
            return true;
        }
    }

    public String getErrors(){
        StringBuilder builder = new StringBuilder();
        for(String s : errors) {
            builder.append(s);
        }
        return builder.toString();
    }
}