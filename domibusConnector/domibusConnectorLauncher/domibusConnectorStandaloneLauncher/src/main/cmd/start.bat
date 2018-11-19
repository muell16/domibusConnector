@echo off
rem This is the automatically built startup script for the DomibusStandaloneConnector.
rem To be able to run the JAVA_HOME system environment variable must be set properly.

if exist "%JAVA_HOME%" goto okJava
call setenv.bat
if exist "%JAVA_HOME%" goto okJava
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okJava

set "PATH=%PATH%;%JAVA_HOME%\bin"
set "CURRENT_DIR=%cd%"

set "CLASSPATH=%CURRENT_DIR%\bin\*"
echo %CLASSPATH%

set "LIB_FOLDER=%CURRENT_DIR\lib\"

set "CONFIG_FOLDER=%CURRENT_DIR\config\"

rem set "LOG_FOLDER=%CURRENT_DIR\logs\"

rem set "FS_STORAGE_FOLDER=%CURRENT_DIR\fsstorage\"

rem set "CONNECTOR_PROPERTIES=%connector-client.properties%"
rem if exist "%CONNECTOR_PROPERTIES%" goto okConnProps
rem set "CONNECTOR_PROPERTIES=conf\connector-client.properties
rem :okConnProps
rem set "connector-client.properties=%CONNECTOR_PROPERTIES%"
rem echo connector-client.properties set to "%CONNECTOR_PROPERTIES%"

rem set "LOGGING_PROPERTIES=%logging.properties%"
rem if exist "%LOGGING_PROPERTIES%" goto okLogProps
rem set "LOGGING_PROPERTIES=conf\log4j.properties
rem :okLogProps
rem set "logging.properties=%LOGGING_PROPERTIES%"
rem echo LOGGING_PROPERTIES set to "%LOGGING_PROPERTIES%"

title "DomibusConnectorClient 10"

"%JAVA_HOME%\bin\java" -D"spring.config.location=%CONFIG_FOLDER%" -D"spring.config.name=connector" -D"spring.cloud.bootstrap.location=%CONFIG_FOLDER%/bootstrap.properties" -D"loader.path=%LIB_FOLDER%" -cp "%CLASSPATH%" org.springframework.boot.loader.PropertiesLauncher

:end



REM PS C:\Entwicklung\repos\domibusConnector\domibusConnector\domibusConnectorLauncher\domibusConnectorStandaloneLauncher> java -D"loader.debug=true" -D"loader.path=./target/lib/" -D"c
REM onnector.config.file=C:\Entwicklung\ecodexENV\EXECUser10\software\domibusConnector-tomcat\conf\connector\connector_oracle.properties" -cp ".\target\domibusConnectorStandaloneLaunch
REM er-4.1.0-RC2-SNAPSHOT-standalone.jar" org.springframework.boot.loader.PropertiesLauncher