package org.teamdowntimecrew.template.go

class UnitTesting implements Serializable {
    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    def runTestsAndGenerateReports() {
            steps.sh '''
                mkdir -p test-reports
                go test ./... -run=^$ -skip=./api -v > test-reports/test-output.txt || true
            '''
            steps.archiveArtifacts artifacts: 'test-reports/test-output.txt', fingerprint: true
    }
}
