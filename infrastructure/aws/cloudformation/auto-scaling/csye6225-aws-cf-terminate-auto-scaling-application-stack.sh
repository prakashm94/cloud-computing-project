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


aws cloudformation update-stack --stack-name $stack_name --template-body file://csye6225-cf-application-terminate.json  --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue ParameterKey=s3BucketCloud,ParameterValue=$s3BucketCloud ParameterKey=DBUSER,ParameterValue=$DBUSER ParameterKey=DBPWD,ParameterValue=$DBPWD ParameterKey=s3BucketLambda,ParameterValue=$s3BucketLambda ParameterKey=topicName,ParameterValue=$topicName ParameterKey=elbSecurityGroupNameTag,ParameterValue=$elb_security_group ParameterKey=domainName,ParameterValue=$domainName ParameterKey=hostedZoneName,ParameterValue=$domainName$endPeriod

aws cloudformation wait stack-update-complete --stack-name $stack_name
echo "STACK UPDATED SUCCESSFULLY"

aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name

echo "STACK TERMINATED SUCCESSFULLY"

