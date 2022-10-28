package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * The main activity of Recipe
 * @author Qiaosong, Zhaoqi
 * @version 2.0
 * @change (version2.0):
 * Placed the FireStore manipulations and ArrayList/Adapter into the RecipeController class.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener{
    final String TAG = "Sample";
    FloatingActionButton addRecipe;
    ListView recipeList;
    RecipeController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        controller = new RecipeController(RecipeActivity.this);
        addRecipe = findViewById(R.id.RecipeAddButton);
        recipeList = findViewById(R.id.recipe_list);
        recipeList.setAdapter(controller.getArrayAdapter());

        //Long click to delete an item in the listView.
        recipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this); //open an alert window
                builder.setCancelable(true)
                        .setTitle("Notice")
                        .setMessage("Are you sure to delete: " +
                                controller.getRecipeAtIndex(position).getRecipeTitle()) //get food description
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
                                controller.deleteRecipe(position);
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
                        new RecipeFragment().newInstance(controller.getRecipeAtIndex(i));
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
            controller.deleteRecipe(oldRecipeTitle);
            controller.addRecipe(newRecipe);
        } else {
            controller.addRecipe(newRecipe);
        }
    }
}