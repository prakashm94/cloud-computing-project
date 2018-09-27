#!/bin/bash
PATH="/usr/local/bin:$PATH"

stack_name=$1
csye_const=-csye6225-
vpc_const=vpc
ig_const=InternetGateway
route_table_const=route-table

vpc_id=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region us-east-1 --query [Vpc.VpcId] --output text)

if [ -z $vpc_id ]; then
    echo 'Error creating VPC. Terminating' $vpc_id
else
    echo 'VPC CREATED SUCCESSFULLY' $vpc_id
fi 

aws ec2 create-tags --resources $vpc_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"

subnet1_id=$(aws ec2 create-subnet --availability-zone us-east-1a --vpc-id $vpc_id --cidr-block 10.0.0.0/24 --query [Subnet.SubnetId] --output text)
subnet2_id=$(aws ec2 create-subnet --availability-zone us-east-1b --vpc-id $vpc_id --cidr-block 10.0.2.0/24 --query [Subnet.SubnetId] --output text)
subnet3_id=$(aws ec2 create-subnet --availability-zone us-east-1c --vpc-id $vpc_id --cidr-block 10.0.10.0/24 --query [Subnet.SubnetId] --output text)
subnet4_id=$(aws ec2 create-subnet --availability-zone us-east-1d --vpc-id $vpc_id --cidr-block 10.0.11.0/24 --query [Subnet.SubnetId] --output text)
subnet5_id=$(aws ec2 create-subnet --availability-zone us-east-1e --vpc-id $vpc_id --cidr-block 10.0.1.0/24 --query [Subnet.SubnetId] --output text)
subnet6_id=$(aws ec2 create-subnet --availability-zone us-east-1f --vpc-id $vpc_id --cidr-block 10.0.3.0/24 --query [Subnet.SubnetId] --output text)

if [ -z $subnet1_id ]; then
    echo 'Error creating SUBNET 1. Terminating' $subnet1_id
else
    echo 'SUBNET 1 CREATED SUCCESSFULLY' $subnet1_id
fi

if [ -z $subnet2_id ]; then
    echo 'Error creating SUBNET 2. Terminating' $subnet2_id
else
    echo 'SUBNET 2 CREATED SUCCESSFULLY' $subnet2_id
fi

if [ -z $subnet3_id ]; then
    echo 'Error creating SUBNET 3. Terminating' $subnet3_id
else
    echo 'SUBNET 3 CREATED SUCCESSFULLY' $subnet3_id
fi

echo '3 PRIVATE SUBNETS CREATED SUCCESSFULLY'

if [ -z $subnet4_id ]; then
    echo 'Error creating SUBNET 4. Terminating' $subnet4_id
else
    echo 'SUBNET 4 CREATED SUCCESSFULLY' $subnet4_id
fi

if [ -z $subnet5_id ]; then
    echo 'Error creating SUBNET 5. Terminating' $subnet5_id
else
    echo 'SUBNET 5 CREATED SUCCESSFULLY' $subnet5_id
fi

if [ -z $subnet6_id ]; then
    echo 'Error creating SUBNET 6. Terminating' $subnet6_id
else
    echo 'SUBNET 6 CREATED SUCCESSFULLY' $subnet6_id
fi

echo '3 PUBLIC SUBNETS CREATED SUCCESSFULLY'

aws ec2 create-tags --resources $subnet1_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"
aws ec2 create-tags --resources $subnet2_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"
aws ec2 create-tags --resources $subnet3_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"
aws ec2 create-tags --resources $subnet4_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"
aws ec2 create-tags --resources $subnet5_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"
aws ec2 create-tags --resources $subnet6_id --tags "Key=Name,Value=$stack_name$csye_const$vpc_const"

ig_id=$(aws ec2 create-internet-gateway --query [InternetGateway.InternetGatewayId] --output text)

if [ -z $ig_id ]; then
    echo 'Error creating INTERNET GATEWAY. Terminating' $ig_id
else
    echo 'INTERNET GATEWAY CREATED SUCCESSFULLY' $ig_id
fi    

aws ec2 create-tags --resources $ig_id --tags "Key=Name,Value=$stack_name$csye_const$ig_const"
aws ec2 attach-internet-gateway --internet-gateway-id $ig_id --vpc-id $vpc_id

route_table_id=$(aws ec2 create-route-table --vpc-id $vpc_id --query [RouteTable.RouteTableId] --output text)

if [ -z $route_table_id ]; then
    echo 'Error creating ROUTE TABLE. Terminating' $route_table_id
else
    echo 'ROUTE TABLE CREATED SUCCESSFULLY' $route_table_id
    aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet1_id
    echo 'SUBNET 1 ATTACHED TO ROUTE TABLE'
    aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet2_id
    echo 'SUBNET 2 ATTACHED TO ROUTE TABLE'
    aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet3_id
    echo 'SUBNET 3 ATTACHED TO ROUTE TABLE'
    aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet4_id
    echo 'SUBNET 4 ATTACHED TO ROUTE TABLE'
    aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet5_id
    echo 'SUBNET 5 ATTACHED TO ROUTE TABLE'
    aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet6_id
    echo 'SUBNET 6 ATTACHED TO ROUTE TABLE'

fi

aws ec2 create-tags --resources $route_table_id --tags "Key=Name,Value=$stack_name$csye_const$route_table_const"
aws ec2 create-route --route-table-id $route_table_id --destination-cidr-block 0.0.0.0/0 --gateway-id $ig_id
echo 'ROUTE CREATED SUCCESSFULLY'

echo 'COMPLETED STACK CREATION'
