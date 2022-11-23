package com.example.joyfulmealplanning;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MealPlanAddFragment extends DialogFragment {
    Button IngredientChoice;
    Button RecipeChoice;
    Button MealPlanAddStage2DatePicker;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference ingredientReference = db.collection("ingredient");
    final CollectionReference recipeReference = db.collection("recipe");
    ArrayList<Ingredients> ingredientDataList;
    ArrayAdapter<Ingredients> ingredientAdaptor;
    ArrayList<Recipe> recipeDataList;
    ArrayAdapter<Recipe> recipeAdaptor;
    final String TAG = "Sample";
    Integer selectedItemPosition;
    View oldSelection = null;
    private DatePickerDialog timePicker;

    public MealPlanAddFragment(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.meal_plan_add_fragment_stage1,null);
        IngredientChoice = view.findViewById(R.id.MealPlanAddStage1Choice1);
        RecipeChoice = view.findViewById(R.id.MealPlanAddStage1Choice2);
        AlertDialog.Builder builder = new AlertDialog.Builder (context);
        IngredientChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog MealPlanAddFragmentStage2
                        = MealPlanAddFragmentStage2("ingredient");
                MealPlanAddFragmentStage2.show();
                Toast.makeText(context,"You are adding a new item from ingredient" ,Toast.LENGTH_LONG).show();
            }
        });
        RecipeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog MealPlanAddFragmentStage2
                        = MealPlanAddFragmentStage2("recipe");
                MealPlanAddFragmentStage2.show();
                Toast.makeText(context,"You are adding a new item from recipe" ,Toast.LENGTH_LONG).show();


            }
        });

        return builder
                .setView(view)
                .setTitle("Add a new meal plan")
                .setMessage("Where do you want to add from")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

    }


    private androidx.appcompat.app.AlertDialog MealPlanAddFragmentStage2(String StorageName){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.meal_plan_add_fragment_stage2,null);
        ListView StorageList = view.findViewById(R.id.MealPlanAddStage2StorageList);
        EditText NumberOfServings = view.findViewById(R.id.MealPlanAddStage2InputNum);
        MealPlanAddStage2DatePicker = view.findViewById(R.id.MealPlanAddStage2DatePicker);
        initDatePicker();
        MealPlanAddStage2DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show();
            }
        });

        if (StorageName.equals("ingredient")){
            ingredientDataList = new ArrayList<>();
            ingredientAdaptor = new IngredientAdapter(context, ingredientDataList);
            StorageList.setAdapter(ingredientAdaptor);

            ingredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    // Clear the old list
                    ingredientDataList.clear();
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                        Log.d(TAG, String.valueOf(doc.getData().get("amount")));
                        Log.d(TAG, String.valueOf(doc.getData().get("best before date")));
                        Log.d(TAG, String.valueOf(doc.getData().get("location")));
                        Log.d(TAG, String.valueOf(doc.getData().get("category")));
                        Log.d(TAG, String.valueOf(doc.getData().get("description")));
                        Log.d(TAG, String.valueOf(doc.getData().get("unit")));

                        Long amount = (Long) doc.getData().get("amount");
                        Long bestBeforeDate = (Long) doc.getData().get("best before date");
                        String location = (String) doc.getData().get("location");
                        String category = (String) doc.getData().get("category");
                        String description = (String) doc.getData().get("description");
                        String unit = (String) doc.getData().get("unit");

                        ingredientDataList.add(new Ingredients(description,bestBeforeDate.intValue(),
                                location,category,amount.intValue(),unit));

                    }
                    ingredientAdaptor.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                }
            });


        }else if(StorageName.equals("recipe")){
            recipeDataList = new ArrayList<>();
            recipeAdaptor = new RecipeAdaptor(context,recipeDataList);
            StorageList.setAdapter(recipeAdaptor);

            recipeReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    // Clear the old list
                    recipeDataList.clear();
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                        Log.d(TAG, String.valueOf(doc.getData().get("title")));
                        Log.d(TAG, String.valueOf(doc.getData().get("category")));
                        Log.d(TAG, String.valueOf(doc.getData().get("preparation time")));
                        Log.d(TAG, String.valueOf(doc.getData().get("number of servings")));

                        String RecipeTitle = (String) doc.getData().get("title");
                        String RecipeCategory = (String) doc.getData().get("category");
                        Long RecipePreparationTime = (Long)doc.getData().get("preparation time");
                        Long RecipeNumberOfServings = (Long)doc.getData().get("number of servings");

                        recipeDataList.add(new Recipe(RecipeTitle, RecipeCategory,"",
                                RecipePreparationTime.intValue(),RecipeNumberOfServings.intValue(),
                                new ArrayList<>(), null));
                        //System.out.println(RecipeTitle);
                    }
                    recipeAdaptor.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                }
            });
        }

        StorageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if((selectedItemPosition == null)){
                    selectedItemPosition = position;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }else if((selectedItemPosition != position)){
                    clearSelection();
                    selectedItemPosition = position;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }
            }

            private void clearSelection() {
                if(oldSelection != null) {
                    oldSelection.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
            }
        });

        androidx.appcompat.app.AlertDialog MealPlanAddFragmentStage2
                = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setView(view)
                .setTitle(StorageName)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String NOS = NumberOfServings.getText().toString();
                        String Date = MealPlanAddStage2DatePicker.getText().toString();
                        if ((!NOS.equals("")) && selectedItemPosition!=null && !Date.equals("Choose Date")){
                            String title;
                            Integer NumOfServing = Integer.parseInt(NOS);
                            if (StorageName.equals("ingredient")){
                                title = ingredientDataList.get(selectedItemPosition).getDescription();
                                new MealPlanController(context).AddMealPlan(title,"ingredient",NumOfServing,Date);
                                Toast.makeText(context,"You have added a new Meal Plan" ,Toast.LENGTH_LONG).show();

                            }else{
                                title = recipeDataList.get(selectedItemPosition).getRecipeTitle();
                                new MealPlanController(context).AddMealPlan(title,"recipe",NumOfServing,Date);
                                Toast.makeText(context,"You have added a new Meal Plan" ,Toast.LENGTH_LONG).show();

                            }
                        }else {
                            Toast.makeText(context,"Adding Fails" ,Toast.LENGTH_LONG).show();
                        }
                        selectedItemPosition = null;
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItemPosition = null;
                        dialog.dismiss();
                    }
                })
                .create();
        return MealPlanAddFragmentStage2;
    }

    /*defines how the the datePicker should be initialized and what to do when a date is selected*/
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String BBDate;
                        BBDate = year + "-" + month + "-" + dayOfMonth;
                        MealPlanAddStage2DatePicker.setText(BBDate);
                    }
                };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;
        timePicker =
                new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }
}
