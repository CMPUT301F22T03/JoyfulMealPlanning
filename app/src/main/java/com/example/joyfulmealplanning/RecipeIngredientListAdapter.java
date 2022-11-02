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

/**
 * The custom Adapter for the ListView of recipe's ingredient list
 * @author Zhaoqi Ma
 * @version 1.0
 */
public class RecipeIngredientListAdapter extends ArrayAdapter<Ingredients> {

    private ArrayList<Ingredients> ingredients;
    private Context context;
    /**
     * The constructor of RecipeIngredientListAdaptor
     * @param context {@link Context}
     * @param Ingredients {@link ArrayList<Ingredients>}
     */
    public RecipeIngredientListAdapter(@NonNull Context context, ArrayList<Ingredients> Ingredients){
        super(context, 0, Ingredients);
        this.context = context;
        this.ingredients = Ingredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_list_adaptor, parent, false);
        }

        Ingredients ingredient = ingredients.get(position);

        TextView ingredientDesc = view.findViewById(R.id.RecipeIngredientDescription);
        TextView ingredientAmount = view.findViewById(R.id.RecipeIngredientAmount);
        TextView ingredientUnit = view.findViewById(R.id.RecipeIngredientUnit);
        TextView ingredientCategory = view.findViewById(R.id.RecipeIngredientCategory);

        ingredientDesc.setText(ingredient.getDescription());
        ingredientAmount.setText(ingredient.getAmount());
        ingredientUnit.setText(ingredient.getUnit());
        ingredientCategory.setText(ingredient.getCategory());
        return view;
    }
}
