package com.example.joyfulmealplanning;

/**
 * The Ingredients object class
 * @author Fan Zhu
 * @since 2022-10-23
 */
public class Ingredients {
    /*Declaration of local variables*/
    private String Description;
    private String Best_before_date;
    private String Location;
    private String Category;
    private String Amount;
    private String Unit;


    /*Constructor*/
    public Ingredients(String description, String best_before_date, String location, String category, String amount, String unit) {

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

    public String getBest_before_date() {
        return this.Best_before_date;
    }

    public void setBest_before_date(String newBest_before_date) {
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

    public String getAmount() {
        return this.Amount;
    }

    public void setAmount(String newAmount) {
        Amount = newAmount;
    }

    public String getUnit() {
        return this.Unit;
    }

    public void setUnit_Cost(String newUnit) {
        Unit = newUnit;
    }
}
