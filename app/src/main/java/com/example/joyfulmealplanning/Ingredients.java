package com.example.joyfulmealplanning;

import java.io.Serializable;

/**
 * The class creates an Ingredients object
 * @author Fan Zhu, Xiangxu Meng & Mashiad
 * @since 2022-10-23
 */
public class Ingredients implements Serializable{

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

    /**
     * This method returns the Description for the ingredient
     * @return {@link String}
     */
    public String getDescription() {
        return this.Description;
    }

    /**
     * This method sets sets a new Description.
     * @param newDescription {@link String}
     */
    public void setDescription(String newDescription) {
        this.Description = newDescription;
    }

    /**
     * This method returns the Best before date.
     * @return {@link Integer}
     */
    public Integer getBest_before_date() {
        return this.Best_before_date;
    }

    /**
     * This methods sets a new Best before date.
     * @param newBest_before_date {@link Integer}
     */
    public void setBest_before_date(Integer newBest_before_date) {
        Best_before_date = newBest_before_date;
    }

    /**
     * This method returns the location for the ingredient.
     * @return {@link String}
     */
    public String getLocation() {
        return this.Location;
    }

    /**
     * This method sets a new location
     * @param newLocation {@link String}
     */
    public void setLocation(String newLocation) {
        Location = newLocation;
    }

    /**
     * This method returns the category for the ingredient.
     * @return {@link String}
     */
    public String getCategory() {
        return this.Category;
    }

    /**
     * This method sets a new Category for the ingredient.
     * @param newCategory {@link String}
     */
    public void setCategory(String newCategory) {
        Category = newCategory;
    }

    /**
     * This method returns the amount for the ingredient.
     * @return {@link Integer}
     */
    public Integer getAmount() {
        return this.Amount;
    }

    /**
     * This method sets a new amount for the ingredient.
     * @param newAmount {@link Integer}
     */
    public void setAmount(Integer newAmount) {
        Amount = newAmount;
    }

    /**
     * This method returns the units being used to measure the ingredient
     * @return {@link String}
     */
    public String getUnit() {
        return this.Unit;
    }

    /**
     * This method sets a new unit for the ingredient.
     * @param newUnit {@link String}
     */
    public void setUnit(String newUnit) {
        Unit = newUnit;
    }
}
