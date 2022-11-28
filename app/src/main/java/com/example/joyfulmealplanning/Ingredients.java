package com.example.joyfulmealplanning;

import java.io.Serializable;

/**
 * The class creates an Ingredients object
 * @author Fan Zhu, Xiangxu Meng & Mashiad Hasan
 * @since 2022-10-23
 */
public class Ingredients implements Serializable{

    /*Declaration of Variables*/
    private String Description;
    private Integer Best_before_date;
    private String Location;
    private String Category;
    private Integer Amount;
    private String Unit;

    /**
     * This is a constructor to create an Ingredients object
     *
     * @param description The brief description for the ingredient. {@link String}
     * @param best_before_date The date by which the ingredient should be used {@link Integer}
     * @param location The location where the ingredient is stored {@link String}
     * @param category The category under which the ingredient may be classified. {@link String}
     * @param amount The amount for that particular ingredient {@link Integer}
     * @param unit The unit being used to measure the amount of the ingredient {@link String}
     */

    public Ingredients(String description, Integer best_before_date, String location, String category, Integer amount, String unit) {

        this.Description = description;
        this.Best_before_date = best_before_date;
        this.Location = location;
        this.Category = category;
        this.Amount = amount;
        this.Unit = unit;
    }

    /**
     * This is an alternative constructor to create an Ingredients object
     *
     * @param description The brief description for the ingredient. {@link String}
     * @param category The category under which the ingredient may be classified. {@link String}
     * @param amount The amount for that particular ingredient {@link Integer}
     * @param unit The unit being used to measure the amount of the ingredient {@link String}
     */
    public Ingredients(String description, Integer amount, String unit, String category){
        this.Description = description;
        this.Category = category;
        this.Amount = amount;
        this.Unit = unit;
    }

    /*Getters & Setters*/
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
