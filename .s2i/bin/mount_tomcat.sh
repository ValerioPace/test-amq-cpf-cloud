#!/bin/bash

DIRECTORY = /mnt/tomcat/webserver/webapps

if [ ! -d "$DIRECTORY" ]; then

echo "$DIRECTORY doesn't exists in volume mount, fill it for the first time.."

## swap mount storage with webserver ephemeral data if not already present in the volume
cp -rf $JWS_HOME/* /mnt/tomcat/webserver
rm -rf $JWS_HOME
ln -s /mnt/tomcat/webserver $JWS_HOME
fi