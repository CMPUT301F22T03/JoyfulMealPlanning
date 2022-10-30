package com.example.joyfulmealplanning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The custom Adapter for the ListView of recipe_list
 * @author Qiaosong
 * @version 1.0
 */
public class RecipeAdaptor extends ArrayAdapter<Recipe>{

    private ArrayList<Recipe> Recipes;
    private Context context;

    /**
     * The constructor of RecipeAdaptor
     * @param context {@link Context}
     * @param Recipes {@link ArrayList<Recipe>}
     */
    public RecipeAdaptor(@NonNull Context context, ArrayList<Recipe> Recipes) {
        super(context, 0, Recipes);
        this.context = context;
        this.Recipes = Recipes;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_content, parent,false);
        }

        Recipe recipe = Recipes.get(position);

        TextView recipeTitle = view.findViewById(R.id.recipeTitle);
        TextView recipePreparationTime = view.findViewById(R.id.recipePreparationTime);
        TextView recipeCategory = view.findViewById(R.id.recipeCategory);
        TextView recipeNumberOfServing = view.findViewById(R.id.recipeNumberOfServing);

        recipeTitle.setText("Recipe Title: "+ recipe.getRecipeTitle());
        recipePreparationTime.setText("Preparation Time: "+ recipe.getRecipePreparationTime().toString());
        recipeCategory.setText("Recipe RecipeCategorySpinner: "+recipe.getRecipeCategory());
        recipeNumberOfServing.setText("Number Of Serving: "+ recipe.getRecipeNumberOfServings().toString());

        return view;
    }
}
