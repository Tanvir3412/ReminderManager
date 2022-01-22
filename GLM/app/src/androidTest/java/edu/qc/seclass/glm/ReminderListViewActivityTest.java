package edu.qc.seclass.glm;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static edu.qc.seclass.glm.util.UtilMatchers.childAtPosition;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.qc.seclass.glm.util.ToastMatcher;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class ReminderListViewActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    private void createListWithNameAndClickOnIt(String myList) {
        onView(withId(R.id.AddListButton)).perform(click());
        onView(withId(R.id.ListNameInput)).perform(typeText(myList), closeSoftKeyboard());
        onView(withId(R.id.CreateListButton)).perform(click());
        onView(withText(myList)).perform(click());
    }

    @Before
    public void setUp() {
        DBHelper dbHelper = new DBHelper(ApplicationProvider.getApplicationContext());
        dbHelper.deleteAll();
    }

    @Test
    public void testGoingToReminderList_shouldShowTheListNameAndButton() {
        String listName = "My List";
        createListWithNameAndClickOnIt(listName);
        onView(withId(R.id.AddReminderButton)).check(matches(isDisplayed()));
        onView(withId(R.id.ReminderList)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickingOnAddReminder_shouldShouldDialog() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderName)).check(matches(isDisplayed()));
        onView(withId(R.id.ClearDateButton)).check(matches(isDisplayed()));
        onView(withId(R.id.SaveReminderButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickOnReminder_withNoName_shouldDisplayToast() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withText("Please Enter a Name/Type for this Reminder")).inRoot(ToastMatcher.isToast()).check(matches(isDisplayed()));
    }

    @Test
    public void testClickOnReminder_withNoType_shouldDisplayToast() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withText("Please Enter a Name/Type for this Reminder")).inRoot(ToastMatcher.isToast()).check(matches(isDisplayed()));
    }

    @Test
    public void testClickOnReminderAdd_withNameAndType_shouldAddIt() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withText("Some Name")).check(matches(isDisplayed()));
    }

    @Test
    public void testCreateReminder_withSameName_shouldNotAddId() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withText("Some Name")).check(matches(isDisplayed()));

        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withText("Reminder names must be unique within a list")).inRoot(ToastMatcher.isToast()).check(matches(isDisplayed()));
    }


    @Test
    public void testClickOnReminderAdd_withNameAndType_andDates_shouldAddIt() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());

        onView(withId(R.id.ReminderDateText)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String text = (month + 1) + "/" + dayOfMonth + "/" + year;
        onView(withId(R.id.ReminderDateText)).check(matches(withText(text)));
    }

    @Test
    public void testClickOnReminderAdd_withNameAndType_andTime_shouldAddIt() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());

        onView(withId(R.id.ReminderTimeText)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        SimpleDateFormat format12Hours = new SimpleDateFormat("hh:mm aa");
        String text = format12Hours.format(new Date());
        onView(withId(R.id.ReminderTimeText)).check(matches(withText(text)));
    }

    @Test
    public void testClickOnReminderAdd_clearTime_shouldClearIt() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());

        onView(withId(R.id.ReminderTimeText)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        SimpleDateFormat format12Hours = new SimpleDateFormat("hh:mm aa");
        String text = format12Hours.format(new Date());
        onView(withId(R.id.ReminderTimeText)).check(matches(withText(text)));
        onView(withId(R.id.ClearTimeButton)).perform(click());
        onView(withId(R.id.ReminderTimeText)).check(matches(withText("Select Time")));
    }

    @Test
    public void testClickOnReminderAdd_clearDate_shouldClearIt() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());

        onView(withId(R.id.ReminderDateText)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String text = (month + 1) + "/" + dayOfMonth + "/" + year;
        onView(withId(R.id.ReminderDateText)).check(matches(withText(text)));
        onView(withId(R.id.ClearDateButton)).perform(click());
        onView(withId(R.id.ReminderDateText)).check(matches(withText("Select Date")));
    }

    @Test
    public void testClickOnReminder_shouldDisplayItsInformation() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());
        onView(withId(R.id.ReminderDescriptionText)).perform(typeText("Some Description"), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(allOf(withId(R.id.ReminderInfoButton), childAtPosition(childAtPosition(withId(R.id.ReminderList), 0), 4))).perform(click());

        onView(withId(R.id.ReminderName)).check(matches(isDisplayed()));
        onView(withId(R.id.ReminderDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.ReminderType)).check(matches(isDisplayed()));
        onView(withId(R.id.ReminderDate)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickOnReminderAdd_withAllInformation_shouldAddIt_andDisplayAllInformations() {
        createListWithNameAndClickOnIt("My List");
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText("Some Name"), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText("Some Type"), closeSoftKeyboard());
        onView(withId(R.id.ReminderDescriptionText)).perform(typeText("Some Description"), closeSoftKeyboard());

        onView(withId(R.id.ReminderTimeText)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.ReminderDateText)).perform(click());
        onView(withText("OK")).perform(click());
        SimpleDateFormat format12Hours = new SimpleDateFormat("hh:mm aa");
        String time = format12Hours.format(new Date());
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + 7;
        int year = calendar.get(Calendar.YEAR);
        String date = (month + 1) + "/" + dayOfMonth + "/" + year;

        onView(withId(R.id.RepeatWeekly)).perform(click());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withId(R.id.ReminderListDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.ReminderListDescription)).check(matches(withText(date + " - " + time)));
    }

    @Test
    public void testClickOnReminder_clickOnCancel_shouldHideTheDialog() {
        createListWithNameAndClickOnIt("My List");
        createReminder("Some Name", "Some Type", "Some Description");
        onView(allOf(withId(R.id.ReminderInfoButton), childAtPosition(childAtPosition(withId(R.id.ReminderList), 0), 4))).perform(click());

        onView(withId(R.id.ReminderName)).check(matches(isDisplayed()));

        onView(withId(R.id.CancelButton)).perform(click());

        onView(withId(R.id.ReminderName)).check(doesNotExist());
    }

    @Test
    public void testClickingOnReminderCheckBox_shouldShowDeleteButton() {
        createListWithNameAndClickOnIt("My List");
        createReminder("Some Name", "Some Type", "Some Description");
        onView(withId(R.id.DeleteReminderButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(allOf(withId(R.id.ReminderCheckbox), childAtPosition(childAtPosition(withId(R.id.ReminderList), 0), 0))).perform(click());
        onView(withId(R.id.DeleteReminderButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testClickingOnDeleteReminder_shouldDeleteIt() {
        createListWithNameAndClickOnIt("My List");
        createReminder("Some Name", "Some Type", "Some Description");
        onView(allOf(withId(R.id.ReminderCheckbox), childAtPosition(childAtPosition(withId(R.id.ReminderList), 0), 0))).perform(click());
        onView(withId(R.id.DeleteReminderButton)).perform(click());
        onView(withText("Some Name")).check(doesNotExist());
    }

    @Test
    public void testClickingOnEdit_shouldShowEditDialog() {
        createListWithNameAndClickOnIt("My List");
        createReminder("Some Name 1", "Some Type 2", "Some Description 3");
        onView(allOf(withId(R.id.ReminderEditButton), childAtPosition(childAtPosition(withId(R.id.ReminderList), 0), 3))).perform(click());

        onView(withId(R.id.ReminderNameText)).check(matches(withText("Some Name 1")));
        onView(withId(R.id.ReminderTypeText)).check(matches(withText("Some Type 2")));
        onView(withId(R.id.ReminderDescriptionText)).check(matches(withText("Some Description 3")));
    }

    @Test
    public void testClickingOnEdit_modifyName_shouldBeModified() {
        createListWithNameAndClickOnIt("My List");
        createReminder("Some Name 1", "Some Type 2", "Some Description 3");
        onView(allOf(withId(R.id.ReminderEditButton), childAtPosition(childAtPosition(withId(R.id.ReminderList), 0), 3))).perform(click());

        onView(withId(R.id.ReminderNameText)).perform(clearText(), typeText("Some Name 3"), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
        onView(withText("Some Name 3")).check(matches(isDisplayed()));
    }

    private void createReminder(String name, String type, String description) {
        onView(withId(R.id.AddReminderButton)).perform(click());
        onView(withId(R.id.ReminderNameText)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.ReminderTypeText)).perform(typeText(type), closeSoftKeyboard());
        onView(withId(R.id.ReminderDescriptionText)).perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.SaveReminderButton)).perform(click());
    }
}