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

package com.github.mjeanroy.spring.bean.mapping.factory.jpa;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.mjeanroy.spring.bean.mapping.utils.FooJpaObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.mjeanroy.spring.bean.mapping.commons.Function;
import com.github.mjeanroy.spring.bean.mapping.utils.Foo;
import com.github.mjeanroy.spring.bean.mapping.utils.FooDto;

@SuppressWarnings("unchecked")
public class JpaObjectFactoryTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void it_should_create_new_object_instance() {
		FooJpaObjectFactory factory = new FooJpaObjectFactory();
		Foo foo = factory.get();
		assertThat(foo).isNotNull();
	}

	@Test
	public void it_should_create_new_object_instance_using_function_to_parse_id() {
		long id = 1L;
		FooDto dto = mock(FooDto.class);
		when(dto.getId()).thenReturn(id);
		Foo foo = mock(Foo.class);

		Function<FooDto, Long> idFunction = mock(Function.class);
		when(idFunction.apply(dto)).thenReturn(id);

		FooJpaObjectFactory factory = new FooJpaObjectFactory(idFunction);
		when(factory.entityManager.find(Foo.class, id)).thenReturn(foo);

		Foo result = factory.get(dto);

		assertThat(result).isNotNull().isSameAs(foo);
		verify(idFunction).apply(dto);
		verify(dto, never()).getId();
		verify(factory.entityManager).find(Foo.class, id);
	}

	@Test
	public void it_should_create_new_object_instance_using_interface_contract() {
		long id = 1L;
		FooDtoIdentifiable dto = mock(FooDtoIdentifiable.class);
		when(dto.getId()).thenReturn(id);

		Foo foo = mock(Foo.class);

		FooJpaObjectFactory factory = new FooJpaObjectFactory();
		when(factory.entityManager.find(Foo.class, id)).thenReturn(foo);

		Foo result = factory.get(dto);

		verify(dto).getId();
		verify(factory.entityManager).find(Foo.class, id);
		assertThat(result).isNotNull().isEqualTo(foo);
	}

	@Test
	public void it_should_throw_exception_if_id_cannot_be_get() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Unable to get jpa id from source object");

		long id = 1L;
		FooDto dto = mock(FooDto.class);
		when(dto.getId()).thenReturn(id);

		Foo foo = mock(Foo.class);

		FooJpaObjectFactory factory = new FooJpaObjectFactory();
		when(factory.entityManager.find(Foo.class, id)).thenReturn(foo);

		factory.get(dto);
	}

	@Test
	public void it_should_create_new_object_instance_if_id_is_null() {
		Long id = null;
		FooDtoIdentifiable dto = mock(FooDtoIdentifiable.class);
		when(dto.getId()).thenReturn(id);

		FooJpaObjectFactory factory = new FooJpaObjectFactory();

		Foo result = factory.get(dto);

		assertThat(result).isNotNull();
		verify(dto).getId();
		verify(factory.entityManager, never()).find(any(Class.class), any());
	}

	public static class FooDtoIdentifiable extends FooDto implements IdentifiableObject<Long> {

		@Override
		public Long getId() {
			return super.getId();
		}
	}
}
