package edu.uncc.mad.group14_inclass12;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by achyu on 11-Apr-16.
 */
public class Expense implements Serializable {
    String name,category,user_email, key;
    float amount;
    String date;
    Boolean empty;

    public Expense() {
        this.amount=0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date_value) {
//        SimpleDateFormat expense_date = new SimpleDateFormat("dd-MM-yyyy");
//        try {
//            this.date = expense_date.parse(date_value);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        this.date = date_value;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", user_email='" + user_email + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }

    public boolean isEmpty(){
        if(this.name ==null || this.category==null || this.amount==0 || this.user_email==null || this.date.isEmpty() ){
            return true;
        }
        return false;
    }

//    public Boolean getEmpty() {
//        return empty;
//    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
