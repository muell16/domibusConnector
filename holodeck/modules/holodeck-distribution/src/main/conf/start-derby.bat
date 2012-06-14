@echo off

set lib_dir=%~dp0..\..\msh\WEB-INF\lib

set CP=.;%lib_dir%\derby-10.4.2.0.jar;%lib_dir%\derbynet.jar;%lib_dir%\derbyclient-10.4.2.0.jar;%lib_dir%\derbytools-10.4.2.0.jar

copy %lib_dir%\derbynet-10.4.2.0.jar %lib_dir%\derbynet.jar

@echo on

java -classpath %CP% org.apache.derby.drda.NetworkServerControl start -h 0.0.0.0