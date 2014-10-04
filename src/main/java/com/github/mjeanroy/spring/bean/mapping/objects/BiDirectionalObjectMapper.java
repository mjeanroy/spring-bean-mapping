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
 * Mapper that can transform object of type T and type U
 * in both ways.
 *
 * This mapper also support transformation of iterable structure of type T
 * and type U in both ways.
 *
 * @param <T> First type.
 * @param <U> Second type.
 */
public interface BiDirectionalObjectMapper<T, U> extends ObjectMapper<T, U> {

	/**
	 * Transform object of type T to object of type U.
	 * Implementation must be null-safe (do not produce {@link NullPointerException}).
	 *
	 * @param source Source object.
	 * @return Destination object.
	 */
	U from(T source);

	/**
	 * Transform iterable structure of type T to new iterable structure
	 * of type U.
	 *
	 * @param sources Source objects.
	 * @return Destination objects.
	 */
	Iterable<U> from(Iterable<T> sources);

	/**
	 * Transform object of type U to object of type T.
	 * Implementation must be null-safe (do not produce {@link NullPointerException}).
	 *
	 * @param source Source object.
	 * @return Destination object.
	 */
	T to(U source);

	/**
	 * Transform iterable structure of type U to new iterable structure
	 * of type T.
	 *
	 * @param sources Source objects.
	 * @return Destination objects.
	 */
	Iterable<T> to(Iterable<U> sources);
}
