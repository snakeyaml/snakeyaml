package org.yaml.snakeyaml.issues.issue9;

import org.springframework.core.style.ToStringCreator;

public class Bean1 implements IBean {

    private String strVal = "BEAN_1";

    private int intVal = 1;

    public Bean1() {
        super();
    }

    public Bean1(int intVal) {
        this.intVal = intVal;
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
