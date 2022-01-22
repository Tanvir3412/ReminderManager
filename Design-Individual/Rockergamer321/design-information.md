1. A list consisting of reminders the users want to be aware of. The application must allow
users to add reminders to a list, delete reminders from a list, and edit the reminders in
the list.

* To fulfill this requirement, this list is considered as a list called ReminderList. Within this class, there are methods that allows the user to add reminders to the list (createReminder), delete reminders(deleteReminder), and edit reminders (editReminder). 
Note: The last method edits the given reminder depending where it came from, which is why the second parameter is an Object rather than a specific type.

2. The application must contain a database (DB) of reminders and corresponding data.

* There is a database in the design, but it is assumed that the database consists of reminderTypes along with reminderNames.

3. Users must be able to add reminders to a list by picking them from a hierarchical list,
where the first level is the reminder type (e.g., Appointment), and the second level is the
name of the actual reminder (e.g., Dentist Appointment).

* This does not affect the design. It is assumed when setting the type and name of the reminder in the Reminder class, the class checks to see if they are part of the database.

4. Users must also be able to specify a reminder by typing its name. In this case, the
application must look in its DB for reminders with similar names and ask the user
whether that is the item they intended to add. If a match (or nearby match) cannot be
found, the application must ask the user to select a reminder type for the reminder, or
add a new one, and then save the new reminder, together with its type, in the DB.

* This does not affect the design because the input will be sent to the Reminder class method, setReminderName. If there is no match, then there will be an if-else statement that either sends the chosen reminder type to setReminderType or adds and saves a new reminder to the DB, thus satisfying the requirement.

5. The reminders must be saved automatically and immediately after they are modified.

* This is an implementation requirement that does not affect the design.

6. Users must be able to check off reminders in the list (without deleting them).

* To fulfill this requirement, the ReminderList has the method checkReminder, which gets the reminder from the database and sets the boolean isChecked in the Reminder class to true/false by sending the boolean to the Reminder's method setCheck.

7. Users must also be able to clear all the check-off marks in the reminder list at once.

* To fulfill this requirement, the ReminderList has the method, clearCheckedReminders, which checks to see if any of the reminders in the list (reminderArray) is checked (if isChecked == true). If any of the reminders are checked, then they are removed/deleted from the list.

8. Check-off marks for the reminder list are persistent and must also be saved immediately.

* This is an implementation requirement that does not affect the design.

9. The application must present the reminders grouped by type.

* To fulfill this requirement, whenever a reminder is created and added to the list (createReminder), there will be a check in the method that will get the reminder type and compare it to the types of the other reminders in the list.

10. The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.

* To fulfill this requirement, the class, UserLists, is created to keep track of the lists that the user creates. The class also has methods to create a reminder list (createList), (re)name list (nameList), select list (Comes from listArray), and delete a reminder list (deleteList).

11. The application should have the option to set up reminders with day and time alert. If this option is selected, allow option to repeat the behavior.

* In the Reminder class, there are two attributes that contain the day and time for the reminder alert as well as getters and setters. There is also a boolean attribute, isRepeated, that determines whether the given day and time is repeated within the given reminder as well as a setter and getter method.

12. Extra Credit: Option to set up reminder based on location.

* Although, it would be simple to have an attribute in the Reminder class that contains the location's longtitude and latitude, getting the user's current location or a specific location would be difficult without an API.

13. The User Interface (UI) must be intuitive and responsive.

* This is an implementation requirement that does not affect the design