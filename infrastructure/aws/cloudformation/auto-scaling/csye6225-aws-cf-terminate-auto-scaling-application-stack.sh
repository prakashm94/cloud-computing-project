#!/bin/bash
echo "Updating Stack To Delete"
stack_name=$1
keyTagValue=$2
s3BucketCloud=$3
DBUSER=$4
DBPWD=$5
s3BucketLambda=$6
topicName=$7
domainName=$8
endPeriod='.'

echo $stack_name

webSecurityGroupTagValue=csye6225-webapp
dbSecurityGroupTagValue=csye6225-rds
elb_security_group=elb-sg

bucket_base_path='s3://'

echo $bucket_base_path$s3Bucket

aws s3 rm $bucket_base_path$s3BucketCloud --recursive
aws s3 rb $bucket_base_path$s3BucketCloud --force


echo $bucket_base_path$s3BucketLambda

aws s3 rm $bucket_base_path$s3BucketLambda --recursive
aws s3 rb $bucket_base_path$s3BucketLambda --force


aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name

echo "STACK TERMINATED SUCCESSFULLY"

