package org.yaml.snakeyaml;

public class ConstructorOptions {

    private boolean enumCaseSensitive = false;

    public boolean isEnumCaseSensitive() {
        return enumCaseSensitive;
    }

    /**
     * Disables or enables case sensitivity during construct enum constant from string value
     * Default is false.
     *
     * @param enumCaseSensitive - true to set enum case sensitive, false the reverse
     */
    public void setEnumCaseSensitive(boolean enumCaseSensitive) {
        this.enumCaseSensitive = enumCaseSensitive;
    }
}
