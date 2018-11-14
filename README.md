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

# Creating a attachments and uoploading attachments to S3 bucket and local system based on profile
  
   1. Create a S3 bucket on aws.
   2. Create a Attachments table in mysql.
   3. Configure the application-dev.properties file with aws region, S3 bucket name and aws user credentials.


    CRUD operations for uploading Attachments into localfile system "If profile is not a Dev".
    
    1. Create : Send Post request http://localhost:8080/transactions/{transactionId}/attachments select Basic Auth and in body select the image file then after succesfully authenticating and authorization your username, password and file extension we will see the success message "Uploaded succesfully" then you can check the attachmentid in mysql in attachment table and check the image in local file system path.

    2. Read : Send Get Request http://localhost:8080/transactions/{transactionId}/attachments select Basic Auth after succesfully authenticating and authorization your username, password then you will see all the file details else we get error message "you dont have access to this transaction".

    3. Update : Send PUT Request http://localhost:8080/transactions/{transactionId}/attachments/{aid} select Basic Auth and in body select the image file to update then after succesfully authenticating and authorization your username, password and file extension we will see the success message "Uploaded succesfully" then you can check the attachmentid in mysql in attachment table and check the image in local file system path.
     
    4. Delete : Send DELETE request http://localhost:8080/transactions/{transactionId}/attachments/{aid} select Basic Auth and in body select the image file to update then after succesfully authenticating and authorization your username, password then we will see the success message "Deleted succesfully" then you can check the attachmentid in mysql in attachment table and check for the image in local file system path.




    CRUD operations for uploading Attachments into AWS S3 Bucket "If profile is Dev".
    
    1. Create : Send Post request http://localhost:8080/transactions/{transactionId}/attachments select Basic Auth and in body select the image file then after succesfully authenticating and authorization your username, password and file extension we will see the success message "Uploaded succesfully" then you can check the attachmentid in mysql in attachment table and check the image in AWS console S3 bucket.

    2. Read : Send Get Request http://localhost:8080/transactions/{transactionId}/attachments select Basic Auth after succesfully authenticating and authorization your username, password then you will see all the file details else we get error message "you dont have access to this transaction".

    3. Update : Send PUT Request http://localhost:8080/transactions/{transactionId}/attachments/{aid} select Basic Auth and in body select the image file to update then after succesfully authenticating and authorization your username, password and file extension we will see the success message "Uploaded succesfully" then you can check the attachmentid in mysql in attachment table and check the image in AWS console S3 bucket.
     
    4. Delete : Send DELETE request http://localhost:8080/transactions/{transactionId}/attachments/{aid} select Basic Auth and in body select the image file to update then after succesfully authenticating and authorization your username, password then we will see the success message "Deleted succesfully" then you can check the attachmentid in mysql in attachment table and check for the image in AWS console S3 bucket.


# Run the Travis build then code will be deployed in ec2
  1. hit the public dns of ec2 follwed by application name
  2. Perform CRUD operations on user, Transactions and Attachments on Postman and check whether Applications is working.
  3. check images in S3

# Instructions to run unit, integration and/or load tests.

  1. Run the LoginControllerTest Junit file which is present in Test folder.

