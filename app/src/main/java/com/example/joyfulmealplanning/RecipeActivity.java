package com.example.joyfulmealplanning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * The main activity of Recipe, allowing users to add, edit and delete recipe entries
 * @author Qiaosong, Zhaoqi
 * @version 2.0
 * @change Placed the FireStore manipulations and ArrayList/Adapter into the RecipeController class.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener,AdapterView.OnItemSelectedListener{
    final String TAG = "Sample";
    FloatingActionButton addRecipe;
    ListView recipeList;
    RecipeController recipeController;
    IngredientController ingredientStorageController;
    IngredientController ingredientListController;
    Spinner recipeSortSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipeController = new RecipeController(RecipeActivity.this);
        ingredientStorageController = new IngredientController(RecipeActivity.this);
        //ingredientListController = new IngredientController(RecipeActivity.this, "");
        addRecipe = findViewById(R.id.RecipeAddButton);
        recipeList = findViewById(R.id.recipe_list);
        recipeList.setAdapter(recipeController.getArrayAdapter());

        //Long click to delete an item in the listView.
        recipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this); //open an alert window
                builder.setCancelable(true)
                        .setTitle("Notice")
                        .setMessage("Are you sure to delete: " +
                                recipeController.getRecipeAtIndex(position).getRecipeTitle()) //get food description
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
                                recipeController.deleteRecipe(position);
                            }
                        })
                        .show();
                return true;
            }

        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeFragment fragment =
                        new RecipeFragment().newInstance(recipeController.getRecipeAtIndex(i));
                fragment.show(getSupportFragmentManager(), "Edit Recipe");
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeFragment().show(getSupportFragmentManager(), "Add Recipe");
            }
        });

        recipeSortSpinner = (Spinner) findViewById(R.id.recipe_list_sort_spinner);
        ArrayAdapter<CharSequence> recipeSortOption = ArrayAdapter.createFromResource(
                this,
                R.array.RecipesSortOptionList,
                android.R.layout.simple_spinner_item
        );
        recipeSortOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeSortSpinner.setAdapter(recipeSortOption);
        recipeSortSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onOkPressed(@Nullable String oldRecipeTitle, Recipe newRecipe) {
        boolean addSuccessful;
        if (oldRecipeTitle != null){
            recipeController.updateRecipe(oldRecipeTitle, newRecipe);
        } else {
            addSuccessful = recipeController.addRecipe(newRecipe);
            if (!addSuccessful){
                new AlertDialog.Builder(this)
                        .setTitle("Add Issue")
                        .setMessage(newRecipe.getRecipeTitle() + " already exist! \n " +
                                "Please edit the existing recipe.")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    //For recipeSortSpinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0){//title
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            recipeController.sortByTitle();
        }else if (position == 1){//preparation time
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            recipeController.sortByPT();
        }else if(position == 2){//number of servings
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            recipeController.sortByNOS();
        }else {//category
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            recipeController.sortByCategory();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}