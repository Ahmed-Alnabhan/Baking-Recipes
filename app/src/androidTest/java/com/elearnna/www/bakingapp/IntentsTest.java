package com.elearnna.www.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.elearnna.www.bakingapp.activities.RecipesActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Ahmed on 7/19/2017.
 */

@RunWith(AndroidJUnit4.class)
public class IntentsTest {

    @Rule
    public IntentsTestRule<RecipesActivity> mRecipeActivity =
            new IntentsTestRule<>(RecipesActivity.class);

    @Before
    public void stubIntents(){
        // Stub intents
        Instrumentation.ActivityResult activityResult =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null);

        intending(not(isInternal())).respondWith(activityResult);
    }

    @Test
    public void testIntent() {
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.scrollToPosition(1));

        SystemClock.sleep(2000);
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        SystemClock.sleep(1000);

        Intent intent = new Intent();
        intent.getParcelableArrayListExtra("recipe");
        Instrumentation.ActivityResult activityResult =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(toPackage("com.elearnna.www.bakingapp")).respondWith(activityResult);

    }
}
