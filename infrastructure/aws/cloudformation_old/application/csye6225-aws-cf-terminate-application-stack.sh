#!/bin/bash
echo "Updating Stack To Delete"
stack_name=$1
keyTagValue=$2
s3BucketTagValue=$3
DBUSER=$4
DBPWD=$5
snsTopicName=$6

echo $stack_name

webSecurityGroupTagValue=csye6225-webapp
dbSecurityGroupTagValue=csye6225-rds

bucket_base_path='s3://'

echo $bucket_base_path$s3BucketTagValue

aws s3 rm $bucket_base_path$s3BucketTagValue --recursive
aws s3 rb $bucket_base_path$s3BucketTagValue --force

aws cloudformation update-stack --stack-name $stack_name --template-body file://csye6225-cf-application-terminate.json  --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue ParameterKey=s3BucketTag,ParameterValue=$s3BucketTagValue ParameterKey=DBUSER,ParameterValue=$DBUSER ParameterKey=DBPWD,ParameterValue=$DBPWD ParameterKey=TopicName,ParameterValue=$snsTopicName

aws cloudformation wait stack-update-complete --stack-name $stack_name
echo "STACK UPDATED SUCCESSFULLY"

aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name

echo "STACK TERMINATED SUCCESSFULLY"

