package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * The controller class for ShoppingList
 * @author Qiaosong Deng & Fan Zhu
 * @version 1.1
 * @since IDK
 */
public class ShoppingListController extends AppCompatActivity{

    /*Declaration of variables*/
    ArrayAdapter<Ingredients> ShoppingListAdaptor;
    private static HashMap<String,Ingredients> IngredientFromIngredientList;
    private HashMap<String,Ingredients> IngredientFromMealPLanList;
    private ArrayList<Ingredients> ShoppingIngredientDataList;
    private Context context;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipeCollectionReference = db.collection("recipe");
    static final CollectionReference ingredientsCollectionReference = db.collection("ingredient");
    final CollectionReference mealPlanCollectionReference = db.collection("mealPlan");

    /*Constructor*/
    public ShoppingListController(Context context) {
        this.context = context;
        IngredientFromIngredientList = new HashMap<>();
        IngredientFromMealPLanList = new HashMap<>();
        ShoppingIngredientDataList = new ArrayList<>();
        ShoppingListAdaptor = new ShoppingListAdaptor(context,ShoppingIngredientDataList);
        realTimeReaction();
    }

    /**
     * Public interface that implements the Callback function in the Firebase
     */
    public interface FirebaseCallback{
        void onCallback(HashMap<String,Ingredients> IngredientMap);
    }

    /**
     * Public method that synchronizes the changes in the IngredientList and the MealPlanList with the changes in the ingredientsCollection
     */
    public void realTimeReaction(){

        ingredientsCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Init();
                Log.d(TAG,"ingredientsCollectionChange!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });

    }

    /**
     * Public method that initializes the DataLists
     */
    public void Init(){
        IngredientFromIngredientListInit(new FirebaseCallback() {
            @Override
            public void onCallback(HashMap<String, Ingredients> IngredientMap) {
                IngredientFromIngredientList = IngredientMap;
                //printDataList(IngredientFromMealPLanList);
            }
        });

        IngredientFromMealPLanListInit(new FirebaseCallback() {
            @Override
            public void onCallback(HashMap<String, Ingredients> IngredientMap) {
                IngredientFromMealPLanList = IngredientMap;
                //printDataList(IngredientFromMealPLanList);
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //printDataList(IngredientFromMealPLanList);
                //printDataList(IngredientFromIngredientList);
                ShoppingIngredientDataInit();
                for (Ingredients ingredient: ShoppingIngredientDataList){
                    System.out.println(ingredient.getDescription()+ " " + ingredient.getAmount());
                }
            }
        }, 1500);
        Log.d(TAG,"initialization complete!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * Public method that initializes the IngredientFromMealPlanList
     * @param callback {@link FirebaseCallback}
     */
    public void IngredientFromMealPLanListInit(final FirebaseCallback callback){
        HashMap<String,Ingredients> IngredientFromMealPLanList = new HashMap<String,Ingredients>();
        //get ingredients from meal plan.
        mealPlanCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot mealPlans) {
                for (QueryDocumentSnapshot mealPlan:mealPlans){
                    //Log.d(TAG,String.valueOf(mealPlan.getData().get("type")));
                    String type = (String) mealPlan.getData().get("type");
                    //Log.d(TAG,String.valueOf(mealPlan.getData().get("ID")));
                    String ID = (String) mealPlan.getData().get("ID");
                    //Log.d(TAG,String.valueOf(mealPlan.getData().get("number of servings")));
                    Long numberOfServings = (Long)mealPlan.getData().get("number of servings");

                    if(type.equals("ingredient")){
                        //Go to Ingredient database to find its details
                        DocumentReference IngredientDocRef = ingredientsCollectionReference.document(ID);
                        IngredientDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot IngredientDoc) {
                                //Log.d(TAG, String.valueOf(IngredientDoc.get("description")));
                                String description = (String) IngredientDoc.get("description");
                                //Log.d(TAG, String.valueOf(IngredientDoc.get("category")));
                                String category = (String) IngredientDoc.get("category");
                                //Log.d(TAG, String.valueOf(IngredientDoc.get("unit")));
                                String unit = (String) IngredientDoc.get("unit");
                                if (description != null && numberOfServings != null){
                                    if (IngredientFromMealPLanList.containsKey(description)){
                                        Ingredients OneIngredient = new Ingredients(description,
                                                00000000,
                                                "",
                                                category,
                                                IngredientFromMealPLanList.get(description).getAmount() + numberOfServings.intValue(),
                                                unit);
                                        IngredientFromMealPLanList.put(description,OneIngredient);
                                    }else{
                                        Ingredients OneIngredient = new Ingredients(description, 00000000,
                                                "", category, numberOfServings.intValue(), unit);
                                        IngredientFromMealPLanList.put(description,OneIngredient);
                                    }
                                }
                                callback.onCallback(IngredientFromMealPLanList);
                            }
                        });
                    }else if(type.equals("recipe")){
                        //Go to recipe database to find its details
                        CollectionReference IngredientsOfRecipeRef = recipeCollectionReference.document(ID).collection("ingredient");
                        IngredientsOfRecipeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot IngredientsOfRecipe) {
                                for (QueryDocumentSnapshot ingredient: IngredientsOfRecipe){
                                    //Log.d(TAG, String.valueOf(ingredient.getData().get("description")));
                                    String description = (String) ingredient.getData().get("description");
                                    //Log.d(TAG, String.valueOf(ingredient.getData().get("amount")));
                                    Long amount = (Long) ingredient.getData().get("amount");
                                    //Log.d(TAG, String.valueOf(ingredient.getData().get("unit")));
                                    String unit = (String) ingredient.getData().get("unit");
                                    //Log.d(TAG, String.valueOf(ingredient.getData().get("category")));
                                    String category = (String) ingredient.getData().get("category");
                                    //Log.d(TAG, String.valueOf(ingredient.getData().get("location")));
                                    String location = (String) ingredient.getData().get("location");
                                    if (description != null && amount != null){
                                        if (IngredientFromMealPLanList.containsKey(description)){
                                            Ingredients OneIngredient = new Ingredients(description,
                                                    00000000,
                                                    "",
                                                    category,
                                                    IngredientFromMealPLanList.get(description).getAmount()
                                                            + amount.intValue()*numberOfServings.intValue(),
                                                    unit);
                                            IngredientFromMealPLanList.put(description,OneIngredient);
                                        }else{
                                            Ingredients OneIngredient = new Ingredients(description,
                                                    00000000,
                                                    "",
                                                    category,
                                                    amount.intValue()*numberOfServings.intValue(),
                                                    unit);
                                            IngredientFromMealPLanList.put(description,OneIngredient);
                                        }
                                    }
                                    //printDataList(IngredientFromMealPLanList);
                                }
                                //printDataList(IngredientFromMealPLanList);
                                callback.onCallback(IngredientFromMealPLanList);
                            }
                        });
                    }
                }
                callback.onCallback(IngredientFromMealPLanList);
            }
        });
    }

    /**
     * Public method that retrieves ingredients from the IngredientList
     * @param callback {@link FirebaseCallback}
     */
    public void IngredientFromIngredientListInit(final FirebaseCallback callback){
        HashMap<String,Ingredients> IngredientFromIngredientList = new HashMap<String,Ingredients>();
        //get ingredients from ingredient.
        ingredientsCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot IngredientsFromIngredient) {
                //HashMap<String,Ingredients> IngredientFromIngredientList2 = new HashMap<String,Ingredients>();
                for(QueryDocumentSnapshot ingredient: IngredientsFromIngredient){
                    //Log.d(TAG, String.valueOf(ingredient.getData().get("description")));
                    String description = (String) ingredient.getData().get("description");
                    //Log.d(TAG, String.valueOf(ingredient.getData().get("amount")));
                    Long amount = (Long) ingredient.getData().get("amount");
                    //Log.d(TAG, String.valueOf(ingredient.getData().get("unit")));
                    String unit = (String) ingredient.getData().get("unit");
                    //Log.d(TAG, String.valueOf(ingredient.getData().get("category")));
                    String category = (String) ingredient.getData().get("category");
                    //Log.d(TAG, String.valueOf(ingredient.getData().get("location")));
                    String location = (String) ingredient.getData().get("location");
                    if (description != null && amount != null){
                        Ingredients OneIngredient = new Ingredients(description, 00000000,
                                "", category, amount.intValue(), unit);
                        IngredientFromIngredientList.put(description,OneIngredient);
                    }
                }
                callback.onCallback(IngredientFromIngredientList);
                //printDataList(IngredientFromIngredientList);
            }
        });
    }

    /**
     * Public method to initialize the ShoppingIngredientDataList
     */
    public void ShoppingIngredientDataInit(){

        ShoppingIngredientDataList.clear();
        //printDataList(IngredientFromMealPLanList);
        //printDataList(IngredientFromIngredientList);

        //Subtract IngredientFromIngredientList from IngredientFromMealPLanList
        for (Map.Entry<String, Ingredients> IngredientFromIngredient: IngredientFromIngredientList.entrySet()){
            String description = IngredientFromIngredient.getKey();
            Ingredients ingredients = IngredientFromIngredient.getValue();
            if (IngredientFromMealPLanList.containsKey(description)){
                Ingredients OneIngredient = new Ingredients(
                        IngredientFromMealPLanList.get(description).getDescription(),
                        IngredientFromMealPLanList.get(description).getBest_before_date(),
                        IngredientFromMealPLanList.get(description).getLocation(),
                        IngredientFromMealPLanList.get(description).getCategory(),
                        IngredientFromMealPLanList.get(description).getAmount()-ingredients.getAmount(),
                        IngredientFromMealPLanList.get(description).getUnit());
                IngredientFromMealPLanList.put(description,OneIngredient);
            }
        }

        //Add ingredients that need to been bought.
        for  (Map.Entry<String, Ingredients> IngredientFromRecipe: IngredientFromMealPLanList.entrySet()){
            if (IngredientFromRecipe.getValue().getAmount()>0){
                ShoppingIngredientDataList.add(IngredientFromRecipe.getValue());
                ShoppingListAdaptor.notifyDataSetChanged();
            }
        }
        //return ShoppingIngredientDataList;
    }

    //TODO: Code smell - Speculative generality, request refactoring
    public void printDataList(HashMap<String,Ingredients> DataList){
        System.out.println("####################################################" + DataList.size());
        for (Map.Entry<String, Ingredients> entry: DataList.entrySet()){
            System.out.println("Ingredient Desp: " + entry.getKey() + " Amount: " + entry.getValue().getAmount());
        }
    }

    /**
     * Fetches the ShoppingListAdapter
     * @return {@link ArrayAdapter<Ingredients>}
     */
    public ArrayAdapter<Ingredients> getShoppingListAdaptor(){
        return ShoppingListAdaptor;
    }

    /**
     * Public method that retrieves the ShoppingIngredientDataList
     * @return {@link ArrayList<Ingredients>}
     */
    public ArrayList<Ingredients> getShoppingIngredientDataList(){
        return ShoppingIngredientDataList;
    }

    /**
     * Adds a given ingredient to the ingredientList as well as the DB collections From shopping list
     * @param ingredient
     * @return {@link Boolean}
     */
    public static boolean addIngredientIntoDBShoppingListVersion(Ingredients ingredient){
        Log.d(TAG, "IngredientFromIngredientList's size: "+IngredientFromIngredientList.size());
        for (Ingredients ing : IngredientFromIngredientList.values()){
            Log.d(TAG, "Compare "+ing.getDescription()+"---"+ingredient.getDescription());
            if (ing.getDescription().equals( ingredient.getDescription())){
                Log.d(TAG, ing.getDescription()+ "'s old amount: " + ing.getAmount());
                Log.d(TAG, ing.getDescription()+ "'s add amount: " + ingredient.getAmount());
                Log.d(TAG, ing.getDescription()+ "'s new amount: " + ing.getAmount()+ingredient.getAmount());
                ingredient.setAmount(ing.getAmount()+ingredient.getAmount());
            }
        }
        Map<String, Object> packedIngredients = packIngredientsToMap(ingredient);
        ingredientsCollectionReference
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
     * Public static method that returns the ingredients from the IngredientList
     * @return {@link HashMap}
     */
    public static HashMap<String, Ingredients> getIngredientFromIngredientList() {
        return IngredientFromIngredientList;
    }

    /**
     * Packs the ingredients into a HashMap
     * @param ingredients
     * @return {@link Map}
     */
    private static Map<String, Object> packIngredientsToMap(Ingredients ingredients){
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
     * Public method that sorts the ShoppingIngredientDataList by description
     */
    public void sortByDescription(){
        Collections.sort(ShoppingIngredientDataList, new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredient1, Ingredients ingredient2) {
                return ingredient1.getDescription().toLowerCase().compareTo(ingredient2.getDescription().toLowerCase());
            }
        });
        ShoppingListAdaptor.notifyDataSetChanged();
    }

    /**
     * Public method that sorts the ShoppingIngredientDataList by Category
     */
    public void sortByCategory(){
        Collections.sort(ShoppingIngredientDataList, new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredient1, Ingredients ingredient2) {
                return ingredient1.getCategory().toLowerCase().compareTo(ingredient2.getCategory().toLowerCase());
            }
        });
        ShoppingListAdaptor.notifyDataSetChanged();
    }
}
