#!/bin/bash
echo "Updating Stack To Delete"
stack_name=$1
topicName=$2
applicationNameLambda=$3

echo $stack_name

#aws cloudformation update-stack --stack-name $stack_name --template-body file://csye6225-cf-application-terminate-with-lambda.json  --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue ParameterKey=s3BucketTag,ParameterValue=$s3BucketTagValue ParameterKey=DBUSER,ParameterValue=$DBUSER ParameterKey=DBPWD,ParameterValue=$DBPWD ParameterKey=topicName,ParameterValue=$topicName

#aws cloudformation wait stack-update-complete --stack-name $stack_name
#echo "STACK UPDATED SUCCESSFULLY"

aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name

echo "STACK TERMINATED SUCCESSFULLY"

