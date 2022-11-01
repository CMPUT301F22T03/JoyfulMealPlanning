package com.example.joyfulmealplanning;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

public class RecipeTest {

    private ArrayList<Map<String, Integer>> mockIngredientsList() {
        Ingredients ingredient1 = new Ingredients("banana", 3, "g", "fruit");
        ArrayList<Map<String, Integer>> ingredientsList = new ArrayList<>();
        ingredientsList.add((Map<String, Integer>) ingredient1);
        return ingredientsList;
    }
    private Recipe mockRecipe() {
        ArrayList<Map<String, Integer>> ingredientsList = mockIngredientsList();
        return new Recipe("recipe1", "appetizer", "1234",
                3, 11, null);
    }

    @Test
    void testGetRecipeTitle() {
        Recipe recipe = mockRecipe();
        assertEquals("recipe1", recipe.getRecipeTitle());
    }

    @Test
    void testSetRecipeTitle() {
        Recipe recipe = mockRecipe();
        recipe.setRecipeTitle("recipe2");
        assertEquals("recipe2", recipe.getRecipeTitle());
    }

    @Test
    void testGetRecipeCategory() {
        Recipe recipe = mockRecipe();
        assertEquals("appetizer", recipe.getRecipeCategory());
    }

    @Test
    void testSetRecipeCategory() {
        Recipe recipe = mockRecipe();
        recipe.setRecipeCategory("main dish");
        assertEquals("main dish", recipe.getRecipeCategory());
    }

    @Test
    void testGetRecipeComments() {
        Recipe recipe = mockRecipe();
        assertEquals("1234", recipe.getRecipeComments());
    }

    @Test
    void testSetRecipeComments() {
        Recipe recipe = mockRecipe();
        recipe.setRecipeComments("5678");
        assertEquals("5678", recipe.getRecipeComments());
    }

    @Test
    void testGetRecipeNumberOfServings() {
        Recipe recipe = mockRecipe();
        Integer number = 11;
        assertEquals(number, recipe.getRecipeNumberOfServings());
    }

    @Test
    void testSetRecipeNumberOfServings() {
        Recipe recipe = mockRecipe();
        Integer number = 20;
        recipe.setRecipeNumberOfServings(number);
        assertEquals(number, recipe.getRecipeNumberOfServings());
    }

    @Test
    void testGetRecipePreparationTime() {
        Recipe recipe = mockRecipe();
        Integer time = 3;
        assertEquals(time, recipe.getRecipePreparationTime());
    }

    @Test
    void testSetRecipePreparationTime() {
        Recipe recipe = mockRecipe();
        Integer time = 10;
        recipe.setRecipePreparationTime(3);
        assertEquals(time, recipe.getRecipePreparationTime());
    }

    @Test
    void testGetRecipeIngredientsList() {
        Recipe recipe = mockRecipe();
        assertEquals(null, recipe.getRecipeIngredientsList());
    }

    @Test
    void testSetRecipeIngredientsList() {
        Recipe recipe = mockRecipe();
        ArrayList<Map<String, Integer>> ingredientsList = mockIngredientsList();
        recipe.setRecipeIngredientsList(ingredientsList);
        assertEquals(ingredientsList, recipe.getRecipeIngredientsList());
    }
}
