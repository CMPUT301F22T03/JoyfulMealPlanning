package com.example.joyfulmealplanning;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;


/**
 * The Ingredients activity class, allowing users to add, edit and delete ingredient entries
 * @author Fan Zhu and Xiangxu Meng
 * @version 2.0
 * @since 2022-10-23
 */
public class IngredientsActivity extends AppCompatActivity implements IngredientFragment.OnFragmentInteractionListener{
    /*Declaration of variables*/
    ListView ingredientsList;
    ArrayAdapter<Ingredients> ingredientAdapter;
    ArrayList<Ingredients> ingredientModels = new ArrayList<>();
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        ingredientsList = findViewById(R.id.listView);

        setUpIngredientModels();//Sets up the data model of the Ingredient object

        ingredientAdapter = new IngredientAdapter(this, ingredientModels );

        ingredientsList.setAdapter(ingredientAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");

            }
        });


    }

    /**
     * Creates a dropdown menu on the top app bar with a list of sorting methods as items
     * @param menu
     * @return {@link Boolean}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Sets up the data model of the Ingredients class
     */
    private void setUpIngredientModels() {
          String[] Descriptions = {"Apple"};
          String[] Dates = {"20221023"};
          String[] Locations = {"Fridge"};
          String[] Categories = {"Fruit"};
          String[] Amounts = {"2"};
          String[] Units = {"g"};


          for (int i = 0; i < Descriptions.length; i++) {
              ingredientModels.add(new Ingredients(Descriptions[i], Dates[i], Locations[i], Categories[i], Amounts[i], Units[i]));
          }
    }

    /**
     * Adds Ingredients object to the adapter
     * @param newIngredients
     */
    @Override
    public void onOkPressed(Ingredients newIngredients) {
        ingredientAdapter.add(newIngredients);
    }
}