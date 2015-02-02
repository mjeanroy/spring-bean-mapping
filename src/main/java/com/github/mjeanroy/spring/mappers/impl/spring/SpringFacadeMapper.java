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

package com.github.mjeanroy.spring.mappers.impl.spring;

import org.springframework.beans.BeanUtils;

/**
 * Spring mapper implementation.
 */
public class SpringFacadeMapper {

	/**
	 * Copy source properties (a.k.a getter values) to
	 * destination object.
	 *
	 * @param source Source object.
	 * @param destination Destination object.
	 * @param <T> Type of source objects.
	 * @param <U> Type of destination objects.
	 */
	public <T, U> void map(T source, U destination) {
		BeanUtils.copyProperties(source, destination);
	}

	/**
	 * Create instance of destination object and copy
	 * source properties (a.k.a getter values of source
	 * object to destination object).
	 *
	 * @param source Source object.
	 * @param klass Class of destination object to instantiate.
	 * @param <T> Type of source object.
	 * @param <U> Type of destination object.
	 * @return New instance of destination object.
	 */
	public <T, U> U map(T source, Class<U> klass) {
		U destination = BeanUtils.instantiateClass(klass);
		map(source, destination);
		return destination;
	}
}
