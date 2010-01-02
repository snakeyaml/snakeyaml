package org.yaml.snakeyaml.issues.issue40;

import java.math.BigDecimal;

public class DogFoodBean {
    BigDecimal decimal;

    public DogFoodBean() {
        decimal = BigDecimal.ZERO;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }
}
