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

package com.github.mjeanroy.spring.bean.mapping.objects;

import java.util.ArrayList;
import java.util.Collection;

import com.github.mjeanroy.spring.bean.mapping.Mapper;
import com.github.mjeanroy.spring.bean.mapping.factory.BeanFactory;

/**
 * In memory mapper implementation.
 * <p/>
 * This implementation returns iterable of destination objects that will be
 * stored in a collection. It means that destination object are mapped when
 * the iterable is returned.
 * <p/>
 * Iterable can be explicitly cast to a collection.
 *
 * @param <T> Type of source objects.
 * @param <U> Type of destination objects.
 */
public class InMemoryObjectMapper<T, U> extends AbstractObjectMapper<T, U> implements ObjectMapper<T, U> {

	/**
	 * Create new in memory mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 */
	protected InMemoryObjectMapper(Mapper mapper) {
		super(mapper);
	}

	/**
	 * Create new in memory mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 */
	protected InMemoryObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		super(mapper, klassT, klassU);
	}

	/**
	 * Create new in memory mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 * @param factory Factory used to instantiate destination object.
	 */
	protected InMemoryObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, BeanFactory<U> factory) {
		super(mapper, klassT, klassU, factory);
	}

	@Override
	public Collection<U> from(Iterable<T> sources) {
		final Collection<U> results = initIterable(sources);
		for (T source : sources) {
			final U destination = from(source);
			results.add(destination);
		}
		return results;
	}

	/**
	 * Init iterable collection object.
	 * By default, this method create a new array list.
	 * Initial capacity of array list is defined by {@link #initialCapacity(Iterable)} method.
	 *
	 * Collection returned must be empty (since it will be filled later).
	 * Source object can be used to compute target collection size.
	 *
	 * @param sources Source objects.
	 * @return Empty destination collection.
	 */
	protected Collection<U> initIterable(Iterable<T> sources) {
		final int size = initialCapacity(sources);
		return new ArrayList<U>(size);
	}

	/**
	 * Try to compute initial capacity of destination collections from
	 * source collection.
	 *
	 * Default is:
	 * - If iterable parameter is an instance of collection, {@link java.util.Collection#size()} is used.
	 * - Otherwise 10 is returned.
	 *
	 * @param iterables Source objects.
	 * @return Initial capacity of destination.
	 */
	protected int initialCapacity(Iterable<T> iterables) {
		final int size;

		if (iterables instanceof Collection) {
			return ((Collection) iterables).size();
		}
		else {
			size = 10;
		}

		return size;
	}
}
