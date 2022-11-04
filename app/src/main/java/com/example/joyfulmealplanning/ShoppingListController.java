package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private HashMap<String,Ingredients> IngredientFromIngredientList;
    private HashMap<String,Ingredients> IngredientFromMealPLanList;
    private ArrayList<Ingredients> ShoppingIngredientDataList;
    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipeCollectionReference = db.collection("recipe");
    final CollectionReference ingredientsCollectionReference = db.collection("ingredient");
    final CollectionReference mealPlanCollectionReference = db.collection("mealPlan");


    public ShoppingListController(Context context) {
        this.context = context;
        IngredientFromIngredientList = new HashMap<>();
        IngredientFromMealPLanList = new HashMap<>();
        ShoppingIngredientDataList = new ArrayList<>();
        ShoppingListAdaptor = new ShoppingListAdaptor(context,ShoppingIngredientDataList);
        realTimeReaction();
    }

    public interface FirebaseCallback{
        void onCallback(HashMap<String,Ingredients> IngredientMap);
    }

    public void realTimeReaction(){
//        recipeCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                Init();
//                Log.d(TAG,"recipeCollectionChange!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            }
//        });
        ingredientsCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Init();
                Log.d(TAG,"ingredientsCollectionChange!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });
//        mealPlanCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                Init();
//                Log.d(TAG,"mealPlanCollectionChange!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            }
//        });
    }

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
        Log.d(TAG,"initialize finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

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

    public void ShoppingIngredientDataInit(){

//        IngredientFromMealPLanListInit(new FirebaseCallback() {
//            @Override
//            public void onCallback(HashMap<String, Ingredients> IngredientMap) {
//                IngredientFromMealPLanList = IngredientMap;
//            }
//        });
//        IngredientFromIngredientListInit(new FirebaseCallback() {
//            @Override
//            public void onCallback(HashMap<String, Ingredients> IngredientMap) {
//                IngredientFromIngredientList = IngredientMap;
//            }
//        });
        ShoppingIngredientDataList.clear();
        //printDataList(IngredientFromMealPLanList);
        //printDataList(IngredientFromIngredientList);
        //ArrayList<Ingredients> ShoppingIngredientDataList = new ArrayList<>();
        //ShoppingIngredientDataList = new ArrayList<>();
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
