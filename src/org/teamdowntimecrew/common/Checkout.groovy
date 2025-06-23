package org.cloudninja.application.generic

class Checkout implements Serializable {
    def steps

    Checkout(steps) {
        this.steps = steps
    }

    def fromGit(String branch = 'main', String repoUrl = '', String credentialsId = '') {
        steps.stage('Checkout') {
            steps.echo "Checking out branch '${branch}' from '${repoUrl}'"
            steps.checkout([
                $class: 'GitSCM',
                branches: [[name: "*/${branch}"]],
                userRemoteConfigs: [[url: repoUrl, credentialsId: credentialsId]]
            ])
        }
    }
}
