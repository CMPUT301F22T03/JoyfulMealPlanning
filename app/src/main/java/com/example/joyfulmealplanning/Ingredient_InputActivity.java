package com.example.joyfulmealplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Ingredient_InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_input);

        EditText description, date, location, amount, unit, category;

        Button Add = findViewById(R.id.iAddbtn);

        description = findViewById(R.id.iDescription);
        date = findViewById(R.id.iDate);
        location = findViewById(R.id.iLocation);
        amount = findViewById(R.id.iAmount);
        unit = findViewById(R.id.iUnit);
        category = findViewById(R.id.iCategory);

        FirebaseFirestore ff = FirebaseFirestore.getInstance();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description_st = description.getText().toString();
                String date_st = date.getText().toString();
                String location_st = location.getText().toString();
                String amount_st = amount.getText().toString();
                String unit_st = unit.getText().toString();
                String category_st = category.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("description", description_st);
                map.put("best before date", date_st);
                map.put("location", location_st);
                map.put("amount", amount_st);
                map.put("unit", unit_st);
                map.put("category", category_st);

                ff.collection("ingredient").add(map);

                Intent inputIntent = new Intent(view.getContext(), IngredientsActivity.class);
                startActivity(inputIntent);
            }
        });
    }
}