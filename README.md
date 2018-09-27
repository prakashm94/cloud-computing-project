#Team Members Details

Name: Madhumathi Prakash
email : prakash.m@husky.neu.edu

Name: Poojith Shankar Shetty
email : shetty.poo@husky.neu.edu

Name: Prashant Reddy
email: reddy.pra@husky.neu.edu

Name: Vrushali Shah
email: shah.vru@husky.neu.edu

#Prerequisites for building and deploying our application locally

* IntellIj
* Tomcat server
* Mysql Server
* Postman client

#Build and Deploy instructions for web application.

* Open the Github link : https://github.com/prakashm94/csye6225-fall2018
* Open IntellIj.
* Run the project using command mvn spring-boot:run in intellIj Terminal.
* Open postman client
     
    1. User Creation:  Send the POST request http://localhost:8080/user/register and Select authentication as Basic 	Auth, if user doesnot exists user gets created with message "User created Succesfully", else we get message "User already exists" .

    2. Send the Get request http://localhost:8080/time and select Basic Auth after succesfully authenticating the request we get the current time else error message will be displayed "Login Unsuccessful, please Check Username and Password".

#Instructions to run unit, integration and/or load tests.

  1. Run the LoginControllerTest Junit file which is present in Test folder.
