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

import com.github.mjeanroy.spring.mappers.ObjectMapper;
import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class LazyIterableIteratorTest {

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
		Iterator<Foo> iterator = mock(Iterator.class);
		ObjectMapper<Foo, FooDto> mapper = mock(ObjectMapper.class);

		LazyIterableIterator<FooDto, Foo> lazyIterator = new LazyIterableIterator<FooDto, Foo>(iterator, mapper);

		Iterator it = (Iterator) readField(lazyIterator, "iterator", true);
		ObjectMapper objMapper = (ObjectMapper) readField(lazyIterator, "mapper", true);

		assertThat(it).isNotNull();
		assertThat(objMapper).isNotNull().isEqualTo(mapper);
	}

	@Test
	public void it_should_check_has_next() {
		when(iterator.hasNext()).thenReturn(true);
		assertThat(lazyIterator.hasNext()).isTrue();

		when(iterator.hasNext()).thenReturn(false);
		assertThat(lazyIterator.hasNext()).isFalse();
	}

	@Test
	public void it_should_get_next_element() {
		FooDto dto = mock(FooDto.class);
		Foo foo = mock(Foo.class);
		when(iterator.next()).thenReturn(foo);
		when(mapper.from(foo)).thenReturn(dto);

		FooDto result = lazyIterator.next();

		assertThat(result).isNotNull().isSameAs(dto);
		verify(iterator).next();
		verify(mapper).from(foo);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_remove() {
		when(iterator.hasNext()).thenReturn(true);
		lazyIterator.remove();
	}
}
