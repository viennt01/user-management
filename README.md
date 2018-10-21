# User Management Demo Application

## 1. How to build

Run the following commands to build:

    $ cd user-management
    $ ./mvnw clean install
    
Only execute `clean` phase when you really need to, because `node_modules` will be deleted!
That makes build slower.

## 2. How to run User Management Application on local

### 2.1. Backend with H2 database (only for development purpose)

Run UserManagementApplication.

    $ cd user-management
    $ cd backend
    $ java -jar target/backend-1.0.0.jar

Now, User Management is running on `http://localhost:8080`

### 2.2. Static Frontend 

In case you want to faster see your change on frontend code, without building whole project again.

    $ cd abccargo
    $ cd frontend
    $ npm start
    
Webpack dev server will serve Static on `http://localhost:9000`