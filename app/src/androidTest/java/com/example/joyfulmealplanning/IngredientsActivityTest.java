package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
     * Check if Ingredient's activity functionality could be worked properly
     */
    @Test
    public void test_Ingredient_All(){
        // Asserts that the current activity is the IngredientsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);

        // check if an item named Ingredient UI Test 1 is added to the list
        test_add(solo);

        // check if an item named Ingredient UI Test 1 is edited from the list
        test_edit(solo);

        // check if an item named Ingredient UI Test 2 is deleted from the list
        test_delete(solo);
    }

    /**
     * check if Recipe could be added properly
     */
    public void test_add(Solo solo){
        // check if an item named Ingredient UI Test1 is in the list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 5000));

        solo.clickOnView(solo.getView(R.id.IngredientAddButton));  // click FA button

        String sample_Name = "Ingredient UI Test1";
        String sample_Amount = "10";
        String sample_Unit = "kg";
        String sample_Category = "fruit";
        String sample_Location = "fridge";

        //get a reference to editTexts
        EditText EditText_name = (EditText) solo.getView(R.id.IngredientDescriptionInput);
        EditText EditText_amount = (EditText) solo.getView(R.id.IngredientAmountInput);
        EditText EditText_unit = (EditText) solo.getView(R.id.IngredientUnitInput);
        EditText EditText_category = (EditText) solo.getView(R.id.IngredientCategoryInput);
        EditText EditText_location = (EditText) solo.getView(R.id.IngredientLocationInput);

        // Enter sample data named "Ingredient UI Test1"
        solo.enterText(EditText_name , sample_Name);

        // Clear the data in the IngredientAmountInput, since initially it sets to 1, we need to remove the initial data
        solo.clearEditText(EditText_amount);

        // Set text for both editTexts
        solo.enterText(EditText_amount,sample_Amount);
        solo.enterText(EditText_unit ,sample_Unit);


        solo.clickOnView(solo.getView(R.id.IngredientBBDatePicker));
        DatePicker datePicker = solo.getView(DatePicker.class, 0);

        // Important Notice:as described in the Android SDK, months are indexed starting at 0.
        // This means October is month 10, or index 9, thus giving you the correct result.
        // Below we want to select in the date picker to have year 2023,October 31st, and use index 9
        solo.setDatePicker(datePicker, 2023,9,31);
        solo.clickOnButton("OK");

        // Set text for both editTexts
        solo.enterText(EditText_category, sample_Category);
        solo.enterText(EditText_location, sample_Location);

        // Assert the result
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
        assertTrue( solo.waitForText("Ingredient UI Test1", 1, 5000, solo.scrollListToTop(0)));
    }

    /**
     * check if Recipe could be edited properly
     */
    public void test_edit(Solo solo){
        String sample_Name1 = "Ingredient UI Test1";
        String sample_Amount1 = "10";
        String sample_Unit1 = "kg";
        String sample_Category1 = "fruit";
        String sample_Location1 = "fridge";

        // check if an item named Ingredient UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test1", 1, 5000));

        // click the item to edit
        solo.clickOnText(sample_Name1);

        //get a reference to editTexts
        EditText EditText_name = (EditText) solo.getView(R.id.IngredientDescriptionInput);
        EditText EditText_amount = (EditText) solo.getView(R.id.IngredientAmountInput);
        EditText EditText_unit = (EditText) solo.getView(R.id.IngredientUnitInput);
        EditText EditText_category = (EditText) solo.getView(R.id.IngredientCategoryInput);
        EditText EditText_location = (EditText) solo.getView(R.id.IngredientLocationInput);


        // Assert the result before editing each value (original value should return true)
        Assert.assertEquals(sample_Name1, EditText_name.getText().toString());
        Assert.assertEquals(sample_Amount1, EditText_amount.getText().toString());
        Assert.assertEquals(sample_Unit1, EditText_unit.getText().toString());

        Assert.assertEquals(sample_Category1, EditText_category.getText().toString());
        Assert.assertEquals(sample_Location1, EditText_location.getText().toString());

        // edit the data
        String sample_Name2 = "Ingredient UI Test2";
        String sample_Amount2 = "50000";
        String sample_Unit2 = "g";
        String sample_Category2 = "soft drink";
        String sample_Location2 = "floor";

        // Assert the result before editing each value (changed value should return false)
        Assert.assertNotEquals(sample_Name2, EditText_name.getText().toString());
        Assert.assertNotEquals(sample_Amount2, EditText_amount.getText().toString());
        Assert.assertNotEquals(sample_Unit2, EditText_unit.getText().toString());

        Assert.assertNotEquals(sample_Category2, EditText_category.getText().toString());
        Assert.assertNotEquals(sample_Location2, EditText_location.getText().toString());

        // Edit sample data named "Ingredient UI Test1" to "Ingredient UI Test2"
        solo.clearEditText(EditText_name);
        solo.enterText(EditText_name , sample_Name2);

        // Edit sample amount 10 to 50000
        solo.clearEditText(EditText_amount);
        solo.enterText(EditText_amount,sample_Amount2);

        // Edit sample unit kg to g
        solo.clearEditText(EditText_unit);
        solo.enterText(EditText_unit ,sample_Unit2);

        // Edit sample category fruit to soft drink
        solo.clearEditText(EditText_category);
        solo.enterText(EditText_category ,sample_Category2);

        // Edit sample location fridge to floor
        solo.clearEditText(EditText_location);
        solo.enterText(EditText_location, sample_Location2);

        solo.clickOnButton("ADD");

        // check if an item named Ingredient UI Test1 is in the list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 5000));

        // check if an item named Ingredient UI Test2 is in the list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test2", 1, 5000, solo.scrollListToTop(0)));

        // click the edited item to check
        solo.clickOnText(sample_Name2);

        // Assert the result after editing each value (original value should return false)
        Assert.assertNotEquals(sample_Name1, EditText_name.getText().toString());
        Assert.assertNotEquals(sample_Amount1, EditText_amount.getText().toString());
        Assert.assertNotEquals(sample_Unit1, EditText_unit.getText().toString());

        Assert.assertNotEquals(sample_Category1, EditText_category.getText().toString());
        Assert.assertNotEquals(sample_Location1, EditText_location.getText().toString());

        // Assert the result after editing each value (edited value should return true)
        Assert.assertEquals(sample_Name2, EditText_name.getText().toString());
        Assert.assertEquals(sample_Amount2, EditText_amount.getText().toString());
        Assert.assertEquals(sample_Unit2, EditText_unit.getText().toString());

        Assert.assertEquals(sample_Category2, EditText_category.getText().toString());
        Assert.assertEquals(sample_Location2, EditText_location.getText().toString());

        solo.clickOnButton("ADD");

        // click the edited item to edit time
        solo.clickOnText(sample_Name2);

        String sample_Time1 = "20231031";
        String sample_Time2 = "2024-11-30";

        TextView textview_time = (TextView) solo.getView(R.id.IngredientBBDateDisplay);

        // Assert the result before editing each value (original value should return true)
        Assert.assertEquals(sample_Time1, textview_time.getText().toString());

        // Assert the result before editing each value (edited value should return false)
        Assert.assertNotEquals(sample_Time2, textview_time.getText().toString());

        solo.clickOnView(solo.getView(R.id.IngredientBBDatePicker));
        DatePicker datePicker = solo.getView(DatePicker.class, 0);

        // Important Notice:as described in the Android SDK, months are indexed starting at 0.
        // This means October is month 10, or index 9, thus giving you the correct result.
        // Below we want to select in the date picker to have year 2024,November 30th, and use index 10
        solo.setDatePicker(datePicker, 2024,10,30);
        solo.clickOnButton("OK");

        Assert.assertEquals(2024, datePicker.getYear());
        Assert.assertEquals(10, datePicker.getMonth());
        Assert.assertEquals(30, datePicker.getDayOfMonth());

        solo.clickOnButton("ADD");

        solo.clickOnText(sample_Name2);

        // Assert the result after editing each value (original value should return false)
        Assert.assertNotEquals(sample_Time1, textview_time.getText().toString());

        // Assert the result after editing each value (edited value should return true)
        Assert.assertEquals(sample_Time2, textview_time.getText().toString());

        solo.clickOnButton("ADD");
    }

    /**
     * check if Recipe could be added properly
     */
    public void test_delete(Solo solo){
        // check if an item named Ingredient UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test2", 1, 5000));

        // delete the item named Ingredient UI Test1 in the list:
        solo.clickLongOnText("Ingredient UI Test2");
        solo.clickOnButton("Confirm");

        // check if an item named Ingredient UI Test1 is in the list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test2", 1, 5000));
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
