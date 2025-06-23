package org.teamdowntimecrew.template.go

class UnitTesting implements Serializable {
    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    def runTestsAndGenerateReports() {
        steps.stage('Run Tests and Generate Reports') {
            steps.sh '''
                sh 'mkdir -p test-reports'
                sh 'go test ./... -run=^$ -skip=./api -v > test-reports/test-output.txt || true'
            '''
        }
    }
}
