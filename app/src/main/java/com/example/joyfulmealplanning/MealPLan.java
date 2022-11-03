package com.example.joyfulmealplanning;

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
     * @param mealPlanID This is the ID for the meal plan {@link String}
     * @param ID This is the ID for the ingredient or recipe the meal plan is based on {@link String}
     * @param numberOfServings This is the number of servings for the meal plan {@link Integer}
     * @param type 
     */
    public MealPLan(String mealPlanID, String ID, Integer numberOfServings, String type) {
        this.mealPlanID = mealPlanID;
        this.ID = ID;
        this.numberOfServings = numberOfServings;
        this.type = type;
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
