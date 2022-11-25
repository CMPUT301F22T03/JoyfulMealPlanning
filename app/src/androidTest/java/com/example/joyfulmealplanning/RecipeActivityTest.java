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

        // check if an recipe item named 'Recipe UI Test 1' is added to the recipe:
        // check if an ingredient item named 'Ingredient UI Test1' is added to the 'Recipe UI Test 1'
        // check if an ingredient item named 'cabbage' from storage is added to the 'Recipe UI Test 1'
        test_add(solo);

        // check if an item named Recipe UI Test 1 is edited from the list
        test_edit(solo);

        // check if an item named Recipe UI Test 2 is deleted from the list
        // check if ingredients in the ingredient list of Recipe UI Test 2 is deleted
        test_delete(solo);

    }

    /**
     * check if Recipe could be added properly
     */
    public void test_add(Solo solo){
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

        solo.scrollListToTop(0);
        // check if an item named Recipe UI Test1 is in the list(should be true):
        assertTrue( solo.waitForText(sample_Title, 1, 5000));

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
    }

    /**
     * check if Recipe could be edited properly
     */
    public void test_edit(Solo solo){
        String sample_Title1 = "Recipe UI Test1";
        String sample_Category1 = "Dessert";
        String sample_Comment1 = "This is a comment";
        String sample_Number1 = "6";
        String sample_Time1 = "30";

        // edit the data
        String sample_Title2 = "Recipe UI Test2";
        String sample_Category2 = "Seafood";
        String sample_Comment2 = "This is an edited comment";
        String sample_Number2 = "2";
        String sample_Time2 = "45";

        // check if Recipe UI Test2 is in the recipe list(should be false):
        // hint: there is no item named Recipe UI Test2 in the recipe list, we need to edit the Test1 to Test2
        solo.scrollListToTop(0);
        assertFalse( solo.waitForText(sample_Title2, 1, 5000));

        solo.scrollListToTop(0);
        // check if Recipe UI Test1 is in the recipe list(should be true):
        assertTrue( solo.waitForText(sample_Title1, 1, 5000));


        // click on the specific recipe to edit:
        solo.clickOnText(sample_Title1);

        //get a reference to editTexts
        EditText EditText_title = (EditText) solo.getView(R.id.RecipeTitleInput);
        EditText EditText_category = (EditText) solo.getView(R.id.RecipeCategoryInput);
        EditText EditText_comment = (EditText) solo.getView(R.id.RecipeCommentsInput);
        EditText EditText_number = (EditText) solo.getView(R.id.RecipeNumberInput);
        EditText EditText_time = (EditText) solo.getView(R.id.RecipeTimeInput);

        // Assert the result before editing each value (original value should return true)
        Assert.assertEquals(sample_Title1, EditText_title.getText().toString());
        Assert.assertEquals(sample_Category1, EditText_category.getText().toString());
        Assert.assertEquals(sample_Comment1, EditText_comment.getText().toString());
        Assert.assertEquals(sample_Number1, EditText_number.getText().toString());
        Assert.assertEquals(sample_Time1, EditText_time.getText().toString());


        // Assert the result before editing each value (changed value should return false)
        Assert.assertNotEquals(sample_Title2, EditText_title.getText().toString());
        Assert.assertNotEquals(sample_Category2, EditText_category.getText().toString());
        Assert.assertNotEquals(sample_Comment2, EditText_comment.getText().toString());
        Assert.assertNotEquals(sample_Number2, EditText_number.getText().toString());
        Assert.assertNotEquals(sample_Time2, EditText_time.getText().toString());

        // Edit sample data named "Recipe UI Test1" to "Recipe UI Test2"
        solo.clearEditText(EditText_title);
        solo.enterText(EditText_title , sample_Title2);

        // Edit sample category Dessert to Seafood
        solo.clearEditText(EditText_category);
        solo.enterText(EditText_category , sample_Category2);

        // Edit sample comment to an edited comment
        solo.clearEditText(EditText_comment);
        solo.enterText(EditText_comment , sample_Comment2);

        // Edit sample serving number 6 to 2
        solo.clearEditText(EditText_number);
        solo.enterText(EditText_number , sample_Number2);

        // Edit sample preparation time 30 to 45
        solo.clearEditText(EditText_time);
        solo.enterText(EditText_time , sample_Time2);

        solo.clickOnButton("OK");

        solo.scrollUpList(0);
        // check if an item named Recipe UI Test1 is in the list(should be false):
        assertFalse( solo.waitForText(sample_Title1, 1, 5000));

        solo.scrollUpList(0);
        // check if an item named Recipe UI Test2 is in the list(should be true):
        assertTrue( solo.waitForText(sample_Title2, 1, 5000));

        // check if items in Recipe UI Test2 is edited as well
        solo.clickOnText(sample_Title2);

        // Assert the result after editing each value (original value should return false)
        Assert.assertNotEquals(sample_Title1, EditText_title.getText().toString());
        Assert.assertNotEquals(sample_Category1, EditText_category.getText().toString());
        Assert.assertNotEquals(sample_Comment1, EditText_comment.getText().toString());
        Assert.assertNotEquals(sample_Number1, EditText_number.getText().toString());
        Assert.assertNotEquals(sample_Time1, EditText_time.getText().toString());

        // Assert the result after editing each value (edited value should return true)
        Assert.assertEquals(sample_Title2, EditText_title.getText().toString());
        Assert.assertEquals(sample_Category2, EditText_category.getText().toString());
        Assert.assertEquals(sample_Comment2, EditText_comment.getText().toString());
        Assert.assertEquals(sample_Number2, EditText_number.getText().toString());
        Assert.assertEquals(sample_Time2, EditText_time.getText().toString());

        solo.clickOnButton("OK");
    }

    /**
     * check if Recipe could be deleted properly
     */
    public void test_delete(Solo solo){
        String sample_Title = "Recipe UI Test2";
        String sample_Ingredient1 = "Ingredient UI Test1";
        String sample_Ingredient2 = "cabbage";

        // check if Recipe UI Test2 is in the recipe list(should be true):
        assertTrue( solo.waitForText(sample_Title, 1, 5000));

        // open recipe named "Recipe UI Test2"
        solo.clickOnText(sample_Title);

        // check to delete Ingredient list first
        // check if an item named Ingredient UI Test1 is in the ingredient list(should be true):
        assertTrue( solo.waitForText(sample_Ingredient1, 1, 2000));

        // check if an item named cabbage is in the ingredient list(should be true):
        assertTrue( solo.waitForText(sample_Ingredient2, 1, 2000));

        // delete the Ingredient list:
        solo.clickOnText(sample_Ingredient1);
        solo.clickOnView(solo.getView(R.id.RecipeDeleteIngredientButton));

        solo.clickOnText(sample_Ingredient2);
        solo.clickOnView(solo.getView(R.id.RecipeDeleteIngredientButton));

        // check if an item named Ingredient UI Test1 is in the ingredient list(should be false):
        assertFalse( solo.waitForText(sample_Ingredient1, 1, 2000));

        // check if an item named cabbage is in the ingredient list(should be false):
        assertFalse( solo.waitForText(sample_Ingredient2, 1, 2000));

        solo.clickOnButton("OK");

        solo.scrollUpList(0);
        // check if an item named Recipe UI Test2 is in the list(should be true):
        assertTrue( solo.waitForText(sample_Title, 1, 5000));

        // delete the item named Recipe UI Test 2 in the list:
        solo.clickLongOnText(sample_Title);

        solo.clickOnButton("Confirm");

        solo.scrollUpList(0);
        // check if an item named Recipe UI Test2 is in the list(should be false):
        assertFalse( solo.waitForText(sample_Title, 1, 5000));

    }

    /**
     * check if Recipe sort spinner could work as expected
     */
    @Test
    public void test_Recipe_Spinner() {
        // check if the sort spinner could work with the sort by title
        test_sort_by_title(solo);

        // check if the sort spinner could work with the sort by preparation time
        test_sort_by_time(solo);

        // check if the sort spinner could work with the sort by number of servings
        test_sort_by_number(solo);

        // check if the sort spinner could work with the sort by recipe category
        test_sort_by_category(solo);
    }

    /**
     * check if Recipe sort spinner could work as sorted by title
     */
    public void test_sort_by_title(Solo solo){
        // Asserts that the current activity is the RecipeActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        // it is sorted by title by default, so change the sort to another one first
        solo.clickOnView(solo.getView(R.id.recipe_list_sort_spinner));
        solo.clickOnText("recipe category");

        solo.sleep(4000);
        // click on spinner and click on the title on spinner
        solo.clickOnView(solo.getView(R.id.recipe_list_sort_spinner));
        solo.clickOnText("title");
        solo.sleep(4000);

        //assertTrue(solo.waitForText("sorted by title",0, 2000, solo.scrollListToBottom(0)));
        solo.scrollDown();
        solo.sleep(3000);
        solo.scrollListToBottom(0);
        solo.sleep(3000);

    }

    /**
     * check if Ingredient sort spinner could work as sorted by preparation time
     */
    public void test_sort_by_time(Solo solo){
        // Asserts that the current activity is the RecipeActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.scrollListToTop(0);

        // click on the spinner and click preparation time on spinner
        solo.clickOnView(solo.getView(R.id.recipe_list_sort_spinner));
        solo.clickOnText("preparation time");
        solo.sleep(4000);

        //assertTrue(solo.waitForText("sorted by preparation time",0, 2000, solo.scrollListToBottom(0)));
        solo.scrollDown();
        solo.sleep(3000);
        solo.scrollListToBottom(0);
        solo.sleep(3000);
    }

    /**
     * check if Ingredient sort spinner could work as sorted by number of servings
     */
    public void test_sort_by_number(Solo solo){
        // Asserts that the current activity is the RecipeActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.scrollListToTop(0);

        // click on the spinner and click number of servings on spinner
        solo.clickOnView(solo.getView(R.id.recipe_list_sort_spinner));
        solo.clickOnText("number of servings");
        solo.sleep(4000);

        //assertTrue(solo.waitForText("sorted by number of servings",0, 2000, solo.scrollListToBottom(0)));
        solo.scrollDown();
        solo.sleep(3000);
        solo.scrollListToBottom(0);
        solo.sleep(3000);
    }

    /**
     * check if Ingredient sort spinner could work as sorted by preparation time
     */
    public void test_sort_by_category(Solo solo){
        // Asserts that the current activity is the RecipeActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.scrollListToTop(0);

        // click on the spinner and click recipe category on spinner
        solo.clickOnView(solo.getView(R.id.recipe_list_sort_spinner));
        solo.clickOnText("recipe category");
        solo.sleep(4000);

        //assertTrue(solo.waitForText("sorted by recipe category",0, 2000, solo.scrollListToBottom(0)));
        solo.scrollDown();
        solo.sleep(3000);
        solo.scrollListToBottom(0);
        solo.sleep(3000);
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
