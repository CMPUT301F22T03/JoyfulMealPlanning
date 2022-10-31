package com.example.joyfulmealplanning;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MealPlanController extends AppCompatActivity {

    final String TAG = "Sample";
    ArrayAdapter<MealPLan> mealPLanAdapter;
    ArrayList<MealPLan> mealPLanDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    final CollectionReference recipeCollectionReference = db.collection("mealPlan");

    public MealPlanController(Context context) {
        this.context = context;

        mealPLanDataList = new ArrayList<>();
        mealPLanAdapter = new MealPLanAdaptor(context, mealPLanDataList);

        //Connect controller to online database
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
    }

    public ArrayAdapter<MealPLan> getMealPLanAdapter() {
        return mealPLanAdapter;
    }

    public ArrayList<MealPLan> getMealPLanDataList() {
        return mealPLanDataList;
    }

    public void Delete(String mealPlanID, String ID){
        AlertDialog diaBox = DeleteCheck(mealPlanID,ID);
        diaBox.show();
    }

    public void AddMealPlanFragment(FragmentManager fragmentManager){
        new MealPlanAddFragment(context).show(fragmentManager, "Add_MealPLan");
    }

    public void AddMealPlan(String ID, String type, Integer numberOfServings){
        String mealPlanID = ID + "_"+ type;
        HashMap<String, Object> data = new HashMap<>();
        if (ID.length()>0 && type.length()>0 && numberOfServings!=null){
            data.put("ID", ID);
            data.put("type",type);
            data.put("mealPlanID",mealPlanID);
            data.put("number of servings", numberOfServings);
            recipeCollectionReference
                    .document(mealPlanID)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, ID + " has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, ID + " could not be added!" + e.toString());
                        }
                    });
        }
    }


    private AlertDialog DeleteCheck(String mealPlanID, String ID)
    {
        AlertDialog DeleteDialogBox = new AlertDialog.Builder(context)
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
                                            Toast.makeText(context,"Recipe: " + mealPlanID + " has been deleted",Toast.LENGTH_LONG).show();
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
        return DeleteDialogBox;
    }

}



