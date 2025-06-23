package org.teamdowntimecrew.frontend

class UnitTesting implements Serializable {
    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    def run() {
            try {
                steps.sh '''
                    mkdir -p test-reports
                    npm test
                    echo "Generated reports:"
                    ls -la test-reports/
                '''
            } finally {
                steps.archiveArtifacts artifacts: 'test-reports/**/*', allowEmptyArchive: true
            }
        }
    }
}
