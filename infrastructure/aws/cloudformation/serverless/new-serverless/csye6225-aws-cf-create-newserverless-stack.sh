#!/bin/bash
echo "Creating Stack"
stack_name=$1
topicName=$2
#applicationNameLambda=$3

echo $stack_name

export account_id=$(aws sts get-caller-identity --query "Account" --output text)
export region=us-east-1
export resource1="arn:aws:iam::"$account_id":role/LambdaExecutionRole"


stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-newserverless.json --parameters ParameterKey=topicName,ParameterValue=$topicName ParameterKey=resource1,ParameterValue=$resource1 --query [StackId] --capabilities CAPABILITY_NAMED_IAM --output text)

#stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-newserverless.json --parameters ParameterKey=resource1,ParameterValue=$resource1 --query [StackId] --capabilities CAPABILITY_NAMED_IAM --output text)

#stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-serverless.json --parameters --query [StackId] --capabilities CAPABILITY_NAMED_IAM --output text)

echo $stackId

if [ -z $stackId ]; then
    echo 'Error occurred.Dont proceed. TERMINATED'
else
    aws cloudformation wait stack-create-complete --stack-name $stack_name
    echo "STACK CREATION COMPLETE."
fi
