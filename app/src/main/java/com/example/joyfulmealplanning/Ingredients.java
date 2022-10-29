package com.example.joyfulmealplanning;

import java.io.Serializable;

/**
 * The Ingredients object class
 * @author Fan Zhu & Xiangxu Meng
 * @since 2022-10-23
 */
public class Ingredients implements Serializable{
    /*Declaration of local variables*/
    private String Description;
    private Integer Best_before_date;
    private String Location;
    private String Category;
    private Integer Amount;
    private String Unit;

    /*Constructor*/
    public Ingredients(String description, Integer best_before_date, String location, String category, Integer amount, String unit) {

        this.Description = description;
        this.Best_before_date = best_before_date;
        this.Location = location;
        this.Category = category;
        this.Amount = amount;
        this.Unit = unit;
    }


    /*Getters & setters*/
    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String newDescription) {
        this.Description = newDescription;
    }

    public Integer getBest_before_date() {
        return this.Best_before_date;
    }

    public void setBest_before_date(Integer newBest_before_date) {
        Best_before_date = newBest_before_date;
    }

    public String getLocation() {
        return this.Location;
    }

    public void setLocation(String newLocation) {
        Location = newLocation;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setCategory(String newCategory) {
        Category = newCategory;
    }

    public Integer getAmount() {
        return this.Amount;
    }

    public void setAmount(Integer newAmount) {
        Amount = newAmount;
    }

    public String getUnit() {
        return this.Unit;
    }

    public void setUnit(String newUnit) {
        Unit = newUnit;
    }
}
