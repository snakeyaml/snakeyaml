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
package org.yaml.snakeyaml.issues.issue124;

import java.util.ArrayList;
import java.util.List;

public class Bean124 {
    private String a;
    private List<Integer> numbers;

    public Bean124() {
        this.a = "aaa";
        this.numbers = new ArrayList<Integer>(3);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
    }

    public Bean124(String a, List<Integer> numbers) {
        super();
        this.a = a;
        this.numbers = numbers;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}