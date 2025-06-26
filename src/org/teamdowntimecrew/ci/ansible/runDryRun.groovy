package org.teamdowntimecrew.ci.ansible

def call(String venvDir, String playbookPath) {
    sh """
        set -e
        . ${venvDir}/bin/activate
        ansible-playbook ${playbookPath} --check
    """
}
return this
