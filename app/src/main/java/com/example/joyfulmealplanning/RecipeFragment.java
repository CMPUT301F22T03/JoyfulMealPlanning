package com.example.joyfulmealplanning;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.joyfulmealplanning.R.id;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * The main activity of Recipe
 * @author Zhaoqi
 * @version 1.0
 */

public class RecipeFragment extends DialogFragment {

    private OnFragmentInteractionListener listener; //the context (in the form of OnFragmentInteractionListener)
    private EditText titleInput;   //text box for user-input food description
    private EditText timeInput;  //text box used to display selected date
    private Spinner categorySpinner; //choose a location spinner
    private EditText commentsInput;
//    private DatePickerDialog timePicker;  //datePicker widget
//    private Button timeButton;  //choose a date button
    private EditText numberInput; //text box for user-input food count
    private ScrollView ingredientList;
    private Button addIngredient;
    private  Button deleteIngredient;
    String title; //intermediate variable to hold the inputted food description
    String comments;
    String selectedTime; //intermediate variable to hold the selected BB date
    String selectedCategory; //intermediate variable to hold the selected location
    int servingNumber = 1;  //intermediate variable to hold the inputted count. Initialized to 0
    ArrayList<Map<String, Integer>> ingredientListArray;


    public interface OnFragmentInteractionListener{
        void onOkPressed(String oldRecipeTitle, Recipe newRecipe);
    }

    /*check if the activity that called this fragment is implementing the OnFragmentInteractionListener interface*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recipe, null);
        initDatePicker();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        titleInput = view.findViewById(R.id.RecipeTitleInput);
        timeInput = view.findViewById(R.id.RecipeTimeInput);
        categorySpinner = view.findViewById(id.RecipeCategorySpinner);
        commentsInput =  view.findViewById(id.RecipeCommentsInput);
        //timeButton = view.findViewById(id.RecipeTimeUnit);
        numberInput = view.findViewById(id.RecipeNumberInput);
        ingredientList = view.findViewById(id.RecipeIngredientList);
        addIngredient = view.findViewById(id.RecipeAddIngredientButton);
        deleteIngredient = view.findViewById(id.RecipeDeleteIngredientButton);


        ArrayList<String> categories = new ArrayList<>();
        categories.add("appetizer");
        categories.add("main dish");
        categories.add("side dish");
        categories.add("dessert");
        categories.add("soup");
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        categoriesAdapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        timeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                timePicker.show();
//            }
//        });


        String dialogTitle = "Add Recipe"; //title of the alert dialog
        String oldRecipeTitle = null;
        boolean addRecipe = true; //fist assume this fragment is used for adding food item
        numberInput.setText(Integer.toString(servingNumber));
        Bundle bundle = getArguments(); //get the bundle data of this instance
        if (bundle != null) {
            /*if the bundle is not empty, then this is an editing fragment*/
            dialogTitle = "Edit Recipe";
            addRecipe = false;
            Recipe recipe = (Recipe) bundle.getSerializable("recipe"); //extract the food object stored in the bundle
            //set the widgets with the provided information from the extracted food object.
            titleInput.setText(recipe.getRecipeTitle());
            oldRecipeTitle = recipe.getRecipeTitle();
            timeInput.setText(recipe.getRecipePreparationTime().toString());
            int spinnerPosition = categoriesAdapter.getPosition(recipe.getRecipeCategory());
            categorySpinner.setSelection(spinnerPosition);
            numberInput.setText(Integer.toString(recipe.getRecipeNumberOfServings()));
        }


        boolean finalAddRecipe = addRecipe;
        String finalOldRecipeTitle = oldRecipeTitle;
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        title = titleInput.getText().toString();
                        comments = commentsInput.getText().toString();
                        selectedTime = timeInput.getText().toString();
                        servingNumber = Integer.parseInt(numberInput.getText().toString());

                        ingredientListArray = new ArrayList<>();

                        Recipe newRecipe = new Recipe(title, selectedCategory, comments,
                                Integer.parseInt(selectedTime), servingNumber, ingredientListArray);
                        if (finalAddRecipe){
                            listener.onOkPressed(null, newRecipe);
                        } else {
                            listener.onOkPressed(finalOldRecipeTitle, newRecipe);
                        }
                    }
                }).create();

    }

    /*defines how the the datePicker should be initialized and what to do when a date is selected*/
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        selectedTime = year + "-" + month + "-" + dayOfMonth;
                        timeInput.setText(selectedTime);
                    }
                };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;
//        timePicker =
//                new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }

    /*return an instance with its bundle filled by the input food object (used for editing fragment)*/
    public static RecipeFragment newInstance(Recipe recipe){
        Bundle args = new Bundle();
        args.putSerializable("recipe", recipe);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
