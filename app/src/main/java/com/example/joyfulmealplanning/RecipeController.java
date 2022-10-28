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
 * The controller class of Recipe
 * @author Zhaoqi, Qiaosong
 * @version 1.0
 */

public class RecipeController {
    private ArrayList<Recipe> recipeList;
    private ArrayAdapter<Recipe> recipeArrayAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipeCollectionReference = db.collection("recipe");

    public RecipeController(Context parentActivity){
        this.recipeList = new ArrayList<>();
        this.recipeArrayAdapter = new RecipeAdaptor(parentActivity, recipeList);
        initDBListener();
    }

    private void initDBListener(){
        recipeCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Log.d(TAG, String.valueOf(doc.getData().get("title")));
                    Log.d(TAG, String.valueOf(doc.getData().get("category")));
                    Log.d(TAG, String.valueOf(doc.getData().get("preparation time")));
                    //System.out.println(String.valueOf(doc.getData().get("preparation time")));
                    Log.d(TAG, String.valueOf(doc.getData().get("number of servings")));
                    //System.out.println(String.valueOf(doc.getData().get("number of servings")));
                    String RecipeTitle = (String) doc.getData().get("title");
                    String RecipeCategory = (String) doc.getData().get("category");
                    Long RecipePreparationTime = (Long)doc.getData().get("preparation time");
                    Long RecipeNumberOfServings = (Long)doc.getData().get("number of servings");

                    //recipeDataList.add(new Recipe(RecipeTitle, RecipeCategory,"",RecipeNumberOfServings,RecipePreparationTime, new ArrayList<>()));
                    recipeList.add(new Recipe(RecipeTitle, RecipeCategory,"",
                            RecipePreparationTime.intValue(),RecipeNumberOfServings.intValue(),
                            new ArrayList<>()));
                    recipeArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public Recipe getRecipeAtIndex(int idx){
        return this.recipeList.get(idx);
    }

    public ArrayList<Recipe> getRecipeList(){
        return this.recipeList;
    }

    public ArrayAdapter<Recipe> getArrayAdapter(){
        return this.recipeArrayAdapter;
    }

    private Map<String, Object> packRecipeToMap(Recipe recipe){
        Map<String, Object> packedRecipe = new HashMap<>();
        packedRecipe.put("title", recipe.getRecipeTitle());
        packedRecipe.put("comments", recipe.getRecipeComments());
        packedRecipe.put("category", recipe.getRecipeCategory());
        packedRecipe.put("preparation time", recipe.getRecipePreparationTime());
        packedRecipe.put("number of servings", recipe.getRecipeNumberOfServings());
        return packedRecipe;
    }

    public boolean addRecipe(Recipe recipe){
        for (Recipe rec : this.recipeList){
            if (rec.getRecipeTitle() == recipe.getRecipeTitle()){
                return false;
            }
        }
        Map<String, Object> packedRecipe = packRecipeToMap(recipe);
        this.recipeCollectionReference
                                .document((String) packedRecipe.get("title"))
                                .set(packedRecipe)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,packedRecipe.get("title") +
                                                "recipe has been added successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, packedRecipe.get("title") +
                                                "recipe could not be added!" + e.toString());
                                    }
                                });
        return true;
    }

    public void deleteRecipe(String recipeTitle){
        this.recipeCollectionReference.document(recipeTitle)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, recipeTitle +
                                "recipe successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document"
                                + recipeTitle + "recipe", e);
                    }
                });
    }

    public void deleteRecipe(int idx){
        Recipe selectedRecipe = this.recipeList.get(idx);
        String title = selectedRecipe.getRecipeTitle();
        deleteRecipe(title);
    }

}
