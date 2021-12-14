/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue322_382;

import java.util.ArrayList;
import java.util.List;

public class PublicFooWithPublicFields {
    public List<String> countryCodes = new ArrayList<String>();
    public String some;

    public void setCountryCodes(List<String> countryCodes) {
        for (Object countryCode : countryCodes) {
            System.out.println(countryCode.getClass().getName());
        }
        this.countryCodes = countryCodes;
    }

    public void setSome(String sime) {
        this.some = sime;
    }
}
