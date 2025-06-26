package org.teamdowntimecrew.ci.ansible

def call(String venvDir) {
    sh """
        set -e
        . ${venvDir}/bin/activate
        ansible-lint .
    """
}
return this
