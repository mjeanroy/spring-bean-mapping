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

package com.github.mjeanroy.spring.mappers.impl.modelmapper;

import com.github.mjeanroy.spring.mappers.Mapper;
import com.github.mjeanroy.spring.mappers.impl.AbstractMapper;
import org.modelmapper.ModelMapper;

import static com.github.mjeanroy.spring.mappers.commons.PreConditions.notNull;

/**
 * Bean mapper implementation using ModelMapper framework.
 */
public class ModelMapperMapper extends AbstractMapper<ModelMapper> implements Mapper {

	/**
	 * Original ModelMapper mapper.
	 * This mapper will be used internally to map bean fields.
	 */
	private final ModelMapper modelMapper;

	/**
	 * Build new mapper.
	 *
	 * @param modelMapper ModelMapper mapper instance.
	 */
	public ModelMapperMapper(ModelMapper modelMapper) {
		this.modelMapper = notNull(modelMapper, "Model mapper must not be null");
	}

	@Override
	public <T, U> void map(T source, U destination) {
		modelMapper.map(source, destination);
	}

	@Override
	public <T, U> U map(T source, Class<U> klass) {
		return modelMapper.map(source, klass);
	}

	@Override
	public ModelMapper getDelegate() {
		return modelMapper;
	}
}
