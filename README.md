Overview
This Java project automates the Assign Leave functionality on the OrangeHRM system using Selenium WebDriver and TestNG.

Prerequisites
- JDK: 17+
- Maven: Installed and configured
- Selenium WebDriver: 4.12.0
- ChromeDriver: Matches your Chrome browser version
- TestNG: Testing framework

Features
- Login: Logs in with admin credentials.
- Assign Leave: Fills the form, selects employee, leave type, dates, and comments.
- Handles dynamic dropdowns and calendars.
- Logout: Logs out from the application.
- Validation.

Installation
- Clone the project.
- Add necessary dependencies to pom.xml
- Configure ChromeDriver in your PATH.

Execution
- Run tests via TestNG in the IDE or use Maven: mvn test