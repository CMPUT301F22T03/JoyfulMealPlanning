package com.example.joyfulmealplanning;

import static com.google.api.ResourceProto.resource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.myVH> {

    private ArrayList<Ingredients> ingredients;

    private Context context;

    public IngredientAdapter(@NonNull Context context, ArrayList<Ingredients> ingredients) {

        this.context = context;
        this.ingredients = ingredients;
    }

    /**@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        return view;
    }*/

    @NonNull
    @Override
    public IngredientAdapter.myVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content, parent, false);
        return new IngredientAdapter.myVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.myVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {

        public myVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
