# cst-323
Projects related to and required by cst-323 cloud computing at GCU.

## Bike Shop Order Tracker

This is a Spring Boot web application for tracking bike shop customers, bikes, parts, and service orders.

## Hosted Application

[Open the Bike Shop Order Tracker](https://cst-323-clc-bike-shop-4c0f8f783461.herokuapp.com/)

## Local Setup

1. Create the local MySQL database by running `database-ddl.sql` in MySQL Workbench.
2. Update `src/main/resources/application.properties` with your local MySQL username and password.
3. Run the project as a Spring Boot application from Eclipse.
4. Open `http://localhost:8080/`.

## Heroku Deployment

The project includes the Heroku files needed for deployment:

- `Procfile` tells Heroku how to start the Spring Boot jar file.
- `system.properties` tells Heroku to use Java 17.
- `application.properties` reads Heroku's port and database settings from environment variables.
- The database connection pool is limited to 5 connections for JawsDB.

## Professor Guide Deployment Flow

1. Create a Heroku Student account using the GCU instructions.
2. Confirm the Heroku account has the required student credits.
3. Create a new Heroku app.
4. In the Heroku app, open the Resources tab.
5. Add the JawsDB MySQL add-on using the free/student plan.
6. Open the JawsDB MySQL settings and copy the host, username, password, and database/schema values into Notepad first.
7. Create a MySQL Workbench connection to the JawsDB database.
8. Run `database-ddl-jawsdb.sql` in MySQL Workbench to create the four database tables.
9. In Heroku, open the Deploy tab and connect the app to the GitHub repository.
10. In Heroku, open the Settings tab and add the Java buildpack.
11. In Heroku, add the database configuration values as config vars.
12. In Heroku, run a manual deploy from the Deploy tab.
13. Click Open App to test the deployed application.

Useful Heroku CLI commands if the application does not start:

```powershell
heroku login
heroku ps:scale web=1 -a [APP_NAME]
heroku logs --tail -a [APP_NAME]
```
