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

package com.github.mjeanroy.spring.mappers.objects;

import com.github.mjeanroy.spring.mappers.Mapper;
import com.github.mjeanroy.spring.mappers.factory.ObjectFactory;
import com.github.mjeanroy.spring.mappers.factory.reflection.ReflectionObjectFactory;
import com.github.mjeanroy.spring.mappers.impl.spring.SpringMapper;
import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import com.github.mjeanroy.spring.mappers.utils.FooLazyMapper;
import com.github.mjeanroy.spring.mappers.utils.FooMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.mjeanroy.spring.mappers.objects.ObjectMappers.lazyObjectMapper;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LazyObjectMapperTest extends AbstractObjectMapperTest {

	private FooMapper fooMapper;

	private Mapper mapper;

	@Before
	public void setUp() {
		mapper = spy(new SpringMapper());
		fooMapper = new FooLazyMapper(mapper);
	}

	@Override
	protected FooMapper fooMapper() {
		return fooMapper;
	}

	@Override
	protected void checkBeforeIteration(Iterable<FooDto> fooDtos, List<Foo> foos) {
		verify(mapper, never()).map(any(), any());
	}

	@Override
	protected void checkAfterIteration(List<FooDto> fooDtos, List<Foo> foos) {
		verify(mapper, times(2)).map(any(), any());
		verify(mapper).map(same(foos.get(0)), any());
		verify(mapper).map(same(foos.get(1)), any());
	}

	@Test
	public void it_should_build_in_memory_mapper_with_explicit_generic_types() throws Exception {
		ObjectMapper<Foo, FooDto> objectMapper = lazyObjectMapper(mapper, Foo.class, FooDto.class);

		ObjectFactory factory = (ObjectFactory) readField(objectMapper, "factory", true);
		Class klassT = (Class) readField(objectMapper, "klassT", true);
		Class klassU = (Class) readField(objectMapper, "klassU", true);

		assertThat(factory).isNull();
		assertThat(klassT).isNotNull().isEqualTo(Foo.class);
		assertThat(klassU).isNotNull().isEqualTo(FooDto.class);
	}

	@Test
	public void it_should_build_in_memory_mapper_with_explicit_generic_types_and_factory() throws Exception {
		ObjectFactory<FooDto> fact = new ReflectionObjectFactory<FooDto>(FooDto.class);
		ObjectMapper<Foo, FooDto> objectMapper = lazyObjectMapper(mapper, Foo.class, FooDto.class, fact);

		ObjectFactory factory = (ObjectFactory) readField(objectMapper, "factory", true);
		Class klassT = (Class) readField(objectMapper, "klassT", true);
		Class klassU = (Class) readField(objectMapper, "klassU", true);

		assertThat(factory).isNotNull().isSameAs(fact);
		assertThat(klassT).isNotNull().isEqualTo(Foo.class);
		assertThat(klassU).isNotNull().isEqualTo(FooDto.class);
	}
}
