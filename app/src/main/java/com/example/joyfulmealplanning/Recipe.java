package com.example.joyfulmealplanning;

import java.util.ArrayList;
import java.util.Map;

public class Recipe {
    private String RecipeTitle;
    private String RecipeCategory;
    private String RecipeComments;
    private Integer RecipeNumberOfServings;
    private Integer RecipePreparationTime;
    private ArrayList<Map<String,Integer>> RecipeIngredientsList;

    public Recipe(String recipeTitle, String recipeCategory, String recipeComments,
                  Integer recipePreparationTime,Integer recipeNumberOfServings,
                  ArrayList<Map<String, Integer>> recipeIngredientsList) {
        RecipeTitle = recipeTitle;
        RecipeCategory = recipeCategory;
        RecipeComments = recipeComments;
        RecipeNumberOfServings = recipeNumberOfServings;
        RecipePreparationTime = recipePreparationTime;
        RecipeIngredientsList = recipeIngredientsList;
    }

    public String getRecipeTitle() {
        return RecipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        RecipeTitle = recipeTitle;
    }

    public String getRecipeCategory() {
        return RecipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        RecipeCategory = recipeCategory;
    }

    public String getRecipeComments() {
        return RecipeComments;
    }

    public void setRecipeComments(String recipeComments) {
        RecipeComments = recipeComments;
    }

    public Integer getRecipeNumberOfServings() {
        return RecipeNumberOfServings;
    }

    public void setRecipeNumberOfServings(Integer recipeNumberOfServings) {
        RecipeNumberOfServings = recipeNumberOfServings;
    }

    public Integer getRecipePreparationTime() {
        return RecipePreparationTime;
    }

    public void setRecipePreparationTime(Integer recipePreparationTime) {
        RecipePreparationTime = recipePreparationTime;
    }

    public ArrayList<Map<String, Integer>> getRecipeIngredientsList() {
        return RecipeIngredientsList;
    }

    public void setRecipeIngredientsList(ArrayList<Map<String, Integer>> recipeIngredientsList) {
        RecipeIngredientsList = recipeIngredientsList;
    }
}
