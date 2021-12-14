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
package org.yaml.snakeyaml.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ArrayUtilsTest {

    @Test
    public void testToUnmodifiableCompositeList() {
        List<Integer> compositeList = ArrayUtils.toUnmodifiableCompositeList(new Integer[]{0, 1, 2}, new Integer[]{3, 4, 5});
        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5), compositeList);
        try {
            compositeList.get(6);
            Assert.fail("An IndexOutOfBoundsException was expected");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals(e.getMessage(), "Index: 6, Size: 6");
        }
    }

    @Test
    public void testToUnmodifiableCompositeEmpty() {
        List<Integer> compositeList = ArrayUtils.toUnmodifiableCompositeList(new Integer[0], new Integer[0]);
        Assert.assertEquals(Collections.emptyList(), compositeList);
    }

    @Test
    public void testToUnmodifiableCompositeLeftEmpty() {
        List<Integer> compositeList = ArrayUtils.toUnmodifiableCompositeList(new Integer[0], new Integer[]{3, 4, 5});
        Assert.assertEquals(Arrays.asList(3, 4, 5), compositeList);
    }

    @Test
    public void testToUnmodifiableCompositeRightEmpty() {
        List<Integer> compositeList = ArrayUtils.toUnmodifiableCompositeList(new Integer[]{1, 2, 3}, new Integer[0]);
        Assert.assertEquals(Arrays.asList(1, 2, 3), compositeList);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToUnmodifiableCompositeException() {
        try {
            ArrayUtils.toUnmodifiableCompositeList(new Integer[]{1}, new Integer[]{2}).get(2);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assert.fail("ArrayIndexOutOfBoundsException wasn't expected, but it was thrown");
        }

    }

}