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
import com.github.mjeanroy.spring.mappers.factory.reflection.ReflectionObjectFactory;
import com.github.mjeanroy.spring.mappers.impl.spring.SpringMapper;
import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import com.github.mjeanroy.spring.mappers.utils.FooInMemoryMapper;
import com.github.mjeanroy.spring.mappers.utils.FooMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.github.mjeanroy.spring.mappers.ObjectMappers.inMemoryObjectMapper;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class InMemoryObjectMapperTest  extends AbstractObjectMapperTest {

	private FooMapper fooMapper;

	private Mapper mapper;

	@Before
	public void setUp() {
		mapper = spy(new SpringMapper());
		fooMapper = new FooInMemoryMapper(mapper);
	}

	@Override
	protected FooMapper fooMapper() {
		return fooMapper;
	}

	@Test
	public void it_should_create_in_memory_mapper_with_default_factory() throws Exception {
		final Mapper mapper = mock(Mapper.class);
		final FooInMemoryMapper fooInMemoryMapper = new FooInMemoryMapper(mapper);

		assertThat(readField(fooInMemoryMapper, "mapper", true))
				.isNotNull()
				.isSameAs(mapper);

		assertThat(readField(fooInMemoryMapper, "factory", true))
				.isNotNull()
				.isExactlyInstanceOf(ReflectionObjectFactory.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void it_should_create_in_memory_mapper_with_custom_factory() throws Exception {
		final Mapper mapper = mock(Mapper.class);
		final ObjectFactory<FooDto, Foo> factory = mock(ObjectFactory.class);
		final FooInMemoryMapper fooInMemoryMapper = new FooInMemoryMapper(mapper, factory);

		assertThat(readField(fooInMemoryMapper, "mapper", true))
				.isNotNull()
				.isSameAs(mapper);

		assertThat(readField(fooInMemoryMapper, "factory", true))
				.isNotNull()
				.isSameAs(factory);
	}

	@Override
	protected void checkBeforeIteration(Iterable<FooDto> fooDtos, List<Foo> foos) {
		verify(mapper, times(2)).map(any(Foo.class), any(ReflectionObjectFactory.class));
		verify(mapper).map(same(foos.get(0)), any(ReflectionObjectFactory.class));
		verify(mapper).map(same(foos.get(1)), any(ReflectionObjectFactory.class));
	}

	@Override
	protected void checkAfterIteration(List<FooDto> fooDtos, List<Foo> foos) {
		verify(mapper, times(2)).map(any(Foo.class), any(ReflectionObjectFactory.class));
		verify(mapper).map(same(foos.get(0)), any(ReflectionObjectFactory.class));
		verify(mapper).map(same(foos.get(1)), any(ReflectionObjectFactory.class));
	}

	@Test
	public void it_should_build_in_memory_mapper_with_explicit_generic_types() throws Exception {
		ObjectMapper<Foo, FooDto> objectMapper = inMemoryObjectMapper(mapper, Foo.class, FooDto.class);

		ObjectFactory factory = (ObjectFactory) readField(objectMapper, "factory", true);
		Class klassT = (Class) readField(objectMapper, "klassT", true);
		Class klassU = (Class) readField(objectMapper, "klassU", true);

		assertThat(factory).isNotNull().isExactlyInstanceOf(ReflectionObjectFactory.class);
		assertThat(klassT).isNotNull().isEqualTo(Foo.class);
		assertThat(klassU).isNotNull().isEqualTo(FooDto.class);
	}

	@Test
	public void it_should_build_in_memory_mapper_with_explicit_generic_types_and_factory() throws Exception {
		ObjectFactory<FooDto, Foo> fact = new ReflectionObjectFactory<>(FooDto.class);
		ObjectMapper<Foo, FooDto> objectMapper = inMemoryObjectMapper(mapper, Foo.class, FooDto.class, fact);

		ObjectFactory factory = (ObjectFactory) readField(objectMapper, "factory", true);
		Class klassT = (Class) readField(objectMapper, "klassT", true);
		Class klassU = (Class) readField(objectMapper, "klassU", true);

		assertThat(factory).isNotNull().isSameAs(fact);
		assertThat(klassT).isNotNull().isEqualTo(Foo.class);
		assertThat(klassU).isNotNull().isEqualTo(FooDto.class);
	}

	@Test
	public void it_should_create_linked_list_with_iterable_sources() throws Exception {
		ObjectMapper<Foo, FooDto> objectMapper = inMemoryObjectMapper(mapper, Foo.class, FooDto.class);

		final Iterator<Foo> iterator = new Iterator<Foo>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Foo next() {
				return null;
			}

			@Override
			public void remove() {
				// Nothing to do.
			}
		};

		Iterable<Foo> iterable = new Iterable<Foo>() {
			@Override
			public Iterator<Foo> iterator() {
				return iterator;
			}
		};

		Iterable<FooDto> results = objectMapper.map(iterable);

		assertThat(results).isInstanceOf(LinkedList.class);
	}

	@Test
	public void it_should_create_array_list_with_collection_sources() throws Exception {
		ObjectMapper<Foo, FooDto> objectMapper = inMemoryObjectMapper(mapper, Foo.class, FooDto.class);
		Collection<Foo> sources = asList(new Foo(), new Foo());

		Iterable<FooDto> results = objectMapper.map(sources);

		assertThat(results).isInstanceOf(ArrayList.class);
	}
}
