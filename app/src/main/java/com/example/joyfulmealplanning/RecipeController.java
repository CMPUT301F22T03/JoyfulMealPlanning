package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The controller class of Recipe
 * @author Zhaoqi, Qiaosong
 * @version 1.0
 */

public class RecipeController {
    private boolean DBActionComplete = false;
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
                    Long RecipePreparationTime = (Long) doc.getData().get("preparation time");
                    Long RecipeNumberOfServings = (Long) doc.getData().get("number of servings");
                    String RecipeComments = (String) doc.getData().get("comments");
                    ArrayList<Ingredients> retrievedIngredients = new ArrayList<>();
                    retrieveIngredientList(RecipeTitle, retrievedIngredients);
                    //recipeDataList.add(new Recipe(RecipeTitle, RecipeCategory,"",RecipeNumberOfServings,RecipePreparationTime, new ArrayList<>()));
                    recipeList.add(new Recipe(RecipeTitle, RecipeCategory,RecipeComments,
                            RecipePreparationTime.intValue(),RecipeNumberOfServings.intValue(),
                            retrievedIngredients));
                    recipeArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void retrieveIngredientList(String recipeTitle, ArrayList<Ingredients> ingredientList){
        recipeCollectionReference.document(recipeTitle).collection("ingredient")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Ingredients ingredient = new Ingredients((String) documentSnapshot.getData().get("description"),
                                    (int)(long) documentSnapshot.getData().get("amount"), (String) documentSnapshot.getData().get("unit"),
                                    (String) documentSnapshot.getData().get("category"));
                            ingredientList.add(ingredient);
                        }
                        //DBActionComplete = true;
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
        addIngredientListTo(recipe.getRecipeTitle(), recipe.getRecipeIngredientsList());
        return true;
    }

    private void deleteOrUpdateRecipe(String recipeTitle, boolean updateRecipe, @Nullable Recipe newRecipe){
        this.recipeCollectionReference.document(recipeTitle).collection("ingredient").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            recipeCollectionReference.document(recipeTitle).collection("ingredient")
                                    .document(doc.getId()).delete();
                        }
                        Log.d(TAG, recipeTitle +
                                "recipe ingredient list successfully deleted!");
                        /*
                        delete the whole recipe only after its ingredients has been deleted
                         */
                        recipeCollectionReference.document(recipeTitle)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, recipeTitle +
                                                "recipe successfully deleted!");
                                        /*
                                        if updating a recipe, add the recipe only after the old one
                                        has been deleted.
                                         */
                                        if (updateRecipe){
                                            addRecipe(newRecipe);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document"
                                                + recipeTitle + "recipe", e);
                                    }
                                });
                        DBActionComplete = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting ingredient list of "
                                + recipeTitle + "recipe", e);
                        DBActionComplete = true;
                    }
                });

        return;
    }

    public void deleteRecipe(int idx){
        Recipe selectedRecipe = this.recipeList.get(idx);
        String title = selectedRecipe.getRecipeTitle();
        deleteOrUpdateRecipe(title, false, null);
    }

    public void deleteRecipe(String recipeTitle){
        deleteOrUpdateRecipe(recipeTitle, false, null);
    }

    public void updateRecipe(String oldRecipeTitle, Recipe updatedRecipe){
        deleteOrUpdateRecipe(oldRecipeTitle, true, updatedRecipe);
    }

    private void addIngredientListTo(String recipeTitle, ArrayList<Ingredients> ingredientList){
        for (Ingredients ingredient : ingredientList){
            Map<String, Object> packedIngredient = new HashMap<>();
            packedIngredient.put("description", ingredient.getDescription());
            packedIngredient.put("amount", ingredient.getAmount());
            packedIngredient.put("unit", ingredient.getUnit());
            packedIngredient.put("category", ingredient.getCategory());
            this.recipeCollectionReference.document(recipeTitle).collection("ingredient")
                    .document(ingredient.getDescription())
                    .set(packedIngredient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, ingredient.getDescription() +
                                    "successfully added to" + recipeTitle);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "failed on adding" + ingredient.getDescription() +
                                    "to" + recipeTitle);
                        }
                    });
        }
    }

}
