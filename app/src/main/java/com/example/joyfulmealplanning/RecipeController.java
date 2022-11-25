package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The controller class of Recipe, responsible for adding, deleting and updating recipes.
 * @author Zhaoqi, Qiaosong, Fan Zhu
 * @version 1.1
 */

public class RecipeController {
    /*Declaration of variables*/
    private boolean DBActionComplete = false;
    private ArrayList<Recipe> recipeList;
    private ArrayAdapter<Recipe> recipeArrayAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipeCollectionReference = db.collection("recipe");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    /*Constructor*/
    public RecipeController(Context parentActivity){
        this.recipeList = new ArrayList<>();
        this.recipeArrayAdapter = new RecipeAdaptor(parentActivity, recipeList);
        initDBListener();
    }

    /**Private method to initialize the local list by filling with data from the Recipe Collection in Firestore.
    At the same time set a listener on the Recipe collection to automatically synchronize local list with cloud data*/
    private void initDBListener(){
        recipeCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String RecipeTitle = (String) doc.getData().get("title");
                    String RecipeCategory = (String) doc.getData().get("category");
                    Long RecipePreparationTime = (Long) doc.getData().get("preparation time");
                    Long RecipeNumberOfServings = (Long) doc.getData().get("number of servings");
                    String RecipeComments = (String) doc.getData().get("comments");
                    ArrayList<Ingredients> retrievedIngredients = new ArrayList<>();
                    //retrieve the ingredient list for this recipe
                    retrieveIngredientList(RecipeTitle, retrievedIngredients);
                    recipeList.add(new Recipe(RecipeTitle, RecipeCategory,RecipeComments,
                            RecipePreparationTime.intValue(),RecipeNumberOfServings.intValue(),
                            retrievedIngredients, null));
                    recipeArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**Private method to retrieve the ingredient list of a recipe (keyed by its title) and store it into
    ingredientList*/
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

    /**
     * Public method to return the recipe at index idx
     * @param idx
     * @return an index idx {@link Integer}
     */
    public Recipe getRecipeAtIndex(int idx){
        return this.recipeList.get(idx);
    }

    /**Public method to return the whole local Recipe list*/
    public ArrayList<Recipe> getRecipeList(){
        return this.recipeList;
    }

    /**Public method to return the array adapter for the Recipe list*/
    public ArrayAdapter<Recipe> getArrayAdapter(){
        return this.recipeArrayAdapter;
    }


    /**
     * Private method to map all attributes (except ingredient list and image) of a Recipe object to
     * a HashMap
     * @param recipe
     * @return packedRecipe {@link Map}
     */
    private Map<String, Object> packRecipeToMap(Recipe recipe){
        Map<String, Object> packedRecipe = new HashMap<>();
        packedRecipe.put("title", recipe.getRecipeTitle());
        packedRecipe.put("comments", recipe.getRecipeComments());
        packedRecipe.put("category", recipe.getRecipeCategory());
        packedRecipe.put("preparation time", recipe.getRecipePreparationTime());
        packedRecipe.put("number of servings", recipe.getRecipeNumberOfServings());
        return packedRecipe;
    }



    /**
     * public boolean method for adding a recipe
     * @param recipe
     * @return {@link Boolean}
     */
    public boolean addRecipe(Recipe recipe){
        for (Recipe rec : this.recipeList){
            if (rec.getRecipeTitle().toLowerCase(Locale.ROOT).equals(recipe.getRecipeTitle().toLowerCase(Locale.ROOT))){
                return false;
            }
        }
        //add image first before adding anything else as this process takes the longest time, and
        //doing the operations out of order can cause failure on image uploading

        //if an image has been chosen
        if (recipe.getImage() != null){
            StorageReference imageRef = storageReference.child(recipe.getRecipeTitle() + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            recipe.getImage().compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] image = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(image);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "image was not added for " + recipe.getRecipeTitle());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "image added for " + recipe.getRecipeTitle());
                    //after the image has been successfully uploaded, add the ingredient list
                    addIngredientList(recipe);
                }
            });
        } else {
            addIngredientList(recipe);
        }
        return true;
    }


    /**
     * private method that aids the public addRecipe method, this method adds all recipe ingredients
     * @param recipe
     */
    private void addIngredientList(Recipe recipe){
        //if there is a list of ingredients
        if (recipe.getRecipeIngredientsList().size() != 0){
            for (Ingredients ingredient : recipe.getRecipeIngredientsList()){
                Map<String, Object> packedIngredient = new HashMap<>();
                packedIngredient.put("description", ingredient.getDescription());
                packedIngredient.put("amount", ingredient.getAmount());
                packedIngredient.put("unit", ingredient.getUnit());
                packedIngredient.put("category", ingredient.getCategory());
                this.recipeCollectionReference.document(recipe.getRecipeTitle()).collection("ingredient")
                        .document(ingredient.getDescription())
                        .set(packedIngredient)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, ingredient.getDescription() +
                                        " successfully added to " + recipe.getRecipeTitle());
                                //after the list has been successfully uploaded,
                                // start uploading other attributes
                                addRecipeAttributes(recipe);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "failed on adding " + ingredient.getDescription() +
                                        " to " + recipe.getRecipeTitle());
                            }
                        });
            }
        } else {
            //if there is no ingredient list, directly upload other attributes
            addRecipeAttributes(recipe);
        }
    }

    /**
     * private method that aids the public addRecipe method, this method adds all attributes of recipe
     * @param recipe
     */
    private void addRecipeAttributes(Recipe recipe){
        Map<String, Object> packedRecipe = packRecipeToMap(recipe);
        this.recipeCollectionReference
                .document((String) packedRecipe.get("title"))
                .set(packedRecipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,packedRecipe.get("title") +
                                " recipe has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, packedRecipe.get("title") +
                                " recipe could not be added!" + e.toString());
                    }
                });
    }

    /**
     * public method for deleting a recipe at index idx
     * @param idx
     */
    public void deleteRecipe(int idx){
        Recipe selectedRecipe = this.recipeList.get(idx);
        String title = selectedRecipe.getRecipeTitle();
        //calls internal method and set the method to delete mode
        deleteOrUpdateRecipe(title, false, null);
    }

    /**
     * public method to update a recipe that was originally tiled by oldRecipeTitle
     * @param oldRecipeTitle
     * @param updatedRecipe
     */
    public void updateRecipe(String oldRecipeTitle, Recipe updatedRecipe){
        //calls internal method and set the method to update mode
        deleteOrUpdateRecipe(oldRecipeTitle, true, updatedRecipe);
    }

    /**
     * private method that either deletes or updates an existing recipe
     * @param oldRecipeTitle
     * @param updateRecipe
     * @param updatedRecipe
     */
    private void deleteOrUpdateRecipe(String oldRecipeTitle, boolean updateRecipe, @Nullable Recipe updatedRecipe){
        //obtain the ingredient list of this recipe
        this.recipeCollectionReference.document(oldRecipeTitle).collection("ingredient").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //first delete the ingredient list, otherwise the list may fail to delete as the other deleting threads are faster
                        StorageReference imageRef = storageReference.child(oldRecipeTitle + ".jpg");
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            recipeCollectionReference.document(oldRecipeTitle).collection("ingredient")
                                    .document(doc.getId()).delete();
                        }
                        Log.d(TAG, oldRecipeTitle +
                                " recipe ingredient list successfully deleted!");
                        //after the list has been deleted, delete the recipe object
                        recipeCollectionReference.document(oldRecipeTitle)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Log.d(TAG, oldRecipeTitle +
                                                " recipe successfully deleted!");
                                        //then, try to delete the image for this recipe
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //if there is one and it has been deleted
                                                Log.d(TAG, oldRecipeTitle + ".jpg deleted");
                                                //add the updated recipe if in update mode
                                                if (updateRecipe){
                                                    addRecipe(updatedRecipe);
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //if deletion of image failed (likely buccaneer there is
                                                // no image for this recipe)
                                                Log.d(TAG, oldRecipeTitle + ".jpg failed to delete," +
                                                        "the image may not exist");
                                                //still add the updated recipe if in update mode
                                                if (updateRecipe){
                                                    addRecipe(updatedRecipe);
                                                }
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //if the recipe attributes failed to delete
                                        Log.w(TAG, "Error deleting document"
                                                + oldRecipeTitle + "recipe", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if the ingredient list failed to be fetched
                        Log.w(TAG, "Error deleting ingredient list of "
                                + oldRecipeTitle + "recipe", e);
                        DBActionComplete = true;
                    }
                });
    }


    /**
     * Public method that retrieves the image of an ingredient and update the ImageView widget (inside
     * RecipeFragment) with the retrieved image
     * @param RecipeTitle
     * @param imageInput
     */
    public void retrieveImage(String RecipeTitle, ImageView imageInput){
        StorageReference imageReference = storageReference.child(RecipeTitle + ".jpg");
        final Bitmap[] image = new Bitmap[1];
        final long ONE_MEGABYTE = 1024*1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG, RecipeTitle + ".jpg successfully downloaded");
                InputStream imageStream = new ByteArrayInputStream(bytes);
                image[0] = BitmapFactory.decodeStream(imageStream);
                imageInput.setImageBitmap(image[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, RecipeTitle + ".jpg failed to download, this image may not exist");
                image[0] = null;
            }
        });
    }

    /**Public method to sort the local list by title*/
    public void sortByTitle(){
        Collections.sort(recipeList, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe recipe, Recipe t1) {
                return recipe.getRecipeTitle().compareTo(t1.getRecipeTitle());
            }
        });
        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**Public method to sort the local list by preparation time*/
    public void sortByPT(){
        Collections.sort(recipeList, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe recipe, Recipe t1) {
                return recipe.getRecipePreparationTime().compareTo(t1.getRecipePreparationTime());
            }
        });
        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**Public method to sort the local list by number of servings*/
    public void sortByNOS(){
        Collections.sort(recipeList, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe recipe, Recipe t1) {
                return recipe.getRecipeNumberOfServings().compareTo(t1.getRecipeNumberOfServings());
            }
        });
        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**Public method to sort the local list by category*/
    public void sortByCategory(){
        Collections.sort(recipeList, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe recipe, Recipe t1) {
                return recipe.getRecipeCategory().compareTo(t1.getRecipeCategory());
            }
        });
        recipeArrayAdapter.notifyDataSetChanged();
    }

}
