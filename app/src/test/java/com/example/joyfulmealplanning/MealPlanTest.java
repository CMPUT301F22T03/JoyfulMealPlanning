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
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient");
    }

    @Test
    public void getMealPlanIDTest(){
        String meanPlanID = "apple_ingredient";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient");
        assertEquals(mealPLan.getMealPlanID(), meanPlanID);
    }

    @Test
    public void setMealPlanIDTest(){
        String meanPlanID = "orange_ingredient";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient");
        mealPLan.setMealPlanID(meanPlanID);
        assertEquals(mealPLan.getMealPlanID(), meanPlanID);
    }

    @Test
    public void getIDTest(){
        String ID = "apple";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient");
        assertEquals(mealPLan.getID(), ID);
    }

    @Test
    public void setIDTest(){
        String ID = "orange";
        mealPLan = new MealPLan ("apple_ingredient","apple",2,"ingredient");
        mealPLan.setID(ID);
        assertEquals(mealPLan.getID(), ID);
    }





}
