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
package org.yaml.snakeyaml.issues.issue310;

public class Person {

    private final int id;
    private final String name;
    private final Option<Double> income;

    public Person() {
        this(0, "", Option.valueOf(0.));
    }

    public Person(int id, String name, Option<Double> income) {
        this.id = id;
        this.name = name;
        this.income = income;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Option<Double> getIncome() {
        return income;
    }

}
