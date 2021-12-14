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
package org.yaml.snakeyaml.issues.issue142;

import junit.framework.TestCase;

import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class DerivedMapTest extends TestCase {

    public static class Features extends LinkedHashMap<String, Object> {
    }

    public static class MyObject {
	private String name;
	private Map<String, Object> features = new LinkedHashMap<String, Object>();
	private Features moreFeatures = new Features();

	public Map<String, Object> getFeatures() {
	    return features;
	}

	public void setFeatures(Map<String, Object> features) {
	    this.features = features;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public Features getMoreFeatures() {
	    return moreFeatures;
	}

	public void setMoreFeatures(Features moreFeatures) {
	    this.moreFeatures = moreFeatures;
	}

    }

    public void testDerivedMap() {
	MyObject o = new MyObject();
	o.setName("Mickey");
	o.getFeatures().put("Address", "Disney");
	o.getMoreFeatures().put("Address", "Disney");
	Yaml yaml = new Yaml();
	String asYaml = yaml.dump(o);
	//System.out.println(asYaml);
	MyObject o2 = (MyObject) yaml.load(asYaml);
    }

}
