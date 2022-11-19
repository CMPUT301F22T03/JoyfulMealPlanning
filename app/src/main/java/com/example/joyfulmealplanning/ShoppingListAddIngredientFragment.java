package com.example.joyfulmealplanning;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;

public class ShoppingListAddIngredientFragment extends DialogFragment {
    Context context;
    private EditText descriptionInput, amountInput, unitInput, categoryInput, locationInput;
    private String description, unit, category, location, BBDate;
    private TextView BBDateDisplay;
    private Button BBDatePicker;
    private DatePickerDialog timePicker;
    private ShoppingListAddIngredientFragment.OnFragmentInteractionListener listener;
    Integer amount = 1;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredients ingredients);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) context;
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

        String dialogTitle = "Add a Ingredient";
        String oldIngredientDesc = null;
        boolean addIngredient = true;
        amountInput.setText(Integer.toString(amount));
        Bundle bundle = getArguments(); //get the bundle data of this instance

        if (bundle != null) {
            /*if the bundle is not empty, then this is an editing fragment*/
            //dialogTitle = "Edit Ingredient";
            addIngredient = false;

            //set the widgets with the provided information from the extracted food object.
            Ingredients ingredients = (Ingredients) bundle.getSerializable("ingredients") ; //extract the food object stored in the bundle

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
                        if (description!=null && amount!=null && BBDate != null && unit!=null && category!=null && location!=null){
                            Ingredients newIngredients = new Ingredients(description,Integer.parseInt(BBDate),
                                    location, category, amount, unit);

                            listener.onOkPressed(newIngredients);
                            Toast.makeText(getContext(),"You have added a new ingredient" ,Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(getContext(),"Some fields are empty, pls retry" ,Toast.LENGTH_LONG).show();
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
    public ShoppingListAddIngredientFragment newInstance(Ingredients ingredients){
        Bundle args = new Bundle();
        //context = getContext();
        args.putSerializable("ingredients", ingredients);
        ShoppingListAddIngredientFragment fragment = new ShoppingListAddIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

}



