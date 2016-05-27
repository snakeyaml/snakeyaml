/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.issues.issue332;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

public class Data {
    private String label;

    private String unit;

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @ConstructorProperties({"label", "value", "unit"})
    public Data(String label, BigDecimal value, String unit) {
        this.label = label;
        this.value = value;
        this.unit = unit;
    }

//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }
//
//    public void setValue(BigDecimal value) {
//        this.value = value;
//    }

    public String getUnit() {
        return unit;
    }

    public Data() {
    }

    @Override
    public String toString() {
        return "Data{" +
                "label='" + label + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
