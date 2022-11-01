package com.example.joyfulmealplanning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class IngredientsTest {

    private Ingredients ingredients;

    @BeforeEach
    private void createTest(){
        ingredients = new Ingredients("apple",20221231,"fridge","fruit",10,"g");
    }

    @Test
    public void getDescriptionTest(){
        String desc = "mango";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        assertEquals(ingredients.getDescription(), desc);
    }

    @Test
    public void setDescriptionTest(){
        String desc = "apple";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        ingredients.setDescription(desc);
        assertEquals(ingredients.getDescription(), desc);
    }

    @Test
    public void getBest_before_dateTest(){
        Integer date = 20221231;
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        assertEquals(ingredients.getBest_before_date(), date);
    }

    @Test
    public void setBest_before_dateTest(){
        Integer date = 20221125;
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        ingredients.setBest_before_date(date);
        assertEquals(ingredients.getBest_before_date(), date);
    }

    @Test
    public void getLocationTest(){
        String location = "fridge";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        assertEquals(ingredients.getLocation(), location);
    }

    @Test
    public void setLocationTest(){
        String location = "pantry";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        ingredients.setLocation(location);
        assertEquals(ingredients.getLocation(), location);
    }

    @Test
    public void getCategoryTest(){
        String category = "fruit";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        assertEquals(ingredients.getCategory(), category);
    }

    @Test
    public void setCategoryTest(){
        String category = "vegetable";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        ingredients.setCategory(category);
        assertEquals(ingredients.getCategory(), category);
    }



    @Test
    public void getAmountTest(){
        Integer amount = 10;
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        assertEquals(ingredients.getAmount(), amount);
    }

    @Test
    public void setAmountTest(){
        Integer amount = 15;
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        ingredients.setAmount(amount);
        assertEquals(ingredients.getAmount(), amount);
    }

    @Test
    public void getUnitTest(){
        String unit = "g";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        assertEquals(ingredients.getUnit(), unit);
    }

    @Test
    public void setUnitTest(){
        String unit = "lbs";
        ingredients = new Ingredients("mango",20221231,"fridge","fruit",10,"g");
        ingredients.setUnit(unit);
        assertEquals(ingredients.getUnit(), unit);
    }
}
