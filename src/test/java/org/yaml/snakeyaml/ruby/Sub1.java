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

import java.util.List;

public class Sub1 {
    private List<Integer> att1;
    private int att2;
    private List<Integer> att3;

    public List<Integer> getAtt1() {
        return att1;
    }

    public void setAtt1(List<Integer> att1) {
        this.att1 = att1;
    }

    public int getAtt2() {
        return att2;
    }

    public void setAtt2(int att2) {
        this.att2 = att2;
    }

    public List<Integer> getAtt3() {
        return att3;
    }

    public void setAtt3(List<Integer> att3) {
        this.att3 = att3;
    }
}
