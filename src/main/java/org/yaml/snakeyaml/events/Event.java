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
package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

/**
 * Basic unit of output from a {@link org.yaml.snakeyaml.parser.Parser} or input of a
 * {@link org.yaml.snakeyaml.emitter.Emitter}.
 */
public abstract class Event {

  /**
   * Event types
   */
  public enum ID {
    Alias, Comment, DocumentEnd, DocumentStart, MappingEnd, MappingStart, Scalar, SequenceEnd, SequenceStart, StreamEnd, StreamStart
  }

  private final Mark startMark;
  private final Mark endMark;

  /**
   * Create
   *
   * @param startMark - start
   * @param endMark - end
   */
  public Event(Mark startMark, Mark endMark) {
    this.startMark = startMark;
    this.endMark = endMark;
  }

  public String toString() {
    return "<" + this.getClass().getName() + "(" + getArguments() + ")>";
  }

  /**
   * getter
   *
   * @return start
   */
  public Mark getStartMark() {
    return startMark;
  }

  /**
   * getter
   *
   * @return end
   */
  public Mark getEndMark() {
    return endMark;
  }

  /**
   * Generate human readable representation of the Event
   *
   * @see "__repr__ for Event in PyYAML"
   * @return representation fore humans
   */
  protected String getArguments() {
    return "";
  }

  /**
   * Check if the Event is of the provided kind
   *
   * @param id - the Event.ID enum
   * @return true then this Event of the provided type
   */
  public boolean is(Event.ID id) {
    return getEventId() == id;
  }

  /**
   * Get the type (kind) if this Event
   *
   * @return the ID of this Event
   */
  public abstract Event.ID getEventId();

  /*
   * for tests only
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Event) {
      return toString().equals(obj.toString());
    } else {
      return false;
    }
  }

  /*
   * for tests only
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
