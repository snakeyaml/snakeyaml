/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package examples.spring;

/**
 * @since 2016-06-06
 * @author kibertoad
 *
 */
public class TestEntity {

  // This field is supposed to be set by the means of calling {@link DataRegistry} and affects ID
  // setting
  private transient int counter;

  // This field is supposed to be serialized and deserialized into YAML
  private String id;

  // This field is supposed to be set by the means of calling {@link DataRegistry} and is affected
  // by ID
  private transient String data;


  public String getId() {
    return id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public void setId(String id) {
    this.id = counter + ":" + id;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }

  public int getCounter() {
    return counter;
  }

}
