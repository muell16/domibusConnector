#!/bin/sh 

# browse given parameters

if [ "$1" == "--help" ]
then
# usage:
echo"     SYNOPSIS
     DomibusStandaloneConnector.sh [-h] [-j=[path]] [-c=[file]] [-l=[file]]
     
 
     DESCRIPTION
     This script start the Domibus standalone connector.
 
     OPTIONS
     -j=[path], --javaPath=[path]    			Set the path to the installed java. Replaces JAVA_HOME
     											environment variable. So if JAVA_HOME is set, this does not need to be set.
     -c=[file], --connectorProperties=[file]    Set connector properties file (default=conf/connector.properties)
     -l=[file], --loggingProperties=[file]   	Set log4j.properties file (default=conf/log4j.properties)
     -h, --help                    				Print this help
     
     EXAMPLES
     DomibusStandaloneConnector.sh -j=/usr/bin/java8"
	exit 0
fi


for i in "$@"
do
case $i in
    -j=*|--javaPath=*)
    JAVA_PATH="${i#*=}"
    JAVA_PATH=$JAVA_PATH/bin/java
    shift # past argument=value
    ;;
    -c=*|--connectorProperties=*)
    CONNECTOR_PROPERTIES="${i#*=}"
    shift # past argument=value
    ;;
    -l=*|--loggingProperties=*)
    LOGGING_PROPERTIES="${i#*=}"
    shift # past argument=value
    ;;
    *)
            # unknown option
			# goto usage
    ;;
esac
done


# Java Path Check

if [ -z "$JAVA_PATH" ]
then
	# no Java path given by the user
	if [ -z "$JAVA_HOME" ]
	then
		# no java home set, try lookup of java executable
		JAVA_PATH=`/usr/bin/which java 2>/dev/null`
		if [ -z "$JAVA_PATH" ]
		then
		  echo "Please specify path to the java home"
		  exit 2
	  	fi
	else
		JAVA_PATH=$JAVA_HOME/bin/java
		if [ ! -f $JAVA_PATH ]
		then
			echo "Invalid JAVA_HOME. Cannot find $JAVA_PATH"
			exit 2
		fi
	fi
else
  if [ ! -f $JAVA_PATH ]
  then
     echo "Cannot find java executable: $JAVA_PATH"
     exit 2 
  fi
fi

# Connector Properties check
if [ -z "$CONNECTOR_PROPERTIES" ]
then
  # Use default properties 
  CONNECTOR_PROPERTIES=`/usr/bin/pwd`'/conf/connector.properties'
fi


# logging Properties check
if [ -z "$LOGGING_PROPERTIES" ]
then
  # Use default properties 
  LOGGING_PROPERTIES=`/usr/bin/pwd`'/conf/log4j.properties'
fi

# building the Classpath
CLASSPATH=`/usr/bin/pwd`'/bin/*:'`/usr/bin/pwd`'/lib/*'

echo "JAVA_PATH  = $JAVA_PATH"
echo "CONNECTOR_PROPERTIES PATH     = $CONNECTOR_PROPERTIES"
echo "LOGGING_PROPERTIES PATH    = $LOGGING_PROPERTIES"
echo "CLASSPATH = $CLASSPATH"

gateway.routing.option=Webservice

COMMAND_LINE="$JAVA_PATH -cp $CLASSPATH -Dconnector.properties=$CONNECTOR_PROPERTIES -Dlogging.properties=$LOGGING_PROPERTIES eu.domibus.connector.runnable.DomibusConnector -gui"
echo "executing $COMMAND_LINE"

#Launch connector
$COMMAND_LINE
