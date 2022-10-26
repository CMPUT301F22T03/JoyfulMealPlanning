package com.example.joyfulmealplanning;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * <h1>Joyful Meal Planning</h1>
 *
 * <p>
 *     This app is designed to keep track of meal plans of users. It consists of four activities, including ingredients, recipes,
 *     shopping lists and meal plans. Each activity records the entries of ingredients, recipes, shopping lists and meal plans,
 *     users are free to add, edit and delete the entries or even upload images.
 * </p>
 *
 * This activity class implements functionalities to switch to different activities, as well as to manipulate data in the Firestore database
 *
 * @author Fan Zhu, Mashiad Hasan, Yuxuan Yang, Xiangxu Meng, Qiaosong Deng & Zhaoqi Ma
 * @version 1.0
 * @since 2022-10-18
 */
public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        /* Add a new document with a generated ID*/
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivity(view);
            }
        });


    }

    public void switchActivity(View view) {
        Intent intent = new Intent(this, IngredientsActivity.class);
        startActivity(intent);
    }


}