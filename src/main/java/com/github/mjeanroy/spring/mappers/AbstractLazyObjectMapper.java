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
import com.github.mjeanroy.spring.mappers.iterables.LazyIterableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lazy mapper implementation.
 *
 * This implementation returns iterable of destination objects that will be
 * mapped during explicit iteration.
 * Default implementation is equivalent to a {@link AbstractLazyObjectMapper}.
 *
 * This class is an abstract class because it needs to be sub-classed to be able to use
 * constructor without generic types.
 *
 * @param <T> Type of source objects.
 * @param <U> Type of destination objects.
 */
public abstract class AbstractLazyObjectMapper<T, U> extends AbstractObjectMapper<T, U> implements ObjectMapper<T, U> {

	/**
	 * Class object.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractLazyObjectMapper.class);

	/**
	 * Create new lazy mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 */
	protected AbstractLazyObjectMapper(Mapper mapper) {
		super(mapper);
	}

	/**
	 * Create new lazy mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 */
	protected AbstractLazyObjectMapper(Mapper mapper, ObjectFactory<U, T> factory) {
		super(mapper, factory);
	}

	/**
	 * Create new lazy mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 */
	AbstractLazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		super(mapper, klassT, klassU);
	}

	/**
	 * Create new lazy mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 * @param factory Factory used to instantiate destination object.
	 */
	AbstractLazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U, T> factory) {
		super(mapper, klassT, klassU, factory);
	}

	@Override
	public Iterable<U> map(Iterable<T> sources) {
		log.debug("Creating lazy iterable destination");
		// Do not copy to a collection, keep it really lazy !
		return new LazyIterableMapper<>(sources, this);
	}
}
