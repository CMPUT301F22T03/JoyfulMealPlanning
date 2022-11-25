package com.example.joyfulmealplanning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The class creates a Meal Plan object
 * @author Qiaosong Deng
 */
public class MealPLan {
    private String mealPlanID;
    private String ID;
    private Integer numberOfServings;
    private String type;
    private String Date;

    /**
     * This is a constructor for a Meal Plan object.
     * @param mealPlanID The ID for the meal plan {@link String}
     * @param ID The ID for the ingredient or recipe the meal plan is based on {@link String}
     * @param numberOfServings The number of servings for the meal plan {@link Integer}
     * @param type Indicates whether the meal plan is based on recipe or ingredient {@link String}
     */
    public MealPLan(String mealPlanID, String ID, Integer numberOfServings, String type, String Date) {
        this.mealPlanID = mealPlanID;
        this.ID = ID;
        this.numberOfServings = numberOfServings;
        this.type = type;
        this.Date = Date;
    }

    /*Getters & setters*/
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMealPlanID() {
        return mealPlanID;
    }

    public void setMealPlanID(String mealPlanID) {
        this.mealPlanID = mealPlanID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
