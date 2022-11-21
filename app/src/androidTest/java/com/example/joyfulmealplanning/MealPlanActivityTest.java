package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MealPlanActivityTest {
    /*Declaration of variables*/
    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<MealPlanActivity> rule =
            new ActivityTestRule<>(MealPlanActivity.class, true, true);

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
     * Check if MealPlan floating action button could open and close properly
     */
    @Test
    public void test_MealPlan_ADD_FAB(){
        // Asserts that the current activity is the MealPanActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);

        solo.clickOnView(solo.getView(R.id.mealPlanAddFAB));  // click FA button
        solo.clickOnButton("Cancel");  // cancel FA button
    }

    /**
     * Check if MealPlan could add from ingredient
     */
    @Test
    public void test_MealPlan_ADD_Ingredient(){
        // Asserts that the current activity is the MealPanActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);

        // check if an item named test ingredient for meal plan is in the list(should be false):
        assertFalse( solo.waitForText("test ingredient for meal plan", 1, 2000));

        // add meal plan based on ingredient
        solo.clickOnView(solo.getView(R.id.mealPlanAddFAB));  // click FA button
        solo.clickOnView(solo.getView(R.id.MealPlanAddStage1Choice1)); // click button INGREDIENT
        solo.enterText((EditText) solo.getView(R.id.MealPlanAddStage2InputNum), "8"); // enter number of servings
        assertTrue( solo.waitForText("test ingredient for meal plan", 1, 4000));
        solo.clickOnText("test ingredient for meal plan");
        solo.clickOnButton("Add");
        solo.clickOnButton("Cancel");
        // check if item named test ingredient for meal plan is in the list(should be false):
        assertTrue( solo.waitForText("test ingredient for meal plan", 1, 2000));

    }

    /**
     * Check if MealPlan could delete from ingredient
     */
    @Test
    public void test_MealPlan_DELETE_Ingredient(){
        // Asserts that the current activity is the MealPanActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);

        // check if an item named test meal plan is in the list(should be true):
        assertTrue( solo.waitForText("test ingredient for meal plan", 1, 2000));

        solo.clickLongOnText("test ingredient for meal plan");
        solo.clickOnButton("Delete");
        // check if an item named test ingredient for meal plan is in the list(should be false):
        assertFalse( solo.waitForText("test ingredient for meal plan", 1, 2000));
    }

    /**
     * Check if MealPlan could add from recipe
     */
    @Test
    public void test_MealPlan_ADD_Recipe(){
        // Asserts that the current activity is the MealPanActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);

        // check if an item named test recipe for meal plan is in the list(should be false):
        assertFalse( solo.waitForText("test recipe for meal plan", 1, 2000));

        // add meal plan based on recipe
        solo.clickOnView(solo.getView(R.id.mealPlanAddFAB));  // click FA button
        solo.clickOnView(solo.getView(R.id.MealPlanAddStage1Choice2)); // click button RECIPE
        solo.enterText((EditText) solo.getView(R.id.MealPlanAddStage2InputNum), "8"); // enter number of servings
        assertTrue( solo.waitForText("test recipe for meal plan", 1, 4000));
        solo.clickOnText("test recipe for meal plan");
        solo.clickOnButton("Add");
        solo.clickOnButton("Cancel");
        // check if item named test recipe for meal plan is in the list(should be false):
        assertTrue( solo.waitForText("test recipe for meal plan", 1, 2000));
    }

    /**
     * Check if MealPlan could delete from recipe
     */
    @Test
    public void test_MealPlan_DELETE_Recipe(){
        // Asserts that the current activity is the MealPanActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",MealPlanActivity.class);

        // check if an item named test meal plan is in the list(should be true):
        assertTrue( solo.waitForText("test recipe for meal plan", 1, 2000));

        solo.clickLongOnText("test recipe for meal plan");
        solo.clickOnButton("Delete");
        // check if an item named test ingredient for meal plan is in the list(should be false):
        assertFalse( solo.waitForText("test recipe for meal plan", 1, 2000));
    }

    @Test
    public void test_Spinner() {
        //TODO: Check if the spinner is viable
        solo.assertCurrentActivity("Wrong activity", MealPlanActivity.class);
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
