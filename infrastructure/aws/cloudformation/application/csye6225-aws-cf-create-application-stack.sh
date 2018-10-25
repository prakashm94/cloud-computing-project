#!/bin/bash
echo "Creating Stack"
stack_name=$1
keyTagValue=$2
s3BucketTagValue=$3
DBUSER=$4
DBPWD=$5
echo $stack_name

webSecurityGroupTagValue=csye6225-webapp
dbSecurityGroupTagValue=csye6225-rds

stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-application.json --parameters ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue ParameterKey=s3BucketTag,ParameterValue=$s3BucketTagValue ParameterKey=DBUSER,ParameterValue=$DBUSER ParameterKey=DBPWD,ParameterValue=$DBPWD --query [StackId] --capabilities CAPABILITY_NAMED_IAM --output text)

echo $stackId

if [ -z $stackId ]; then
    echo 'Error occurred.Dont proceed. TERMINATED'
else
    aws cloudformation wait stack-create-complete --stack-name $stack_name
    echo "STACK CREATION COMPLETE."
fi
