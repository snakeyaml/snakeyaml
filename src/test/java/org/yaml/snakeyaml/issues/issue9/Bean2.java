/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue9;

import org.springframework.core.style.ToStringCreator;

public class Bean2 implements IBean {

    private String strVal = "BEAN_2";

    private int intVal = 2;

    public Bean2() {
        super();
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append(this.strVal);
        builder.append(this.intVal);
        return builder.toString();
    }

}
