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

    private ArrayList<Ingredients> ingredients;

    private Context context;

    public IngredientAdapter(@NonNull Context context, ArrayList<Ingredients> ingredients) {
        super(context,0,ingredients);

        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        //CardView cardView;
        //CalendarView calendarView;
        TextView desText, locationText, CategoryText, AmountText, UnitText, DateText;

        //cardView = view.findViewById(R.id.cardView);
        //calendarView = view.findViewById(R.id.calendarView2);
        desText = view.findViewById(R.id.textView4);
        locationText = view.findViewById(R.id.textView5);
        CategoryText = view.findViewById(R.id.textView6);
        AmountText = view.findViewById(R.id.textView7);
        UnitText = view.findViewById(R.id.textView8);
        DateText = view.findViewById(R.id.textView9);


        //calendarView.setDate(20221030);
        desText.setText(ingredients.get(0).getDescription());
        locationText.setText(ingredients.get(0).getLocation());
        CategoryText.setText(ingredients.get(0).getCategory());
        AmountText.setText(ingredients.get(0).getAmount().toString());
        UnitText.setText(ingredients.get(0).getUnit());
        DateText.setText(ingredients.get(0).getBest_before_date().toString());

        return view;
    }


}
