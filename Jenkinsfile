node {
   
    stage ("Checkout") {
        scmInfo = checkout scm
 
        echo "scm : ${scmInfo}"
        echo "${scmInfo.GIT_COMMIT}"
    }
        
    String jdktool = tool name: "JAVA 8", type: 'hudson.model.JDK'
    def mvnHome = tool name: 'MAVEN 3.0.5'
 
    /* Set JAVA_HOME, and special PATH variables. */
    List javaEnv = [
        "PATH+MVN=${jdktool}/bin:${mvnHome}/bin",
        "M2_HOME=${mvnHome}",
        "JAVA_HOME=${jdktool}",
    ]

    
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
                                            
        //TODO: always clean up workspace!
    }
    
	//FAILURE, SUCCESS, UNSTABLE
            
    dir ('domibusConnector') {                    
		stage ('Build') {
			sh 'mvn compile'
		}
        stage ('Test') {
            try {
                sh 'mvn test'
            } catch (e) {
                currentBuild.result = 'UNSTABLE'
            }
        }
		stage ('Integration Test') {
			try {
				sh 'docker ps'
				sh 'mvn -P integration-testing verify'
			} catch (e) {
				currentBuild.result = 'UNSTABLE'
			}
		}
		
        stage ('Post') {
            if (currentBuild.result == null || currentBuild.result != 'FAILURE') {
                junit '**/surefire-reports/*.xml,**/failsafe-reports/*.xml'  //publish test reports
            }
        }
		
        
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
                
 
                
        }
        
    }
    
}
