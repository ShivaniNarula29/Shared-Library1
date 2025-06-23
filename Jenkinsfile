@Library('shivani-shared-library@main') _

import org.teamdowntimecrew.common.CleanWorkspace
import org.teamdowntimecrew.common.Checkout
import org.teamdowntimecrew.common.Notification
import org.teamdowntimecrew.template.python.UnitTesting

properties([
    parameters([
        choice(name: 'UNIT_TEST_SCOPE', choices: ['ALL', 'ATTENDANCE', 'NOTIFICATION'], description: 'Select which unit tests to run')
    ])
])

node {
    env.ATTENDANCE_REPO = 'https://github.com/snaatak-Downtime-Crew/attendance-api.git'
    env.NOTIFICATION_REPO = 'https://github.com/snaatak-Downtime-Crew/notification-worker.git'
    env.CREDENTIAL_ID = 'downtime_github'
    env.BRANCH = 'main'
    env.PRIORITY = 'P1'
    env.SLACK_CHANNEL = 'python-notification'
    env.SLACK_CRED_ID = 'downtime-crew'
    env.EMAIL_TO = 'shivani.narula.snaatak@mygurukulam.co'

    def UNIT_TEST_SCOPE = params.UNIT_TEST_SCOPE ?: 'ALL'
    def buildTrigger = ''
    def failedStage = ''
    def failureReason = ''

    try {
        stage('Cleanup') {
            def cleaner = new CleanWorkspace(this)
            cleaner.call()
        }

        stage('Initialize') {
            buildTrigger = currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Auto-triggered'
        }

        stage('Checkout Selected Repositories') {
            def checkoutUtil = new Checkout(this)

            if (UNIT_TEST_SCOPE in ['ALL', 'ATTENDANCE']) {
                dir('attendance-api') {
                    checkoutUtil.checkout(env.BRANCH, env.ATTENDANCE_REPO, env.CREDENTIAL_ID)
                }
            }

            if (UNIT_TEST_SCOPE in ['ALL', 'NOTIFICATION']) {
                dir('notification-worker') {
                    checkoutUtil.checkout(env.BRANCH, env.NOTIFICATION_REPO, env.CREDENTIAL_ID)
                }
            }
        }

        stage('Run Python Unit Tests') {
            def tester = new UnitTesting(this)

            if (UNIT_TEST_SCOPE in ['ALL', 'ATTENDANCE']) {
                dir('attendance-api') {
                    tester.runTestsAndGenerateReports([
                        reportFile: 'unit_test_attendance.txt'
                    ])
                }
            }

            if (UNIT_TEST_SCOPE in ['ALL', 'NOTIFICATION']) {
                dir('notification-worker') {
                    tester.runTestsAndGenerateReports([
                        reportFile: 'unit_test_notification.txt'
                    ])
                }
            }
        }

        currentBuild.result = 'SUCCESS'

        stage('Notify Email / Slack') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'SUCCESS',
                buildTrigger: buildTrigger,
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_TO,
                reportLinks: [
                    [name: 'Attendance Test Report', url: "${env.BUILD_URL}artifact/attendance-api/unit_test_attendance.txt"],
                    [name: 'Notification Test Report', url: "${env.BUILD_URL}artifact/notification-worker/unit_test_notification.txt"]
                ]
            ])
        }

    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        failedStage = env.STAGE_NAME ?: 'Unknown'
        failureReason = e.message

        stage('Notify Email / Slack') {
            def notifier = new Notification(this)
            notifier.call([
                status: 'FAILURE',
                buildTrigger: buildTrigger,
                failedStage: failedStage,
                failureReason: failureReason,
                slackChannel: env.SLACK_CHANNEL,
                slackCredId: env.SLACK_CRED_ID,
                emailTo: env.EMAIL_TO,
                reportLinks: [
                    [name: 'Attendance Test Report', url: "${env.BUILD_URL}artifact/attendance-api/unit_test_attendance.txt"],
                    [name: 'Notification Test Report', url: "${env.BUILD_URL}artifact/notification-worker/unit_test_notification.txt"]
                ]
            ])
        }
        throw e
    }
}
