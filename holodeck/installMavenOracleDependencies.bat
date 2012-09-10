call mvn install:install-file -Dfile=.\extra\oracle\9.2.0.8\ojdbc14.jar -DgroupId=com.oracle -DartifactId=ojdbc -Dversion=9.2.0.8 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile=.\extra\oracle\10.2.0.5\ojdbc14.jar -DgroupId=com.oracle -DartifactId=ojdbc -Dversion=10.2.0.5 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile=.\extra\oracle\11.1.0.7.0\ojdbc6-11.1.0.7.0.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.1.0.7.0 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile=.\extra\oracle\11.2.0.3\ojdbc5.jar -DgroupId=com.oracle -DartifactId=ojdbc -Dversion=11.2.0.3 -Dpackaging=jar -DgeneratePom=true