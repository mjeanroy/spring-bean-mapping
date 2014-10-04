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

/**
 * Mapper that will use two internal simple object mapper
 * to transform objects in both ways.
 *
 * @param <T> First type of objects.
 * @param <U> Second type of objects.
 */
public class DelegatingBiDirectionalObjectMapper<T, U> implements BiDirectionalObjectMapper<T, U>, ObjectMapper<T, U> {

	/**
	 * Object mapper used to map object T to object U.
	 */
	protected final ObjectMapper<T, U> mapper1;

	/**
	 * Object mapper used to map object U to object T.
	 */
	protected final ObjectMapper<U, T> mapper2;

	/**
	 * Create bi-directional mapper using simple object mapper to
	 * map objects.
	 *
	 * @param mapper1 Internal mapper used to map object T to object U.
	 * @param mapper2 Internal mapper used to map object U to object T.
	 */
	public DelegatingBiDirectionalObjectMapper(ObjectMapper<T, U> mapper1, ObjectMapper<U, T> mapper2) {
		this.mapper1 = mapper1;
		this.mapper2 = mapper2;
	}

	@Override
	public U from(T source) {
		return mapper1.from(source);
	}

	@Override
	public Iterable<U> from(Iterable<T> sources) {
		return mapper1.from(sources);
	}

	@Override
	public T to(U source) {
		return mapper2.from(source);
	}

	@Override
	public Iterable<T> to(Iterable<U> sources) {
		return mapper2.from(sources);
	}
}
