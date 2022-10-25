package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


/**
 * The Ingredients activity class
 * @author Fan Zhu
 * @version 1.0
 */
public class IngredientsActivity extends AppCompatActivity {
    ListView ingredientsList;
    IngredientAdapter ingredientsAdapter;
    ArrayAdapter<Ingredients> ingredientsArrayAdapter;
    ArrayList<Ingredients> ingredientModels = new ArrayList<>();
    ImageButton back_button;
    FloatingActionButton floatingActionButton;
    //Ingredients ING;



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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inputIntent = new Intent(view.getContext(), Ingredient_InputActivity.class);
                startActivity(inputIntent);

//                  ingredients.add(ING);
//                  ingredientsAdapter = new IngredientAdapter(view.getContext(), ingredients);
//                  ingredientsList.setAdapter(ingredientsAdapter);

            }
        });

        ingredientsList.setAdapter(ingredientsArrayAdapter);

        ingredientsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(IngredientsActivity.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView desView = view.findViewById(R.id.textView4);
                                final String des = desView.getText().toString();


                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
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
          Integer[] Dates = {20221023};
          String[] Locations = {"Fridge"};
          String[] Categories = {"Fruit"};
          Integer[] Amounts = {2};
          String[] Units = {"g"};


          for (int i = 0; i < Descriptions.length; i++) {
              ingredientModels.add(new Ingredients(Descriptions[i], Dates[i], Locations[i], Categories[i], Amounts[i], Units[i]));
          }
    }
}
