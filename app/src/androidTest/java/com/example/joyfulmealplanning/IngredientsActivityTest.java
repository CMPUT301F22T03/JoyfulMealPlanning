package com.example.joyfulmealplanning;

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
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity",IngredientsActivity.class);

        solo.clickOnView(solo.getView(R.id.IngredientAddButton));

        solo.enterText((EditText) solo.getView(R.id.IngredientDescriptionInput) , "Test_Ingredient1");
        solo.enterText((EditText) solo.getView(R.id.IngredientAmountInput) , String.valueOf(10));
        solo.enterText((EditText) solo.getView(R.id.IngredientUnitInput) , "kg");
        solo.clickOnView(solo.getView(R.id.IngredientBBDatePicker));
        solo.enterText((EditText) solo.getView(R.id.IngredientCategoryInput) , "fruit");
        solo.enterText((EditText) solo.getView(R.id.IngredientLocationInput) , "fridge");
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
