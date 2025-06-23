package org.teamdowntimecrew.common

class GitClone {
    
    static void cloneRepo(context, Map config = [:]) {
        def repoName = config.repoName ?: error("Missing repoName")
        def branch = config.branch ?: 'main'
        def url = config.url ?: error("Missing Git repo URL")
        def credentialsId = config.credentialsId ?: error("Missing credentialsId")

        context.dir(repoName) {
            context.git branch: branch, credentialsId: credentialsId, url: url
        }
    }

    private static void error(String message) {
        throw new IllegalArgumentException(message)
    }
}
