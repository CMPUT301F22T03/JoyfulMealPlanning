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
public class SignUpActivityTest{
    /*Declaration of variables*/
    private Solo solo;


    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<SignUpActivity> rule =
            new ActivityTestRule<>(SignUpActivity.class, true, true);

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
     * Test if the sign up activity could work
     */
    @Test
    public void test_signup(){
        // Asserts that the current activity is the SignUpActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        // sample username and password:
        String sample_UserName = "test@gmail.com";
        String sample_PassWord = "administrationTEST";

        //get a reference to editTexts
        EditText EditText_UserName = (EditText) solo.getView(R.id.user_email_registration);
        EditText EditText_PassWord = (EditText) solo.getView(R.id.psw_registration);

        // Enter sample UserName named "test@gmail.com"
        solo.enterText(EditText_UserName , sample_UserName);

        // Enter sample PassWord named "administrationTEST"
        solo.enterText(EditText_PassWord , sample_PassWord);

        // Assert the result
        Assert.assertEquals(sample_UserName, EditText_UserName.getText().toString());
        Assert.assertEquals(sample_PassWord, EditText_PassWord.getText().toString());
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
