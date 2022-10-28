package com.example.joyfulmealplanning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * The IngredientFragment class
 * Creating a DialogFragment that prompts users for information of ingredients
 * @author Xiangxu Meng
 *
 */
public class IngredientFragment extends DialogFragment {
    private EditText description, date, location, amount, unit, category;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredients newIngredients);
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
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient, null);
        description = view.findViewById(R.id.Description_editText);
        date = view.findViewById(R.id.Date_editText);
        location = view.findViewById(R.id.Location_editText);
        amount = view.findViewById(R.id.Amount_editText);
        unit = view.findViewById(R.id.Unit_editText);
        category = view.findViewById(R.id.Category_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add ingredient")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String description_st = description.getText().toString();
                        String date_st = date.getText().toString();
                        String location_st = location.getText().toString();
                        String category_st = category.getText().toString();
                        String amount_st = amount.getText().toString();
                        String unit_st = unit.getText().toString();
                        ToFireBaseFireStore(description_st,date_st,location_st,category_st,amount_st,unit_st);
                        listener.onOkPressed(new Ingredients(description_st,date_st,location_st,category_st,amount_st,unit_st));
                    }
                }).create();
    }
    private void ToFireBaseFireStore(String description_st,String date_st,String location_st,String category_st,String amount_st,String unit_st){
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("description", description_st);
        map.put("best before date", date_st);
        map.put("location", location_st);
        map.put("amount", amount_st);
        map.put("unit", unit_st);
        map.put("category", category_st);

        ff.collection("ingredient").add(map);
    }
}