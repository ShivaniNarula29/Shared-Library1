@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.template.go.UnitTesting
import org.teamdowntimecrew.common.CleanWorkspace
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
        SLACK_CRED_ID = 'downtime-crew'
    }

    stages {
        stage('Cleanup') {
            steps {
                script {
                    def cleaner = new CleanWorkspace(this)
                    cleaner.call()
                }
            }
        }

        stage('Checkout Code') {
            steps {
                script {
                    def checkoutUtil = new Checkout(this)
                    checkoutUtil.checkout(env.BRANCH, env.REPO_URL, env.CREDENTIAL_ID)
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
                notifier.call([
                    status: 'SUCCESS',
                    buildTrigger: currentBuild.getBuildCauses()[0].shortDescription,
                    slackChannel: env.SLACK_CHANNEL,
                    slackCredId: env.SLACK_CRED_ID,
                    emailTo: env.EMAIL_RECIPIENTS,
                    reportLinks: [
                        [name: 'Go Test Report', url: "${env.BUILD_URL}artifact/test-reports/test-output.txt"]
                    ]
                ])
            }
        }

        failure {
            script {
                def notifier = new Notification(this)
                notifier.call([
                    status: 'FAILURE',
                    buildTrigger: currentBuild.getBuildCauses()[0].shortDescription,
                    failedStage: env.STAGE_NAME ?: 'Unknown',
                    failureReason: currentBuild.rawBuild.getLog(100).join("\n"),
                    slackChannel: env.SLACK_CHANNEL,
                    slackCredId: env.SLACK_CRED_ID,
                    emailTo: env.EMAIL_RECIPIENTS,
                    reportLinks: [
                        [name: 'Go Test Report', url: "${env.BUILD_URL}artifact/test-reports/test-output.txt"]
                    ]
                ])
            }
        }
    }
}
