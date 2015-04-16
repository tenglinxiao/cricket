/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership.
//
// Julian Hyde licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package org.olap4j.impl;

import java.util.*;

/**
 * Unmodifiable list backed by an array.
 *
 * <p>The traditional solution to this problem is to call
 * {@link java.util.Arrays#asList(Object[])} followed by
 * {@link java.util.Collections#unmodifiableList(java.util.List)}, but this
 * class has one fewer wrapper object, saving space and indirection effort.
 *
 * @author jhyde
 * @since May 7, 2009
 */
public class UnmodifiableArrayList<T>
    extends AbstractList<T>
    implements List<T>
{
    private final T[] elements;

    /**
     * Creates an UnmodifiableArrayList.
     *
     * <p>Does not create a copy of the array. Future changes to the array will
     * be reflected in the contents of the list.
     *
     * @param elements Array
     */
    public UnmodifiableArrayList(T... elements) {
        this.elements = elements;
    }

    public T get(int index) {
        return elements[index];
    }

    public int size() {
        return elements.length;
    }

    /**
     * Creates an unmodifable list as a shallow copy of an array.
     *
     * <p>Future changes to the array will not be reflected in the contents
     * of the list.
     *
     * @param elements Elements of list
     * @param <T> Type of elements
     * @return Unmodifiable list with same contents that the array had at call
     * time
     */
    public static <T> UnmodifiableArrayList<T> asCopyOf(T... elements) {
        return new UnmodifiableArrayList<T>(elements.clone());
    }

    /**
     * Creates an unmodifable list as a shallow copy of a collection.
     *
     * <p>Future changes to the collection will not be reflected in the contents
     * of the list.
     *
     * @param collection Elements of list
     * @param <T> Type of elements
     * @return Unmodifiable list with same contents that the collection had at
     * call time
     */
    public static <T> UnmodifiableArrayList<T> of(
        Collection<? extends T> collection)
    {
        //noinspection unchecked
        return new UnmodifiableArrayList<T>(
            (T[]) collection.toArray());
    }
}

// End UnmodifiableArrayList.java
