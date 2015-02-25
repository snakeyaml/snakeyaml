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
package org.pyyaml;

public class AnInstance {
    private Object foo;
    private Object bar;

    public AnInstance() {
    }

    public AnInstance(Object foo, Object bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public Object getFoo() {
        return foo;
    }

    public void setFoo(Object foo) {
        this.foo = foo;
    }

    public Object getBar() {
        return bar;
    }

    public void setBar(Object bar) {
        this.bar = bar;
    }
}