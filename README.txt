<h1>JWipe - Disk Sanitization</h1>

 ### [YouTube Demonstration](https://youtu.be/7eJexJVCqJo)

<h2>Description</h2>
Project consists of a simple PowerShell script that walks the user through "zeroing out" (wiping) any drives that are connected to the system. The utility allows you to select the target disk and choose the number of passes that are performed. The PowerShell script will configure a diskpart script file based on the user's selections and then launch Diskpart to perform the disk sanitization.
<br />


<h2>Languages and Utilities Used</h2>

- <b>PowerShell</b> 
- <b>Diskpart</b>

<h2>Environments Used </h2>

- <b>Windows 10</b> (21H2)

<h2>Program walk-through:</h2>

<p align="center">
Launch the utility: <br/>
<img src="https://i.imgur.com/62TgaWL.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Select the disk:  <br/>
<img src="https://i.imgur.com/tcTyMUE.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Enter the number of passes: <br/>
<img src="https://i.imgur.com/nCIbXbg.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Confirm your selection:  <br/>
<img src="https://i.imgur.com/cdFHBiU.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Wait for process to complete (may take some time):  <br/>
<img src="https://i.imgur.com/JL945Ga.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Sanitization complete:  <br/>
<img src="https://i.imgur.com/K71yaM2.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Observe the wiped disk:  <br/>
<img src="https://i.imgur.com/AeZkvFQ.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
</p>

<!--
 ```diff
- text in red
+ text in green
! text in orange
# text in gray
@@ text in purple (and bold)@@
```
--!>


4/4/2022

Intellij Version: Community 2021.3.2
JDK Version: Java SE 17.0.2
JavaFX Version: JavaFX-SDK-17.0.2
MySQL Connector Driver: mysql-connector-java-8.0.28

This application was designed for doctors and nurses to schedule patient appointments.

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







