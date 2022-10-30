package com.example.joyfulmealplanning;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.runner.RunWith;


/**
 * @author Fan Zhu
 * @see <a href = "https://github.com/RobotiumTech/">Robotium Test Framework</a>
 *
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
}
