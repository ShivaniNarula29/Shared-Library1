package org.teamdowntime.common

class Notification implements Serializable {
    def steps

    notification(steps) {
        this.steps = steps
    }

    def call(Map config = [:]) {
        steps.script {
            def status = config.status
            def buildTrigger = config.buildTrigger
            def failureReason = config.failureReason
            def failedStage = config.failedStage
            def slackChannel = config.slackChannel
            def slackCredId = config.slackCredId
            def emailTo = config.emailTo
            def reportLinks = config.reportLinks ?: []

            def isSuccess = (status == 'SUCCESS')
            def color = isSuccess ? 'good' : 'danger'
            def now = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('Asia/Kolkata'))

            def slackMsg = """*Build #${steps.env.BUILD_NUMBER} - ${status}*
*Job:* `${steps.env.JOB_NAME}`
*Triggered by:* ${buildTrigger}
*Time (IST):* ${now}
${!isSuccess ? "*Failure Reason:* ${failureReason}\n*Failed Stage:* ${failedStage}" : ""}
*Build URL:* <${steps.env.BUILD_URL}|View Build>"""

            if (reportLinks) {
                reportLinks.each { link ->
                    slackMsg += "\n${link.name}: ${link.url}"
                }
            }

            def emailSubject = "${status}: ${steps.env.JOB_NAME} #${steps.env.BUILD_NUMBER}"

            def emailBody = """
<html>
  <body>
    <h2>Jenkins Build Notification: ${status}</h2>
    <p><strong>Job:</strong> ${steps.env.JOB_NAME}</p>
    <p><strong>Build Number:</strong> #${steps.env.BUILD_NUMBER}</p>
    <p><strong>Status:</strong> <span style="color:${isSuccess ? 'green' : 'red'}">${status}</span></p>
    <p><strong>Triggered By:</strong> ${buildTrigger}</p>
    <p><strong>Time (IST):</strong> ${now}</p>
    <p><strong>Build URL:</strong> <a href="${steps.env.BUILD_URL}">${steps.env.BUILD_URL}</a></p>
"""

            if (!isSuccess) {
                emailBody += """
    <p><strong>Failure Reason:</strong> ${failureReason}</p>
    <p><strong>Failed Stage:</strong> ${failedStage}</p>
"""
            }

            if (reportLinks) {
                emailBody += "<h3>Reports:</h3><ul>"
                reportLinks.each { link ->
                    emailBody += "<li><a href='${link.url}'>${link.name}</a></li>"
                }
                emailBody += "</ul>"
            }

            emailBody += "</body></html>"

            steps.mail(to: emailTo, subject: emailSubject, body: emailBody, mimeType: 'text/html')
            steps.slackSend(channel: slackChannel, color: color, message: slackMsg, tokenCredentialId: slackCredId)
        }
    }
}
