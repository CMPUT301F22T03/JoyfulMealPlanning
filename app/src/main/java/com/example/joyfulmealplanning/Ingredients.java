package com.example.joyfulmealplanning;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class Ingredients extends RecyclerView.ViewHolder {
    private String Description;
    private Date Best_before_date;
    private String Location;
    private String Category;
    private Integer Amount;
    private Integer Unit_Cost;

    public Ingredients(@NonNull View itemView, String description, Date best_before_date, String location, String category, Integer amount, Integer unit_Cost) {
        super(itemView);
        Description = description;
        Best_before_date = best_before_date;
        Location = location;
        Category = category;
        Amount = amount;
        Unit_Cost = unit_Cost;
    }

    public Ingredients(@NonNull View itemView) {
        super(itemView);
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getBest_before_date() {
        return Best_before_date;
    }

    public void setBest_before_date(Date best_before_date) {
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

    public Integer getUnit_Cost() {
        return Unit_Cost;
    }

    public void setUnit_Cost(Integer unit_Cost) {
        Unit_Cost = unit_Cost;
    }
}
