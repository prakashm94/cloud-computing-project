# Steps to run cloudformation on your local:

1. Make sure you have aws credentials set. following colland: aws credentials
then enter the access and secret key

2. Git clone or download the project zip and go to the path: infrastructure/aws/cloudformation/ which has application and network folders. Both these folders should have setup.sh, terminate.sh, and json files.

3. Make sure you do not change location of these files. 

4. To create the network stack, go to the network folder and run this command in the command line  with all items: bash csye6225-aws-cf-create-stack.sh "STACK_NAME"

5. To create the cicd stack, go to cicd folder and run this command in the command line with all items: bash csye6225-aws-cf-create-cicd-stack.sh "STACK_NAME" "DOMAIN_NAME" "LAMBDA_APPLICATION_NAME"

6. Once the stack is successfully created, go to the application folder and run this command in the command line  with all items: bash csye6225-aws-cf-create-application-stack.sh "STACK_NAME" "KEY_TAG" "S3_BUCKET_NAME_CLOUD" "DBUSER" "DBPASSWORD" "S3_BUCKET_NAME_LAMBDA" "TOPIC_NAME"

7. You can see the application stack will be created. Wait until the EC2 instance is initialized completely.

8. At this point, the S3 bucket for Lambda Code deployment will be created. Upload blank lambdaapp.zip inside this bucket. This will be needed to create the Serverless Stack.

9. Once this is done, go to the serverless folder and run this command in the command line  with all items: bash csye6225-aws-cf-create-serverless-stack.sh "STACK_NAME" "TOPIC_NAME" "DOMAIN_NAME"

10. You can see the Serverless Stack is created successfully with the Lambda Function and SNS Topic. 

11. Trigger the Travis Build for CLoudApp and LambdaApp with appropriate setting configured in the Travis CI.

12. Once the deployment is successful, you can perform CRUD operation using Postman.

13. To delete the Serverless Stack, go to the serverless folder and run this command in the command line  with all items: bash csye6225-aws-cf-terminate-serverless-stack.sh "STACK_NAME"

14. In order to terminate the application resources, go to the application folder and run this command in the command line  with all items: bash csye6225-aws-cf-terminate-application-stack.sh "STACK_NAME" "KEY_TAG" "S3_BUCKET_NAME_CLOUD" "DBUSER" "DBPASSWORD" "S3_BUCKET_NAME_LAMBDA" "TOPIC_NAME"

15. To delete the cicd stack, go to the cicd folder and run this command in the command line with all items: bash csye6225-aws-cf-terminate-cicd-stack.sh "STACK_NAME" "DOMAIN_NAME" "LAMBDA_APPLICATION_NAME"

16. To delete the network stack, go to the network folder and run this command in the command line  with all items: bash csye6225-aws-cf-terminate-stack.sh "STACK_NAME"

