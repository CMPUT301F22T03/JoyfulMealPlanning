package com.example.joyfulmealplanning;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyfulmealplanning.R.id;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * The main activity of Recipe
 * @author Zhaoqi
 * @version 1.0
 */

public class RecipeFragment extends DialogFragment implements IngredientFragment.OnFragmentInteractionListener {

    private Context activityContext;
    private OnFragmentInteractionListener listener; //the context (in the form of OnFragmentInteractionListener)
    private EditText titleInput;   //text box for user-input food description
    private EditText timeInput;  //text box used to display selected date
    private Spinner categorySpinner; //choose a location spinner
    private EditText commentsInput;
//    private DatePickerDialog timePicker;  //datePicker widget
//    private Button timeButton;  //choose a date button
    private EditText numberInput; //text box for user-input food count
    private ListView recipeIngredientList;
    private Button addStorageIngredientButton;
    private Button addNewIngredientButton;
    private Button deleteIngredientButton;
    String title; //intermediate variable to hold the inputted food description
    String comments;
    String selectedTime; //intermediate variable to hold the selected BB date
    String selectedCategory; //intermediate variable to hold the selected location
    int servingNumber = 1;  //intermediate variable to hold the inputted count. Initialized to 0
    Integer selectedIngredientPosition = null; //integer location of the selected ingredient from the ingredient list
    private IngredientController ingredientStorageController;
    ArrayList<Ingredients> requiredIngredients;
    ArrayAdapter<Ingredients> recipeIngredientListAdaptor;

    @Override
    public void onOkPressed(String oldIngredientDesc, Ingredients newIngredients) {

    }

    public interface OnFragmentInteractionListener{
        void onOkPressed(String oldRecipeTitle, Recipe newRecipe);
    }

    /*check if the activity that called this fragment is implementing the OnFragmentInteractionListener interface*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityContext = context;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ingredientStorageController = new IngredientController(view.getContext());

        titleInput = view.findViewById(R.id.RecipeTitleInput);
        timeInput = view.findViewById(R.id.RecipeTimeInput);
        categorySpinner = view.findViewById(id.RecipeCategorySpinner);
        commentsInput =  view.findViewById(id.RecipeCommentsInput);
        numberInput = view.findViewById(id.RecipeNumberInput);
        recipeIngredientList = view.findViewById(id.RecipeIngredientList);
        addStorageIngredientButton = view.findViewById(id.RecipeAddIngredientFromStorageButton);
        addNewIngredientButton = view.findViewById(id.RecipeAddNewIngredientButton);
        deleteIngredientButton = view.findViewById(id.RecipeDeleteIngredientButton);
        requiredIngredients = new ArrayList<>();
//        requiredIngredients.add(new Ingredients("test ingredient1", 1, "kg", "meat"));
//        requiredIngredients.add(new Ingredients("test ingredient2", 2, "pack", "fruit"));
        recipeIngredientListAdaptor = new RecipeIngredientListAdapter(getContext(), requiredIngredients);
//        recipeIngredientList.setLayoutManager(new LinearLayoutManager(getContext()));
//        recipeIngredientList.setAdapter(new RecipeIngredientListAdaptor(getContext(), requiredIngredients));
        recipeIngredientList.setAdapter(recipeIngredientListAdaptor);

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

        recipeIngredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if((selectedIngredientPosition == null)){
                    selectedIngredientPosition = i;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }else if((selectedIngredientPosition != i)){
                    clearSelection();
                    selectedIngredientPosition = i;
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

        deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requiredIngredients.remove(Integer.parseInt(selectedIngredientPosition.toString()));
                recipeIngredientListAdaptor.notifyDataSetChanged();
            }
        });

        addStorageIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addStorageIngredientDialog = RecipeSelectIngredientDialog();
                addStorageIngredientDialog.show();
                //recipeIngredientListAdaptor.notifyDataSetChanged();
            }
        });

        addNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addNewIngredientDialog = RecipeAddNewIngredientDialog();
                addNewIngredientDialog.show();
                //recipeIngredientListAdaptor.notifyDataSetChanged();
            }
        });



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
            requiredIngredients.clear();
            for (Ingredients ingredient : recipe.getRecipeIngredientsList()){
                requiredIngredients.add(ingredient);
            }
            recipeIngredientListAdaptor.notifyDataSetChanged();
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
                        Recipe newRecipe = new Recipe(title, selectedCategory, comments,
                                Integer.parseInt(selectedTime), servingNumber, requiredIngredients);
                        if (finalAddRecipe){
                            listener.onOkPressed(null, newRecipe);
                        } else {
                            listener.onOkPressed(finalOldRecipeTitle, newRecipe);
                        }
                    }
                }).create();

    }




    Integer selectedItemPosition = null;
    View oldSelection = null;

    private AlertDialog.Builder RecipeSelectIngredientDialog(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_ingredient_from_storage,null);
        ListView ingredientList = view.findViewById(id.RecipeAddIngredientListView);
        EditText ingredientAmountInput = view.findViewById(id.RecipeAddStorageIngredientAmountInput);
        TextView unit = view.findViewById(id.RecipeAddStorageIngredientUnit);
        ingredientList.setAdapter(ingredientStorageController.getArrayAdapter());

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if((selectedItemPosition == null)){
                    selectedItemPosition = i;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }else if((selectedItemPosition != i)){
                    clearSelection();
                    selectedItemPosition = i;
                    oldSelection = view;
                    view.setBackgroundColor(Color.parseColor("#FF9A9595"));
                }
                unit.setText(ingredientStorageController.getIngredientAtIndex(i).getUnit());
            }
            private void clearSelection() {
                if(oldSelection != null) {
                    oldSelection.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Select a stored ingredient")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Ingredients selectedIngredient = ingredientStorageController.getIngredientAtIndex(selectedItemPosition);
                        Integer ingredientAmount = Integer.valueOf(ingredientAmountInput.getText().toString());
//                        recipeIngredientListAdaptor.add(new Ingredients(
//                                selectedIngredient.getDescription(),
//                                ingredientAmount, selectedIngredient.getUnit(),
//                                selectedIngredient.getCategory()));
                        requiredIngredients.add(new Ingredients(
                                selectedIngredient.getDescription(),
                                ingredientAmount, selectedIngredient.getUnit(),
                                selectedIngredient.getCategory()));
                        recipeIngredientListAdaptor.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();
        return builder;
    }

    String selectedIngredientUnit;
    String selectedIngredientCategory;
    private AlertDialog.Builder RecipeAddNewIngredientDialog(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_new_ingredient,null);
        EditText ingredientDescInput = view.findViewById(id.RecipeNewIngredientDescriptionInput);
        EditText ingredientAmountInput = view.findViewById(id.RecipeNewIngredientAmountInput);
        Spinner ingredientUnitSpinner = view.findViewById(id.RecipeNewIngredientUnitSpinner);
        Spinner ingredientCategorySpinner = view.findViewById(id.RecipeNewIngredientCategorySpinner);

        ArrayList<String> units = new ArrayList<>();
        units.add("pack");
        units.add("bottle");
        units.add("g");
        units.add("kg");
        units.add("lb");
        units.add("oz");
        units.add("mL");
        units.add("L");
        units.add("tsp");
        units.add("tbsp");

        ArrayList<String > categories = new ArrayList<>();
        categories.add("meat");
        categories.add("vegetable");
        categories.add("fruit");
        categories.add("spices");
        categories.add("seasoning");
        categories.add("drink");
        categories.add("alcohol");

        initializeSpinner(ingredientUnitSpinner, units);
        initializeSpinner(ingredientCategorySpinner, categories);

        ingredientUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIngredientUnit = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ingredientCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIngredientCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Add a new ingredient")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer ingredientAmount = Integer.valueOf(ingredientAmountInput.getText().toString());
//                        recipeIngredientListAdaptor.add(new Ingredients(
//                                ingredientDescInput.getText().toString(),
//                                ingredientAmount, selectedIngredientUnit,
//                                selectedIngredientCategory));
                        requiredIngredients.add(new Ingredients(
                                ingredientDescInput.getText().toString(),
                                ingredientAmount, selectedIngredientUnit,
                                selectedIngredientCategory));
                        recipeIngredientListAdaptor.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();
        return builder;
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

    private void initializeSpinner(Spinner spinner, ArrayList<String> selectionList){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, selectionList);
        adapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
