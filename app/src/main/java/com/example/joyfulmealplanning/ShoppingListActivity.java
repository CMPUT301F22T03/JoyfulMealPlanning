package com.example.joyfulmealplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingListController ShoppingListController;
    ListView ShoppingList;
    ArrayAdapter<Ingredients> ShoppingListAdaptor;
    ArrayList<Ingredients> ShoppingIngredientDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ShoppingList = findViewById(R.id.ShoppingList);
        ShoppingListController = new ShoppingListController(this);
        ShoppingListAdaptor = ShoppingListController.getShoppingListAdaptor();
        ShoppingIngredientDataList = ShoppingListController.getShoppingIngredientDataList();
        ShoppingList.setAdapter(ShoppingListAdaptor);
    }
}