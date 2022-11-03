package com.example.joyfulmealplanning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * The main activity of Recipe
 * @author Qiaosong, Zhaoqi
 * @version 2.0
 * @change Placed the FireStore manipulations and ArrayList/Adapter into the RecipeController class.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener{
    final String TAG = "Sample";
    FloatingActionButton addRecipe;
    ListView recipeList;
    RecipeController recipeController;
    IngredientController ingredientStorageController;
    IngredientController ingredientListController;

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
    }

    @Override
    public void onOkPressed(@Nullable String oldRecipeTitle, Recipe newRecipe) {
        if (oldRecipeTitle != null){
            recipeController.updateRecipe(oldRecipeTitle, newRecipe);
        } else {
            recipeController.addRecipe(newRecipe);
        }
    }
}