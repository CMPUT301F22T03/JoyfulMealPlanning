package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ShoppingListActivityTest {
    
    /*Declaration of variables*/
    private Solo soloIngredient, soloShopping, soloMealPlan;

    /*Establishes test rules*/
    @Rule

    public ActivityTestRule<IngredientsActivity> ruleIngredient =
            new ActivityTestRule<>(IngredientsActivity.class, true, true);

    public ActivityTestRule<ShoppingListActivity> ruleShopping =
            new ActivityTestRule<>(ShoppingListActivity.class, true, true);

    public ActivityTestRule<MealPlanActivity> ruleMealPlan =
            new ActivityTestRule<>(MealPlanActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        //soloShopping = new Solo(InstrumentationRegistry.getInstrumentation(),ruleShopping.getActivity());

        //soloMealPlan = new Solo(InstrumentationRegistry.getInstrumentation(),ruleMealPlan.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        //Activity activityShopping = ruleShopping.getActivity();
        //Activity activityMealPlan = ruleMealPlan.getActivity();
        //Activity activityIngredient = ruleIngredient.getActivity();

    }

    @Test
    public void checkShoppingList() {
        add_ingredient();
        add_MeanPlan();

    }

    public void add_ingredient() {
        soloIngredient = new Solo(InstrumentationRegistry.getInstrumentation(),ruleIngredient.getActivity());
        this.soloIngredient.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);

        assertFalse( soloIngredient.waitForText("Ingredient for Shopping", 1, 5000));

        soloIngredient.clickOnView(soloIngredient.getView(R.id.IngredientAddButton));  // click FA button

        String sample_Name = "Ingredient for Shopping";
        String sample_Amount = "10";
        String sample_Unit = "kg";
        String sample_Category = "fruit";
        String sample_Location = "fridge";

        //get a reference to editTexts
        EditText EditText_name = (EditText) soloIngredient.getView(R.id.IngredientDescriptionInput);
        EditText EditText_amount = (EditText) soloIngredient.getView(R.id.IngredientAmountInput);
        EditText EditText_unit = (EditText) soloIngredient.getView(R.id.IngredientUnitInput);
        EditText EditText_category = (EditText) soloIngredient.getView(R.id.IngredientCategoryInput);
        EditText EditText_location = (EditText) soloIngredient.getView(R.id.IngredientLocationInput);

        // Enter sample data named "Ingredient UI Test1"
        soloIngredient.enterText(EditText_name , sample_Name);

        // Clear the data in the IngredientAmountInput, since initially it sets to 1, we need to remove the initial data
        soloIngredient.clearEditText(EditText_amount);

        // Set text for both editTexts
        soloIngredient.enterText(EditText_amount,sample_Amount);
        soloIngredient.enterText(EditText_unit ,sample_Unit);


        soloIngredient.clickOnView(soloIngredient.getView(R.id.IngredientBBDatePicker));
        DatePicker datePicker = soloIngredient.getView(DatePicker.class, 0);

        // Important Notice:as described in the Android SDK, months are indexed starting at 0.
        // This means October is month 10, or index 9, thus giving you the correct result.
        // Below we want to select in the date picker to have year 2023,October 31st, and use index 9
        soloIngredient.setDatePicker(datePicker, 2023,9,31);
        soloIngredient.clickOnButton("OK");

        // Set text for both editTexts
        soloIngredient.enterText(EditText_category, sample_Category);
        soloIngredient.enterText(EditText_location, sample_Location);

        soloIngredient.clickOnButton("ADD");

        // check if an item named Ingredient UI Test1 is in the list(should be true):
        assertTrue( soloIngredient.waitForText("Ingredient for Shopping", 1, 5000, soloIngredient.scrollListToTop(0)));
        soloIngredient.finishOpenedActivities();
    }

    public void add_MeanPlan() {
        
    }
    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void knockDown() throws Exception{
        //soloShopping.finishOpenedActivities();

        //soloMealPlan.finishOpenedActivities();
    }
}
