package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RecipeActivityTest {
    private Solo solo;


    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<RecipeActivity> rule =
            new ActivityTestRule<>(RecipeActivity.class, true, true);

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
     * Check if Recipe floating action button could open and close properly
     */
    @Test
    public void test_Recipe_ADD_FAB(){
        // Asserts that the current activity is the IngredientsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.RecipeAddButton));  // click FA button
        solo.clickOnButton("cancel");  // cancel FA button
    }

    /**
     * Check if Recipe could be added,edited,delete properly
     */
    @Test
    public void test_Recipe_All(){
        // Asserts that the current activity is the IngredientsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",RecipeActivity.class);
        // check if an item named Recipe UI Test1 is in the list(should be false):
        assertFalse( solo.waitForText("Recipe UI Test1", 1, 2000));

        solo.clickOnView(solo.getView(R.id.RecipeAddButton));  // click FA button
        solo.enterText((EditText) solo.getView(R.id.RecipeTitleInput) , "Recipe UI Test1");
        solo.enterText((EditText) solo.getView(R.id.RecipeCategoryInput) , "Dessert");
        solo.enterText((EditText) solo.getView(R.id.RecipeCommentsInput) , "This is a comment");
        solo.enterText((EditText) solo.getView(R.id.RecipeNumberInput) , "6");
        solo.enterText((EditText) solo.getView(R.id.RecipeTimeInput) , "30");
        solo.clickOnButton("OK");

        // check if an item named Recipe UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText("Recipe UI Test1", 1, 2000));

        // check if could add item to the ingredient list in a recipe
        solo.clickOnText("Recipe UI Test1");
        // check if an item named Ingredient UI Test1 is in the ingredient list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 2000));
        solo.clickOnView(solo.getView(R.id.RecipeAddNewIngredientButton));

        solo.enterText((EditText) solo.getView(R.id.RecipeNewIngredientDescriptionInput) , "Ingredient UI Test1");
        solo.enterText((EditText) solo.getView(R.id.RecipeNewIngredientAmountInput) , "6");
        solo.enterText((EditText) solo.getView(R.id.RecipeNewIngredientUnitInput) , "kg");
        solo.enterText((EditText) solo.getView(R.id.RecipeNewIngredientCategoryInput) , "dessert");
        solo.clickOnButton("Confirm");

        // check if an item named Ingredient UI Test1 is in the ingredient list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test1", 1, 2000));

        // check if an item named test ingredient for meal plan is in the ingredient list(should be false):
        assertFalse( solo.waitForText("test ingredient for meal plan", 1, 2000));

        solo.clickOnView(solo.getView(R.id.RecipeAddIngredientFromStorageButton));
        solo.enterText((EditText) solo.getView(R.id.RecipeAddStorageIngredientAmountInput) , "6");
        solo.clickOnText("test ingredient for meal plan");
        solo.clickOnButton("Confirm");

        // check if an item named test ingredient for meal plan is in the ingredient list(should be true):
        assertTrue( solo.waitForText("test ingredient for meal plan", 1, 2000));
        solo.clickOnButton("OK");

        // check if user could edit recipe from name Recipe UI Test1 to Ingredient UI Test2:
        assertTrue( solo.waitForText("Recipe UI Test1", 1, 2000));
        assertFalse( solo.waitForText("Recipe UI Test2", 1, 2000));
        solo.clickOnText("Recipe UI Test1");
        solo.clearEditText((EditText) solo.getView(R.id.RecipeTitleInput));
        solo.enterText((EditText) solo.getView(R.id.RecipeTitleInput) , "Recipe UI Test2");
        solo.clickOnButton("OK");

        assertFalse( solo.waitForText("Recipe UI Test1", 1, 2000));
        assertTrue( solo.waitForText("Recipe UI Test2", 1, 2000));

        test_delete(solo);

    }

    /**
     * check if Recipe could be deleted properly
     */
    public void test_delete(Solo solo){
        solo.clickOnText("Recipe UI Test2");

        // check to delete Ingredient list first
        // check if an item named Ingredient UI Test1 is in the ingredient list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test1", 1, 2000));
        // check if an item named test ingredient for meal plan is in the ingredient list(should be true):
        assertTrue( solo.waitForText("test ingredient for meal plan", 1, 2000));

        // delete the Ingredient list:
        solo.clickOnText("Ingredient UI Test1");
        solo.clickOnView(solo.getView(R.id.RecipeDeleteIngredientButton));

        solo.clickOnText("test ingredient for meal plan");
        solo.clickOnView(solo.getView(R.id.RecipeDeleteIngredientButton));

        // check if an item named Ingredient UI Test1 is in the ingredient list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 2000));

        // check if an item named test ingredient for meal plan is in the ingredient list(should be false):
        assertFalse( solo.waitForText("test ingredient for meal plan", 1, 2000));
        solo.clickOnButton("OK");

        // check if an item named Recipe UI Test2 is in the list(should be true):
        assertTrue( solo.waitForText("Recipe UI Test2", 1, 2000));

        // delete the item named Recipe UI Test 2 in the list:
        solo.clickLongOnText("Recipe UI Test2");
        solo.clickOnButton("Confirm");

        // check if an item named Recipe UI Test2 is in the list(should be false):
        assertFalse( solo.waitForText("Recipe UI Test2", 1, 2000));

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
