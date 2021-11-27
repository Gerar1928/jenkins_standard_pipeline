def call (Map pipelineParams) {
    pipeline {
        parameters {
            string(name: 'NAME', defaultValue: pipelineParams.name, description: '')
        }
        enviroment {
            REPO = pipelineParams.repo
        }

        agent any

        stages {
            stage('TestWebApp') {
                matrix {
                    
                    agent any 

                    axes {
                        axis {
                            name 'BROWSER'
                            values 'chrome', 'firefox', 'opera', 'safari', 'edge'
                        }
                    }
                }
                stages {
                    stage('Test') {
                        steps {
                            sh "git clone ${env.REPO}"
                            sh "cd ./${params.NAME}"
                            sh "npm install"
                            sh "npx cypress run --browser ${BROWSER}"
                        }
                    }
                }
            }
            post {
                failure {
                    echo "Tests have been failed."
                }
                success {
                    echo "Tests have been successfully passed"
                }
            }
        }
    }
}