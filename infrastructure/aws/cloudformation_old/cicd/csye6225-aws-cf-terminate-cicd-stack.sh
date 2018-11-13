echo 'Deleting CICD Stack'
stack_name=$1
domain_name=$2

bucket_base_path='s3://'

echo $bucket_base_path$domain_name

aws s3 rm $bucket_base_path$domain_name --recursive
aws s3 rb $bucket_base_path$domain_name --force

aws cloudformation delete-stack --stack-name $stack_name
aws cloudformation wait stack-delete-complete --stack-name $stack_name


echo "CICD STACK TERMINATED SUCCESSFULLY"
