package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class MealPlanActivity extends AppCompatActivity {

    final String TAG = "Sample";
    ListView mealPlanList;
    ArrayAdapter<MealPLan> mealPLanAdapter;
    ArrayList<MealPLan> mealPLanDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipeCollectionReference = db.collection("mealPlan");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        mealPlanList = findViewById(R.id.mealPlan_list);
        mealPLanDataList = new ArrayList<>();
        mealPLanAdapter = new MealPLanAdaptor(this, mealPLanDataList);
        mealPlanList.setAdapter(mealPLanAdapter);

        recipeCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                mealPLanDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Log.d(TAG, String.valueOf(doc.getData().get("ID")));
                    Log.d(TAG, String.valueOf(doc.getData().get("mealPlanID")));
                    Log.d(TAG, String.valueOf(doc.getData().get("number of servings")));
                    Log.d(TAG, String.valueOf(doc.getData().get("type")));


                    String mealPlanID = (String) doc.getData().get("mealPlanID");
                    String ID = (String) doc.getData().get("ID");
                    Long numberOfServings = (Long)doc.getData().get("number of servings");;
                    String type = (String) doc.getData().get("type");

                    mealPLanDataList.add(new MealPLan(mealPlanID, ID,
                            numberOfServings.intValue(), type));
                }
                mealPLanAdapter.notifyDataSetChanged();
            }
        });

        //Long click to delete an item in the listView.
        mealPlanList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String mealPlanID = mealPLanDataList.get(position).getMealPlanID();
                final String ID = mealPLanDataList.get(position).getID();
                //AlertDialog diaBox = DeleteCheck(mealPlanID,ID);
                //diaBox.show();
                return true;
            }
        });
    }

    private AlertDialog DeleteCheck(String mealPlanID, String ID)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete item: "+ ID)
                //.setIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_focused)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        recipeCollectionReference.document(mealPlanID)
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            Toast.makeText(getApplicationContext(),"Recipe: " + mealPlanID + " has been deleted",Toast.LENGTH_LONG).show();
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
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}