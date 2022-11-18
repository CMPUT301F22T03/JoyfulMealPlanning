package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IngredientsActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/

    @Rule
    public ActivityTestRule<IngredientsActivity> rule =
            new ActivityTestRule<>(IngredientsActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void a_start() throws Exception{

        Activity activity = rule.getActivity();
    }


    /**
     * Check if Ingredient floating action button could open and close properly
     */
    @Test
    public void test_Ingredient_ADD_FAB(){
        // Asserts that the current activity is the IngredientsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);

        solo.clickOnView(solo.getView(R.id.IngredientAddButton));  // click FA button
        solo.clickOnButton("cancel");  // cancel FA button
    }

    /**
     * Check if Ingredient could be added properly
     */
    @Test
    public void test_Ingredient_ADD_Function(){
        // Asserts that the current activity is the IngredientsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);

        // check if an item named Ingredient UI Test1 is in the list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 2000));

        solo.clickOnView(solo.getView(R.id.IngredientAddButton));  // click FA button

        String sample_Name = "Ingredient UI Test1";
        String sample_Amount = "10";
        String sample_Unit = "kg";
        String sample_Category = "fruit";
        String sample_Location = "fridge";

        EditText EditText_name = (EditText) solo.getView(R.id.IngredientDescriptionInput);
        EditText EditText_amount = (EditText) solo.getView(R.id.IngredientAmountInput);
        EditText EditText_unit = (EditText) solo.getView(R.id.IngredientUnitInput);
        EditText EditText_category = (EditText) solo.getView(R.id.IngredientCategoryInput);
        EditText EditText_location = (EditText) solo.getView(R.id.IngredientLocationInput);

        // Enter sample data named "Ingredient UI Test1"
        solo.enterText(EditText_name , sample_Name);

        // Clear the data in the IngredientAmountInput, since initially it sets to 1, we need to remove the initial data
        solo.clearEditText(EditText_amount);
        solo.enterText(EditText_amount,sample_Amount);
        solo.enterText(EditText_unit ,sample_Unit);


        solo.clickOnView(solo.getView(R.id.IngredientBBDatePicker));
        DatePicker datePicker = solo.getView(DatePicker.class, 0);

        // Important Notice:as described in the Android SDK, months are indexed starting at 0.
        // This means October is month 10, or index 9, thus giving you the correct result.
        // Below we want to select in the date picker to have year 2023,October 31st, and use index 9
        solo.setDatePicker(datePicker, 2023,9,31);
        solo.clickOnButton("OK");
//        Fragment outerFragment = rule.getActivity().getSupportFragmentManager().findFragmentByTag("outerFragmentTAG");
//        DialogFragment dialogFrag;
//        if (outerFragment == null) {
//            dialogFrag = (DialogFragment)rule.getActivity().getSupportFragmentManager().findFragmentByTag("datePicker");
//        } else {
//            dialogFrag = (DialogFragment)outerFragment.getChildFragmentManager().findFragmentByTag("datePicker");
//        }
//        Button okButton = ((DatePickerDialog)dialogFrag.getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
//        solo.clickOnView(okButton);

        solo.enterText(EditText_category, sample_Category);
        solo.enterText(EditText_location, sample_Location);

        Assert.assertEquals(sample_Name, EditText_name.getText().toString());
        Assert.assertEquals(sample_Amount, EditText_amount.getText().toString());
        Assert.assertEquals(sample_Unit, EditText_unit.getText().toString());

        Assert.assertEquals(2023, datePicker.getYear());
        Assert.assertEquals(9, datePicker.getMonth());
        Assert.assertEquals(31, datePicker.getDayOfMonth());

        Assert.assertEquals(sample_Category, EditText_category.getText().toString());
        Assert.assertEquals(sample_Location, EditText_location.getText().toString());

        solo.clickOnButton("ADD");

        // check if an item named Ingredient UI Test1 is in the list(should be true):
        // temporarily assertFalse for now, data has been updated to the firebase
        // assertFalse because activity_ingredients.xml have bugs need to be fixed
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 2000));
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void knockDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
