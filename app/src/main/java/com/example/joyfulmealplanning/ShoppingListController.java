package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListController {

    ArrayAdapter<Ingredients> ShoppingListAdaptor;
    private HashMap<String,Ingredients> IngredientFromRecipeList;//(ingredientDescription, Ingredients object)
    private HashMap<String,Ingredients> IngredientFromIngredientList;
    private ArrayList<Ingredients> ShoppingIngredientDataList;
    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipeCollectionReference = db.collection("recipe");
    final CollectionReference ingredientsCollectionReference = db.collection("ingredient");

    public ShoppingListController(Context context) {
        this.context = context;
        IngredientFromRecipeList = new HashMap<String,Ingredients>();
        IngredientFromIngredientList = new HashMap<String,Ingredients>();
        ShoppingIngredientDataList = new ArrayList<>();
        ShoppingListAdaptor = new ShoppingListAdaptor(context,ShoppingIngredientDataList);
        ShoppingIngredientDataInit();
    }

    public void ShoppingIngredientDataInit(){
        db.collectionGroup("ingredient").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                IngredientFromRecipeList.clear();
                IngredientFromIngredientList.clear();
                ShoppingIngredientDataList.clear();

                for(QueryDocumentSnapshot ingredient: value) {
                    Log.d(TAG, String.valueOf(ingredient.getData().get("description")));
                    String description = (String) ingredient.getData().get("description");
                    Log.d(TAG, String.valueOf(ingredient.getData().get("amount")));
                    Long amount = (Long) ingredient.getData().get("amount");
                    Log.d(TAG, String.valueOf(ingredient.getData().get("unit")));
                    String unit = (String) ingredient.getData().get("unit");
                    Log.d(TAG, String.valueOf(ingredient.getData().get("category")));
                    String category = (String) ingredient.getData().get("category");
                    Log.d(TAG, String.valueOf(ingredient.getData().get("location")));
                    String location = (String) ingredient.getData().get("location");
                    System.out.println(ingredient + "'s ingredient: " + description
                            +" "+amount
                            +" "+unit
                            +" "+category
                            +" "+location);

                    String Path = ingredient.getReference().getPath();
                    System.out.println("ingredientTitle: " + description + " from " + Path);
                    if (description != null && amount != null){
                        if(Path.substring(0,6).equals("recipe")){//from recipe
                            if (IngredientFromRecipeList.containsKey(description)){
                                Ingredients OneIngredient = new Ingredients(description,
                                        00000000,
                                        "",
                                        category,
                                        IngredientFromRecipeList.get(description).getAmount() + amount.intValue(),
                                        unit);
                                IngredientFromRecipeList.put(description,OneIngredient);
                            }else{
                                Ingredients OneIngredient = new Ingredients(description, 00000000,
                                        "", category, amount.intValue(), unit);
                                IngredientFromRecipeList.put(description,OneIngredient);
                            }
                        }else{//from ingredient
                            if (description != null && amount != null){
                                Ingredients OneIngredient = new Ingredients(description, 00000000,
                                        "", category, amount.intValue(), unit);
                                IngredientFromIngredientList.put(description,OneIngredient);
                            }
                        }
                    }
                }
                printDataList(IngredientFromIngredientList);
                printDataList(IngredientFromRecipeList);

                //Subtract IngredientFromIngredientList from IngredientFromRecipeList
                for (Map.Entry<String, Ingredients> IngredientFromIngredient: IngredientFromIngredientList.entrySet()){
                    String description = IngredientFromIngredient.getKey();
                    Ingredients ingredients = IngredientFromIngredient.getValue();
                    if (IngredientFromRecipeList.containsKey(description)){
                        Ingredients OneIngredient = new Ingredients(
                                IngredientFromRecipeList.get(description).getDescription(),
                                IngredientFromRecipeList.get(description).getBest_before_date(),
                                IngredientFromRecipeList.get(description).getLocation(),
                                IngredientFromRecipeList.get(description).getCategory(),
                                IngredientFromRecipeList.get(description).getAmount()-ingredients.getAmount(),
                                IngredientFromRecipeList.get(description).getUnit());
                        IngredientFromRecipeList.put(description,OneIngredient);
                    }
                }
                //Add ingredients that need to been bought.
                for  (Map.Entry<String, Ingredients> IngredientFromRecipe: IngredientFromRecipeList.entrySet()){
                    if (IngredientFromRecipe.getValue().getAmount()>0){
                        ShoppingIngredientDataList.add(IngredientFromRecipe.getValue());
                    }
                }
                ShoppingListAdaptor.notifyDataSetChanged();
            }

        });

//        recipeCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
//                ShoppingIngredientDataList.clear();
//                for(QueryDocumentSnapshot recipe: queryDocumentSnapshots){
//                    Log.d(TAG, String.valueOf(recipe.getData().get("title")));
//                    String RecipeTitle = (String) recipe.getData().get("title");
//                    System.out.println("RecipeTitle: "+ RecipeTitle);
//
//                    final CollectionReference ingredientListCollectionReference = db.collection("recipe").
//                            document(RecipeTitle).collection("ingredient list");
//                    ingredientListCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            for(QueryDocumentSnapshot ingredientOfThisRecipe: value){
//                                Log.d(TAG, String.valueOf(ingredientOfThisRecipe.getData().get("description")));
//                                String ingredientDescription = (String) ingredientOfThisRecipe.getData().get("description");
//                                Log.d(TAG, String.valueOf(ingredientOfThisRecipe.getData().get("amount")));
//                                Long amount = (Long) ingredientOfThisRecipe.getData().get("amount");
//                                Log.d(TAG, String.valueOf(ingredientOfThisRecipe.getData().get("unit")));
//                                String unit = (String) ingredientOfThisRecipe.getData().get("unit");
//                                Log.d(TAG, String.valueOf(ingredientOfThisRecipe.getData().get("category")));
//                                String category = (String) ingredientOfThisRecipe.getData().get("category");
//                                Log.d(TAG, String.valueOf(ingredientOfThisRecipe.getData().get("location")));
//                                String location = (String) ingredientOfThisRecipe.getData().get("location");
//                                System.out.println(RecipeTitle + "'s ingredient: " + ingredientDescription
//                                        +" "+amount
//                                        +" "+unit
//                                        +" "+category
//                                        +" "+location);
//
//                                if (ingredientDescription != null && amount != null){
//                                    if (ShoppingIngredientDataList.containsKey(ingredientDescription)){
//                                        Ingredients OneIngredient = new Ingredients(ingredientDescription,
//                                                00000000,
//                                                "",
//                                                category,
//                                                ShoppingIngredientDataList.get(ingredientDescription).getAmount() + amount.intValue(),
//                                                unit);
//                                        ShoppingIngredientDataList.put(ingredientDescription,OneIngredient);
//                                    }else{
//                                        Ingredients OneIngredient = new Ingredients(ingredientDescription, 00000000,
//                                                "", category, amount.intValue(), unit);
//                                        ShoppingIngredientDataList.put(ingredientDescription,OneIngredient);
//                                    }
//                                    printDataList();
//                                }
//
//                            }
//
//                        }
//
//                    });
//
//                }
//            }
//
//        });

//        ingredientsCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for(QueryDocumentSnapshot ingredient: value){
//                    Log.d(TAG, String.valueOf(ingredient.getData().get("description")));
//                    String ingredientDescription = (String) ingredient.getData().get("description");
//                    Log.d(TAG, String.valueOf(ingredient.getData().get("amount")));
//                    Long amount = (Long) ingredient.getData().get("amount");
//                    Log.d(TAG, String.valueOf(ingredient.getData().get("unit")));
//                    String unit = (String) ingredient.getData().get("unit");
//                    Log.d(TAG, String.valueOf(ingredient.getData().get("category")));
//                    String category = (String) ingredient.getData().get("category");
//                    Log.d(TAG, String.valueOf(ingredient.getData().get("location")));
//                    String location = (String) ingredient.getData().get("location");
//                    if (ingredientDescription != null && amount != null){
//                        if (ShoppingIngredientDataList.containsKey(ingredientDescription)){
//                            Ingredients OneIngredient = new Ingredients(ingredientDescription,
//                                    00000000,
//                                    "",
//                                    category,
//                                    ShoppingIngredientDataList.get(ingredientDescription).getAmount()-amount.intValue(),
//                                    unit);
//                            ShoppingIngredientDataList.put(ingredientDescription,OneIngredient);
//                        }
//                    }
//                }
//            }
//        });
    }

    public void printDataList(HashMap<String,Ingredients> DataList){
        System.out.println("####################################################" + DataList.size());
        for (Map.Entry<String, Ingredients> entry: DataList.entrySet()){
            System.out.println("Ingredient Desp: " + entry.getKey() + " Amount: " + entry.getValue().getAmount());
        }
    }

    public ArrayAdapter<Ingredients> getShoppingListAdaptor(){
        return ShoppingListAdaptor;
    }

    public ArrayList<Ingredients> getShoppingIngredientDataList(){
        return ShoppingIngredientDataList;
    }
}
