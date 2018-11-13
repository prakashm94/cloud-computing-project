echo "Creating CICD Stack"

stack_name=$1
domain_name=$2
applicationName=$3

export account_id=$(aws sts get-caller-identity --query "Account" --output text)
export region=us-east-1

export resource1="arn:aws:codedeploy:"$region":"$account_id":application:"$applicationName
export resource2="arn:aws:codedeploy:"$region":"$account_id":deploymentconfig:CodeDeployDefault.OneAtATime"
export resource3="arn:aws:codedeploy:"$region":"$account_id":deploymentconfig:CodeDeployDefault.HalfAtATime"
export resource4="arn:aws:codedeploy:"$region":"$account_id":deploymentconfig:CodeDeployDefault.AllAtOnce"
export resource5="arn:aws:s3:::code-deploy."$domain_name"/"


stackId=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-cicd.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=resource1,ParameterValue=$resource1 ParameterKey=resource2,ParameterValue=$resource2 ParameterKey=resource3,ParameterValue=$resource3 ParameterKey=resource4,ParameterValue=$resource4 ParameterKey=resource5,ParameterValue=$resource5 ParameterKey=domainName,ParameterValue=$domain_name ParameterKey=applicationName,ParameterValue=$applicationName --query [StackId] --output text)

echo "#############################"
echo $stackId
echo "#############################"

if [ -z $stackId ]; then
    echo 'Error occurred.Dont proceed. TERMINATED'
else
    aws cloudformation wait stack-create-complete --stack-name $stack_name
    echo "STACK CREATION COMPLETE."
fi
