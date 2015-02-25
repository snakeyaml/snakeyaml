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
package org.yaml.snakeyaml.constructor;

import java.util.Date;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 */
public class TestBean {
    private String name;
    private int age;
    private Date born;

    public TestBean() {
    }

    public TestBean(final String name, final int age, final Date born) {
        this.name = name;
        this.age = age;
        this.born = born;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }

    public Date getBorn() {
        return born;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setBorn(final Date born) {
        this.born = born;
    }

    public boolean equals(final Object other) {
        boolean ret = this == other;
        if (!ret && other instanceof TestBean) {
            TestBean o = (TestBean) other;
            ret = this.name == null ? o.name == null : this.name.equals(o.name)
                    && this.age == o.age && this.born == null ? o.born == null : this.born
                    .equals(o.born);
        }
        return ret;
    }

    public int hashCode() {
        int val = 3;
        val += 3 * (name == null ? 0 : name.hashCode());
        val += 3 * age;
        val += 3 * (born == null ? 0 : born.hashCode());
        return val;
    }

    public String toString() {
        return "#<org.jvyaml.TestBean name=\"" + name + "\" age=" + age + " born=\"" + born + "\">";
    }
}
