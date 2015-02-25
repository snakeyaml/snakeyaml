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
package org.yaml.snakeyaml.ruby;

public class TestObject {
    private Sub1 sub1;
    private Sub2 sub2;

    public Sub1 getSub1() {
        return sub1;
    }

    public void setSub1(Sub1 sub1) {
        this.sub1 = sub1;
    }

    public Sub2 getSub2() {
        return sub2;
    }

    public void setSub2(Sub2 sub2) {
        this.sub2 = sub2;
    }
}
