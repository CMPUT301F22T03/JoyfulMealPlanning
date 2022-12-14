package com.example.joyfulmealplanning;

import android.app.Activity;
import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented Test class for MainActivity. All the UI tests are written here. Robotium test framework is used
 * @version 2.0
 * @author Fan Zhu & Xiangxu Meng
 * @see <a href = "https://github.com/RobotiumTech/">Robotium Test Framework</a>
 * @see <a href = "https://developer.android.com/training/testing/instrumented-tests">Instrumented Tests</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /*Declaration of variables*/
    private Solo solo;


    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

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


    @Test
    public void switchToIngredientsTest() {
          solo.assertCurrentActivity("Wrong activity", MainActivity.class);

          MainActivity activity = (MainActivity) solo.getCurrentActivity();
          final ImageView IngredientsImage = activity.imageView;
          solo.clickOnView(IngredientsImage);

          solo.assertCurrentActivity("Wrong activity", IngredientsActivity.class);
    }

    @Test
    public void switchToShoppingListTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ImageView S_List_image = activity.findViewById(R.id.shoppingListButton);
        solo.clickOnView(S_List_image);
        solo.assertCurrentActivity("Wrong activity", ShoppingListActivity.class);
    }


    @Test
    public void switchToRecipeTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ImageView RecipeImage = activity.findViewById(R.id.recipeButton);
        solo.clickOnView(RecipeImage);
        solo.assertCurrentActivity("Wrong activity", RecipeActivity.class);
    }

    @Test
    public void switchToMealPlanTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ImageView Meal_plan_image = activity.findViewById(R.id.mealPlanButton);
        solo.clickOnView(Meal_plan_image);
        solo.assertCurrentActivity("Wrong activity", MealPlanActivity.class);
    }


    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
