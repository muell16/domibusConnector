#!/bin/sh 

# browse given parameters

Usage="SYNOPSIS
    template.sh [-hv] [-o[file]] args ...

    DESCRIPTION
    This is a script template
    to start any good shell script.

    OPTIONS
    -o [file], --output=[file]    Set log file (default=/dev/null)
                                  use DEFAULT keyword to autoname file
                                  The default value is /dev/null.
    -t, --timelog                 Add timestamp to log ("+%y/%m/%d@%H:%M:%S")
    -x, --ignorelock              Ignore if lock file exists
    -h, --help                    Print this help
    -v, --version                 Print script information

    EXAMPLES
    template.sh -o DEFAULT arg1 arg2"

if [ "$1" == "--help" ]
then
# usage:
echo"     SYNOPSIS
     template.sh [-hv] [-o[file]] args ...
     
 
     DESCRIPTION
     This is a script template
     to start any good shell script.
 
     OPTIONS
     -o [file], --output=[file]    Set log file (default=/dev/null)
                                   use DEFAULT keyword to autoname file
                                   The default value is /dev/null.
     -t, --timelog                 Add timestamp to log ("+%y/%m/%d@%H:%M:%S")
     -x, --ignorelock              Ignore if lock file exists
     -h, --help                    Print this help
     -v, --version                 Print script information
 
     EXAMPLES
     template.sh -o DEFAULT arg1 arg2"
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
  CONNECTOR_PROPERTIES='conf/connector.properties'
fi


# logging Properties check
if [ -z "$LOGGING_PROPERTIES" ]
then
  # Use default properties 
  LOGGING_PROPERTIES='conf/log4j.properties'
fi

echo "JAVA_PATH  = $JAVA_PATH"
echo "CONNECTOR_PROPERTIES PATH     = $CONNECTOR_PROPERTIES"
echo "LOGGING_PROPERTIES PATH    = $LOGGING_PROPERTIES"


COMMAND_LINE="$JAVA_PATH -Dconnector.properties=$CONNECTOR_PROPERTIES -Dlogging.properties=$LOGGING_PROPERTIES -jar bin/ECodexConnectorRunnable.jar"
echo "executing $COMMAND_LINE"

#Launch connector
$COMMAND_LINE
