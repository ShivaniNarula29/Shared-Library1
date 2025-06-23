package org.teamdowntimecrew.template.react

class InstallDependencies implements Serializable {
    def steps

    InstallDependencies(steps) {
        this.steps = steps
    }

    def run() {
        steps.stage('Install Dependencies') {
            steps.sh '''
                npm install --save-dev \\
                jest@26.6.3 \\
                babel-jest@26.6.3 \\
                @testing-library/react@12.1.2 \\
                @testing-library/jest-dom@5.16.5 \\
                @testing-library/user-event@13.5.0 \\
                jest-environment-jsdom@26.6.2 \\
                react-test-renderer@16.14.0 \\
                --legacy-peer-deps
            '''
        }
    }
}
