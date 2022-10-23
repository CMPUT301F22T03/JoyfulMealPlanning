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
        TextView desText, locationText, CategoryText, AmountText, Unit_CostText;

        //cardView = view.findViewById(R.id.cardView);
        //calendarView = view.findViewById(R.id.calendarView2);
        desText = view.findViewById(R.id.textView4);
        locationText = view.findViewById(R.id.textView5);
        CategoryText = view.findViewById(R.id.textView6);
        AmountText = view.findViewById(R.id.textView7);

        //calendarView.setDate(20221030);
        desText.setText(ingredients.get(0).getDescription());
        locationText.setText(ingredients.get(0).getLocation());
        CategoryText.setText(ingredients.get(0).getCategory());
        AmountText.setText(ingredients.get(0).getAmount().toString());
        //Unit_CostText.setText(ingredients.get(0).getUnit_Cost().toString());

        return view;
    }

    /**@NonNull
    @Override
    public IngredientAdapter.myVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content, parent, false);
        return new IngredientAdapter.myVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.myVH holder, int position) {
          holder.calendarView.setDate(20221030);
          holder.desText.setText(ingredients.get(0).getDescription());
          holder.locationText.setText(ingredients.get(0).getLocation());
          holder.CategoryText.setText(ingredients.get(0).getCategory());
          holder.AmountText.setText(ingredients.get(0).getAmount().toString());
          holder.Unit_CostText.setText(ingredients.get(0).getUnit_Cost().toString());



    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {

        CalendarView calendarView;
        TextView desText, locationText, CategoryText, AmountText, Unit_CostText;


        public myVH(@NonNull View itemView) {
            super(itemView);

            calendarView = itemView.findViewById(R.id.calendarView2);
            desText = itemView.findViewById(R.id.textView4);
            locationText = itemView.findViewById(R.id.textView5);
            CategoryText = itemView.findViewById(R.id.textView6);
            AmountText = itemView.findViewById(R.id.textView7);

        }
    }*/
}
