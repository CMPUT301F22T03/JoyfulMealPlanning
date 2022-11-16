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

public class MealPLanAdaptor extends ArrayAdapter<MealPLan> {

    private ArrayList<MealPLan> MealPlans;
    private Context context;

    public MealPLanAdaptor(@NonNull Context context, @NonNull ArrayList<MealPLan> MealPlans) {
        super(context, 0, MealPlans);
        this.context = context;
        this.MealPlans = MealPlans;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable android.view.View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.meal_plan_content, parent,false);
        }

        MealPLan mealPlan = MealPlans.get(position);

        TextView mealPlanActivityTitle = view.findViewById(R.id.mealPlanTitle);
        TextView mealPlanNumberOfServings = view.findViewById(R.id.mealPlanNumberOfServings);
        TextView mealPlanType = view.findViewById(R.id.mealPlanType);
        TextView mealPlanDate = view.findViewById(R.id.mealPlanDate);

        mealPlanActivityTitle.setText("Title: " + mealPlan.getID()+"   ");
        mealPlanNumberOfServings.setText("Number of Servings: " + mealPlan.getNumberOfServings()+"   ");
        mealPlanType.setText("Type: " + mealPlan.getType());
        mealPlanDate.setText("Date: " + mealPlan.getDate());

        return view;
    }
}
