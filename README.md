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
