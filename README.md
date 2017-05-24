# POO Project Calendar

## Code Style Guidelines
- Code in (portuguese/english)
- Comments in (portuguese/english)

## Initial Project Tree
```
. poo
 \. calendar
   \
   |. frontend
   | \
   | |. MainApplication # Connects frontend and backend
   | |. MainWindow      # Global parent window
   | |. CalendarWindow  # Displays the calendar
   | |. TaskWindow      # Displays the tasks
   |
   |. backend
   | \
   | |. Manager        # Wraps user-session initialization and provides onEvent() methods to connect to GUI events
   | |. Schedule       # Manages appointments and tasks, and relations among them
   | |. Appointment    # Represents an appointment (title, init date, final date)
   | |. Task           # Represents a task (title, date)
   | |. XMLBuilder     # Read/Write tasks, appointments and other information to a .xml file
```

## Libraries
### JavaFX
Seems to be the most suitable for implementing the sophisticated UI envisioned for the final product (see javafx.scene.canvas and javaFX Scene Builder)

### javax.xml.parsers.SAXParsers
XML handling
