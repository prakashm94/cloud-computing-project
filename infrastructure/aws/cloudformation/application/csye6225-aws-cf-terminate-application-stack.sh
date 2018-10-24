#!/bin/bash
echo "Updating Stack To Delete"
stack_name=$1
keyTagValue=$2
s3BucketTagValue=$3
echo $stack_name

webSecurityGroupTagValue=csye6225-webapp
dbSecurityGroupTagValue=csye6225-rds

aws cloudformation update-stack --stack-name $stack_name --template-body file://csye6225-cf-application-terminate.json --parameters ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue ParameterKey=s3BucketTag,ParameterValue=$s3BucketTagValue 

aws cloudformation wait stack-update-complete --stack-name $stack_name
echo "STACK UPDATED SUCCESSFULLY"

aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name

echo "STACK TERMINATED SUCCESSFULLY"

