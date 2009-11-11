/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package org.yaml.snakeyaml.parser;

import java.util.List;

import org.yaml.snakeyaml.events.Event;

/**
 * This interface represents an input stream of {@link Event Events}.
 * <p>
 * The parser and the scanner form together the 'Parse' step in
 * the loading process (see chapter 3.1 of the 
 * <a href="http://www.yaml.org/spec/1.2/spec.html">YAML Specification</a>).
 * </p>
 * 
 * @see org.yaml.snakeyaml.events.Event
 */
public interface Parser {
	
    /**
     * Check if the next event is one of the given types.
	 *
     * @param choices List of event types.
     * @return <code>true</code>  if the next event can be assigned to a 
     * variable of at least one of the given types. Returns <code>false</code>
     * if no more events are available.
     * @throws ParserException Thrown in case of malformed input.
     */
    public boolean checkEvent(List<Class<? extends Event>> choices);

    /**
     * Check if the next event is assignable to the given type.
     * <p>
     * This is a convenience method to avoid <code>List</code> creation if
     * calling {@link #checkEvent(List)} for
     * a single type.
     * </p>
     * @param choice Event type.
     * @return True if the next event is an instance of <code>type</code>.
     * False if no more events are available.
     * @throws ParserException Thrown in case of malformed input.
     */
    public boolean checkEvent(Class<? extends Event> choice);

    /**
     * Return the next event, but do not delete it from the stream.
     * @return The event that will be returned on the next call to {@link #getEvent}
     * @throws ParserException Thrown in case of malformed input.
     */
    public Event peekEvent();

    /**
     * Returns the next event.
     * <p>The event will be removed from the stream.</p>
     * @throws ParserException Thrown in case of malformed input.
     */
    public Event getEvent();
}
