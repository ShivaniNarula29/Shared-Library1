package org.teamdowntimecrew.ci.ansible

def installDependencies(String workspace, String venvDir) {
    script.sh """
    set -e
    cd ${workspace}

    if [ ! -d "${venvDir}" ]; then
        echo "[INFO] Creating Python virtual environment at ${venvDir}..."
        python3 -m venv ${venvDir}
    else
        echo "[INFO] Using existing virtual environment at ${venvDir}."
    fi

    source ${venvDir}/bin/activate
    pip install --upgrade pip ansible ansible-lint yamllint gitleaks

    echo "[INFO] Versions:"
    ansible-lint --version
    yamllint --version
    gitleaks version
    """
}

return this
