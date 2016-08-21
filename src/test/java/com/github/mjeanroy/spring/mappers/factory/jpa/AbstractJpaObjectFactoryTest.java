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

package com.github.mjeanroy.spring.mappers.factory.jpa;

import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import com.github.mjeanroy.spring.mappers.utils.FooJpaObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class AbstractJpaObjectFactoryTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void it_should_get_generic_types() {
		FooJpaObjectFactory factory = new FooJpaObjectFactory();
		assertThat(factory.getPkClass()).isEqualTo(Long.class);
	}

	@Test
	public void it_should_create_new_object_instance() {
		FooJpaObjectFactory factory = new FooJpaObjectFactory();
		Foo foo = factory.get();
		assertThat(foo).isNotNull();
	}

	@Test
	public void it_should_create_new_object_instance_if_id_is_null() {
		Long id = null;
		FooDto dto = mock(FooDto.class);
		when(dto.getId()).thenReturn(id);

		FooJpaObjectFactory factory = new FooJpaObjectFactory();

		Foo result = factory.get(dto);

		assertThat(result).isNotNull();
		verify(dto).getId();
		verify(factory.entityManager, never()).find(any(Class.class), any());
	}
}
