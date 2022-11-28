package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * The Ingredients controller class, responsible for the modification of a list of
 * ingredients in FireStore database and the connection with a local list of ingredients.
 * @author Zhaoqi Ma & Fan Zhu & Qiaosong Deng
 * @version 1.1
 * @since 2022-10-28
 */
public class IngredientController {
    /*Declaration of variables*/
    private ArrayList<Ingredients> ingredientList;
    private ArrayAdapter<Ingredients> ingredientsArrayAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ingredientsCollectionReference = db.collection("ingredient");
//, String collectionPath
    /*Constructor*/
    public IngredientController(Context parentActivity){
        this.ingredientList = new ArrayList<>();
        //this.ingredientsCollectionReference = this.db.collection(collectionPath);
        this.ingredientsArrayAdapter = new IngredientAdapter(parentActivity, ingredientList);
        initDBListener();
    }

    /**
    * Initialize Database and add ingredients into the ArrayList
     */
    private void initDBListener(){
        ingredientsCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ingredientList.clear();

                for(QueryDocumentSnapshot doc: value){
                    String desc = (String) doc.getData().get("description");
                    Integer amount = Integer.valueOf(doc.getData().get("amount").toString());
                    Integer bbdate = Integer.valueOf(doc.getData().get("best before date").toString());
                    String category = (String) doc.getData().get("category");
                    String location = (String) doc.getData().get("location");
                    String unit = (String) doc.getData().get("unit");

                    ingredientList.add(new Ingredients(desc, bbdate, location, category, amount, unit));
                    ingredientsArrayAdapter.notifyDataSetChanged();

                }

            }
        });
    }

    /**
     * Switch the path of the DB collection to newPath
     * @param newPath
     */
    public void switchDBPath(String newPath){
        this.ingredientsCollectionReference = this.db.collection(newPath);
    }

    /**
     * Returns the Ingredient at given index
     * @param idx
     * @return {@link Ingredients}
     */
    public Ingredients getIngredientAtIndex(int idx){
        return this.ingredientList.get(idx);
    }

    /**
     * Fetches the IngredientList
     * @return {@link ArrayList}
     */
    public ArrayList<Ingredients> getIngredientList(){
        return this.ingredientList;
    }


    /**
     * Returns the adapter for the Ingredients
     * @return {@link ArrayAdapter}
     */
    public ArrayAdapter<Ingredients> getArrayAdapter(){
        return this.ingredientsArrayAdapter;
    }

    /**
     * Packs the ingredients into a HashMap
     * @param ingredients
     * @return {@link Map}
     */
    private Map<String, Object> packIngredientsToMap(Ingredients ingredients){
        Map<String, Object> packedIngredients = new HashMap<>();
        packedIngredients.put("description", ingredients.getDescription());
        packedIngredients.put("amount", ingredients.getAmount());
        packedIngredients.put("category", ingredients.getCategory());
        packedIngredients.put("best before date", ingredients.getBest_before_date());
        packedIngredients.put("location", ingredients.getLocation());
        packedIngredients.put("unit", ingredients.getUnit());
        return packedIngredients;
    }

    /**
     * Adds a given ingredient to the ingredientList as well as the DB collections
     * @param ingredients
     * @return {@link Boolean}
     */

    public boolean addIngredient(Ingredients ingredients){
        for (Ingredients ing : this.ingredientList){
            if (ing.getDescription().toLowerCase(Locale.ROOT).equals(ingredients.getDescription().toLowerCase(Locale.ROOT))){
                return false;
            }
        }
        Map<String, Object> packedIngredients = packIngredientsToMap(ingredients);
        this.ingredientsCollectionReference
                .document((String) packedIngredients.get("description"))
                .set(packedIngredients)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,packedIngredients.get("description") +
                                " ingredient has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, packedIngredients.get("title") +
                                " ingredient could not be added!" + e.toString());
                    }
                });
        return true;
    }

    /**
     * Adds a given ingredient to the ingredientList as well as the DB collections From shopping list
     * @param ingredient
     * @return {@link Boolean}
     */
    public boolean addIngredientShoppingListVersion(Ingredients ingredient){
        for (Ingredients ing : this.ingredientList){
            if (ing.getDescription() == ingredient.getDescription()){
                ingredient.setAmount(ing.getAmount()+ingredient.getAmount());
            }
        }
        Map<String, Object> packedIngredients = packIngredientsToMap(ingredient);
        this.ingredientsCollectionReference
                .document((String) packedIngredients.get("description"))
                .set(packedIngredients)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,packedIngredients.get("description") +
                                " ingredient has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, packedIngredients.get("title") +
                                " ingredient could not be added!" + e.toString());
                    }
                });
        return true;
    }

    /**
     * Deletes an ingredient at given index
     * @param idx
     */
    public void deleteIngredient(int idx){
        Ingredients selectedIngredient = this.ingredientList.get(idx);
        String title = selectedIngredient.getDescription();
        //calls internal method and set the method to delete mode
        deleteOrUpdateIngredient(title, false, null);
    }

    /**
     * public method to update an ingredient that was originally tiled by oldRecipeTitle
     * @param oldIngredientDesc
     * @param updatedIngredient
     */
    public void updateIngredient(String oldIngredientDesc, Ingredients updatedIngredient){
        deleteOrUpdateIngredient(oldIngredientDesc, true, updatedIngredient);
    }

    /**
     * private method that either deletes or updates an existing recipe
     * @param oldIngredientDesc
     * @param updateIngredient
     * @param updatedIngredient
     */
    private void deleteOrUpdateIngredient(String oldIngredientDesc, boolean updateIngredient,
                                         @Nullable Ingredients updatedIngredient){
        this.ingredientsCollectionReference.document(oldIngredientDesc)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, oldIngredientDesc +
                                " ingredient successfully deleted!");
                        if (updateIngredient){
                            addIngredient(updatedIngredient);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document"
                                + oldIngredientDesc + " ingredient", e);
                    }
                });
    }

    /**
     * Adds an Ingredients object to the local ingredientList
     * @param ingredients
     */
    public void addToLocalList(Ingredients ingredients){
        this.ingredientList.add(ingredients);
    }

    /**
     * Removes an Ingredients object from the local ingredientList at the given index
     * @param idx
     */
    public void deleteFromLocalList(int idx){
        this.ingredientList.remove(idx);
    }

    /**
     * Public method that sort the ingredientList by the descriptions
     */
    public void sortByDescription(){
        Collections.sort(ingredientList, new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredients, Ingredients t1) {
                return ingredients.getDescription().toLowerCase().compareTo(t1.getDescription().toLowerCase());
            }
        });
        ingredientsArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Public method that sort the ingredientList according the Best Before Date
     */
    public void sortByBBD(){
        Collections.sort(ingredientList, new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredients, Ingredients t1) {
                return ingredients.getBest_before_date().compareTo(t1.getBest_before_date());
            }
        });
        ingredientsArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Public method that sort the ingredientList by Location
     */
    public void sortByLocation(){
        Collections.sort(ingredientList, new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredients, Ingredients t1) {
                return ingredients.getLocation().toLowerCase().compareTo(t1.getLocation().toLowerCase());
            }
        });
        ingredientsArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Public method that sort the ingredientList by Category
     */
    public void sortByCategory(){
        Collections.sort(ingredientList, new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredients, Ingredients t1) {
                return ingredients.getCategory().toLowerCase().compareTo(t1.getCategory().toLowerCase());
            }
        });
        ingredientsArrayAdapter.notifyDataSetChanged();
    }


}
