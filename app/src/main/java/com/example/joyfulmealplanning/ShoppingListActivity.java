package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity implements IngredientFragment.OnFragmentInteractionListener{

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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //ShoppingListAdaptor = ShoppingListController.getShoppingListAdaptor();
                ShoppingIngredientDataList = ShoppingListController.getShoppingIngredientDataList();
                Log.d(TAG,"Try to get Adaptor!!!!!!!!!!!!");
                ShoppingListAdaptor = new ShoppingListAdaptor(ShoppingListActivity.this,ShoppingIngredientDataList);
                ShoppingListAdaptor = ShoppingListController.getShoppingListAdaptor();
                ShoppingList.setAdapter(ShoppingListAdaptor);
            }
        }, 2000);
        ShoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                ShoppingListFragment fragment =
                        new ShoppingListFragment().newInstance(ShoppingListAdaptor.getItem(position));
                fragment.show(getSupportFragmentManager(), "Edit Ingredient");
                return true;
            }
        });
    }

    @Override
    public void onOkPressed(String oldIngredientDesc, Ingredients newIngredients) {

    }
}