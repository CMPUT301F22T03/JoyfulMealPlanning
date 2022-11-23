package com.example.joyfulmealplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;


/**
 * <h1>Joyful Meal Planning</h1>
 *
 * <b>Credited to:</b> CMPUT301F22T03
 *
 * <p>
 *     This app is designed to keep track of meal plans of users. It consists of four activities, including ingredients, recipes,
 *     shopping lists and meal plans. Each activity records the entries of ingredients, recipes, shopping lists and meal plans,
 *     users are free to add, edit and delete the entries or even upload images.
 * </p>
 *
 * This activity class implements the start page of the app, as well as functionalities to switch to different activities.
 *
 * @author Fan Zhu, Mashiad Hasan, Yuxuan Yang, Xiangxu Meng, Qiaosong Deng & Zhaoqi Ma
 * @version 2.0
 * @since 2022-10-18
 */
public class MainActivity extends AppCompatActivity {
    /*Declaration of variables*/
    ImageView Recipe;
    ImageView MealPlan;
    ImageView imageView;
    ImageView ShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Recipe = findViewById(R.id.recipeButton);
        Recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(RecipeIntent);
            }
        });

        MealPlan = findViewById(R.id.mealPlanButton);
        MealPlan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent MealPlanIntent = new Intent(MainActivity.this, MealPlanActivity.class);
                startActivity(MealPlanIntent);
            }
        });

        ShoppingList = findViewById(R.id.shoppingListButton);
        ShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShoppingListIntent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(ShoppingListIntent);
            }
        });


        imageView = findViewById(R.id.ingredientButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivity(view);
            }
        });


    }

    /**
     * Switches the app from MainActivity to IngredientsActivity
     * @param view
     */
    public void switchActivity(View view) {
        Intent intent = new Intent(this, IngredientsActivity.class);
        startActivity(intent);

    }


}
