package com.example.joyfulmealplanning;

import static com.google.api.ResourceProto.resource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class IngredientAdapter extends ArrayAdapter<Ingredients> {

    /*Declaration of variables*/
    private ArrayList<Ingredients> ingredients;

    private Context context;

    /**
     * Constructor of the IngredientAdapter
     * @param context
     * @param ingredients
     */
    public IngredientAdapter(@NonNull Context context, ArrayList<Ingredients> ingredients) {
        super(context,0,ingredients);

        this.context = context;
        this.ingredients = ingredients;
    }

    /**
     * Inflates a view from content.xml to display data
     * @param position
     * @param convertView
     * @param parent
     * @return {@link View}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Ingredients ingredient = ingredients.get(position);
        TextView desText, locationText, CategoryText, AmountText, UnitText, DateText;

        desText = view.findViewById(R.id.description_text);
        locationText = view.findViewById(R.id.location_text);
        CategoryText = view.findViewById(R.id.category_text);
        AmountText = view.findViewById(R.id.amount_text);
        UnitText = view.findViewById(R.id.unit_text);
        DateText = view.findViewById(R.id.date_text);



        desText.setText(ingredient.getDescription());
        locationText.setText(ingredient.getLocation());
        CategoryText.setText(ingredient.getCategory());
        AmountText.setText(ingredient.getAmount().toString());
        UnitText.setText(ingredient.getUnit());
        DateText.setText(ingredient.getBest_before_date().toString());

        return view;
    }


}
