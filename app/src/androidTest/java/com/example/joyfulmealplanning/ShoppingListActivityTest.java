package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class ShoppingListActivityTest {
    
    /*Declaration of variables*/
    private Solo solo;

    /*Establishes test rules*/
    @Rule

    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

//    public ActivityTestRule<MealPlanActivity> ruleMealPlan =
//            new ActivityTestRule<>(MealPlanActivity.class, true, true);
//
//    public ActivityTestRule<ShoppingListActivity> ruleShopping =
//            new ActivityTestRule<>(ShoppingListActivity.class, true, true);


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //soloShopping = new Solo(InstrumentationRegistry.getInstrumentation(),ruleShopping.getActivity());

        //solo = new Solo(InstrumentationRegistry.getInstrumentation(),ruleMealPlan.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
        //Activity activityShopping = ruleShopping.getActivity();
        //Activity activityMealPlan = ruleMealPlan.getActivity();
        //Activity activityIngredient = ruleIngredient.getActivity();

    }

    @Test
    public void checkShoppingList() {
        add_ingredient(solo);
        add_MealPlan(solo);
        test_ShoppingList(solo);
        delete_ingredient_and_meal(solo);
    }

    public void add_ingredient(Solo solo) {
        
        this.solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.imageView));

        this.solo.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);
        assertFalse( solo.waitForText("Ingredient for Shopping", 1, 5000));

        solo.clickOnView(solo.getView(R.id.IngredientAddButton));  // click FA button

        String sample_Name = "Ingredient for Shopping";
        String sample_Amount = "1";
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

        solo.clickOnButton("ADD");

        // check if an item named Ingredient UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText("Ingredient for Shopping", 1, 5000, solo.scrollListToTop(0)));
        solo.goBack();
    }

    public void add_MealPlan(Solo solo) {

        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

        //solo.clickOnButton(R.id.MealPlan);
        solo.clickOnView(solo.getView(R.id.MealPlan));
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);
        // check if an item named "MealPlan for Shopping" is in the list(should be false):
        assertFalse( solo.waitForText("Ingredient for Shopping", 1, 2000));

        // add meal plan based on ingredient
        solo.clickOnView(solo.getView(R.id.mealPlanAddFAB));  // click FA button
        solo.clickOnView(solo.getView(R.id.MealPlanAddStage1Choice1)); // click button INGREDIENT
        solo.enterText((EditText) solo.getView(R.id.MealPlanAddStage2InputNum), "10"); // enter number of servings

        solo.clickOnView(solo.getView(R.id.MealPlanAddStage2DatePicker));
        DatePicker datePicker = solo.getView(DatePicker.class, 0);

        // Important Notice:as described in the Android SDK, months are indexed starting at 0.
        // This means October is month 10, or index 9, thus giving you the correct result.
        // Below we want to select in the date picker to have year 2023,October 31st, and use index 9
        solo.setDatePicker(datePicker, 2023,9,31);
        solo.clickOnButton("OK");

        assertTrue( solo.waitForText("Ingredient for Shopping", 1, 4000));
        solo.clickOnText("Ingredient for Shopping");
        solo.clickOnButton("Add");
        solo.clickOnButton("Cancel");
        // check if item named Ingredient for Shopping is in the list:
        assertTrue( solo.waitForText("Ingredient for Shopping", 1, 2000, solo.scrollListToTop(0)));

        solo.goBack();
    }

    public void test_ShoppingList (Solo solo) {
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.ShoppingList));
        solo.assertCurrentActivity("Wrong Activity",ShoppingListActivity.class);

        assertTrue(solo.waitForText("Ingredient for Shopping", 1, 2000));
        assertTrue(solo.waitForText("Amount Needed: "+ (10 - 1), 1, 2000));
        assertTrue(solo.waitForText("Category: fruit", 1, 2000));
        assertTrue(solo.waitForText("Unit: kg", 1, 2000));

        solo.goBack();
    }

    public void delete_ingredient_and_meal(Solo solo) {

        // Delete ingredient
        this.solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.imageView));

        this.solo.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);
        assertTrue(solo.waitForText("Ingredient for Shopping", 1, 5000));

        // delete the item named Ingredient for Shopping in the list:
        solo.clickLongOnText("Ingredient for Shopping");
        solo.clickOnButton("Confirm");

        // check if an item named Ingredient for Shopping is in the list(should be false):
        assertFalse(solo.waitForText("Ingredient for Shopping", 1, 5000));
        solo.goBack();;

        // Delete Meal Plan
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.MealPlan));
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);

        assertTrue(solo.waitForText("Ingredient for Shopping", 1, 2000));
        solo.clickLongOnText("Ingredient for Shopping");
        solo.clickOnButton("Delete");
        // check if an item named test ingredient for meal plan is in the list(should be false):
        assertFalse(solo.waitForText("Ingredient for Shopping", 1, 2000));

        solo.goBack();

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
