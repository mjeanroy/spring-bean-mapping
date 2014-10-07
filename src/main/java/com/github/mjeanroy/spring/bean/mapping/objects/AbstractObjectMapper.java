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

import com.github.mjeanroy.spring.bean.mapping.Mapper;
import com.github.mjeanroy.spring.bean.mapping.commons.ClassUtils;
import com.github.mjeanroy.spring.bean.mapping.factory.ObjectFactory;
import com.github.mjeanroy.spring.bean.mapping.iterables.LazyIterableMapper;

/**
 * Simple abstraction that defines commons methods to object mapper implementations.
 * These mappers offers class level type checking with generic and can be used to
 * map iterables source objects.
 *
 * @param <T> Type of source objects.
 * @param <U> Type of destination objects.
 */
public abstract class AbstractObjectMapper<T, U> implements ObjectMapper<T, U> {

	/**
	 * Mapper that will be used internally to map source object
	 * to destination object.
	 */
	protected final Mapper mapper;

	/**
	 * Class of source objects.
	 */
	protected final Class<T> klassT;

	/**
	 * Class of destination objects.
	 */
	protected final Class<U> klassU;

	/**
	 * Factory used to instantiate destination objects.
	 */
	protected final ObjectFactory<U> factory;

	/**
	 * Create new mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractObjectMapper(Mapper mapper) {
		this.mapper = mapper;
		this.klassT = (Class<T>) ClassUtils.getGenericType(getClass(), 0);
		this.klassU = (Class<U>) ClassUtils.getGenericType(getClass(), 1);
		this.factory = null;
	}

	/**
	 * Create new mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 */
	protected AbstractObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		this.mapper = mapper;
		this.klassT = klassT;
		this.klassU = klassU;
		this.factory = null;
	}

	/**
	 * Create new mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 * @param factory Factory used to instantiate destination object.
	 */
	protected AbstractObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U> factory) {
		this.mapper = mapper;
		this.klassT = klassT;
		this.klassU = klassU;
		this.factory = factory;
	}

	@Override
	public U from(T source) {
		return source == null ? null : convert(source);
	}

	@Override
	public U convert(T source) {
		if (factory != null) {
			return mapper.map(source, factory);
		} else {
			return mapper.map(source, klassU);
		}
	}

	@Override
	public Iterable<U> from(Iterable<T> sources) {
		return new LazyIterableMapper<U, T>(sources, this);
	}
}
