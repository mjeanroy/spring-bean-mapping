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

import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringFacadeMapperTest {

	private SpringFacadeMapper springFacadeMapper;

	@Before
	public void setUp() {
		springFacadeMapper = new SpringFacadeMapper();
	}

	@Test
	public void it_should_map_object_to_object() {
		Long id = 1L;
		String name = "foo";
		Foo foo = new Foo(id, name);

		FooDto fooDto = springFacadeMapper.map(foo, FooDto.class);

		assertThat(fooDto).isNotNull();
		assertThat(fooDto.getId()).isNotNull().isEqualTo(id);
		assertThat(fooDto.getName()).isNotNull().isEqualTo(name);
	}

	@Test
	public void it_should_map_object_to_instance_object() {
		Long id = 1L;
		String name = "foo";
		Foo foo = new Foo(id, name);

		FooDto fooDto = new FooDto();
		springFacadeMapper.map(foo, fooDto);

		assertThat(fooDto).isNotNull();
		assertThat(fooDto.getId()).isNotNull().isEqualTo(id);
		assertThat(fooDto.getName()).isNotNull().isEqualTo(name);
	}
}
