/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.mjeanroy.spring.mappers.iterables;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Iterables static utilities.
 */
public final class Iterables {

	private Iterables() {
	}

	/**
	 * Create new list map iterables elements.
	 *
	 * @param iterables Iterables.
	 * @param <T> Type of iterable elements.
	 * @return List.
	 */
	public static <T> List<T> toList(Iterable<T> iterables) {
		if (iterables instanceof Collection) {
			Collection<T> c = (Collection<T>) iterables;
			return new LinkedList<>(c);
		}

		List<T> list = new LinkedList<>();
		for (T current : iterables) {
			list.add(current);
		}

		return list;
	}

	/**
	 * Create new set map iterables elements.
	 * Set will be a {@link java.util.LinkedHashSet} implementation.
	 *
	 * @param iterables Iterables.
	 * @param <T> Type if iterable elements.
	 * @return Set.
	 */
	public static <T> Set<T> toSet(Iterable<T> iterables) {
		if (iterables instanceof Collection) {
			Collection<T> collection = (Collection<T>) iterables;
			return new LinkedHashSet<>(collection);
		}

		LinkedHashSet<T> linkedList = new LinkedHashSet<>();
		for (T current : iterables) {
			linkedList.add(current);
		}

		return linkedList;
	}

	/**
	 * Compute size of iterable structure.
	 * If structure is an instance of Collection, operation is
	 * just the result of {@link java.util.Collection#size()}.
	 * Otherwise, operation need to loop over all iterable elements.
	 *
	 * @param iterables Iterable structure.
	 * @return Size of iterable structure.
	 */
	public static int size(Iterable iterables) {
		if (iterables instanceof Collection) {
			return ((Collection) iterables).size();
		}

		int size = 0;
		for (Object current : iterables) {
			size++;
		}

		return size;
	}
}
