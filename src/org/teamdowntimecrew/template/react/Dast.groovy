package org.teamdowntimecrew.template.react

import org.teamdowntimecrew.common.owaspzap 

class Dast implements Serializable {
    def script

    Dast(script) {
        this.script = script
    }

    def call(Map config = [:]) {
        if (!config.TARGET_URL || !config.ZAP_HOME || !config.ZAP_DIR || !config.ZAP_PORT) {
            throw new IllegalArgumentException("Missing required ZAP configuration: TARGET_URL, ZAP_HOME, ZAP_DIR, ZAP_PORT")
        }

        def zapRunner = new owaspzap(script)
        zapRunner.call(config)
    }
}
