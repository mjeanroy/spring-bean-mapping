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

package com.github.mjeanroy.spring.bean.mapping.iterables;

import java.util.Iterator;

import com.github.mjeanroy.spring.bean.mapping.objects.ObjectMapper;

/**
 * Iterable implementation that map elements from original iterables structure
 * to new iterable element during iteration.
 *
 * This iterable can be considered as a lazy iterable implement since it
 * does not map all elements at instantiation but on only during iteration.
 *
 * @param <U> Type of iterable elements.
 * @param <T> Type of original iterable elements.
 */
public class LazyIterableMapper<U, T> implements Iterable<U> {

	/**
	 * Original iterable structure.
	 * Each elements will be automatically mapped during iteration.
	 */
	private final Iterable<T> from;

	/**
	 * Mapper used to map original iterable structure to new elements.
	 */
	private final ObjectMapper<T, U> mapper;

	/**
	 * Create new lazy iterable.
	 *
	 * @param from Original iterable structure containing elements to map.
	 * @param mapper Mapper that will be used to map original objects to new objects.
	 */
	public LazyIterableMapper(Iterable<T> from, ObjectMapper<T, U> mapper) {
		this.from = from;
		this.mapper = mapper;
	}

	@Override
	public Iterator<U> iterator() {
		return new LazyIterableIterator<U, T>(from.iterator(), mapper);
	}
}
