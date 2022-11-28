package com.example.joyfulmealplanning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class MealPlanTest {

    private MealPLan mealPLan;

    @BeforeEach
    private void createTest(){
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
    }

    @Test
    public void getMealPlanIDTest(){
        String meanPlanID = "apple_ingredient";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        assertEquals(mealPLan.getMealPlanID(), meanPlanID);
    }

    @Test
    public void setMealPlanIDTest(){
        String meanPlanID = "orange_ingredient";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        mealPLan.setMealPlanID(meanPlanID);
        assertEquals(mealPLan.getMealPlanID(), meanPlanID);
    }

    @Test
    public void getIDTest(){
        String ID = "apple";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        assertEquals(mealPLan.getID(), ID);
    }

    @Test
    public void setIDTest(){
        String ID = "orange";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        mealPLan.setID(ID);
        assertEquals(mealPLan.getID(), ID);
    }

    @Test
    public void getNumberOfServingsTest(){
        Integer numberOfServings = 2;
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        assertEquals(mealPLan.getNumberOfServings(), numberOfServings);
    }

    @Test
    public void setNumberOfServingsTest(){
        Integer numberOfServings = 5;
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        mealPLan.setNumberOfServings(numberOfServings);
        assertEquals(mealPLan.getNumberOfServings(), numberOfServings );
    }

    @Test
    public void getTypeTest(){
        String type = "ingredient";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        assertEquals(mealPLan.getType(), type);
    }

    @Test
    public void setTypeTest(){
        String type = "recipe";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        mealPLan.setType(type);
        assertEquals(mealPLan.getType(), type);
    }

    @Test
    public void getDateTest(){
        String date = "20231031";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20231031");
        assertEquals(mealPLan.getDate(), date);
    }

    @Test
    public void setDateTest(){
        String date = "20231031";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient","20221231");
        mealPLan.setDate(date);
        assertEquals(mealPLan.getDate(), date);
    }


}
