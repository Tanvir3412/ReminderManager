package edu.qc.seclass.glm;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static edu.qc.seclass.glm.util.UtilMatchers.childAtPosition;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        DBHelper dbHelper = new DBHelper(ApplicationProvider.getApplicationContext());
        dbHelper.deleteAll();
    }

    @Test
    public void testAfterClickingOnAddList_theDialogIsShowed() {
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.CreateListButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testAfterCreatingAList_shouldAppeared() {
        String randomName = UUID.randomUUID().toString().substring(0, 10);
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());
        onView(withText(randomName)).check(matches(isDisplayed()));
        onView(withId(R.id.CreateListButton)).check(doesNotExist());
    }

    @Test
    public void testAfterCreatingAList_withSameName_shouldNotBeAdded() {
        // the first list will be created
        String randomName = UUID.randomUUID().toString().substring(0, 10);
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());
        onView(withId(R.id.CreateListButton)).check(doesNotExist());

        // the dialog should still be displayed
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());
        onView(withId(R.id.CreateListButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddingAListAndDeleteIt() {
        String randomName = UUID.randomUUID().toString().substring(0, 10);
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());
        onView(withText(randomName)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.ListCheckbox), childAtPosition(childAtPosition(withId(R.id.ReminderList_List), 0), 0)))
                .perform(click());

        onView(withId(R.id.DeleteListButton)).perform(click());
        onView(withText(randomName)).check(doesNotExist());
    }

    @Test
    public void testWhenClickingOnEdit_shouldDisplayDialogWithListName() {
        String randomName = UUID.randomUUID().toString().substring(0, 10);
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());

        onView(allOf(withId(R.id.EditListButton), childAtPosition(childAtPosition(withId(R.id.ReminderList_List), 0), 2)))
                .perform(click());

        onView(withId(R.id.ListNameInput)).check(matches(withText(randomName)));
    }

    @Test
    public void testWhenClickingOnEdit_AndChangeTheListName_shouldChangeToNewNameDialogWithListName() {
        String randomName = UUID.randomUUID().toString().substring(0, 10);
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());

        onView(allOf(withId(R.id.EditListButton), childAtPosition(childAtPosition(withId(R.id.ReminderList_List), 0), 2)))
                .perform(click());

        onView(withId(R.id.ListNameInput)).check(matches(withText(randomName)));
        onView(withId(R.id.ListNameInput)).perform(clearText(), typeText("m_" + randomName), closeSoftKeyboard());
        onView(withId(R.id.SaveButton)).perform(click());

        onView(withText("m_" + randomName)).check(matches(isDisplayed()));
    }

    @Test
    public void testAfterClickingOnItem_shouldGoToReminderListViewActivity() {
        Intents.init();
        String randomName = UUID.randomUUID().toString().substring(0, 10);
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());

        onView(withText(randomName)).perform(click());

        Intents.intended(allOf(IntentMatchers.hasComponent(ReminderListViewActivity.class.getName()),
                IntentMatchers.hasExtra("List", randomName)));
        Intents.release();
    }
}
