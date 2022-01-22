# Reminders Requirements

1. A list consisting of reminders the users want to be aware of. The application must allow users to add reminders to a list, delete reminders from a list, and edit the reminders in the list.
To realize this, the UserList class was added to the design with the operations addReminder, deleteReminder, and editReminder.
2. The application must contain a database (DB) of reminders and corresponding data.
The database is not directly implemented since it would not be a class, but an operation in the Reminder class addToDataBase, and the searchByName and searchByType operations in the MainInterface class would have to interact with the DataBase to work.
3. Users must be able to add reminders to a list by picking them from a hierarchical list, where the first level is the reminder type (e.g., Appointment), and the second level is the name of the actual reminder (e.g., Dentist Appointment).
To implement this the TypeList class was added. This class has the attribute list which is a list of reminders. The operations of add, delete, edit, and getReminder were added to allow for changes. The UserList class interacts with this class to add reminders.
4. Users must also be able to specify a reminder by typing its name. In this case, the application must look in its DB for reminders with similar names and ask the user whether that is the item they intended to add. If a match (or nearby match) cannot be found, the application must ask the user to select a reminder type for the reminder, or add a new one, and then save the new reminder, together with its type, in the DB.
This is implemented in the MainInterface class with the operations searchByName and searchByType. These would have to interact with the DB to work. The idea of selection and display are for the UI which is not considered here.
5. The reminders must be saved automatically and immediately after they are modified.
This is a task for the implementation. The operations of editReminder, addReminder, and so on that appear in multiple classes would be making the changes, but how is not discussed in the class diagram.
6. Users must be able to check off reminders in the list (without deleting them).
This is implemented with the checked attribute and checked operation in the Reminder class. It is also implemented with the check and uncheck operations in the MainInterface class.
7. Users must also be able to clear all the check-off marks in the reminder list at once.
The uncheckAll operation in the MainInterface class will do this for a whole UserList
8. Check-off marks for the reminder list are persistent and must also be saved immediately.
This is also something for the implementation. The uncheckAll operation and the checked operation would have to do this. That is not shown here.
9. The application must present the reminders grouped by type.
The display is not discussed here, but the MainInterface class has the attribute typeLists that is a list of TypeList objects, meaning the MainInterface class will have access to the information for the UI.
10. The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.
This is implemented with the ListOfUserLists class with its attribute userLists and its operations create, rename, delete, and getUserList.
11. The application should have the option to set up reminders with day and time alert. If this option is selected allow option to repeat the behavior.
The Reminder class has the attributes day and time as well as repeatAlert. The operations that go with this are setDayTimeAlert. The MainInterface and UseList classes also have operations alert to interact with this part of the Reminder class. The MainInterface also has day and time attributes.
12. Extra Credit: Option to set up reminder based on location.
The Reminder class has the attribute location and the operation setLocationAlert. The MainInterface also has the attribute location.
13. The User Interface (UI) must be intuitive and responsive.
Since this is a class diagram, the UI design is not discussed.





