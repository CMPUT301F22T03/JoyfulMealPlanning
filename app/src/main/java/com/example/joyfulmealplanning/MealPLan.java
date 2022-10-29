package com.example.joyfulmealplanning;

public class MealPLan {
    private String mealPlanID;
    private String ID;
    private Integer numberOfServings;
    private String type;

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
