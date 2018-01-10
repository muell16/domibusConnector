node {
   
   
	List GIT_ENV = [
		"GIT_SSL_NO_VERIFY=true",
	]
	
	withEnv(GIT_ENV) {
		stage ("Checkout") {
			scmInfo = checkout scm
	 
			echo "scm : ${scmInfo}"
			echo "${scmInfo.GIT_COMMIT}"
		}
				
			
		String jdktool = tool name: "JAVA 8", type: 'hudson.model.JDK'
		def mvnHome = tool name: 'MAVEN 3.3.x'
	 
		/* Set JAVA_HOME, and special PATH variables. */
		List javaEnv = [
			"PATH+MVN=${jdktool}/bin:${mvnHome}/bin",
			"M2_HOME=${mvnHome}",
			"JAVA_HOME=${jdktool}",
		]

		def boolean docker_available = false
		
		withEnv(javaEnv) {
			def RELEASE = false
			def releaseVersion = ""
			stage ('Initialize') {
				sh '''
					echo "PATH = ${PATH}"
					echo "M2_HOME = ${M2_HOME}"   
				'''
				
				if ( scmInfo.GIT_BRANCH.startsWith("release/") ) {
					RELEASE = true 
					releaseVersion = scmInfo.GIT_BRANCH.substring(8)
				}
						
				try {
					docker_available = true
					sh 'docker ps'
				} catch (e) {
					docker_available = false
				}
						
				//TODO: always clean up workspace!
			}
			
			//FAILURE, SUCCESS, UNSTABLE
					
			dir ('domibusConnector') {
			
				stage ('Build') {
					sh 'mvn -DskipTests=true -pl domibusConnectorDomain -am install' //package domain so other modules can load wsdl via dependency plugin
					sh 'mvn compile'
					//sh 'mvn -DskipTests package'
				}
				

				try {
					stage ('Test') {
						sh 'mvn test'
					}
				} catch (e) {
					currentBuild.result = 'UNSTABLE'
				}
				
					
				try {					
					stage ('Integration Test') {
						sh 'mvn -P integration-testing,dbunit-testing verify'
					}
				} catch (e) {
						currentBuild.result = 'UNSTABLE'
				}
				
				stage ('Post') {
					if (currentBuild.result == null || currentBuild.result != 'FAILURE') {
						junit '**/surefire-reports/*.xml,**/failsafe-reports/*.xml,**/dbunit-reports/*.xml'  //publish test reports
						try {
							jacoco() //ignore failures
						} catch (e) {
							
						}
					}
				}
				
				
				//sonar analysis only if sonar available!
				def sonarAvailable = false;
				try {					
					def scannerHome = tool 'Sonar Scanner';
					sonarAvailable = true
				} catch (e) {
					//ignore tool not found 
				}
				if (sonarAvailable) {
					stage('SonarQube analysis') {
						//try {
							//JU 3.0 SonarQube Server
							// requires SonarQube Scanner 2.8+
							
							withSonarQubeEnv {
								//sh "${scannerHome}/bin/sonar-scanner"
								def projectPostfix = scmInfo.GIT_BRANCH.replace("/", ":")
								sh "mvn -Dsonar.branch=${scmInfo.GIT_BRANCH} org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
							}
						//} catch (e) {
						
						//}
					}
				}
				
				
				//TODO: only execute when build stable/success!
				if (docker_available) {	
					stage ('test deployment') {
						
					}
				}
				
				/*
					println "download tomcat"
					sh 'mkdir testDeploy'
					sh '''mvn dependency:get -DgroupId=org.apache.tomcat -DartifactId=tomcat -Dversion=7.0.82 -Dpackaging=zip
							mvn dependency:copy -Dartifact=org.apache.tomcat:tomcat:7.0.82:zip -Dmdep.stripVersion=true -DoutputDirectory=./testDeploy'''
							
					//sh 'cd testDeploy'
					sh 'ls -la testDeploy'
					sh 'cd testDeploy; unzip tomcat.zip'
					
					println "download testdb"
					sh '''mvn dependency:get -DgroupId=ch.vorburger.mariaDB4j -DartifactId=mariaDB4j-app -Dversion=2.2.3 ; 
						mvn dependency:copy -DgroupId=ch.vorburger.mariaDB4j -DartifactId=mariaDB4j-app -Dversion=2.2.3  -Dmdep.stripVersion=true  -DoutputDirectory=.
					'''

				
				} */
			} //end dir ('domibusConnector')	
				
			if (RELEASE) {
				stage("INIT Release") {
				   
					echo "releasing to version ${releaseVersion}"
					
					echo "TODO: check correct release version...does not exist yet on nexus/repo!"
				}
				//TODO: if release...start release prepare...
				//increment version number: MAJOR, MINOR, PATCH according to BRANCH NAME or just check?
				 
				stage ("do Release Checks....") {                
					echo "TODO: do tests..."
				}    
					
				
				def deployRelease = "false"
				stage ("REALLY DEPLOY?") {
					input(message: 'Start deploy?', ok: 'Yes') //press abort raises exception
					//parameters: [booleanParam(defaultValue: true, 
					//description: 'If you presse yes, deployment to nexus starts',name: 'Yes?')])
				}
				
				stage ("DEPLOY and TAG Release") {               
						//TODO: do tag release
						echo "STARTING DEPLOY"
				}
				
				stage ("repo cleanup") {
					//TODO: clean up
					//TODO: delete RELEASE branch from repo
					//TODO: merge master back to dev or manual?
					//TODO: merge release into dev with snapshot version?
					
					//switch to master branch    
					//sh "git branch -d ${scmInfo.GIT_BRANCH}" //delete release branch (only local!)
				}                
			}     
						
		 
						
			
				
		} // end withEnv(javaEnv)
	}  //end withEnv(GIT_ENV)
    
}
