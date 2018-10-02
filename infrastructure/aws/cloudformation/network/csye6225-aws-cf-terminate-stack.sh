#!/bin/bash
echo "Stack Deleting in progress"
stack_name=$1
csye_const=-csye6225-
vpc_const=-vpc

vpcTag=$stack_name$csye_const$vpc_const

aws cloudformation delete-stack --stack-name $stack_name

aws cloudformation wait stack-delete-complete --stack-name $stack_name


echo "Successfully Deleted"
