package org.teamdowntimecrew.common

class Checkout implements Serializable {
    def steps

    Checkout(steps) {
        this.steps = steps
    }

    def checkout(String branch, String url, String credentialsId) {
            steps.checkout([
                $class: 'GitSCM',
                branches: [[name: "*/${branch}"]],
                userRemoteConfigs: [[
                    url: url,
                    credentialsId: credentialsId
                ]]
            ])
        }
}
