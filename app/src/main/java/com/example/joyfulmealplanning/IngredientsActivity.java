package com.example.joyfulmealplanning;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * The Ingredients activity class, allowing users to add, edit and delete ingredient entries
 * @author Fan Zhu, Xiangxu Meng, Zhaoqi Ma
 * @version 3.0
 * @change deleted all database functionalities, arraylist, and testing data.
 * database functionalities, arraylist, and arraylist adapters have been created inside
 * the IngredientController class.
 * @since 2022-10-23
 */
public class IngredientsActivity extends AppCompatActivity implements IngredientFragment.OnFragmentInteractionListener{
    /*Declaration of variables*/
    ListView ingredientsList;
    FloatingActionButton addIngredientButton;
    IngredientController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        controller = new IngredientController(IngredientsActivity.this);
        addIngredientButton = findViewById(R.id.IngredientAddButton);
        ingredientsList = findViewById(R.id.IngredientListView);
        ingredientsList.setAdapter(controller.getArrayAdapter());

        ingredientsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IngredientsActivity.this); //open an alert window
                builder.setCancelable(true)
                        .setTitle("Notice")
                        .setMessage("Are you sure to delete: " +
                                controller.getIngredientAtIndex(i).getDescription()) //get food description
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            /*do nothing if 'cancel' is pressed*/
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            /*remove the selected item if 'confirm' is pressed*/
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                controller.deleteIngredient(i);
                            }
                        })
                        .show();
                return true;
            }
        });

        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IngredientFragment fragment =
                        new IngredientFragment().newInstance(controller.getIngredientAtIndex(i));
                fragment.show(getSupportFragmentManager(), "Edit Ingredient");
            }
        });

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IngredientFragment().show(getSupportFragmentManager(), "Add Ingredient");
            }
        });


    }


    /**
     * Adds Ingredients object to the adapter
     * @param newIngredients
     * @param oldIngredientDesc {@link String}
     */
    @Override
    public void onOkPressed(String oldIngredientDesc, Ingredients newIngredients) {
        if (oldIngredientDesc != null){
            controller.deleteIngredient(oldIngredientDesc);
            controller.addIngredient(newIngredients);
        } else {
            controller.addIngredient(newIngredients);
        }
    }
}