package org.frugo.reversecalculator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityStateTest {

    @Test
    public void calculatorStateSurvivesActivityRecreation() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withText(R.string._1)).perform(click());
            onView(withText(R.string.enter)).perform(click());
            onView(withText(R.string._2)).perform(click());

            scenario.recreate();

            onView(withId(R.id.buffer)).check(matches(withText("2")));
            onView(withId(R.id.state)).check(matches(withText("[1]")));
        }
    }
}
