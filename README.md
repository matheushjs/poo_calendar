# POO Project Calendar

## Code Style Guidelines
- Code in english
- Comments in english

## Java Version
8

## Build Tool
Maven

## Project Management
(todo.txt / trello)

## First GUI Example Image
[Example Image](./example.png)

## Initial Project Tree
```
. poo
 \. calendar
   \
   |. model
   | |. Appointment    # Represents an appointment (title, init date, final date)
   | |. Task           # Represents a task (title, date)
   | |. XMLBuilder     # Read/Write tasks, appointments and other information to a .xml file
   |. view
   | |. MainWindow      # Global parent window
   | |. CalendarWindow  # Displays the calendar
   | |. TaskWindow      # Displays the tasks
   |. controller
   | |. MainApplication # Connects frontend and backend
   | |. Manager         # Wraps user-session initialization and provides onEvent() methods to connect to GUI events
   | |. Schedule        # Manages appointments and tasks, and relations among them
```

## Libraries
### JavaFX
Seems to be the most suitable for implementing the sophisticated UI envisioned for the final product (see javafx.scene.canvas and javaFX Scene Builder)

### Swing
Part of the JDK, but it's simple.

### javax.xml.parsers.SAXParsers
XML handling
