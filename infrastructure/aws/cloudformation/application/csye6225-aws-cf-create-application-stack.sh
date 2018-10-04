#!/bin/bash
echo "Creating Stack"
stack_name=$1
keyTagValue=$2
ec2InstanceTagValue=$3
echo $stack_name

webSecurityGroupTagValue=csye6225-webapp
dbSecurityGroupTagValue=csye6225-rds

stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-application.json --parameters ParameterKey=ec2InstanceTag,ParameterValue=$ec2InstanceTagValue ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue --query [StackId] --output text)

echo $stackId

if [ -z $stackId ]; then
    echo 'Error occurred.Dont proceed. TERMINATED'
else
    aws cloudformation wait stack-create-complete --stack-name $stack_name
    echo "STACK CREATION COMPLETE."
fi
