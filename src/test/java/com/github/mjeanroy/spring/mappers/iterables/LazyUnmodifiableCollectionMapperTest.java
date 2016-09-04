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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LazyUnmodifiableCollectionMapperTest {

	private LazyUnmodifiableCollectionMapper<FooDto, Foo> lazyCollection;
	private List<Foo> foos;
	private ObjectMapper<Foo, FooDto> mapper;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		Foo f1 = mock(Foo.class);
		Foo f2 = mock(Foo.class);
		foos = asList(f1, f2);

		mapper = mock(ObjectMapper.class);

		lazyCollection = new LazyUnmodifiableCollectionMapper<>(foos, mapper);
	}

	@Test
	public void it_should_get_size_of_collection() {
		final int size = lazyCollection.size();
		assertThat(size).isEqualTo(2);
	}

	@Test
	public void it_should_chec_if_collection_is_empty() {
		final boolean empty = lazyCollection.isEmpty();
		assertThat(empty).isFalse();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_add_element() {
		lazyCollection.add(mock(FooDto.class));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_add_all_element() {
		lazyCollection.addAll(singleton(mock(FooDto.class)));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_remove_element() {
		lazyCollection.remove(mock(FooDto.class));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_remove_all_element() {
		lazyCollection.removeAll(singleton(mock(FooDto.class)));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_retain_all_element() {
		lazyCollection.retainAll(singleton(mock(FooDto.class)));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_not_clear_iterable() {
		lazyCollection.clear();
	}

	@Test
	public void it_should_iterate_collection() {
		List<FooDto> dtos = new ArrayList<>(foos.size());
		for (Foo foo : foos) {
			FooDto dto = mock(FooDto.class);
			when(mapper.map(foo)).thenReturn(dto);
			dtos.add(dto);
		}

		Iterator<FooDto> it = lazyCollection.iterator();
		int i = 0;
		while (it.hasNext()) {
			assertThat(it.next()).isSameAs(dtos.get(i));
			verify(mapper).map(foos.get(i));
			i++;
		}

		assertThat(i).isEqualTo(foos.size());
	}
}
