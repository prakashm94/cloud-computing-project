# Steps to run cloudformation on your local:

1. Make sure you have aws credentials set. following colland: aws credentials
then enter the access and secret key

2. Download the 3 files in the cloudformation folder (setup.sh, terminate.sh, and the json file)

3. Make sure all three files are in the same directory

4. in command line run this command to create the stack with all items: bash csye6225-aws-cf-create-stack.sh "STACK_NAME"
5. in command line run this command to delete the stack and all items: bash csye6225-aws-cf-terminate-stack.sh "STACK_NAME"

