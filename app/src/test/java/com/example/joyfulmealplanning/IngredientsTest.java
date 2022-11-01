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

}
