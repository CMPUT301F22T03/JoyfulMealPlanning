package com.example.joyfulmealplanning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity {
    RecyclerView ingredientsList;
    IngredientAdapter ingredientsAdapter;
    ArrayList<Ingredients> ingredients = new ArrayList<>();
    ImageButton back_button;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Intent intent = getIntent();
        back_button = findViewById(R.id.imageButton);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMain(view);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ingredientsList = findViewById(R.id.recyclerView);
        ingredientsAdapter = new IngredientAdapter(this, ingredients );
        ingredientsList.setAdapter(ingredientsAdapter);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void switchToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}