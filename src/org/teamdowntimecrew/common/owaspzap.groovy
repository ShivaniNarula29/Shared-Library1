package org.teamdowntimecrew.common

class owaspzap implements Serializable {
    def steps

    owaspzap(steps) {
        this.steps = steps
    }

    def call(Map config = [:]) {
        def ZAP_HOME = config.ZAP_HOME
        def ZAP_DIR = config.ZAP_DIR
        def TARGET_URL = config.TARGET_URL
        def ZAP_PORT = config.ZAP_PORT
        def ZAP_REPORT = "${ZAP_DIR}/report.html"

        try {
                steps.echo "Running ZAP scan on ${TARGET_URL} using port ${ZAP_PORT}"
                steps.sh """
                    cd ${ZAP_DIR}
                    chmod +x zap.sh
                    ZAP_HOME=${ZAP_HOME} ./zap.sh -cmd \\
                        -port ${ZAP_PORT} \\
                        -quickurl ${TARGET_URL} \\
                        -quickprogress \\
                        -quickout ${ZAP_REPORT}
                """
            }

            steps.stage('Archive ZAP Report') {
                steps.sh "cp ${ZAP_REPORT} ."
                steps.archiveArtifacts artifacts: 'report.html', allowEmptyArchive: false
            }

            steps.echo "✅ ZAP scan completed successfully."

        } catch (Exception e) {
            steps.echo "❌ ZAP scan failed: ${e.message}"
            steps.currentBuild.result = 'FAILURE'
            throw e
        }
}
