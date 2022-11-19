package com.example.joyfulmealplanning;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
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
        assertFalse( solo.waitForText("Recipe UI Test1", 1, 5000));

        solo.clickOnView(solo.getView(R.id.RecipeAddButton));  // click FA button

        String sample_Title = "Recipe UI Test1";
        String sample_Category = "Dessert";
        String sample_Comment = "This is a comment";
        String sample_Number = "6";
        String sample_Time = "30";

        //get a reference to editTexts
        EditText EditText_title = (EditText) solo.getView(R.id.RecipeTitleInput);
        EditText EditText_category = (EditText) solo.getView(R.id.RecipeCategoryInput);
        EditText EditText_comment = (EditText) solo.getView(R.id.RecipeCommentsInput);
        EditText EditText_number = (EditText) solo.getView(R.id.RecipeNumberInput);
        EditText EditText_time = (EditText) solo.getView(R.id.RecipeTimeInput);

        // Enter sample data named "Recipe UI Test1"
        solo.enterText(EditText_title, sample_Title);

        // Set the texts for category,comment,number,and time:
        solo.enterText(EditText_category, sample_Category);
        solo.enterText(EditText_comment , sample_Comment);

        solo.clearEditText(EditText_number);       // number set to default 1
        solo.enterText(EditText_number , sample_Number);
        solo.enterText(EditText_time , sample_Time);

        // Assert the result
        Assert.assertEquals(sample_Title, EditText_title.getText().toString());
        Assert.assertEquals(sample_Category, EditText_category.getText().toString());
        Assert.assertEquals(sample_Comment, EditText_comment.getText().toString());
        Assert.assertEquals(sample_Number, EditText_number.getText().toString());
        Assert.assertEquals(sample_Time, EditText_time.getText().toString());

        solo.clickOnButton("OK");

        // check if an item named Recipe UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText(sample_Title, 1, 5000,solo.scrollUpList(0)));

        // check if could add item of the ingredient list to a recipe, recipe example: Recipe UI Test1
        solo.clickOnText(sample_Title);

        String non_exist_ingredient_sample = "Ingredient UI Test1";
        String non_exist_ingredient_amount = "300";
        String non_exist_ingredient_unit = "g";
        String non_exist_ingredient_category = "liquor";

        // check if an item named Ingredient UI Test1 is in the ingredient list(should be false):
        assertFalse( solo.waitForText(non_exist_ingredient_sample, 1, 2000));

        // add a new ingredient to the ingredient list:
        solo.clickOnView(solo.getView(R.id.RecipeAddNewIngredientButton));   // click ADD NEW button

        //get a reference to editTexts
        EditText EditText_ingredient_title = (EditText) solo.getView(R.id.RecipeNewIngredientDescriptionInput);
        EditText EditText_ingredient_amount = (EditText) solo.getView(R.id.RecipeNewIngredientAmountInput);
        EditText EditText_ingredient_unit = (EditText) solo.getView(R.id.RecipeNewIngredientUnitInput);
        EditText EditText_ingredient_category = (EditText) solo.getView(R.id.RecipeNewIngredientCategoryInput);

        // set text for EditText variable title,amount,unit,category:
        solo.enterText(EditText_ingredient_title , non_exist_ingredient_sample);
        solo.enterText(EditText_ingredient_amount , non_exist_ingredient_amount);
        solo.enterText(EditText_ingredient_unit, non_exist_ingredient_unit);
        solo.enterText(EditText_ingredient_category , non_exist_ingredient_category);

        // Assert the result
        Assert.assertEquals(non_exist_ingredient_sample, EditText_ingredient_title.getText().toString());
        Assert.assertEquals(non_exist_ingredient_amount, EditText_ingredient_amount.getText().toString());
        Assert.assertEquals(non_exist_ingredient_unit, EditText_ingredient_unit.getText().toString());
        Assert.assertEquals(non_exist_ingredient_category, EditText_ingredient_category.getText().toString());

        solo.clickOnButton("Confirm");

        // check if an item named Ingredient UI Test1 is in the ingredient list(should be true):
        assertTrue( solo.waitForText(non_exist_ingredient_sample, 1, 2000));

        String exist_ingredient_sample = "cabbage";
        String exist_ingredient_amount = "2";

        // check if an existed ingredient named cabbage is in the ingredient list(should be false):
        assertFalse( solo.waitForText(exist_ingredient_sample, 1, 2000));

        // click button ADD FROM STORAGE:
        solo.clickOnView(solo.getView(R.id.RecipeAddIngredientFromStorageButton));

        //get a reference to editTexts
        EditText EditText_amount = (EditText) solo.getView(R.id.RecipeAddStorageIngredientAmountInput);

        // select a stored ingredient
        solo.enterText(EditText_amount, exist_ingredient_amount);
        solo.clickOnText(exist_ingredient_sample);

        Assert.assertEquals(exist_ingredient_amount, EditText_amount.getText().toString());

        solo.clickOnButton("Confirm");

        // check if an item named cabbage is in the ingredient list(should be true):
        assertTrue( solo.waitForText(exist_ingredient_sample, 1, 2000));
        solo.clickOnButton("OK");

//        // check if user could edit recipe from name Recipe UI Test1 to Ingredient UI Test2:
//        assertTrue( solo.waitForText("Recipe UI Test1", 1, 2000));
//        assertFalse( solo.waitForText("Recipe UI Test2", 1, 2000));
//        solo.clickOnText("Recipe UI Test1");
//        solo.clearEditText((EditText) solo.getView(R.id.RecipeTitleInput));
//        solo.enterText((EditText) solo.getView(R.id.RecipeTitleInput) , "Recipe UI Test2");
//        solo.clickOnButton("OK");
//
//        assertFalse( solo.waitForText("Recipe UI Test1", 1, 2000));
//        assertTrue( solo.waitForText("Recipe UI Test2", 1, 2000));

        test_delete(solo);

    }

    /**
     * check if Recipe could be deleted properly
     */
    public void test_delete(Solo solo){
        solo.clickOnText("Recipe UI Test1");

        // check to delete Ingredient list first
        // check if an item named Ingredient UI Test1 is in the ingredient list(should be true):
        assertTrue( solo.waitForText("Ingredient UI Test1", 1, 2000));
        // check if an item named test ingredient for meal plan is in the ingredient list(should be true):
        assertTrue( solo.waitForText("cabbage", 1, 2000));

        // delete the Ingredient list:
        solo.clickOnText("Ingredient UI Test1");
        solo.clickOnView(solo.getView(R.id.RecipeDeleteIngredientButton));

        solo.clickOnText("cabbage");
        solo.clickOnView(solo.getView(R.id.RecipeDeleteIngredientButton));

        // check if an item named Ingredient UI Test1 is in the ingredient list(should be false):
        assertFalse( solo.waitForText("Ingredient UI Test1", 1, 2000));

        // check if an item named test ingredient for meal plan is in the ingredient list(should be false):
        assertFalse( solo.waitForText("cabbage", 1, 2000));
        solo.clickOnButton("OK");

        // check if an item named Recipe UI Test2 is in the list(should be true):
        assertTrue( solo.waitForText("Recipe UI Test1", 1, 2000));

        // delete the item named Recipe UI Test 2 in the list:
        solo.clickLongOnText("Recipe UI Test1");
        solo.clickOnButton("Confirm");

        // check if an item named Recipe UI Test2 is in the list(should be false):
        assertFalse( solo.waitForText("Recipe UI Test1", 1, 2000));

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
