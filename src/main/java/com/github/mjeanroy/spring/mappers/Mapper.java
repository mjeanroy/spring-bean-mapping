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
 * Mapper interface.
 * This interface define simple contract that needs to be
 * implemented to map bean to another bean.
 */
public interface Mapper {

	/**
	 * Map bean to another bean.
	 * Target bean will be automatically created.
	 *
	 * @param source  Source bean.
	 * @param factory Factory used to build target bean.
	 * @param <T>     Source type.
	 * @param <U>     Target type.
	 * @return Mapped bean (a.k.a destination).
	 */
	<T, U> U map(T source, ObjectFactory<U, T> factory);

	/**
	 * Map bean to another bean.
	 * Target bean will be automatically created.
	 *
	 * @param source Source bean.
	 * @param klass  Target bean class.
	 * @param <T>    Source type.
	 * @param <U>    Target type.
	 * @return Mapped bean (a.k.a destination).
	 */
	<T, U> U map(T source, Class<U> klass);

	/**
	 * Map bean to another bean.
	 * Target bean is already created and fields will be automatically
	 * mapped map source bean.
	 *
	 * @param source      Source bean.
	 * @param destination Target bean.
	 * @param <T>         Source type.
	 * @param <U>         Target type.
	 */
	<T, U> void map(T source, U destination);

	/**
	 * Get object that is internally used to execute
	 * bean transformation.
	 *
	 * Implementation should return any object, including null if no internal
	 * object are used. A valid implementation is to return null or {@link this}.
	 *
	 * @return Delegate implementation.
	 */
	Object getDelegate();
}
