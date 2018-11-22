#!/bin/bash
echo "Creating Stack"
stack_name=$1
keyTagValue=$2
s3BucketCloud=$3
DBUSER=$4
DBPWD=$5
s3BucketLambda=$6
topicName=$7
domainName=$8
applicationName=$9
endPeriod='.'
export accId=$(aws sts get-caller-identity --query "Account" --output text)
export CERTIFICATE_ARN=$(aws acm list-certificates --query "CertificateSummaryList[0].CertificateArn" --output text)
echo $stack_name

webSecurityGroupTagValue=csye6225-webapp
dbSecurityGroupTagValue=csye6225-rds
elb_security_group=elb-sg

stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-auto-scaling-application.json --parameters ParameterKey=webSecurityGroupTag,ParameterValue=$webSecurityGroupTagValue ParameterKey=dbSecurityGroupTag,ParameterValue=$dbSecurityGroupTagValue ParameterKey=keyTag,ParameterValue=$keyTagValue ParameterKey=s3BucketCloud,ParameterValue=$s3BucketCloud ParameterKey=DBUSER,ParameterValue=$DBUSER ParameterKey=DBPWD,ParameterValue=$DBPWD ParameterKey=s3BucketLambda,ParameterValue=$s3BucketLambda ParameterKey=topicName,ParameterValue=$topicName ParameterKey=accountId,ParameterValue=$accId ParameterKey=elbSecurityGroupNameTag,ParameterValue=$elb_security_group ParameterKey=domainName,ParameterValue=$domainName ParameterKey=hostedZoneName,ParameterValue=$domainName$endPeriod ParameterKey=CertificateArn1,ParameterValue=$CERTIFICATE_ARN ParameterKey=applicationName,ParameterValue=$applicationName --query [StackId] --capabilities CAPABILITY_NAMED_IAM --output text)

echo $stackId

if [ -z $stackId ]; then
    echo 'Error occurred.Dont proceed. TERMINATED'
else
    aws cloudformation wait stack-create-complete --stack-name $stack_name
    echo "STACK CREATION COMPLETE."
fi
