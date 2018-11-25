#!/bin/bash
echo "Stack Deleting in progress"
stack_name=$1

echo $stack_name

aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name

echo "STACK TERMINATED SUCCESSFULLY"

