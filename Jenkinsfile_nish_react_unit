@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.template.react.InstallDependencies
import org.teamdowntimecrew.template.react.SetupTestConfig
import org.teamdowntimecrew.template.react.UnitTesting
import org.teamdowntimecrew.common.CleanWorkspace
import org.teamdowntimecrew.common.Checkout
import org.teamdowntimecrew.common.Notification

node {
    def nodeHome = tool name: 'NodeJS_16', type: 'nodejs'
    env.PATH = "${nodeHome}/bin:${env.PATH}"

    // Environment Configuration
    env.REPO_URL = 'https://github.com/OT-MICROSERVICES/frontend.git'
    env.BRANCH = 'main'
    env.SLACK_CHANNEL = 'react-notification'
    env.EMAIL_RECIPIENTS = 'shivaninarula9211@gmail.com'
    env.SLACK_CRED_ID = 'downtime-crew'

    try {
        stage('Cleanup') {
            def cleaner = new CleanWorkspace(this)
            cleaner.call()
        }

        stage('Checkout Code') {
            def checkoutUtil = new Checkout(this)
            checkoutUtil.checkout(env.BRANCH, env.REPO_URL, null)
        }

        stage('Install Dependencies') {
            def deps = new InstallDependencies(this)
            deps.run()
        }

        stage('Setup Test Configuration') {
            def setup = new SetupTestConfig(this)
            setup.run()
        }

        stage('Run Unit Tests & Archive Report') {
            def runner = new UnitTesting(this)
            runner.run()
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
                    [name: 'Jest Test Report', url: "${env.BUILD_URL}artifact/test-reports/test-results.json"]
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
                    [name: 'Jest Test Report', url: "${env.BUILD_URL}artifact/test-reports/test-results.json"]
                ]
            ])
        }
        throw e
    }
}
