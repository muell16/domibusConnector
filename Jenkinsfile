
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
          }
        }

         stage("Install Node & NPM") {
              steps {
                withEnv([]) {
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

//                     withCredentials([usernamePassword(credentialsId: 'your-credential-id', passwordVariable: 'password', usernameVariable: 'username')]) {
//                       sh '''
//                         echo "Using node & npm version: "
//
//                         npm config set registry $npm_registry
//
//                         npm install -g npm@${npmVersion}
//                         node --version && npm --version && which node && which npm
//
//                         npm install -g npm-cli-login
//                         npm-cli-login -u $username -p $password -e "central-ci@brz.gv.at" -r $npm_registry
//                         npm install -g npm-cli-adduser
//                         npm-cli-adduser -u $username -p $password -e "central-ci@brz.gv.at"
//                       '''
//                     }
                }
              }
         }

        stage("mvn install") {
          steps {
            withMaven {
//               sh("")
//               sh("mvn versions:set -DnewVersion= ")
              sh("mvn -U -T1C -Dmaven.test.failure.ignore=true install")
            }
          }

        }

        stage("SonarQube") {
          steps {
            sh("/usr/lib/CloneView/bin/conqat.sh "
                        + "-f de.itestra.conqat.extensions.BackfiredFunctionPoints "
                        + "-p src.dir=${WORKSPACE}/src "
                        + "-p output.dir=${WORKSPACE}/cloneview " // Output directory for CloneView report
                        + "-p include.pattern=**/src/main/**/*.java "
                        + "-p language.name=java")

            withMaven(publisherStrategy: 'EXPLICIT') {
                        sh("mkdir ${WORKSPACE}/dc-nist-db")
                        sh("mvn "
                         + "-Dwagon.url=$NVD_CVE_LOCAL_DB_URL "
                         + "-Dwagon.fromFile=odc.v6.mv.db "
                         + "-Dwagon.toFile=${WORKSPACE}/dc-nist-db/odc.mv.db "
                         + "-DdataDirectory=${WORKSPACE}/dc-nist-db "
                         + "-DautoUpdate=false "
                         + "-Dformat=ALL " // generates report in all formats
                         + "-DassemblyAnalyzerEnabled=false " // disable .NET analysis
                         + "-DretireJsAnalyzerEnabled=false " // disable retireJsAnalyzer (doesn't work yet)
                         + "-DossindexAnalyzerEnabled=false " // needs internet connection
                         + "-DcveUrlModified=$NVD_CVE_JSON_MIRROR_URL/1.1/nvdcve-1.1-modified.json.gz " // Artifactory as CVE DB cache
                         + "-DcveUrlBase=$NVD_CVE_JSON_MIRROR_URL/1.1/nvdcve-1.1-%d.json.gz " // Artifactory as CVE DB cache
                         + "org.codehaus.mojo:wagon-maven-plugin:2.0.0:download-single org.owasp:dependency-check-maven:6.5.3:aggregate" // Download + OWASP check goal
                        )
            }

            // SonarQube Scanner analysis.
            withSonarQubeEnv('SonarQubeServer') {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    withCredentials([string(credentialsId: 	"JUEUMV_SonarQubeToken", variable: 'sonarQubeToken')]) {
                        sh("mvn "
                         + "-Dsonar.projectKey=jueumv:epo-parent "
                         + "-Dsonar.login=$sonarQubeToken "
                         + "-Dsonar.branch.name=$env.BRANCH_NAME "
                         + "-Dcom.itestra.cloneview.enabled=true " // enables CloneView reports
                         + "-Dcom.itestra.cloneview.resultpath=${WORKSPACE}/cloneview " // includes CloneView report in SonarQube
                         + "-Dsonar.dependencyCheck.reportPath=target/dependency-check-report.xml " // includes OWASP check XML report in SonarQube
                         + "-Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html " // includes OWASP check HTML report in SonarQube
                         + "org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar" // sonarqube analysis goal
                        )
                    }
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