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
package examples.spring;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a test bean that simulates storing external data that will be used for instantiating beans
 * 
 * @since 2016-06-06
 * @author kibertoad
 *
 */
public class DataRegistry {

    private AtomicInteger counter = new AtomicInteger(1);
    
    /**
     * Generates pseudodata of format "<id>]-<id>"
     * @param id
     * @return
     */
    public String getDataForId (String id) {
        return id +"-"+id;
    }

    /**
     *  Returns next unassigned counter value
     * @return
     */
    public int getNextCounterValue () {
        return counter.getAndIncrement();
    }
}
