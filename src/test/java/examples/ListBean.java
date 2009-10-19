/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package examples;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ListBean {
    private List<String> children;
    private String name;
    private List<Developer> developers;

    public ListBean() {
        name = "Bean123";
    }

    public static void main(String[] args) throws Exception {
        for (PropertyDescriptor property : Introspector.getBeanInfo(ListBean.class)
                .getPropertyDescriptors()) {
            System.out.println("Name: " + property.getName());
            System.out.println("Pr type: " + property.getPropertyType());
            System.out.println("Read method: " + property.getReadMethod());
            if (property.getReadMethod().getGenericReturnType() instanceof ParameterizedType) {
                ParameterizedType grt = (ParameterizedType) property.getReadMethod()
                        .getGenericReturnType();
                System.out.println(grt);
                for (Type ata : grt.getActualTypeArguments()) {
                    System.out.println("-> " + ata);
                }
                System.out.println("Raw: " + grt.getRawType());
            } else {
                System.err.println("no: " + property.getName());
            }
            System.out.println();
        }
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }
}
