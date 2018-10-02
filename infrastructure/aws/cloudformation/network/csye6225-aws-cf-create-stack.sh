#!/bin/bash
echo "Stack creation in progress"
stackName=$1
csye_const=-csye6225-
vpc_const=-vpc
subnetA=subnetA
subnetB=subnetB
subnetC=subnetC
subnetA=subnetD
subnetB=subnetF
subnetC=subnetF
ig_const=-InternetGateway
route_table_const=-route-table

vpcTag=$stackName$csye_const$vpc_const
echo $vpcTag
stackId=$(aws cloudformation create-stack --stack-name $stackName --template-body file://csye6225-cf-networking.json --parameters ParameterKey=vpcTag,ParameterValue=$vpcTag ParameterKey=subnetTagA,ParameterValue=$stackName$csye_const$SubnetA ParameterKey=subnetTagB,ParameterValue=$stackName$csye_const$SubnetB ParameterKey=subnetTagC,ParameterValue=$stackName$csye_const$SubnetC ParameterKey=subnetTagD,ParameterValue=$stackName$csye_const$SubnetD ParameterKey=subnetTagE,ParameterValue=$stackName$csye_const$SubnetE ParameterKey=subnetTagF,ParameterValue=$stackName$csye_const$SubnetF ParameterKey=igTag,ParameterValue=$stackName$csye_const$ig_const ParameterKey=routeTableTag,ParameterValue=$stackName$csye_const$route_table_const --query [StackId] --output text)
echo $stackId

if [ -z $stackId ]; then
    echo ' fail: stack was not created created!'
else
    aws cloudformation wait stack-create-complete --stack-name $stackId
    echo "success: Stack was created created!"
fi
