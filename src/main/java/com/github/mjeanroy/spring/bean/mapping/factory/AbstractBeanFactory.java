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

package com.github.mjeanroy.spring.bean.mapping.factory;

import com.github.mjeanroy.spring.bean.mapping.commons.ClassUtils;

/**
 * Abstraction of factory that define commons methods to factories.
 * @param <T> Type of created objects.
 */
public abstract class AbstractBeanFactory<T> implements BeanFactory<T> {

	/**
	 * Get klass of objects created by this factory.
	 */
	protected final Class<T> klass;

	@SuppressWarnings("unchecked")
	protected AbstractBeanFactory() {
		this.klass = (Class<T>) ClassUtils.getGenericType(getClass(), 0);
	}

	@SuppressWarnings("unchecked")
	protected AbstractBeanFactory(Class klass) {
		this.klass = klass;
	}

	@Override
	public T get(Object... from) {
		return get();
	}
}
