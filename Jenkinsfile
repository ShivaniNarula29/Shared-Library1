@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.template.go.UnitTesting
import org.teamdowntimecrew.common.CleanWorkspace
import org.teamdowntimecrew.common.Checkout
import org.teamdowntimecrew.common.Notification

node {
    def goHome = tool 'go'

    env.REPO_URL = 'https://github.com/snaatak-Downtime-Crew/employee-api.git'
    env.CREDENTIAL_ID = 'downtime_github'
    env.BRANCH = 'main'
    env.PRIORITY = 'P0'
    env.SLACK_CHANNEL = 'golang-notification'
    env.EMAIL_RECIPIENTS = 'shivaninarula9211@gmail.com'
    env.SLACK_CRED_ID = 'downtime-crew'

    env.PATH = "${goHome}/bin:${env.PATH}"

    try {
        stage('Cleanup') {
            def cleaner = new CleanWorkspace(this)
            cleaner.call()
        }

        stage('Checkout Code') {
            def checkoutUtil = new Checkout(this)
            checkoutUtil.checkout(env.BRANCH, env.REPO_URL, env.CREDENTIAL_ID)
        }

        stage('Go Unit Tests & Report') {
            def goTest = new UnitTesting(this)
            goTest.runTestsAndGenerateReports()
        }

        currentBuild.result = 'SUCCESS'

        stage('Notify Success') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'SUCCESS',
                buildTrigger: currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Unknown',
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_RECIPIENTS,
                reportLinks: [
                    [name: 'Go Test Report', url: "${env.BUILD_URL}artifact/test-reports/test-output.txt"]
                ]
            ])
        }

    } catch (Exception e) {
        currentBuild.result = 'FAILURE'

        stage('Notify Failure') {
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
                    [name: 'Go Test Report', url: "${env.BUILD_URL}artifact/test-reports/test-output.txt"]
                ]
            ])
        }
        throw e
    }
}
