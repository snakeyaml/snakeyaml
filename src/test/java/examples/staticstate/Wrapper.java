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
package examples.staticstate;

public class Wrapper {
    private String name;
    private int age;
    private String color;
    private String type;

    public JavaBeanWithStaticState createBean() {
        JavaBeanWithStaticState bean = new JavaBeanWithStaticState();
        bean.setAge(age);
        bean.setName(name);
        JavaBeanWithStaticState.color = color;
        JavaBeanWithStaticState.setType(type);
        return bean;
    }

    public Wrapper() {
        color = JavaBeanWithStaticState.color;
        type = JavaBeanWithStaticState.getType();
    }

    public Wrapper(JavaBeanWithStaticState bean) {
        this();
        name = bean.getName();
        age = bean.getAge();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}