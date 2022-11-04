package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

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
        solo.enterText((EditText) solo.getView(R.id.IngredientDescriptionInput) , "Ingredient UI Test1");
        solo.clearEditText((EditText) solo.getView(R.id.IngredientAmountInput));
        solo.enterText((EditText) solo.getView(R.id.IngredientAmountInput) ,"10");
        solo.enterText((EditText) solo.getView(R.id.IngredientUnitInput) , "kg");
        solo.clickOnView(solo.getView(R.id.IngredientBBDatePicker));
        solo.setDatePicker(0, 2022,11,4);
        solo.clickOnButton(0);
        solo.enterText((EditText) solo.getView(R.id.IngredientCategoryInput) , "fruit");
        solo.enterText((EditText) solo.getView(R.id.IngredientLocationInput) , "fridge");
        solo.clickOnButton("OK");

        // check if an item named Ingredient UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test1", 1, 2000));
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
