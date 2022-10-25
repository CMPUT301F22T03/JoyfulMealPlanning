package com.example.joyfulmealplanning;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class Ingredients {
    private String Description;
    private Integer Best_before_date;
    private String Location;
    private String Category;
    private Integer Amount;
    private String Unit;

    public Ingredients(String description, Integer best_before_date, String location, String category, Integer amount, String unit) {

        Description = description;
        Best_before_date = best_before_date;
        Location = location;
        Category = category;
        Amount = amount;
        Unit = unit;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getBest_before_date() {
        return Best_before_date;
    }

    public void setBest_before_date(Integer best_before_date) {
        Best_before_date = best_before_date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit_Cost(String unit) {
        Unit = unit;
    }
}
