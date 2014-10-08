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

package com.github.mjeanroy.spring.mappers.impl.dozer;

import com.github.mjeanroy.spring.mappers.Mapper;
import com.github.mjeanroy.spring.mappers.impl.AbstractMapper;
import org.dozer.DozerBeanMapper;

import static com.github.mjeanroy.spring.mappers.commons.PreConditions.notNull;

/**
 * Bean mapper implementation using Dozer framework.
 */
public class DozerMapper extends AbstractMapper implements Mapper {

	/**
	 * Original Dozer Mapper.
	 * This mapper will be used internally to map bean fields.
	 */
	private final DozerBeanMapper mapper;

	/**
	 * Build new mapper.
	 *
	 * @param mapper Dozer mapper instance.
	 */
	public DozerMapper(DozerBeanMapper mapper) {
		this.mapper = notNull(mapper, "Dozer bean mapper must not be null");
	}

	@Override
	public <T, U> void map(T source, U destination) {
		mapper.map(source, destination);
	}
}
