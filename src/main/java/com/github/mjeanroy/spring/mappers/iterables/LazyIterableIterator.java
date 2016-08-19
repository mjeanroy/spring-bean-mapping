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

import com.github.mjeanroy.spring.mappers.ObjectMapper;

import java.util.Iterator;

import static com.github.mjeanroy.spring.mappers.commons.PreConditions.notNull;

/**
 * Iterator that should be used with {@link LazyIterableIterator}.
 *
 * @param <U> Type of objects returned by iterator.
 * @param <T> Type of original objects to map.
 */
class LazyIterableIterator<U, T> implements Iterator<U> {

	/**
	 * Original iterator that will be used to iterate.
	 * Each iterator elements will be automatically mapped to new
	 * objects.
	 */
	private final Iterator<T> iterator;

	/**
	 * Mapper to use to transform elements from original iterator
	 * to new objects.
	 */
	private final ObjectMapper<T, U> mapper;

	/**
	 * Create new iterator.
	 *
	 * @param iterator Original iterator.
	 * @param mapper Mapper.
	 */
	LazyIterableIterator(Iterator<T> iterator, ObjectMapper<T, U> mapper) {
		this.iterator = notNull(iterator, "Original iterator must not be null");
		this.mapper = notNull(mapper, "Mapper must not be null");
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public U next() {
		T next = iterator.next();
		return mapper.convert(next);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
