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

/**
 * Static factories to object mappers.
 */
public final class ObjectMappers {

	// Ensure non instantiation
	private ObjectMappers() {
	}

	/**
	 * Create new mapper.
	 * Iterable collection will be an in memory data structure (i.e an instance of Collection).
	 *
	 * @param mapper Internal mapper.
	 * @param klassT Class of objects to map from.
	 * @param klassU Class of objects to map to.
	 * @param <T> Type of objects to map from.
	 * @param <U> Type of objects to map to.
	 * @return Mapper.
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> ObjectMapper<T, U> inMemoryObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		return new InMemoryObjectMapper(mapper, klassT, klassU);
	}

	/**
	 * Create new mapper.
	 * Destination objects will be created using custom factory.
	 * Iterable collection will be an in memory data structure (i.e an instance of Collection).
	 *
	 * @param mapper Internal mapper.
	 * @param klassT Class of objects to map from.
	 * @param klassU Class of objects to map to.
	 * @param factory Custom factory for destination objects.
	 * @param <T> Type of objects to map from.
	 * @param <U> Type of objects to map to.
	 * @return Mapper.
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> ObjectMapper<T, U> inMemoryObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U> factory) {
		return new InMemoryObjectMapper(mapper, klassT, klassU, factory);
	}

	/**
	 * Create new mapper.
	 * Iterable collection will be an a lazy data structure (i.e an instance of Iterable but not
	 * an instance of Collection).
	 *
	 * @param mapper Internal mapper.
	 * @param klassT Class of objects to map from.
	 * @param klassU Class of objects to map to.
	 * @param <T> Type of objects to map from.
	 * @param <U> Type of objects to map to.
	 * @return Mapper.
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> ObjectMapper<T, U> lazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		return new LazyObjectMapper(mapper, klassT, klassU);
	}

	/**
	 * Create new mapper.
	 * Destination objects will be created using custom factory.
	 * Iterable collection will be an a lazy data structure (i.e an instance of Iterable but not
	 * an instance of Collection).
	 *
	 * @param mapper Internal mapper.
	 * @param klassT Class of objects to map from.
	 * @param klassU Class of objects to map to.
	 * @param factory Custom factory for destination objects.
	 * @param <T> Type of objects to map from.
	 * @param <U> Type of objects to map to.
	 * @return Mapper.
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> ObjectMapper<T, U> lazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U> factory) {
		return new LazyObjectMapper(mapper, klassT, klassU, factory);
	}

	private static class InMemoryObjectMapper<T, U> extends AbstractInMemoryObjectMapper<T, U> implements ObjectMapper<T, U> {
		private InMemoryObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
			super(mapper, klassT, klassU);
		}

		private InMemoryObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U> factory) {
			super(mapper, klassT, klassU, factory);
		}
	}

	private static class LazyObjectMapper<T, U> extends AbstractLazyObjectMapper<T, U> implements ObjectMapper<T, U> {
		private LazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
			super(mapper, klassT, klassU);
		}

		private LazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U> factory) {
			super(mapper, klassT, klassU, factory);
		}
	}
}
