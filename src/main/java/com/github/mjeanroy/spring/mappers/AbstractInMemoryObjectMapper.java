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

package com.github.mjeanroy.spring.mappers;

import com.github.mjeanroy.spring.mappers.factory.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * In memory mapper implementation.
 *
 * This implementation returns iterable of destination objects that will be
 * stored in a collection. It means that destination object are mapped when
 * the iterable is returned.
 *
 * Iterable can be explicitly cast to a collection.
 *
 * This class is an abstract class because it needs to be sub-classed to be able to use
 * constructor without generic types.
 *
 * @param <T> Type of source objects.
 * @param <U> Type of destination objects.
 */
public abstract class AbstractInMemoryObjectMapper<T, U> extends AbstractObjectMapper<T, U> implements ObjectMapper<T, U> {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractInMemoryObjectMapper.class);

	/**
	 * Create new in memory mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 */
	protected AbstractInMemoryObjectMapper(Mapper mapper) {
		super(mapper);
	}

	/**
	 * Create new in memory mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param factory Object factory used to create destination empty bean.
	 */
	protected AbstractInMemoryObjectMapper(Mapper mapper, ObjectFactory<U, T> factory) {
		super(mapper, factory);
	}

	/**
	 * Create new in memory mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassU Destination type.
	 */
	AbstractInMemoryObjectMapper(Mapper mapper, Class<U> klassU) {
		super(mapper, klassU);
	}

	/**
	 * Create new in memory mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassU Destination type.
	 * @param factory Factory used to instantiate destination object.
	 */
	AbstractInMemoryObjectMapper(Mapper mapper, Class<U> klassU, ObjectFactory<U, T> factory) {
		super(mapper, klassU, factory);
	}

	@Override
	public Collection<U> map(Iterable<T> sources) {
		log.debug("Map source of iterables");
		final Collection<U> results = initIterable(sources);
		log.trace(" - Target collection created, start mapping each entries");

		for (T source : sources) {
			log.trace("  --> Mapping: {}", source);
			final U destination = map(source);
			log.trace("  --> Result is: {}", destination);
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
		log.debug("Create new empty collection implementation");
		final int size = initialCapacity(sources);
		log.trace("  - Initial size: {}", size);
		Collection<U> emptyCollection = size < 0 ? new LinkedList<U>() : new ArrayList<U>(size);
		log.trace("  - Collection implementation: {}", emptyCollection.getClass());
		return emptyCollection;
	}

	/**
	 * Try to compute initial capacity of destination collections map
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
		log.debug("Trying to detect initial capacity");
		if (iterables instanceof Collection) {
			log.trace("  --> Iterable source is a collection, return the source size");
			return ((Collection) iterables).size();
		}

		log.trace("  -> Iterable source is not a collection, return -1");
		return -1;
	}
}
