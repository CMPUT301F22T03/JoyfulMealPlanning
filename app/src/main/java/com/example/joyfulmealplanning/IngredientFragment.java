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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * The IngredientFragment class
 * Creating a DialogFragment that prompts users for information of ingredients
 * @author Xiangxu Meng, Zhaoqi Ma, Mashiad
 * @version 2.0
 * @change Modified the fragment so that it can properly handle both editing and addition
 * of ingredients
 * @since 2022-10-23
 */
public class IngredientFragment extends DialogFragment {

    private IngredientFragment.OnFragmentInteractionListener listener;
    private EditText descriptionInput, amountInput, unitInput, categoryInput, locationInput;
    private TextView BBDateDisplay;
    private Button BBDatePicker;
    private DatePickerDialog timePicker;
    private String description, unit, category, location, BBDate;
    Integer amount = 1;

    public interface OnFragmentInteractionListener {
        void onOkPressed(String oldIngredientDesc, Ingredients newIngredients);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    +"must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient, null);
        initDatePicker();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        descriptionInput = view.findViewById(R.id.IngredientDescriptionInput);
        amountInput = view.findViewById(R.id.IngredientAmountInput);
        BBDateDisplay = view.findViewById(R.id.IngredientBBDateDisplay);
        unitInput = view.findViewById(R.id.IngredientUnitInput);
        categoryInput = view.findViewById(R.id.IngredientCategoryInput);
        locationInput = view.findViewById(R.id.IngredientLocationInput);
        BBDatePicker = view.findViewById(R.id.IngredientBBDatePicker);

        BBDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show();
            }
        });

        String dialogTitle = "Add Ingredient";
        String oldIngredientDesc = null;
        boolean addIngredient = true;
        amountInput.setText(Integer.toString(amount));
        Bundle bundle = getArguments(); //get the bundle data of this instance

        if (bundle != null) {
            /*if the bundle is not empty, then this is an editing fragment*/
            dialogTitle = "Edit Ingredient";
            addIngredient = false;
            Ingredients ingredients = (Ingredients) bundle.getSerializable("ingredients"); //extract the food object stored in the bundle
            //set the widgets with the provided information from the extracted food object.
            descriptionInput.setText(ingredients.getDescription());
            oldIngredientDesc = ingredients.getDescription();
            amountInput.setText(ingredients.getAmount().toString());
            BBDateDisplay.setText(ingredients.getBest_before_date().toString());
            unitInput.setText(ingredients.getUnit());
            categoryInput.setText(ingredients.getCategory());
            locationInput.setText(ingredients.getLocation());
        }

        boolean finalAddIngredient = addIngredient;
        String finalOldIngredientDesc = oldIngredientDesc;
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        description = descriptionInput.getText().toString();
                        amount = Integer.valueOf(amountInput.getText().toString());
                        BBDate = BBDateDisplay.getText().toString().replace("-", "");
                        unit = unitInput.getText().toString();
                        category = categoryInput.getText().toString();
                        location = locationInput.getText().toString();
                        Ingredients newIngredients = new Ingredients(description,Integer.parseInt(BBDate),
                                location, category, amount, unit);
                        if (finalAddIngredient){
                            listener.onOkPressed(null, newIngredients);
                        } else {
                            listener.onOkPressed(finalOldIngredientDesc, newIngredients);
                        }
                    }
                }).create();

    }

    private void initializeSpinner(Spinner spinner, ArrayList<String> selectionList){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, selectionList);
        adapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /*defines how the the datePicker should be initialized and what to do when a date is selected*/
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        BBDate = year + "-" + month + "-" + dayOfMonth;
                        BBDateDisplay.setText(BBDate);
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

    public static IngredientFragment newInstance(Ingredients ingredients){
        Bundle args = new Bundle();
        args.putSerializable("ingredients", ingredients);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}