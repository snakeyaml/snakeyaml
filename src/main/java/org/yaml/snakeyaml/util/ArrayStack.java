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
package org.yaml.snakeyaml.util;

import java.util.ArrayList;

/**
 * Custom stack
 *
 * @param <T> data to keep in stack
 */
public class ArrayStack<T> {

  private final ArrayList<T> stack;

  /**
   * Create
   *
   * @param initSize - book the size
   */
  public ArrayStack(int initSize) {
    stack = new ArrayList<T>(initSize);
  }

  /**
   * Add the element to the head
   *
   * @param obj - data to be added
   */
  public void push(T obj) {
    stack.add(obj);
  }

  /**
   * Get the head and remove it from the stack
   *
   * @return the head
   */
  public T pop() {
    return stack.remove(stack.size() - 1);
  }

  /**
   * Check
   *
   * @return true when it contains nothing
   */
  public boolean isEmpty() {
    return stack.isEmpty();
  }

  /**
   * remove all items in the stack
   */
  public void clear() {
    stack.clear();
  }
}
