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

package com.github.mjeanroy.spring.mappers.iterables;

import com.github.mjeanroy.spring.mappers.Mapper;
import com.github.mjeanroy.spring.mappers.factory.reflection.ReflectionObjectFactory;
import com.github.mjeanroy.spring.mappers.impl.spring.SpringMapper;
import com.github.mjeanroy.spring.mappers.ObjectMapper;
import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import com.github.mjeanroy.spring.mappers.utils.FooLazyMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class LazyIterableMapperTest {

	private Iterator<Foo> iterator;

	private ObjectMapper<Foo, FooDto> mapper;

	private LazyIterableIterator<FooDto, Foo> lazyIterator;

	@Before
	public void setUp() {
		iterator = mock(Iterator.class);
		mapper = mock(ObjectMapper.class);
		lazyIterator = new LazyIterableIterator<FooDto, Foo>(iterator, mapper);
	}

	@Test
	public void it_should_create_new_iterator() throws Exception {
		List<Foo> list = emptyList();
		ObjectMapper<Foo, FooDto> mapper = mock(ObjectMapper.class);

		LazyIterableMapper<FooDto, Foo> lazyIterableMapper = new LazyIterableMapper<FooDto, Foo>(list, mapper);

		Iterable it = (Iterable) readField(lazyIterableMapper, "from", true);
		ObjectMapper objMapper = (ObjectMapper) readField(lazyIterableMapper, "mapper", true);

		assertThat(it).isNotNull().isSameAs(list);
		assertThat(objMapper).isNotNull().isEqualTo(mapper);
	}

	@Test
	public void it_should_iterate() throws Exception {
		Foo foo1 = new Foo(1L, "foo1");
		Foo foo2 = new Foo(2L, "foo2");
		List<Foo> list = asList(foo1, foo2);

		SpringMapper springMapper = new SpringMapper();
		Mapper mapper = spy(springMapper);
		ObjectMapper<Foo, FooDto> fooMapper = new FooLazyMapper(mapper);

		LazyIterableMapper<FooDto, Foo> lazyIterableMapper = new LazyIterableMapper<FooDto, Foo>(list, fooMapper);

		verify(mapper, never()).map(any(), any());

		int i = 0;
		for (FooDto dto : lazyIterableMapper) {
			Foo foo = list.get(i);
			assertThat(dto.getId()).isEqualTo(foo.getId());
			assertThat(dto.getName()).isEqualTo(foo.getName());
			verify(mapper).map(same(foo), any(ReflectionObjectFactory.class));
			i++;
		}
	}

}
