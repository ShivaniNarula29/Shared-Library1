@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.template.java.UnitTesting
import org.teamdowntimecrew.common.CleanWorkspace
import org.teamdowntimecrew.common.Checkout
import org.teamdowntimecrew.common.Notification

node {
    def mvnTool = tool 'maven3.9.9' // Change this to match your configured Maven tool in Jenkins

    env.REPO_URL = 'https://github.com/snaatak-Downtime-Crew/employee-api.git'
    env.CREDENTIAL_ID = 'downtime_github'
    env.BRANCH = 'main'
    env.PRIORITY = 'P0'
    env.SLACK_CHANNEL = 'java-notification'
    env.EMAIL_RECIPIENTS = 'shivaninarula9211@gmail.com'
    env.SLACK_CRED_ID = 'downtime-crew'

    env.PATH = "${mvnTool}/bin:${env.PATH}"

    try {
        stage('Cleanup') {
            def cleaner = new CleanWorkspace(this)
            cleaner.call()
        }

        stage('Checkout Code') {
            def checkoutUtil = new Checkout(this)
            checkoutUtil.checkout(env.BRANCH, env.REPO_URL, env.CREDENTIAL_ID)
        }

        stage('Java Unit Tests & Report') {
            def javaTest = new UnitTesting(this)
            javaTest.runTestsAndGenerateReports()
        }

        currentBuild.result = 'SUCCESS'

        stage('Notify E-Mail/Slack') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'SUCCESS',
                buildTrigger: currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Unknown',
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_RECIPIENTS,
                reportLinks: [
                    [name: 'Java Test Report', url: "${env.BUILD_URL}artifact/target/surefire-reports/index.html"]
                ]
            ])
        }

    } catch (Exception e) {
        currentBuild.result = 'FAILURE'

        stage('Notify E-Mail/Slack') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'FAILURE',
                buildTrigger: currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Unknown',
                failedStage: env.STAGE_NAME ?: 'Unknown',
                failureReason: e.message,
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_RECIPIENTS,
                reportLinks: [
                    [name: 'Java Test Report', url: "${env.BUILD_URL}artifact/target/surefire-reports/index.html"]
                ]
            ])
        }
        throw e
    }
}
