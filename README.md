# ReminderManager
Create List
* Requirements: 
1. The user can create a list, give it a name, and add reminders to it.
* Pre-conditions:
1. That there are reminders already created
2. The reminders have a type and then a name
* Post-conditions:
1. The list is saved.
2. The list can be modified.
3. Can view reminders by name.
4. The database can look for reminders with similar names.
* Scenarios:
1. Normal event sequence: The user creates a list and adds the reminders to the list. They can get the reminders from the existing list of reminders.
2. Alternate event sequence: The user goes to create a list before there are any reminders.
3. Exceptional event sequence: The user saves an empty list.

Modify List
* Requirements:
1. The user can add reminders to an existing list, remove reminders, and change the list's name.
* Pre-conditions:
1. There is a list already created.
* Post-conditions:
1. The list is saved with the changes.
Scenarios:
1. Normal event sequence: The user selects an already create list and makes their changes to it.
2. Alternate event sequence: The user makes no changes when modifying a list.
3. Exceptional event sequence: The user cancels before saving the changes.
Modify Reminder
* Requirements:
1. The user can select a previously made reminder and change the name, time, and other aspects of it.
* Pre-conditions:
1. There are reminders already made.
Post-conditions:
1. The changes are saved.
2. The local database is updated.
3. If the reminder is in a list, the list is also updated
* Scenarios:
1. Normal event sequence: The user selects an already created reminder and makes their changes.
2. Alternate event sequence: The user makes no changes when modifying the reminder.
3. Exceptional event sequence: The user cancels before saving the changes
Create Reminder
* Requirements:
1. The user can create a reminder, if they want they can set it as a location based reminder.
* Pre-conditions:
1. None. This will be the first thing the user does when starting the app for the first time.
* Post-conditions:
1. The reminder is saved.
2. The reminder is saved in the local database.
3. The reminder can be modified.
4. If location based is chosen, the Google Maps API will communicate with the device and app.
* Scenarios:
1. Normal event sequence: The user creates the reminder and saves it.
2. Alternate event sequence: The user does not save the reminder.
3. Exceptional event sequence: The user does not give the reminder a name.
Add on to List
* Requirements:
1. Adds the selected reminder to a list.
* Pre-conditions:
1. A list is created.
2. At least one reminder is created.
* Post-conditions:
1. The reminder is now in the list.
* Scenarios:
1. Normal event sequence: The user adds a reminder to an existing list or a list they just created.
2. Alternate event sequence: The user does not add the reminder to a list.
3. Exceptional event sequence: There are no reminders to add.
Update Reminder
* Requirements:
The user can update an existing reminder from the list.
* Pre-conditions:
1. There is a list.
2. The reminder is created.
3. The reminder is in the list.
* Post-conditions:
1. The database is updated.
2. The changes are saved.
3. The list is also updated.
* Scenarios:
1. Normal event sequence: The user selects a reminder from the list, makes their changes, and saves them.
2. Alternate event sequence: The user does not make any changes.
3. Exceptional event sequence: The user does not save their changes.
Save List
* Requirements:
1. The user saves the list, whether new or already created.
* Pre-conditions:
1. The list is being edited or created.
* Post-conditions:
1. The changes are saved.
* Scenarios:
1. Normal event sequence: The user saves the list they were modifying.
2. Alternate event sequence: The user saves, but keeps making changes.
3. Exceptional event sequence: The user does not save their changes.
Remove from List
* Requirements:
1. The user can remove a reminder that they had added to the list.
* Pre-conditions:
1. The reminder is already in the list.
2. The list is being edited or created.
* Post-conditions:
1. The reminder is no longer in the list.
* Scenarios:
1. Normal event sequence: The user removes the selected reminder from the list they are editing.
2. Alternate event sequence: The user does not remove the selected reminder.
3. Exceptional event sequence: The user removes the reminder, but does not save the changes
