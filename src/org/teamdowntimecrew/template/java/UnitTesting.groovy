package org.teamdowntimecrew.template.java

class UnitTesting implements Serializable {
    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    def runTestsAndGenerateReports() {
        steps.stage('Run Java Unit Tests') {
            steps.sh 'mvn clean test'
            steps.junit '**/target/surefire-reports/*.xml'
            steps.archiveArtifacts artifacts: 'target/surefire-reports/**/*.*', fingerprint: true
        }
    }
}
