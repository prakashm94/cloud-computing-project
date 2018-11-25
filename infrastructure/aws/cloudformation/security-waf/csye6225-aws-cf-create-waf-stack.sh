#!/bin/bash
echo "Creating WAF Stack"
stack_name=$1

echo $stack_name


stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-waf.json --query [StackId] --capabilities CAPABILITY_NAMED_IAM --output text)

echo $stackId

if [ -z $stackId ]; then
    echo 'Error occurred.Dont proceed. TERMINATED'
else
    aws cloudformation wait stack-create-complete --stack-name $stack_name
    echo "STACK CREATION COMPLETE."
fi
