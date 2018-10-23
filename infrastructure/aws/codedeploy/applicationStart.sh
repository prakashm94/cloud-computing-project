#!/bin/bash
cd /opt/tomcat/webapps
sudo rm -rf CloudApp
sudo systemctl start tomcat.service
