package org.frugo.reversecalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class MainActivityStateTest {

    private static final long ACTIVITY_READY_TIMEOUT_MS = 10_000;
    private static final long ACTIVITY_READY_POLL_INTERVAL_MS = 50;

    @Test
    public void calculatorStateSurvivesActivityRecreation() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withText(R.string._1)).perform(click());
            onView(withText(R.string.enter)).perform(click());
            onView(withText(R.string._2)).perform(click());

            scenario.recreate();

            onView(withId(R.id.buffer)).check(matches(withText("2")));
            onView(withId(R.id.state)).check(matches(withText("1")));
        }
    }

    @Test
    public void explicitButtonListenersPerformCalculation() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.digit_3_button)).perform(click());
            onView(withId(R.id.enter_button)).perform(click());
            onView(withId(R.id.digit_4_button)).perform(click());
            onView(withId(R.id.add_button)).perform(click());

            onView(withId(R.id.buffer)).check(matches(withText("7")));
        }
    }

    @Test
    public void keypadDoesNotMoveWhenStackFills() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            AtomicInteger initialKeypadTop = new AtomicInteger();
            onView(withId(R.id.keypad)).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                initialKeypadTop.set(view.getTop());
            });

            for (int digitId : new int[]{R.id.digit_1_button, R.id.digit_2_button,
                    R.id.digit_3_button}) {
                onView(withId(digitId)).perform(click());
                onView(withId(R.id.enter_button)).perform(click());
            }

            onView(withId(R.id.keypad)).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertEquals(initialKeypadTop.get(), view.getTop());
            });
        }
    }

    @Test
    public void errorMessageFitsAndLongValuesTruncateAtTheEnd() {
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.buffer)).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertEquals(TextUtils.TruncateAt.END, ((TextView) view).getEllipsize());
            });

            onView(withId(R.id.add_button)).perform(click());

            onView(withId(R.id.buffer)).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                TextView buffer = (TextView) view;
                Layout layout = buffer.getLayout();
                assertNotNull(layout);
                assertNull(buffer.getEllipsize());
                assertEquals(0, layout.getEllipsisCount(0));
            });
        }
    }

    @Test
    public void landscapeButtonsHaveEnoughHeightForTheirLabels() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> activity.setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));
            waitForLandscapeLayout(scenario);

            for (int buttonId : new int[]{R.id.enter_button, R.id.clear_entry_button,
                    R.id.clear_all_button, R.id.add_button, R.id.digit_8_button}) {
                onView(withId(buttonId)).check((view, noViewFoundException) -> {
                    if (noViewFoundException != null) {
                        throw noViewFoundException;
                    }
                    TextView button = (TextView) view;
                    Layout layout = button.getLayout();
                    assertNotNull(layout);
                    int availableHeight = button.getHeight()
                            - button.getCompoundPaddingTop()
                            - button.getCompoundPaddingBottom();
                    assertTrue(layout.getHeight() <= availableHeight);
                });
            }
        }
    }

    private static void waitForLandscapeLayout(ActivityScenario<MainActivity> scenario) {
        long deadline = SystemClock.uptimeMillis() + ACTIVITY_READY_TIMEOUT_MS;
        AtomicInteger lastOrientation = new AtomicInteger(Configuration.ORIENTATION_UNDEFINED);

        while (SystemClock.uptimeMillis() < deadline) {
            boolean[] ready = {false};
            try {
                scenario.onActivity(activity -> {
                    lastOrientation.set(activity.getResources().getConfiguration().orientation);
                    View enterButton = activity.findViewById(R.id.enter_button);
                    ready[0] = lastOrientation.get() == Configuration.ORIENTATION_LANDSCAPE
                            && activity.hasWindowFocus()
                            && enterButton != null
                            && enterButton.isLaidOut()
                            && !enterButton.isLayoutRequested();
                });
            } catch (IllegalStateException ignored) {
                // The previous Activity may be between DESTROYED and the replacement RESUMED state.
            }

            if (ready[0]) {
                return;
            }
            SystemClock.sleep(ACTIVITY_READY_POLL_INTERVAL_MS);
        }

        fail("Landscape Activity did not gain focus and finish layout within "
                + ACTIVITY_READY_TIMEOUT_MS + " ms; last orientation=" + lastOrientation.get());
    }
}
