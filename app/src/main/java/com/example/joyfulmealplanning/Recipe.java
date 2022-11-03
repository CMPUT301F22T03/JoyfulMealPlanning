package com.example.joyfulmealplanning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


/**
 * This class create an object of Recipe with Six variables.
 * RecipeTitle {@link String}
 * RecipeCategory {@link String}
 * RecipeComments {@link String}
 * RecipeNumberOfServings {@link Integer}
 * RecipePreparationTime {@link Integer}
 * RecipeIngredientsList {@link ArrayList<Map<String,Integer>>}
 * @author Qiaosong
 * @version 1.0
 */
public class Recipe implements Serializable {
    private String RecipeTitle;
    private String RecipeCategory;
    private String RecipeComments;
    private Integer RecipeNumberOfServings;
    private Integer RecipePreparationTime;
    private ArrayList<Ingredients> RecipeIngredientsList;

    /**
     *This is a constructor to create a Recipe object.
     *
     * @param recipeTitle The title of the Recipe {@link String}
     * @param recipeCategory The category of the Recipe {@link String}
     * @param recipeComments The comments of the Recipe {@link String}
     * @param recipePreparationTime The Preparation Time of the Recipe {@link Integer}
     * @param recipeNumberOfServings The Number of Servings of the Recipe {@link Integer}
     * @param recipeIngredientsList The Ingredient List of the Recipe {@link ArrayList<Ingredients>}
     */
    public Recipe(String recipeTitle, String recipeCategory, String recipeComments,
                  Integer recipePreparationTime,Integer recipeNumberOfServings,
                  ArrayList<Ingredients> recipeIngredientsList) {
        RecipeTitle = recipeTitle;
        RecipeCategory = recipeCategory;
        RecipeComments = recipeComments;
        RecipeNumberOfServings = recipeNumberOfServings;
        RecipePreparationTime = recipePreparationTime;
        RecipeIngredientsList = recipeIngredientsList;
    }

    /**
     * This function returns Title
     * @return {@link String}
     */
    public String getRecipeTitle() {
        return RecipeTitle;
    }

    /**
     * This function sets a new title.
     * @param recipeTitle {@link String}
     */
    public void setRecipeTitle(String recipeTitle) {
        RecipeTitle = recipeTitle;
    }

    /**
     * This function returns RecipeCategorySpinner
     * @return {@link String}
     */
    public String getRecipeCategory() {
        return RecipeCategory;
    }

    /**
     * This function sets a new RecipeCategorySpinner.
     * @param recipeCategory {@link String}
     */
    public void setRecipeCategory(String recipeCategory) {
        RecipeCategory = recipeCategory;
    }

    /**
     * This function returns Comments
     * @return {@link String}
     */
    public String getRecipeComments() {
        return RecipeComments;
    }

    /**
     * This function sets a new Comments.
     * @param recipeComments {@link String}
     */
    public void setRecipeComments(String recipeComments) {
        RecipeComments = recipeComments;
    }

    /**
     * This function returns Number of servings
     * @return {@link Integer}
     */
    public Integer getRecipeNumberOfServings() {
        return RecipeNumberOfServings;
    }

    /**
     * This function sets a new Number Of Servings.
      * @param recipeNumberOfServings {@link Integer}
     */
    public void setRecipeNumberOfServings(Integer recipeNumberOfServings) {
        RecipeNumberOfServings = recipeNumberOfServings;
    }

    /**
     * This function returns preparation time
     * @return {@link Integer}
     */
    public Integer getRecipePreparationTime() {
        return RecipePreparationTime;
    }

    /**
     * This function sets a new Preparation Time.
     * @param recipePreparationTime {@link Integer}
     */
    public void setRecipePreparationTime(Integer recipePreparationTime) {
        RecipePreparationTime = recipePreparationTime;
    }

    /**
     * This function returns Ingredient list
     * @return {@link ArrayList<Map<String, Integer>>}
     */
    public ArrayList<Ingredients> getRecipeIngredientsList() {
        return RecipeIngredientsList;
    }

    /**
     * This function sets a new Ingredients List.
     * @param recipeIngredientsList {@link ArrayList<Map<String, Integer>>}
     */
    public void setRecipeIngredientsList(ArrayList<Ingredients> recipeIngredientsList) {
        RecipeIngredientsList = recipeIngredientsList;
    }
}
