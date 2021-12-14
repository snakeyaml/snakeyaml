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
package org.yaml.snakeyaml.issues.issue310;

import org.hamcrest.Matcher;
import org.junit.internal.matchers.ThrowableCauseMatcher;

public class DeepThrowableCauseMatcher extends ThrowableCauseMatcher<Throwable> {

    public DeepThrowableCauseMatcher(Matcher<? extends Throwable> causeMatcher) {
        super(causeMatcher);
    }

    @Override
    protected boolean matchesSafely(Throwable item) {
        for (Throwable cause = item; cause != null; cause = cause.getCause()) {
            if (super.matchesSafely(cause)) {
                return true;
            }
        }
        return false;
    }
}
