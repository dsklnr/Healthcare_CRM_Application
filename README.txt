4/4/2022

Intellij Version: Community 2021.3.2
JDK Version: Java SE 17.0.2
JavaFX Version: JavaFX-SDK-17.0.2
MySQL Connector Driver: mysql-connector-java-8.0.28

This application was designed for doctors and nurses to schedule patient appointment.

The main screen is where the user may either create a new account or login. Either nurses or 
doctors can create an account. When creating an account, passwords are encrypted for secutity 
purposes. To login the user must enter their username and password. If the username and password 
are invalid, an alert displays to the user. If the user inputs valid credentials, an alert will 
display. This alert tells the user if they have an appointment within the next 15 minutes. After 
closing the alert, the user is logged in.

Once logged in, the user is directed to the dashboard screen. Here doctors will see a table with 
their upcoming appointments within the next month. If a nurse is logged in, they will see all doctors 
appointments within the next month. There are two radio buttons at the top right of the table to 
toggle a month or week button. The month button displays upcoming appointments within the next month.
The week button displays upcoming appointments within the next week. At the top right of the 
application the user can navigate to different pages (home, patients, appointments, and reports). 
The home label will open the dashboard screen.

The patients page contains a table that displays every patients contact info. At the bottom left 
of the table, the user can add, modify a selected patient, and delete a selected patient. Note all 
of a patients appointments must be deleted before the patient can be deleted. However, that constraint 
is allocated for in the Java code.

The appointments page contains a table that displays every upcoming appointment. At the bottom left 
of the table, the user can add, modify a selected appointment, and delete a selected appointment.

The reports page contains two tables that run business reports. The first table displays the total 
number of customer appointments by type and month. The second table displays the number of patients 
in each division. These tables are both automatically populated for the user.







