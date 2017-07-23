package com.elearnna.www.bakingapp;

import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.elearnna.www.bakingapp.activities.RecipesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Ahmed on 7/17/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {
    @Rule public ActivityTestRule<RecipesActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void testRecipesAndSteps () {
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.scrollToPosition(1));

        SystemClock.sleep(2000);
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withText(R.string.brownies)).check(matches(isDisplayed()));

        SystemClock.sleep(3000);
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.scrollToPosition(1));

        SystemClock.sleep(3000);
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withText(R.string.starting_prep)).check(matches(isDisplayed()));

        // Test the next button
        SystemClock.sleep(3000);
        onView(withId(R.id.next_btn)).perform(click());

        onView(withText(R.string.melt_step_3)).check(matches(isDisplayed()));

        // Test the previous button
        SystemClock.sleep(3000);
        onView(withId(R.id.previous_btn)).perform(click());

        onView(withText(R.string.starting_prep)).check(matches(isDisplayed()));
    }

}
