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
import java.util.HashMap;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListAddIngredientFragment.OnFragmentInteractionListener{

    private ShoppingListController ShoppingListController;
    ListView ShoppingList;
    ArrayAdapter<Ingredients> ShoppingListAdaptor;
    ArrayList<Ingredients> ShoppingIngredientDataList;
    private HashMap<String,Ingredients> IngredientFromIngredientList;

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
                IngredientFromIngredientList = ShoppingListController.getIngredientFromIngredientList();
                Log.d(TAG,"Try to get Adaptor!!!!!!!!!!!!");
                ShoppingListAdaptor = new ShoppingListAdaptor(ShoppingListActivity.this,ShoppingIngredientDataList);
                ShoppingListAdaptor = ShoppingListController.getShoppingListAdaptor();
                ShoppingList.setAdapter(ShoppingListAdaptor);
            }
        }, 1500);

//        ShoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                ShoppingListAddIngredientFragment fragment =
//                        new ShoppingListAddIngredientFragment().newInstance(ShoppingListAdaptor.getItem(position),getApplicationContext());
//                fragment.show(getSupportFragmentManager(), "Edit Ingredient");
//                return true;
//            }
//        });

        ShoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                ShoppingListAddIngredientFragment fragment =
                        new ShoppingListAddIngredientFragment().newInstance(ShoppingListAdaptor.getItem(position));
                fragment.show(getSupportFragmentManager(), "Edit Ingredient");
                return true;
            }
        });
    }


    /**
     * Add ingredient from ShoppingListAddIngredientFragment into DB when OK is pressed
     * @param ingredients
     */
    @Override
    public void onOkPressed(Ingredients ingredients) {
        ShoppingListController.addIngredientIntoDBShoppingListVersion(ingredients);
    }
}