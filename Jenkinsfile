#!/usr/bin/env groovy
// JENKINS PIPELINE JOB FILE
//
// requirements:
// required plugins: jenkins pipeline plugin, git plugin, clean workspace plugin, configFileProviderPlugin
// required tools: MAVEN 3.5.x, JAVA 8
// loads a external file with name proxy_environment (a list with properties and passes them to maven)
// requires multiple exceptions from scripting sandbox
//
//
//



	List MY_ENV = []
	MY_ENV.add("GIT_SSL_NO_VERIFY=true")


	withEnv(MY_ENV) {

				stage ("Checkout") {
					def commonJob
					def MAVEN_PROJECT_DIR 
					node {
						//sh 'git config credential.helper cache'

						//TODO: set git username password

						scmInfo = checkout([
								$class: 'GitSCM',
								branches: scm.branches,
								doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
								extensions: [[$class: 'CloneOption', noTags: false, shallow: false, depth: 0, reference: '']],
								userRemoteConfigs: scm.userRemoteConfigs,
						])

						//TODO: git push in extra function withCredentials!

						println "scm : ${scmInfo}"
						println "${scmInfo.GIT_COMMIT}"

						sh "rm -Rf pipeline_sources"
						dir('pipeline_sources') { // switch to subdir
							git([url: "https://secure.e-codex.eu/gitblit/r/~spindlers/connector-jenkins-jobs.git", credentialsId: 'IT-NRW GIT Repo'])
						}
					
						MAVEN_PROJECT_DIR = pwd() + "/domibus-connector-plugin"
					  	commonJob = load("pipeline_sources/common-job")

						  commonJob.execute(MAVEN_PROJECT_DIR)
				}


					

				}
		}
