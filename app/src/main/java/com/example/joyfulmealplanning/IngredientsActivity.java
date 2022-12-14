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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public class IngredientsActivity extends AppCompatActivity implements IngredientFragment.OnFragmentInteractionListener,AdapterView.OnItemSelectedListener {
    /*Declaration of variables*/
    ListView ingredientsList;
    FloatingActionButton addIngredientButton;
    IngredientController controller;
    Spinner ingredientSortSpinner;


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

        ingredientSortSpinner = (Spinner) findViewById(R.id.ingredients_sort_spinner);
        ArrayAdapter<CharSequence> ingredientSortOption = ArrayAdapter.createFromResource(
                this,
                R.array.IngredientsSortOptionList,
                android.R.layout.simple_spinner_item
        );
        ingredientSortOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSortSpinner.setAdapter(ingredientSortOption);
        ingredientSortSpinner.setOnItemSelectedListener(this);

    }


    /**
     * Adds Ingredients object to the adapter
     * @param newIngredients
     * @param oldIngredientDesc {@link String}
     */
    @Override
    public void onOkPressed(String oldIngredientDesc, Ingredients newIngredients) {
        boolean addSuccessful;
        if (oldIngredientDesc != null){
            controller.updateIngredient(oldIngredientDesc, newIngredients);
        } else {
            addSuccessful = controller.addIngredient(newIngredients);
            if (!addSuccessful){
                new AlertDialog.Builder(this)
                        .setTitle("Add Issue")
                        .setMessage(newIngredients.getDescription() + " already exist! \n " +
                                "Please edit the existing ingredient.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }


    }


    //For IngredientSortSpinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0){//description
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            controller.sortByDescription();
        }else if(position == 1){//best before date
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            controller.sortByBBD();
        }else if(position == 2) {//location
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            controller.sortByLocation();
        }else {//ingredient category
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            controller.sortByCategory();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}