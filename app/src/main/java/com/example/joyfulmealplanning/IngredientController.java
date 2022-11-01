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
import java.util.HashMap;
import java.util.Map;
/**
 * The Ingredients controller class, responsible for the modification of a list of
 * ingredients in FireStore database and the connection with a local list of ingredients.
 * @author Zhaoqi Ma & Fan Zhu
 * @version 1.0
 * @since 2022-10-28
 */
public class IngredientController {
    /*Declaration of variables*/
    private ArrayList<Ingredients> ingredientList;
    private ArrayAdapter<Ingredients> ingredientsArrayAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ingredientsCollectionReference = db.collection("ingredients");
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
                    Log.d(TAG, String.valueOf(doc.getData().get("description")));
                    Log.d(TAG, String.valueOf(doc.getData().get("amount")));
                    Log.d(TAG, String.valueOf(doc.getData().get("best before date")));
                    Log.d(TAG, String.valueOf(doc.getData().get("category")));
                    Log.d(TAG, String.valueOf(doc.getData().get("location")));
                    Log.d(TAG, String.valueOf(doc.getData().get("unit")));
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
            if (ing.getDescription() == ingredients.getDescription()){
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
                                "ingredient has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, packedIngredients.get("title") +
                                "ingredient could not be added!" + e.toString());
                    }
                });
        return true;
    }

    /**
     * Takes the description of ingredient as the parameter and delete the document from the DB
     * @param ingredientDesc
     */
    public void deleteIngredient(String ingredientDesc){
        this.ingredientsCollectionReference.document(ingredientDesc)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, ingredientDesc +
                                "ingredient successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document"
                                + ingredientDesc + "ingredient", e);
                    }
                });
    }

    /**
     * Deletes an ingredient at given index
     * @param idx
     */
    public void deleteIngredient(int idx){
        Ingredients selectedIngredient = this.ingredientList.get(idx);
        String desc = selectedIngredient.getDescription();
        deleteIngredient(desc);
    }

}
