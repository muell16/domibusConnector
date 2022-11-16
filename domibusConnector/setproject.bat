REM download jdk

REM SET DOWNLOAD_URL=https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.17+8/OpenJDK11U-jdk_x64_windows_hotspot_11.0.17_8.zip
SET DOWNLOAD_URL=https://centralrepo.brz.gv.at/artifactory/github-mirror/adoptium/temurin11-binaries/releases/download/jdk-11.0.17+8/OpenJDK11U-jdk_x64_windows_hotspot_11.0.17_8.zip

SET DOWNLOAD_FOLDER=%~dp0\target\
REM SET DOWNLOAD_FOLDER=%Temp%\jdk11

SET DOWNLOAD_TARGET=%DOWNLOAD_FOLDER%\jdk11.zip

mkdir %DOWNLOAD_FOLDER%

REM DOWNLOAD JDK 11
REM BITSADMIN /TRANSFER copyJdk /DOWNLOAD /priority normal %DOWNLOAD_URL% %DOWNLOAD_TARGET%

REM extract downloaded
tar -xf %DOWNLOAD_TARGET% -C %DOWNLOAD_FOLDER%
