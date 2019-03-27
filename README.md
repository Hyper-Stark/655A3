# README.md

### Changes we made
1. added a user table respect to two databases
2. implemented delete function respect to two systems
3. implemented sign in, sign up and signout functions respect to two systems
4. added server-side credential validation mechanism respect to two systems
5. implemented log functions respect to two systems
6. modified user interface to require users to sign in or sign up before they can use the real services.

### System installations
1. install nodejs, MySQL and other dependencies,
    for detail explanation please refer to A3-installation-Guide.pdf,
    section "setting up the web-services system" and
    "Installation of MySQL"
2. Create databases and users
    a) login to the mysql with command:
    mysql --user=root --password='YourRootPassword'
    b) Create the web-services databas with SQL command:
    create database ws_orderinfo;
    c) Create the micro-services database with SQL command:
    create database ms_orderinfo;
    d) Create the users with SQL command:
    source dbuser.sql;
3. Copy the schema of the database from the template provided:
    mysql --user=root --password='YourRootPassword' –D ws_orderinfo <
    dbtemplate.sql
    mysql --user=root --password='YourRootPassword' –D ms_orderinfo <
    dbtemplate.sql


### Build Web Service based application
1. enter into the ws folder with command:
    cd ws
2. install all the modules required for the node server with commands:
    npm install express --save
    npm install mysql --save
    npm install body-parser --save
    npm init (accept all defaults)
3. Run the below command to import the json package:
    export CLASSPATH=.:/path_to_ws_directory/json-20160810.jar.
    For Windows use: set CLASSPATH=.:/path_to_ws_directory/json-20160810.jar
4. compile all java files use the command:
    javac *.java
5. start nodejs server using command:
    node Server.js
6. Start OrdersUI using command:
    java OrdersUI

### Micro Sevice based application

1. enter into the ws folder with command:
    cd ms
2. Run the below command to import the mysql-connector package:
    export CLASSPATH=.:/path_to_ms_directory/mysql-connector-java-5.1.45-bin.jar.
    For Windows use: set CLASSPATH=.:/path_to_ws_directory/json-20160810.jar
3. compile all java files use the command:
    javac *.java
4. start rmiregistry using the command:
    rmiregistry &
5. run all services using commands:
    java RetrieveServices &
    java UserServices &
    java DeleteServices &
    java CreateServices &
5. Start OrdersUI using command:
    java OrdersUI
