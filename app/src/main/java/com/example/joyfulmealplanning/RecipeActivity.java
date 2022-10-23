package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * The main activity of Recipe
 * @author Qiaosong
 * @version 1.0
 */
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


        //Make the changes in DB can be reflected in the listview
        //NOTE: The Recipe Object in recipeDataList can only be updated with 4 parameters:
        //RecipeTitle, RecipeCategory, RecipePreparationTime, RecipeNumberOfServings
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

        //Long click to delete an item in the listView.
        recipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String RecipeTitle = recipeDataList.get(position).getRecipeTitle();
                recipeCollectionReference.document(RecipeTitle)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(getApplicationContext(),"Recipe: " + RecipeTitle + " has been deleted",Toast.LENGTH_LONG).show();
                                } else{
                                    Log.d(TAG, "DocumentSnapshot not deleted!");
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                return true;
            }
        });
    }
}