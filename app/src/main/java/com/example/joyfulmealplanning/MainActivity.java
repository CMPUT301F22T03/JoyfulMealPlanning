package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
 * @version 1.0
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


        Recipe = findViewById(R.id.imageView3);
        Recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(RecipeIntent);
            }
        });

        MealPlan = findViewById(R.id.imageView4);
        MealPlan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent MealPlanIntent = new Intent(MainActivity.this, MealPlanActivity.class);
                startActivity(MealPlanIntent);
            }
        });

        ShoppingList = findViewById(R.id.imageView2);
        ShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShoppingListIntent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(ShoppingListIntent);
            }
        });


        imageView = findViewById(R.id.imageView);

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
