package org.teamdowntime.common

class CleanWorkspace implements Serializable {
    def steps

    CleanWorkspace(steps) {
        this.steps = steps
    }

    def call() {
            steps.echo "Cleaning up workspace"
            steps.cleanWs()
    }
}
