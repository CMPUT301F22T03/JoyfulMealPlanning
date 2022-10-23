package com.example.joyfulmealplanning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    final String TAG = "Sample";
    ListView recipeList;
    ArrayAdapter<Recipe> recipeAdaptor;
    ArrayList<Recipe> recipeDataList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeList = findViewById(R.id.recipe_list);
        recipeDataList = new ArrayList<>();
        recipeAdaptor = new RecipeAdaptor(this, recipeDataList);
        recipeList.setAdapter(recipeAdaptor);
        db = FirebaseFirestore.getInstance();
        final CollectionReference recipeCollectionReference = db.collection("recipe");

        recipeCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                recipeDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Log.d(TAG, String.valueOf(doc.getData().get("title")));
                    Log.d(TAG, String.valueOf(doc.getData().get("category")));
                    Log.d(TAG, String.valueOf(doc.getData().get("preparation time")));
                    //System.out.println(String.valueOf(doc.getData().get("preparation time")));
                    Log.d(TAG, String.valueOf(doc.getData().get("number of servings")));
                    //System.out.println(String.valueOf(doc.getData().get("number of servings")));
                    String RecipeTitle = (String) doc.getData().get("title");
                    String RecipeCategory = (String) doc.getData().get("category");
                    Long RecipePreparationTime = (Long)doc.getData().get("preparation time");
                    Long RecipeNumberOfServings = (Long)doc.getData().get("number of servings");

                    //recipeDataList.add(new Recipe(RecipeTitle, RecipeCategory,"",RecipeNumberOfServings,RecipePreparationTime, new ArrayList<>()));
                    recipeDataList.add(new Recipe(RecipeTitle, RecipeCategory,"",
                            RecipePreparationTime.intValue(),RecipeNumberOfServings.intValue(),
                            new ArrayList<>()));
                }
                recipeAdaptor.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

    }
}