package org.teamdowntimecrew.common

class Checkout {

    static void checkoutRepo(context, Map config = [:]) {
        def repoName = config.repoName ?: error("Missing repoName")
        def branch = config.branch ?: 'main'
        def url = config.url ?: error("Missing Git repo URL")
        def credentialsId = config.credentialsId ?: error("Missing credentialsId")

       context.dir(repoName) {
            context.checkout([
                $class: 'GitSCM',
                branches: [[name: "*/${branch}"]],
                userRemoteConfigs: [[
                    url: url,
                    credentialsId: credentialsId
                ]]
            ])
        }
    }

private static void error(String message) {
    throw new IllegalArgumentException(message)
 }
}
