/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

public class WrongJavaBean {
    String packageField;
    static String staticField;
    public transient String dynamo;
    public String publicField;
    private int privateValue;

    public WrongJavaBean() {
        method();
    }

    private void method() {
        privateValue++;
    }

    @Override
    public String toString() {
        return "WrongJavaBean";
    }
}
