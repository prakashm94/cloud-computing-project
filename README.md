# Team Members Details

Name: Madhumathi Prakash
email : prakash.m@husky.neu.edu

Name: Poojith Shankar Shetty
email : shetty.poo@husky.neu.edu

Name: Prashant Reddy
email: reddy.pra@husky.neu.edu

Name: Vrushali Shah
email: shah.vru@husky.neu.edu

# Prerequisites for building and deploying our application locally

* IntellIj
* Tomcat server
* Mysql Server
* Postman client

# Build and Deploy instructions for web application.

* Open the Github link : https://github.com/prakashm94/csye6225-fall2018
* Open IntellIj.
     
      1. Create Userdata model class with variables username and password.
      2. Create TransactionData model class with related variables and use OneToMany mapping.
      3. Create UserRepository and TransactionRepository.
      4. Create LoginController class which contains code for user creation using basic auth and code for displaying time after authenticating the user.
      5. Create TransactionController contains code for all CRUD operations.

* Run the project using command mvn spring-boot:run in intellIj Terminal.      
* Open MySql

     1. Create database of name "login_aws" and table with name "Userdata" with columns username and password.
     2. Run Select all method on table Userdata to check whether the userdata has been inserted succesfully after creating the user using postman
     3. Create table TransactionData under "login_aws" database with respective columns and should conatin username column.
     4. Run Select all on table TransactionData to see all the values after each CRUD operation.

* Open postman client
     
    1. User Creation:  Send the POST request http://localhost:8080/user/register and Select authentication as Basic 	Auth, if user doesnot exists user gets created with message "User created Succesfully", else we get message "User already exists" .

    2. Send the Get request http://localhost:8080/time and select Basic Auth after succesfully authenticating the request we get the current time else error message will be displayed "Login Unsuccessful, please Check Username and Password".

* CRUD Operations of Transactions on Postaman client
    1. Creating Tranactions: Send the POST request http://localhost:8080/transactions/register and select Basic Auth and in body give parameters and values to create Tranaction.

    2. Get all Transcations: Send the GET request http://localhost:8080/transactions/{transactionid} select Basic Auth, after authenticating and authorisation your username and password we will see all the tranaction realted to that transactionid else we get error message "you dont have access to this transaction".

    3. Update Trancations: Send PUT request http://localhost:8080/transactions/{transactionid} select Basic Auth and in the body update the changes then after succesfully authenticating and authorization your username and password we will see the success message "updation succesful" else  error message "you dont have access to this transaction". and you can check the updated values in mysql.

    4. Delete Transactions: Send DELETE request http://localhost:8080/transactions/{transctionid} Select Basic Auth
 after succesfully authenticating and authorization your username and password we will see the success message "Deletion succesful" else  error message "you dont have access to this transaction". and you can check the updated values in mysql.

    5. In all the above operations if tranactionid is incorrect/invalid we get error message "Transactionid not found".
     

# Instructions to run unit, integration and/or load tests.

  1. Run the LoginControllerTest Junit file which is present in Test folder.
