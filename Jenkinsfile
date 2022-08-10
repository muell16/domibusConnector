
pipeline {
    agent {
        label "general"
    }

    environment {

    //setup environment for node install
      NODEJS_ORG_MIRROR = "https://centralrepo.brz.gv.at/artifactory/nodejs-dist-mirror"
      SASS_BINARY_SITE = "https://centralrepo.brz.gv.at/artifactory/node-sass-mirror/sass/node-sass/releases/download"
      npm_registry = "https://centralrepo.brz.gv.at/artifactory/api/npm/npm-npmjs/"
      NO_PROXY = "centralrepo.brz.gv.at"
      nodeVersion = "v16.16.0"
      npmVersion = "7.0.0"
      nodeDistro = "linux-x64"
      PATH = "${WORKSPACE}/node/bin:${WORKSPACE}/node_modules/.bin/:$PATH"

      //depndency checks:
      //NVD_CVE_LOCAL_DB_URL = "https://centralrepo.brz.gv.at/artifactory/brz-nvd-cve-local/"
      //NVD_CVE_JSON_MIRROR_URL = "https://centralrepo.brz.gv.at/artifactory/nvd-cve-json-mirror/"
    }

    tools {
        jdk "OpenJDK11"
        maven "Maven 3.8"
    }

    stages {
        stage("checkout") {
          steps {
            checkout scm

						 checkout([  
								$class: 'GitSCM', 
								branches: [[name: 'refs/heads/feature/central-ci']], 
								doGenerateSubmoduleConfigurations: false, 
								extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'pipeline']], 
								submoduleCfg: [], 
								userRemoteConfigs: [[credentialsId: 'ju-eu-ecodex-bitbucket-ssh-key', url: 'ssh://git@git.brz.gv.at:7999/jueuecodex/jenkins-jobs.git']]
        		])

						sh ('ls -la pipeline')

          }
        }

         stage("Install Node & NPM") {
              steps {
                withEnv([]) {
										sh("ls -la /maven-cache")
                    sh("curl -Ok ${NODEJS_ORG_MIRROR}/${nodeVersion}/node-${nodeVersion}-${nodeDistro}.tar.gz")
                    sh("tar -xzf node-${nodeVersion}-${nodeDistro}.tar.gz -C ${WORKSPACE}")
                    sh("rm node-${nodeVersion}-${nodeDistro}.tar.gz")
                    sh("mv ${WORKSPACE}/node-${nodeVersion}-${nodeDistro} ${WORKSPACE}/node")

                    sh '''
                      echo "Using node & npm version: "
                      npm config set registry $npm_registry
                      npm install -g npm@${npmVersion}
                      node --version && npm --version && which node && which npm
                      '''

                }
              }
         }

        stage("mvn install") {
          steps {
						dir ('domibusConnector') {
            withMaven {
//               sh("")
//               sh("mvn versions:set -DnewVersion= ")
              sh("mvn -T2C -Dmaven.test.failure.ignore=true install")
            }
						}
          }

        }

		}
    post {
      always {
        junit '**/target/surefire-reports/*.xml'
      }
    }
}