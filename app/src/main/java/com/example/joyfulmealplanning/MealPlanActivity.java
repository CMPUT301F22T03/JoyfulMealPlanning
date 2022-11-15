package com.example.joyfulmealplanning;

import androidx.appcompat.app.AppCompatActivity;

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
 *
 */
public class MealPlanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView mealPlanList;
    FloatingActionButton mealPlanAddFAB;
    ArrayAdapter<MealPLan> mealPLanAdapter;
    ArrayList<MealPLan> mealPLanDataList;
    Spinner mealPlanSortSpinner;
    MealPlanController MealPlanController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        mealPlanList = findViewById(R.id.mealPlan_list);
        MealPlanController = new MealPlanController(this);
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

        mealPlanSortSpinner = (Spinner)findViewById(R.id.mealPlan_list_sort_spinner);
        ArrayAdapter<CharSequence> mealPlanSortOption = ArrayAdapter.createFromResource(
                this,
                R.array.mealPlanSortOptionList,
                android.R.layout.simple_spinner_item);
        mealPlanSortOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealPlanSortSpinner.setAdapter(mealPlanSortOption);
        mealPlanSortSpinner.setOnItemSelectedListener(this);
    }


    //For mealPlanSortSpinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0){//ID
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            MealPlanController.sortByID();
            mealPLanAdapter.notifyDataSetChanged();
            mealPLanDataList = MealPlanController.getMealPLanDataList();
        }else if (position == 1){//type
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            MealPlanController.sortByType();
            mealPLanAdapter.notifyDataSetChanged();
            mealPLanDataList = MealPlanController.getMealPLanDataList();
        }else if (position == 2){//number of serving
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            MealPlanController.sortByNOS();
            mealPLanAdapter.notifyDataSetChanged();
            mealPLanDataList = MealPlanController.getMealPLanDataList();
        }else{
            Toast.makeText(this,"sorted by "+adapterView.getItemAtPosition(position).toString() ,Toast.LENGTH_LONG).show();
            MealPlanController.sortByDate();;
            mealPLanAdapter.notifyDataSetChanged();
            mealPLanDataList = MealPlanController.getMealPLanDataList();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}