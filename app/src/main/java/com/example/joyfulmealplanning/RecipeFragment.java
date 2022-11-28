package com.example.joyfulmealplanning;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.joyfulmealplanning.R.id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class creates a DialogFragment that prompts users for information for recipes
 * @author Zhaoqi
 * @version 1.0
 */

public class RecipeFragment extends DialogFragment implements IngredientFragment.OnFragmentInteractionListener {
    private OnFragmentInteractionListener listener; //the context (in the form of OnFragmentInteractionListener)
    private EditText titleInput, timeInput, commentsInput, categoryInput, numberInput;   //text box for user inputs
    private ListView recipeIngredientList;
    private Button addStorageIngredientButton, addNewIngredientButton, deleteIngredientButton, chooseImageButton, deleteImageButton;
    private ImageView imageInput;
    private static final int storagePermissionCode = 0;
    private Uri imagePath;
    private Bitmap selectedImage;
    String title, comments, selectedTime, category; //intermediate variable to hold the user inputs
    int servingNumber = 1;  //intermediate variable to hold the inputted count. Initialized to 0
    Integer selectedIngredientPosition = null; //integer location of the selected ingredient from the ingredient list
    private IngredientController ingredientStorageController;
    private RecipeController recipeController;
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
        recipeController = new RecipeController(view.getContext());
        titleInput = view.findViewById(R.id.RecipeTitleInput);
        timeInput = view.findViewById(R.id.RecipeTimeInput);
        categoryInput = view.findViewById(id.RecipeCategoryInput);
        commentsInput =  view.findViewById(id.RecipeCommentsInput);
        numberInput = view.findViewById(id.RecipeNumberInput);
        recipeIngredientList = view.findViewById(id.RecipeIngredientList);
        addStorageIngredientButton = view.findViewById(id.RecipeAddIngredientFromStorageButton);
        addNewIngredientButton = view.findViewById(id.RecipeAddNewIngredientButton);
        deleteIngredientButton = view.findViewById(id.RecipeDeleteIngredientButton);
        imageInput = view.findViewById(id.RecipeImageInput);
        chooseImageButton = view.findViewById(id.RecipeChooseImageButton);
        deleteImageButton = view.findViewById(id.RecipeDeleteImageButton);
        requiredIngredients = new ArrayList<>();
        recipeIngredientListAdaptor = new RecipeIngredientListAdapter(getContext(), requiredIngredients);
        recipeIngredientList.setAdapter(recipeIngredientListAdaptor);

        selectedImage = null;

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
                if (selectedIngredientPosition != null){
                    requiredIngredients.remove(Integer.parseInt(selectedIngredientPosition.toString()));
                    recipeIngredientListAdaptor.notifyDataSetChanged();
                }
            }
        });

        addStorageIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog addStorageIngredientDialog = RecipeSelectIngredientDialog();
            }
        });

        addNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog addNewIngredientDialog = RecipeAddNewIngredientDialog();
            }
        });

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
                chooseImage();
            }
        });

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = null;
                imageInput.setImageDrawable(null);
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
            oldRecipeTitle = recipe.getRecipeTitle();
            titleInput.setText(recipe.getRecipeTitle());
            commentsInput.setText(recipe.getRecipeComments());
            timeInput.setText(recipe.getRecipePreparationTime().toString());
            categoryInput.setText(recipe.getRecipeCategory());
            numberInput.setText(Integer.toString(recipe.getRecipeNumberOfServings()));
            recipeController.retrieveImage(oldRecipeTitle, imageInput);
        }

        boolean finalAddRecipe = addRecipe;
        String finalOldRecipeTitle = oldRecipeTitle;


        builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("cancel", null)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleInput.getText().toString();
                comments = commentsInput.getText().toString();
                selectedTime = timeInput.getText().toString();
                category = categoryInput.getText().toString();
                if (imageInput.getDrawable() != null){
                    selectedImage = ((BitmapDrawable)imageInput.getDrawable()).getBitmap();
                } else {
                    selectedImage = null;
                }

                if (title.isEmpty() || selectedTime.isEmpty() ||
                        category.isEmpty() || numberInput.getText().toString().isEmpty()){
                    Toast toast=Toast.makeText(getContext(),
                            "Please make sure all fields are filled",Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    servingNumber = Integer.parseInt(numberInput.getText().toString());
                    Recipe newRecipe = new Recipe(title, category, comments,
                            Integer.parseInt(selectedTime), servingNumber, requiredIngredients,
                            selectedImage);
                    if (finalAddRecipe){
                        listener.onOkPressed(null, newRecipe);
                    } else {
                        listener.onOkPressed(finalOldRecipeTitle, newRecipe);
                    }
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }


    private void requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){
            return;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storagePermissionCode);
        }
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        chooseActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> chooseActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        //do some operations
                        imagePath = data.getData();
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imagePath);
                            imageInput.setImageBitmap(selectedImage);
                        } catch (IOException e) {
                        }
                    }
                }
            });

    Integer selectedItemPosition = null;
    View oldSelection = null;

    private AlertDialog RecipeSelectIngredientDialog(){
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
                .setPositiveButton("Confirm", null)
                .setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedItemPosition == null){
                        Toast toast = Toast.makeText(getContext(),
                                "Please select an ingredient", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (ingredientAmountInput.getText().toString().isEmpty()) {
                        Toast toast = Toast.makeText(getContext(),
                                "Please enter an amount", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Ingredients selectedIngredient = ingredientStorageController.getIngredientAtIndex(selectedItemPosition);
                        Integer ingredientAmount = Integer.valueOf(ingredientAmountInput.getText().toString());
                        requiredIngredients.add(new Ingredients(
                                selectedIngredient.getDescription(),
                                ingredientAmount, selectedIngredient.getUnit(),
                                selectedIngredient.getCategory()));
                        recipeIngredientListAdaptor.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });


        return dialog;
    }


    private AlertDialog RecipeAddNewIngredientDialog(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_new_ingredient,null);
        EditText ingredientDescInput = view.findViewById(id.RecipeNewIngredientDescriptionInput);
        EditText ingredientAmountInput = view.findViewById(id.RecipeNewIngredientAmountInput);
        EditText ingredientUnitInput = view.findViewById(id.RecipeNewIngredientUnitInput);
        EditText ingredientCategoryInput = view.findViewById(id.RecipeNewIngredientCategoryInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Add a new ingredient")
                .setPositiveButton("Confirm", null)
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredientDesc = ingredientDescInput.getText().toString();
                String ingredientAmount = ingredientAmountInput.getText().toString();
                String ingredientUnit = ingredientUnitInput.getText().toString();
                String ingredientCategory = ingredientCategoryInput.getText().toString();
                if (ingredientDesc.isEmpty() || ingredientAmount.isEmpty() ||
                        ingredientUnit.isEmpty() || ingredientCategory.isEmpty()){
                    Toast toast=Toast.makeText(getContext(),
                            "Please make sure all fields are filled",Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Integer ingredientAmountInt = Integer.valueOf(ingredientAmount);
                    requiredIngredients.add(new Ingredients(
                            ingredientDesc,
                            ingredientAmountInt, ingredientUnit,
                            ingredientCategory));
                    recipeIngredientListAdaptor.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }


    /*defines how the the datePicker should be initialized and what to do when a date is selected*/
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String monthStr = Integer.toString(month);
                        String dayOfMonthStr = Integer.toString(dayOfMonth);
                        if (monthStr.length()<2){
                            monthStr = "0"+monthStr;
                        }
                        if (dayOfMonthStr.length()<2){
                            dayOfMonthStr = "0"+dayOfMonthStr;
                        }
                        selectedTime = year + "-" + monthStr + "-" + dayOfMonthStr;
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
