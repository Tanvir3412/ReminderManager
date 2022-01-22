1. This was implemented by creating a class called listReminders which has the following methods respectively named to allow the user to add reminders to a list, delete reminders from a list, and edit the reminders
   addReminders(reminderName)
   deleteReminder(reminderName)
   editReminder(reminderName)
   selectRemindersForCheckOff(reminderName)
   clearCheckOffMarks()

2. Implemented but not modeled as its not part of UML guidelines, a database of reminders

3. When the user goes to invoke the addReminders method there is a class called ReminderType that has a list of predefined reminderType in which the user can choose from, at which point there's another class called Reminders that handles the creation of reminders as objects.

4. Not fully displayed as it's a UI feature more than a UML but in the listView class there is a method called SearchReminders in which a user can search for a reminder by directly querying the database across multiple lists which is handled by the rearchReminders method. The addReminderType method also lets users add reminder types to be used when creating new reminders in the ReminderTypeSet class

5. Once a reminder is created the listReminders class will immediately update the database as the CRUD for reminders in the DB line illustrates

6. Each object of a Reminder class has an attribute of type boolean that will identify whether its crossed off or not as per the isCheckedOff attribute.

7. There is a method in listReminders class that is responsible for obtaining all reminders in the DB pertaining to the list by identifying every item that shares the value of belongingList attribute in each Reminder object and resets there boolean value of isCheckedOff to flase

8. isCheckedOff attribute in the Reminder class is saved and carried with the object to the database as soon as the object is finished being edited via the CRUD for reminders in DB connection

9. Since this is a UI element it won't fully reflect in the UML but each Reminder object has a reminderType attribute that is saved with the reminder thus making each reminder to be grouped by their reminder type when needed

10. The listView class is a collection of objects of type listReminders which also lets the users have access to the following methods to create, (re)name, select, and delete reminder lists
    createList(listName)
    renameList(selectList, newListName)
    deleteList(listName)

11. There are three attributes that handle this aspect in the Reminder class. The isAllDay boolean determines whether the reminder will just be an all day instance and will set the dateTime attribute to null, however if isAllDay is set to false it will ask for a dateTime to be entered and the repeatsReminder attribute executes the RepeatsReminder class in which a user can set an endingDate for the series as well as how concurrently this reminder repeats

12. Location attribute in the Reminders class calls the setLocation() method in which it invokes the google maps API class

13. Not displayed or modeled in the UML as its a UI element however the goal is to adhere to the simplicity and structure of the Google Material Design guidelines.
