package org.teamdowntime.common

class CleanWorkspace implements Serializable {
    def steps

    CleanWorkspace(steps) {
        this.steps = steps
    }

    def clean() {
        steps.stage('Clean Workspace') {
            steps.echo "Cleaning workspace"
            steps.cleanWs()
        }
    }
}

