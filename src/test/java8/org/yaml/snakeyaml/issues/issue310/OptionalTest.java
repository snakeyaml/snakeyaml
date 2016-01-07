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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class OptionalTest {

    public static class Salary {

        private Optional<Double> income = Optional.empty();

        public Optional<Double> getIncome() {
            return income;
        }

        public void setIncome(Double income) {
            this.income = Optional.of(income);
        }

        public void setIncome(Optional<Double> income) {
            this.income = income;
        }

        @Override
        public String toString() {
            return "Salary{" + "income=" + income + '}';
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((income == null) ? 0 : income.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Salary other = (Salary) obj;
            if (income == null) {
                if (other.income != null)
                    return false;
            } else if (!income.equals(other.income))
                return false;
            return true;
        }
    }

    public static class Person {

        private String name;

        private Optional<Salary> salary = Optional.empty();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Optional<Salary> getSalary() {
            return salary;
        }

        public void setSalary(Optional<Salary> salary) {
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", salary=" + salary
                    + '}';
        }
    }

    public static class OptionalRepresenter extends Representer {
        public OptionalRepresenter() {
            this.representers.put(Optional.class, new RepresentOptional());
        }

        private class RepresentOptional implements Represent {

            public Node representData(Object data) {
                Optional<?> opt = (Optional<?>) data;
                List<Object> seq = new ArrayList<>(1);
                seq.add(opt.get());
                return representSequence(Tag.SEQ, seq, true);
            }
        }
    }

    @Test
    public void testOptionaStringLoad() {
        final String yamlStr = "name: Neo Anderson\nsalary: [{income: [123456.78]}]\n";
        final Yaml yamlParser = new Yaml(new OptionalRepresenter());
        Person expectedPerson = new Person();
        Salary s = new Salary();
        s.setIncome(123456.78);
        expectedPerson.setName("Neo Anderson");
        expectedPerson.setSalary(Optional.of(s));

        final Person pFromStr = yamlParser.loadAs(yamlStr, Person.class);

        assertEquals(expectedPerson.getName(), pFromStr.getName());
        assertEquals(expectedPerson.getSalary(), pFromStr.getSalary());
    }

    @Test
    public void testOptionalDumpLoad() {
        final Yaml yamlParser = new Yaml(new OptionalRepresenter());
        Person expectedPerson = new Person();
        Salary s = new Salary();
        s.setIncome(123456.78);
        expectedPerson.setName("Neo Anderson");
        expectedPerson.setSalary(Optional.of(s));

        String pDump = yamlParser.dump(expectedPerson);
        // System.out.println(pDump);
        final Person pFromDump = yamlParser.loadAs(pDump, Person.class);

        assertEquals(expectedPerson.getName(), pFromDump.getName());
        assertEquals(expectedPerson.getSalary(), pFromDump.getSalary());
    }
}
