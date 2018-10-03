# Steps to run cloudformation on your local:

1. Make sure you have aws credentials set. following colland: aws credentials
then enter the access and secret key

2. Git clone or download the project zip and go to the path: infrastructure/aws/cloudformation/ which has application and network folders. Both these folders should have setup.sh, terminate.sh, and json files.

3. Make sure you do not change location of these files. 

4. To create the network stack, go to the network folder and run this command in the command line  with all items: bash csye6225-aws-cf-create-stack.sh "STACK_NAME"

5. Once the stack is successfully created, go to the application folder and run this command in the command line  with all items: bash csye6225-aws-cf-create-application-stack.sh "STACK_NAME" "KEY_TAG" "EC2_INSTANCE"

6. You can see the application stack will be created. Wait until the EC2 instance is initialized completely.

7. In order to terminate the application resources, go to the application folder and run this command in the command line  with all items: bash csye6225-aws-cf-terminate-application-stack.sh "STACK_NAME" "KEY_TAG" "EC2_INSTANCE"

8. To delete the network stack, go to the network folder and run this command in the command line  with all items: bash csye6225-aws-cf-terminate-stack.sh "STACK_NAME"

