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
import com.github.mjeanroy.spring.bean.mapping.impl.spring.SpringMapper;
import com.github.mjeanroy.spring.bean.mapping.utils.Foo;
import com.github.mjeanroy.spring.bean.mapping.utils.FooDto;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DelegatingBiDirectionalObjectMapperTest {

	private ObjectMapper<Foo, FooDto> mapper1;

	private ObjectMapper<FooDto, Foo> mapper2;

	private DelegatingBiDirectionalObjectMapper<Foo, FooDto> objMapper;

	@Before
	public void setUp() {
		Mapper mapper = spy(new SpringMapper());
		mapper1 = new LazyObjectMapper<Foo, FooDto>(mapper, Foo.class, FooDto.class);
		mapper2 = new LazyObjectMapper<FooDto, Foo>(mapper, FooDto.class, Foo.class);
		objMapper = new DelegatingBiDirectionalObjectMapper<Foo, FooDto>(mapper1, mapper2);
	}

	@Test
	public void it_should_build_object_mapper() {
		assertThat(objMapper.mapper1).isNotNull().isSameAs(mapper1);
		assertThat(objMapper.mapper2).isNotNull().isSameAs(mapper2);
	}

	@Test
	public void it_should_transform_object_in_both_ways() {
		Foo foo = new Foo(1L, "foo");

		FooDto dto = objMapper.from(foo);
		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(foo.getId());
		assertThat(dto.getName()).isEqualTo(foo.getName());
		verify(mapper1).from(foo);

		Foo result = objMapper.to(dto);
		assertThat(result).isNotNull().isNotSameAs(foo);
		assertThat(result.getId()).isEqualTo(foo.getId());
		assertThat(result.getName()).isEqualTo(foo.getName());
		verify(mapper2).from(dto);
	}

	@Test
	public void it_should_transform_iterables_in_both_ways() {
		Foo foo1 = new Foo(1L, "foo1");
		Foo foo2 = new Foo(2L, "foo2");
		List<Foo> foos = asList(foo1, foo2);

		List<FooDto> dtos = toList(objMapper.from(foos));
		assertThat(dtos).isNotNull().isNotEmpty().hasSameSizeAs(foos);
		assertThat(dtos.get(0).getId()).isEqualTo(foo1.getId());
		assertThat(dtos.get(0).getName()).isEqualTo(foo1.getName());
		assertThat(dtos.get(1).getId()).isEqualTo(foo2.getId());
		assertThat(dtos.get(1).getName()).isEqualTo(foo2.getName());
		verify(mapper1).from(foos);

		List<Foo> results = toList(objMapper.to(dtos));
		assertThat(results).isNotNull().isNotEmpty().hasSameSizeAs(foos);
		assertThat(results.get(0).getId()).isEqualTo(foo1.getId());
		assertThat(results.get(0).getName()).isEqualTo(foo1.getName());
		assertThat(results.get(1).getId()).isEqualTo(foo2.getId());
		assertThat(results.get(1).getName()).isEqualTo(foo2.getName());
		verify(mapper2).from(dtos);
	}

	private static <T> List<T> toList(Iterable<T> iterables) {
		List<T> list = new ArrayList<T>();
		for (T t : iterables) {
			list.add(t);
		}
		return list;
	}
}
