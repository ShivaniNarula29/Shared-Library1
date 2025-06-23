@Library('shared-library@main') _

import org.teamdowntimecrew.template.go.UnitTesting
import org.teamdowntimecrew.common.Cleanworkspace
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
        stage('Clean WorkSpace') {
            steps {
                script {
                    def checkoutUtil = new CleanWorkSpace(this)
                    checkoutUtil.call()
                }
            }
        }


    stages {
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

    post {
        success {
            script {
                def notifier = new Notification(this)
                notifier.send('SUCCESS', env.PRIORITY, env.SLACK_CHANNEL, env.EMAIL_RECIPIENTS)
            }
        }
        failure {
            script {
                def notifier = new Notification(this)
                notifier.send('FAILURE', env.PRIORITY, env.SLACK_CHANNEL, env.EMAIL_RECIPIENTS)
            }
        }
        always {
            archiveArtifacts artifacts: 'coverage-reports/coverage.out, coverage-reports/coverage.html, test-reports/report.xml', fingerprint: true
            echo "Build completed"
            deleteDir()
        }
    }
}
