# Test Plan

**Author**: Group 4
**Version**: 2

## 1 Testing Strategy

### 1.1 Overall strategy

 - Unit-Testing: White-box testing
 - Integration-Testing: White-box testing
 - System-Testing: White-box testing 
 - Regression-Testing: White-box testing

The tester role will take the lead in testing, but all members will contribute.

### 1.2 Test Selection

 - White-Box Testing: We will use Partition Testing. The testers will create input scenarios based on the different possibilities (Which fields are empty, which are not, etc). This will be better than just randomly selecting inputs.

### 1.3 Adequacy Criterion

Input types must match what they are supposed to represent. Date must have a valid date, Day must have a valid string, etc.
The amount of characters that can be entered will be limited.

### 1.4 Bug Tracking

We will use the Android Studio debugger. Enhancement requests will not be handled because the application is not being published.


### 1.5 Technology

JUnit will be used.

## 2 Test Cases

Test cases are defined in QuickSortTest.java under test, and MainActivityTest.java and ReminderListViewActivityTest.java under androidTest within the package edu.qc.seclass.glm.
 |Test Case | Purpose | Steps | Expected Result | Actual Result | Pass/Fail | 
 |:--------:|:-------:|:-----:|:---------------:|:-------------:|:---------:|
 |testOrderedList|Test if the sorted list will be the same|1. Create an array of Reminders with types in sorted order 2. Use quicksort on it 3. Check that array is still sorted|List will stay sorted|List stayed sorted|Pass|
 |testNonOrderedList|Test if a random sorted list will become sorted|1. Create an array of Reminders with types in random order 2. Use quicksort on it 3. Check that array is now in sorted order|List will become sorted|List became sorted|Pass|
 |testCompleteReversedList|Test if a reversed sorted list will become sorted|1. Create an array of Reminders with types in reversed alphabetical order 2. Use quicksort on it 3. Check that it is sorted|List will become sorted|List became sorted|Pass|
 |testAfterClickingOnAddList|Test if dialog pops up after clicking add button|1. Tap add button 2. Is dialog there?|Dialog pops up|Dialog popped up|Pass|
 |testAfterCreatingAList|Test if list will display after creating a list|1. Tap add button 2. Type in a name 3. Tap Create List 4. Is the displayed list the same as what was typed|List name is the same as what is displayed in the recycler viewer|List name was the same as what is displayed in the recycler viewer|Pass|
|testAfterCreatingAList_withSameName|Test that toast appears when trying to add a list with the same name as another|1. Tap on add button 2. Enter a name 3. Tap Create List 4. Tap add again 5. Try and enter the same name as the previous list 6. Toast should appear saying don't do that|The toast appears saying names need to be unique|The toast appeared saying names need to be unique|Pass|
|testAddingAListAndDeleteIt|Test that a list no longer appears after deletion|1. Tap add button 2. Enter list name 3. Tap Create List 4. Tap list checkbox 5. Tap delete button 6. Is the list gone?|The list no longer appears after deletion|The list no longer appeared after deletion|Pass|
|testWhenClickingOnEdit|Test that clicking to edit a list shows the dialog|1. Tap add button 2. Enter some name 3. Tap Create List 4. Tap edit button next to list 5. Check that the name in the field is the one that was entered before|The dialog appears|The dialog appeared|Pass|
|testWhenClickingOnEdit_AndChangeTheListName|Test that editing the list name results in the new name appearing in the list|1. Create a list 2. Tap the edit button next to the list 3. Delete previous name and enter a new one 4. Tap Save 5. Check that new name displays in list|The new name appears in the recycler viewer|The name appeared in the recycler viewer|Pass|
|testAfterClickingOnItem|Tests when tapping a list name, the ReminderListView activity is started with the correct list name shown|1. Create a list 2. Tap on the list name 3. Check that the ReminderListViewActivity started and the list name is the same|The ReminderListViewActivity opens and the list name is the same|The ReminderListViewActivity opened and the list name was the same|Pass|
|testGoingToReminderList|Makes sure the list name and add button appear in the ReminderListView activity|1. Create a list 2. Tap on the list name 3. Check that the ReminderListViewActivity started and displays the same list name as the one created and the add button has appeared|The ReminderListViewActivity opens, the list name is the same, and the add button appears at the bottom of the screen|The ReminderListViewActivity opened, the list name was the same, and the add button appeared at the bottom of the screen|Pass|
|testClickingOnAddReminder|Tapping the add button shows the add reminder dialog|1. Create a list and tap it 2. Tap add button 3. Check that dialog opens|The Add Reminder dialog opens| The Add Reminder dialog opened|Pass|
|testClickOnReminder_withNoName|Tests that trying to save a new reminder with no name shows the toast|1. Create list and tap it 2. Tap add button 3. Tap Create Reminder 4. Toast message should show|The toast appears saying a name and type are needed|The toast appeared saying a name and type are needed|Pass|
|testClickOnReminder_withNoType|Tests that trying to save a new reminder with no type shows the toast|1. Create list and tap it 2. Tap add button 3. Enter a name 4. Tap Create Reminder 4. Toast message should show|The toast appears saying a name and type are needed|The toast appeared saying a name and type are needed|Pass|
|testClickOnReminderAdd_withNameAndType|Tests that saving a new reminder with a name and type shows the reminder|1. Create list and tap it 2. Tap add button 3. Enter name and type 4. Tap Create Reminder 5. Check that name displayed is the one typed|The reminder shown has the same name as what was entered|The reminder shown had the same name as what was entered|Pass|
|testCreateReminder_withSameName|Tests that trying to save a reminder with the name of another shows toast|1. Create list and tap it 2. Follow steps to create a reminder 3. Tap add button 4. Enter same name as before with some type 5. Toast message should display|The toast appears saying names within a list must be unique|The toast appeared saying names within a list must be unique|Pass|
|testClickOnReminderAdd_withNameAndType_andDates|Tests that selecting a date displays it on the button|1. Create list and tap it 2. Tap add button 3. Enter some name and type 4. Tap Select Date 5. Save the current date 6. Check that the current date is displayed on the Select Date button|The date is displayed where Select Date is|The date was displayed where Select Date was|Pass|
|testClickOnReminderAdd_withNameAndType_andTime|Tests that selecting a time displays it on the button| 1. Create list and tap it 2. Tap add button 3. Enter some name and type 4. Tap Select Time 5. Save the current time 6. Check that the current time is displayed on the Select Time button|The time is displayed where Select Time is|The time was displayed where Select Time was|Pass|
|testClickOnReminderAdd_clearTime|Tests that tapping clear time resets the time button|1. Create list and tap it 2. Tap add button 3. Tap Select Time 4. Save the current time 5. Tap Clear Time 6. Check that the Select Time button now says Select Time again|The Select Time button displays Select Time again|The Select Time button displayed Select Time again|Pass|
|testClickOnReminderAdd_clearDate|Tests that tapping clear date resets the date button|1. Create list and tap it 2. Tap add button 3. Tap Select Date 4. Save the current date 5. Tap Clear Date 6. Check that the Select Date button now says Select Date again|The Select Date button displays Select Date again|The Select Date button displayed Select Date again|Pass|
|testClickOnReminder_shouldDisplayItsInformation|Tests that tapping the info for reminder displays the correct information|1. Create list and tap it 2. Tap add button 3. Enter information into name, type, and description 4. Tap Create Reminder 5. Tap on the info button next to the reminder 6. Check that all the displayed information matches what was entered|The info dialog displays the correct information|The info dialog displayed the correct information|Pass|
|testClickOnReminderAdd_withAllInformation|Tests that after inputting all fields, the correct information is displayed in the list|1. Create list and tap it 2. Tap add button 3. Enter information into name, type, description 4. Select a date and time 5. Tap repeat weekly 6. Tap Create Reminder 7. Check that the information entered matches the displayed information in the list|The correct Date/Time info is displayed in the list|The correct Date/Time info was displayed in the list|Pass|
|testClickOnReminder_clickOnCancel|Tests that tapping cancel in the info dialog closes it|1. Create list and tap it 2. Create a reminder 3. Tap the info button 4. Tap cancel 5. Check that the dialog closes|The info dialog closes|The info dialog closed|Pass|
|testClickingOnReminderCheckBox|Tests that tapping a checkbox causes the delete button to be shown|1. Create list and tap it 2. Create a reminder 3. Tap the checkbox next to the reminder 4. Check that the delete button appears|The delete button appears|The delete button appeared|Pass|
|testClickingOnDeleteReminder|Tests that tapping the delete button removes the selected reminder from the list|1. Create list and tap it 2. Create a reminder 3. Tap the checkbox next to the reminder 4. Tap the delete button 5. Check that the reminder no longer appears in the list|The reminder is removed|The reminder was removed|Pass|
|testClickingOnEdit|Tests that tapping edit on a reminder populates the dialog with the previously entered information|1. Create list and tap it 2. Create a reminder 3. Tap the edit button next to the reminder 4. Check that the edit dialog opened and its fields are populated with the reminder's information|The reminder's information populates the input fields|The reminder's information populated the input fields|Pass|
|testClickingOnEdit_modifyName|Tests that changing the name of a reminder changes the name displayed in the list|1. Create list and tap it 2. Create a reminder 3. Tap the edit button next to the reminder 4. Enter a new name into the name field 5. Tap Save 6. Check that the new name is displayed in the list|The new name appears in the recycler view|The new name appeared in the recycler view|Pass|