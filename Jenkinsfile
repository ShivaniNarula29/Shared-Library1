@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.template.go.UnitTesting
import org.teamdowntime.common.CleanWorkspace
import org.teamdowntimecrew.common.Checkout
import org.teamdowntimecrew.common.Notification

pipeline {
    agent any

    tools {
        go 'go'
    }

    environment {
        REPO_URL = 'https://github.com/snaatak-Downtime-Crew/employee-api.git'
        CREDENTIAL_ID = 'downtime_github'
        BRANCH = 'main'
        PRIORITY = 'P0'
        SLACK_CHANNEL = 'golang-notification'
        EMAIL_RECIPIENTS = 'shivaninarula9211@gmail.com'
    }

  
         stages {
            stage('Cleanup') {
                steps {
                    script {
                        def cleaner = new CleanWorkspace(this)
                        cleaner.clean()
                    }
                }
            }
        }


        stage('Checkout Code') {
            steps {
                script {
                    def checkoutUtil = new Checkout(this)
                    checkoutUtil.Checkout(env.BRANCH, env.REPO_URL, env.CREDENTIAL_ID)
                }
            }
        }

        stage('Go Unit Testing') {
            steps {
                script {
                    def goTest = new UnitTesting(this)
                    goTest.runTestsAndGenerateReports()
                }
            }
        }
    }
}
