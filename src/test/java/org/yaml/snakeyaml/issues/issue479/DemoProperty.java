package org.yaml.snakeyaml.issues.issue479;

import java.util.Map;

public class DemoProperty {
    private Map<String, CredentialAppConfig> system;

    public Map<String, CredentialAppConfig> getSystem() {
        return system;
    }

    public void setSystem(Map<String, CredentialAppConfig> system) {
        this.system = system;
    }
}
