package com.example.joyfulmealplanning;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;

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
public class LoginActivityTest {
    /*Declaration of variables*/
    private Solo solo;


    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

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
     * Test if can switch login activity to signup activity
     */
    @Test
    public void test_switch_to_signup(){
        // Asserts that the current activity is the LoginActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // click SIGN UP HERE
        solo.clickOnView(solo.getView(R.id.registration));

        // Asserts that the current activity is the SignUpActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",SignUpActivity.class);

        solo.sleep(3000);

        // go back to LoginActivity
        solo.goBack();

        solo.sleep(3000);
    }

    /**
     * Test if can log in to the main menu with administration user and password
     */
    @Test
    public void test_login(){
        // Asserts that the current activity is the LoginActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // sample username and password:
        String sample_UserName = "admin@gmail.com";
        String sample_PassWord = "administration";

        //get a reference to editTexts
        EditText EditText_UserName = (EditText) solo.getView(R.id.userEmailInput);
        EditText EditText_PassWord = (EditText) solo.getView(R.id.userPasswordInput);

        // Enter sample UserName named "admin@gmail.com"
        solo.enterText(EditText_UserName , sample_UserName);

        // Enter sample PassWord named "administration"
        solo.enterText(EditText_PassWord , sample_PassWord);

        // Assert the result
        Assert.assertEquals(sample_UserName, EditText_UserName.getText().toString());
        Assert.assertEquals(sample_PassWord, EditText_PassWord.getText().toString());

        // click on log in button
        solo.clickOnView(solo.getView(R.id.login_button));

        solo.sleep(8000);

        // Asserts that the current activity is the MainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
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
