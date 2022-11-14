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

    /**
     * This is a constructor for a Meal Plan object.
     * @param mealPlanID The ID for the meal plan {@link String}
     * @param ID The ID for the ingredient or recipe the meal plan is based on {@link String}
     * @param numberOfServings The number of servings for the meal plan {@link Integer}
     * @param type Indicates whether the meal plan is based on recipe or ingredient {@link String}
     */
    public MealPLan(String mealPlanID, String ID, Integer numberOfServings, String type) {
        this.mealPlanID = mealPlanID;
        this.ID = ID;
        this.numberOfServings = numberOfServings;
        this.type = type;
    }

    /**
     * This method returns the mealPlanID for the meal plan
     * @return {@link String}
     */
    public String getMealPlanID() {
        return mealPlanID;
    }

    /**
     * This method sets a new mealPlanID
     * @param mealPlanID
     */
    public void setMealPlanID(String mealPlanID) {
        this.mealPlanID = mealPlanID;
    }

    /**
     * This method returns the ID for the ingredient or recipe the meal plan is based on
     * @return {@link String}
     */
    public String getID() {
        return ID;
    }

    /**
     * This methods sets a new ID for the ingredient or recipe the meal plan is based on
     * @param ID {@link String}
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     *This method returns the number of servings for the meal plan
     * @return {@link Integer}
     */
    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    /**
     *This method sets a new number of servings for the meal plan
     * @param numberOfServings {@link Integer}
     */
    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    /**
     *This methods returns whether the meal plan is based on recipe or ingredient (type of meal plan)
     * @return {@link String}
     */
    public String getType() {
        return type;
    }

    /**
     *This methods sets a new type for the meal plan, to modify whether the meal plan is based on recipe or ingredient
     * @param type {@link String}
     */
    public void setType(String type) {
        this.type = type;
    }
}
