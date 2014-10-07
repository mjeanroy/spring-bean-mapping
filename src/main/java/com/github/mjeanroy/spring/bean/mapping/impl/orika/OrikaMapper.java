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

package com.github.mjeanroy.spring.bean.mapping.impl.orika;

import com.github.mjeanroy.spring.bean.mapping.Mapper;
import com.github.mjeanroy.spring.bean.mapping.impl.AbstractMapper;
import ma.glasnost.orika.MapperFacade;

import static com.github.mjeanroy.spring.bean.mapping.commons.PreConditions.notNull;

/**
 * Bean mapper implementation using Orika framework.
 */
public class OrikaMapper extends AbstractMapper implements Mapper {

	/**
	 * Original Orika Mapper.
	 * This mapper will be used internally to map bean fields.
	 */
	private final MapperFacade mapperFacade;

	/**
	 * Build new mapper.
	 *
	 * @param mapperFacade Orika mapper instance.
	 */
	public OrikaMapper(MapperFacade mapperFacade) {
		this.mapperFacade = notNull(mapperFacade, "Orika facade must not be null");
	}

	@Override
	public <T, U> void map(T source, U destination) {
		mapperFacade.map(source, destination);
	}
}
