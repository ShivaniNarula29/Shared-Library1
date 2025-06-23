
package org.teamdowntimecrew.template.python

class UnitTesting implements Serializable {
    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    void runTestsAndGenerateReports(Map args = [:]) {
        def reportFile = args.get('reportFile', 'unit_test_report.txt')
        def venvDir = 'venv'
            steps.echo "🔧 Setting up Python virtual environment and running unit tests..."

            steps.sh """
                set -e
                python3 -m venv ${venvDir}
                . ${venvDir}/bin/activate
                pip install --upgrade pip
                if [ -f requirements.txt ]; then pip install -r requirements.txt; fi
                pip install pytest
                pytest > ${reportFile} || true
                deactivate
            """

            // Archive report
            steps.archiveArtifacts artifacts: reportFile, allowEmptyArchive: true
       
    }
}
