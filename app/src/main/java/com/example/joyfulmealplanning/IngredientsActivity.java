package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class IngredientsActivity extends AppCompatActivity {
    private ListView ingredientsList;
    private IngredientAdapter ingredientsAdapter;
    private ArrayAdapter<Ingredients> ingredientsArrayAdapter;
    private ArrayList<Ingredients> ingredientModels = new ArrayList<>();
    private ImageButton back_button;
    private FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Intent intent = getIntent();
        back_button = findViewById(R.id.imageButton);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        ingredientsList = findViewById(R.id.listView);

        setUpIngredientModels();

        ingredientsArrayAdapter = new IngredientAdapter(this, ingredientModels );

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMain(view);
            }
        });

        /**floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  ingredients.add(ING);
                  ingredientsAdapter = new IngredientAdapter(view.getContext(), ingredients);
                  ingredientsList.setAdapter(ingredientsAdapter);
            }
        });*/

        ingredientsList.setAdapter(ingredientsArrayAdapter);


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

    private void setUpIngredientModels() {
          String[] Descriptions = {"Apple"};
          Date[] Dates = {new Date(2022, 10, 30)};
          String[] Locations = {"Fridge"};
          String[] Categories = {"Fruit"};
          Integer[] Amounts = {2};
          Integer[] Unit_Costs = {1};

          for (int i = 0; i < Descriptions.length; i++) {
              ingredientModels.add(new Ingredients(Descriptions[i], Dates[i], Locations[i], Categories[i], Amounts[i], Unit_Costs[i]));
          }
    }
}