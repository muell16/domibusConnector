//
// JENKINS PIPELINE JOB FILE
//
// requirements: 
// required plugins: jenkins pipeline plugin, git plugin, clean workspace plugin, configFileProviderPlugin
// required tools: MAVEN 3.3.x, JAVA 8
// loads a external file with name proxy_environment (a list with properties and passes them to maven)
// requires multiple exceptions from scripting sandbox
//
//
//

node {

		def mavenProperties = "";
		List MY_ENV = []
		try {
			configFileProvider([configFile(fileId: 'proxy_environment', variable: 'PROXY_ENV')]) {
				
										
				def lines = new File(PROXY_ENV)
				lines.each { line ->		
					echo "adding ${line} to env"
					MY_ENV.add(line)
					mavenProperties = mavenProperties + " -D${line}"
				}
				mavenProperties = mavenProperties + " "
				println "maven properties are: ${mavenProperties}"
				
			} //END CONFIG FILE PROVIDER PROXY_ENV
		} catch(e) {
			//do nothing if file not found...
		}
   
	
				
		MY_ENV.add("GIT_SSL_NO_VERIFY=true")
		
		echo "env: ${MY_ENV}"

		withEnv(MY_ENV) {
			stage ("Checkout") {
				cleanWs()
				scmInfo = checkout([
						$class: 'GitSCM',
						branches: scm.branches,
						doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
						extensions: [[$class: 'CloneOption', noTags: false, shallow: false, depth: 0, reference: '']],
						userRemoteConfigs: scm.userRemoteConfigs,
				])
		 
				echo "scm : ${scmInfo}"
				echo "${scmInfo.GIT_COMMIT}"
				
			}
					
				
			String jdktool = tool name: "JAVA 8", type: 'hudson.model.JDK'
			def mvnHome
			try {
				mvnHome = tool name: 'MAVEN 3.3.x'
			} catch (e) {			
				mvnHome = tool name: 'MAVEN'			
			}
		 
			//create a function mvn with maven properties appended on mvn call
			def mvn = { arg ->
				sh "mvn ${mavenProperties} ${arg}"
			}
		 
			/* Set JAVA_HOME, and special PATH variables. */
			List javaEnv = [
				"PATH+MVN=${jdktool}/bin:${mvnHome}/bin",
				"M2_HOME=${mvnHome}",
				"JAVA_HOME=${jdktool}",
			]

			def boolean docker_available = false
			
			withEnv(javaEnv) {
				def RELEASE = false
				def HOTFIX = false			
				def releaseVersion = ""
				def hotfixVersion = ""
				def buildShouldFail = false
				def buildShouldUnstable = false
				def versionName = ""
				stage ('Initialize') {
					sh '''
						echo "PATH = ${PATH}"
						echo "M2_HOME = ${M2_HOME}"   
						echo "HOTFIX = ${HOTFIX}"
						echo "RELEASE = ${RELEASE}"	
					    env
					'''
					mvn '-v'
					
					//check if its an release branch
					if ( scmInfo.GIT_BRANCH.startsWith("origin/release/") ) {
						RELEASE = true
						versionName = scmInfo.GIT_BRANCH[15..-1]
					}
					if (scmInfo.GIT_BRANCH.startsWith("origin/hotfix/")) {
						HOTFIX = true
						versionName = scmInfo.GIT_BRANCH[14..-1]
					}
					if (versionName[0] == "v") {
						versionName = versionName[1..-1]
					}
							
					try {
						docker_available = true
						sh 'docker ps'
					} catch (e) {
						docker_available = false
					}							
				}
				
						
				//chdir to project directory and execute maven tasks					
				dir ('domibusConnector') {
					if (RELEASE || HOTFIX ) {
						stage("INIT Release / Hotfix") {
							versionName = versionName.split("_")[0] //split branch version name from additional text 
							//(major, minor, patch) = versionName.tokenize(".")
							
							releaseVersion = versionName
							hotfixVersion = versionName

							println("setting Version to ${versionName}-SNAPSHOT")
							mvn "build-helper:parse-version versions:set -DnewVersion=${releaseVersion}-SNAPSHOT"
														
						}
					}
				
				
				
					//run build install - there are side effects if running multiple jobs, because they all use the same local maven repo
					stage ('Build') {						
						mvn '-DskipTests=true clean install'
					}
					

					try {
						stage ('Test') {
							//sh 'mvn -fn test' //ignore test failures to execute ALL tests and fail never
							//sh 'mvn test' //execute tests again to get failure: TODO find better solution to fail this step if test failures occure
							//using failsafe plugin for unitTests to run all unit tests and fail afterwards
							//sh '''mvn -DfailIfNoTests=false -Dit.test="**/*Test.java" -D'reportsDirectory=${project.build.directory}/test-reports' failsafe:integration-test'''
							//sh '''mvn -DfailIfNoTests=false -Dit.test="**/*Test.java" -D'reportsDirectory=${project.build.directory}/test-reports' failsafe:verify'''
							mvn '''-DfailIfNoTests=false -Punit-testing-with-failsafe failsafe:integration-test'''
							mvn '''-DfailIfNoTests=false -Dunit-testing-with-failsafe failsafe:verify'''

						
						}
					} catch (e) {
						//currentBuild.result = 'FAILURE'
						buildShouldFail = true
					}
					
						
					try {					
						//-dmaven.test.failure.ignore=true
						stage ('Integration Test') {
							mvn '-P integration-testing,dbunit-testing verify'
							mvn '-P integration-testing,dbunit-testing failsafe:verify' //verify executed tests
						}
					} catch (e) {
						buildShouldUnstable = true
					} 
					
					stage ('Post') {
						try {
							junit '**/test-reports/*.xml,**/surefire-reports/*.xml,**/failsafe-reports/*.xml,**/dbunit-reports/*.xml'  //publish test reports
						} catch(e) {}
						try {
							jacoco() 
						} catch(e) {}
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
								sh "mvn test" //run unit tests again so sonar can detect them
								withSonarQubeEnv {
									//sh "${scannerHome}/bin/sonar-scanner"
									def projectPostfix = scmInfo.GIT_BRANCH.replace("/", ":")
									mvn "-Dsonar.branch=${scmInfo.GIT_BRANCH} org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
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
				
					if (buildShouldUnstable) {
						currentBuild.result = 'UNSTABLE'
					}
					if (buildShouldFail) {
						currentBuild.result = 'FAILURE'
					}
					
					
					if (!buildShouldFail && RELEASE || HOTFIX )  {

						//TODO: if release...start release prepare...
						//increment version number: MAJOR, MINOR, PATCH according to BRANCH NAME or just check?
						 
						//stage ("do Release Checks....") {                
						//	echo "TODO: do tests..."
						//	TODO: check if this version does not exist yet in nexus repo
						//}    
						
						if (buildShouldUnstable) {
							releaseVersion = releaseVersion + "-UNSTABLE"
						}
							
						
						def deployRelease = "false"
						stage ("REALLY DEPLOY?") {
							timeout(time: 15, unit: 'MINUTES') {
								input(message: "Start tag, deploy for version: ${releaseVersion}?", ok: 'Yes') //press abort raises exception
								//parameters: [booleanParam(defaultValue: true, 
								//description: 'If you press yes, deployment to nexus starts',name: 'Yes?')])
							}
						}
						
						stage ("DEPLOY and TAG Release") {    
							println "CHANGING VERSION NUMBER"
							mvn "build-helper:parse-version versions:set -DnewVersion=${releaseVersion}"
							
							echo "STARTING DEPLOY"
							mvn "clean deploy"
							
							echo "TAGGING REPOSITORY"						
							sh 'git tag v${releaseVersion}'						
							sh 'git push --tags'
												
						}
						
						stage ("repo cleanup") {
							//TODO: clean up
							//TODO: delete RELEASE / HOTFIX branch from repo
							//TODO: merge RELEASE / HOTFIX Branch back to master?
							//TODO: merge release into dev with snapshot version or manual?
							
							//switch to master branch    
							//sh "git branch -d ${scmInfo.GIT_BRANCH}" //delete release branch (only local!)

							if (RELEASE || HOTFIX) {
								
								println("merge feature branch into master branch and push")
								sh "git checkout master"
								sh "git merge --ff-only ${scmInfo.GIT_BRANCH}"								
								sh "git push"
								
								println("merge feature branch into development branch and push")
								sh "git checkout development"
								sh "git merge --ff-only ${scmInfo.GIT_BRANCH}"								
								sh "git push"
								
								println("delete old feature branch in remote repo")
								sh "git push origin :${scmInfo.GIT_BRANCH}"
							}
							
						}                
					}     
							
							
				} //end dir ('domibusConnector')	
										
					
			} // end withEnv(javaEnv)
		}  //end withEnv(GIT_ENV)
	
	
	
    
}
