@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.common.CleanWorkspace
import org.teamdowntimecrew.common.Notification
import org.teamdowntimecrew.template.react.Dast

node {
    env.ZAP_HOME = "/var/lib/jenkins/.ZAP-CI"
    env.ZAP_DIR = "/var/lib/jenkins/.ZAP-CI"
    env.TARGET_URL = "http://18.223.106.116:3000/"
    env.ZAP_REPORT = "${env.ZAP_DIR}/report.html"
    env.SLACK_CHANNEL = 'react-notification'
    env.EMAIL_RECIPIENTS = 'raiprateek19azm@gmail.com'
    env.SLACK_CRED_ID = 'downtime-crew'

    try {
        stage('Clean Workspace') {
            def cleaner = new CleanWorkspace(this)
            cleaner.call()
        }

        stage('Run React ZAP DAST Scan') {
           def dastRunner = new Dast(this)
            dastRunner.call([
                ZAP_HOME  : env.ZAP_HOME,
                ZAP_DIR   : env.ZAP_DIR,
                TARGET_URL: env.TARGET_URL,
                ZAP_PORT  : '8092'
            ])

        }

        currentBuild.result = 'SUCCESS'

        stage('Notify E-Mail/Slack') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'SUCCESS',
                buildTrigger: currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Manual Trigger',
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_RECIPIENTS,
                reportLinks: [
                    [name: 'ZAP Report', url: "${env.BUILD_URL}artifact/report.html"]
                ]
            ])
        }

    } catch (Exception e) {
        currentBuild.result = 'FAILURE'

        stage('Notify E-Mail/Slack') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'FAILURE',
                buildTrigger: currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Manual Trigger',
                failedStage: env.STAGE_NAME ?: 'Unknown',
                failureReason: e.message,
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_RECIPIENTS,
                reportLinks: [
                    [name: 'ZAP Report', url: "${env.BUILD_URL}artifact/report.html"]
                ]
            ])
        }
        throw e
    } finally {
        stage('Cleanup ZAP Processes') {
            echo '🧹 Cleaning up ZAP processes if needed...'
            sh '''
                ps aux | grep '[z]ap' | awk '{print $2}' | xargs -r sudo kill -9 || true
            '''
        }
    }
}
