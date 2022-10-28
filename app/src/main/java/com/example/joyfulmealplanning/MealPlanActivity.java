package com.example.joyfulmealplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MealPlanActivity extends AppCompatActivity {

    ListView mealPlanList;
    FloatingActionButton mealPlanAddFAB;
    ArrayAdapter<MealPLan> mealPLanAdapter;
    ArrayList<MealPLan> mealPLanDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        mealPlanList = findViewById(R.id.mealPlan_list);
        MealPlanController MealPlanController = new MealPlanController(this);
        mealPLanAdapter = MealPlanController.getMealPLanAdapter();
        mealPLanDataList = MealPlanController.getMealPLanDataList();
        mealPlanList.setAdapter(mealPLanAdapter);

        //Long click to delete an item in the listView.
        mealPlanList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String mealPlanID = mealPLanDataList.get(position).getMealPlanID();
                final String ID = mealPLanDataList.get(position).getID();
                MealPlanController.Delete(mealPlanID,ID);
                return true;
            }
        });

        mealPlanAddFAB = findViewById(R.id.mealPlanAddFAB);
        mealPlanAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanController.AddMealPlanFragment(getSupportFragmentManager());
            }
        });
    }
}