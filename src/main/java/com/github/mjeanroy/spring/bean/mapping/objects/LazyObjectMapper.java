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
import com.github.mjeanroy.spring.bean.mapping.factory.ObjectFactory;

import static com.github.mjeanroy.spring.bean.mapping.commons.PreConditions.notNull;

/**
 * Lazy mapper implementation.
 *
 * This implementation returns iterable of destination objects that will be
 * mapped during explicit iteration.
 * Default implementation is equivalent to a {@link com.github.mjeanroy.spring.bean.mapping.objects.LazyObjectMapper}.
 *
 * @param <T> Type of source objects.
 * @param <U> Type of destination objects.
 */
public class LazyObjectMapper<T, U> extends AbstractLazyObjectMapper<T, U> implements ObjectMapper<T, U> {

	/**
	 * Create new lazy mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 */
	public LazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		super(
				notNull(mapper, "Mapper must not be null"),
				notNull(klassT, "Class T must bot be null"),
				notNull(klassU, "Class U must not be null")
		);
	}

	/**
	 * Create new lazy mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 * @param factory Factory used to instantiate destination object.
	 */
	public LazyObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U> factory) {
		super(
				notNull(mapper, "Mapper must not be null"),
				notNull(klassT, "Class T must bot be null"),
				notNull(klassU, "Class U must not be null"),
				notNull(factory, "Factory must not be null")
		);
	}
}
