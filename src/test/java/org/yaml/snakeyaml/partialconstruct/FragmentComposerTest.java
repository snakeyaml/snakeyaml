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
package org.yaml.snakeyaml.partialconstruct;

import junit.framework.TestCase;

import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.resolver.Resolver;

public class FragmentComposerTest extends TestCase {

    public void testFragment() {
        String document = "foo:  blargle\n"
                + "developer:  { name: \"Bjarne Stroustrup\", language: \"C++\"}\n"
                + "gee:  [ \"whiz\", \"bang\"]\n";//

        StreamReader reader = new StreamReader(document);
        Composer composer = new FragmentComposer(new ParserImpl(reader), new Resolver(),
                "developer");
        Constructor constructor = new Constructor();
        constructor.setComposer(composer);
        DeveloperBean developer = (DeveloperBean) constructor.getSingleData(DeveloperBean.class);
        assertEquals("Bjarne Stroustrup", developer.name);
        assertEquals("C++", developer.language);
    }
}
