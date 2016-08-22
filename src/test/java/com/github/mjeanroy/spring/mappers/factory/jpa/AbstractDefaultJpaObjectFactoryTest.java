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
import com.github.mjeanroy.spring.mappers.utils.FooDefaultJpaObjectFactory;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class AbstractDefaultJpaObjectFactoryTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	private EntityManagerFactory emf;

	private EntityManager em;

	private FooDefaultJpaObjectFactory factory;

	@Before
	public void setUp() {
		emf = mock(EntityManagerFactory.class);
		em = mock(EntityManager.class);
		when(emf.createEntityManager()).thenReturn(em);

		factory = new FooDefaultJpaObjectFactory();
		when(factory.appContext.getBean(EntityManagerFactory.class)).thenReturn(emf);
	}

	@Test
	public void it_should_get_generic_types() {
		assertThat(factory.getPkClass()).isEqualTo(Long.class);
	}

	@Test
	public void it_should_return_entity_if_it_is_id() {
		Long source = 1L;
		Foo foo = mock(Foo.class);
		when(em.find(Foo.class, source)).thenReturn(foo);

		Foo result = factory.get(source);
		assertThat(result).isNotNull().isSameAs(foo);
		verify(emf).createEntityManager();
		verify(em).find(Foo.class, source);
	}

	@Test
	public void it_should_use_converter_to_get_id_of_entity() {
		Long source = 1L;
		FooDto dto = mock(FooDto.class);
		Foo foo = mock(Foo.class);
		when(em.find(Foo.class, source)).thenReturn(foo);

		Converter<Object, Long> converter = mock(Converter.class);
		when(converter.convert(dto)).thenReturn(source);

		factory = new FooDefaultJpaObjectFactory(converter);
		when(factory.appContext.getBean(EntityManagerFactory.class)).thenReturn(emf);

		Foo result = factory.get(dto);
		assertThat(result).isNotNull().isSameAs(foo);
		verify(converter).convert(dto);
		verify(emf).createEntityManager();
		verify(em).find(Foo.class, source);
	}

	@Test
	public void it_should_use_conversion_service_to_get_id_of_entity() {
		Long source = 1L;
		FooDto dto = new FooDto();
		Foo foo = mock(Foo.class);
		when(em.find(Foo.class, source)).thenReturn(foo);

		ConversionService conversionService = mock(ConversionService.class);
		when(conversionService.canConvert(FooDto.class, Long.class)).thenReturn(true);
		when(conversionService.convert(dto, Long.class)).thenReturn(source);
		when(factory.appContext.getBean(ConversionService.class)).thenReturn(conversionService);

		Foo result = factory.get(dto);
		assertThat(result).isNotNull().isSameAs(foo);
		verify(conversionService).canConvert(FooDto.class, Long.class);
		verify(conversionService).convert(dto, Long.class);
		verify(emf).createEntityManager();
		verify(em).find(Foo.class, source);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void it_should_throw_exception_if_id_cannot_be_get() {
		Long source = 1L;
		FooDto dto = new FooDto();
		Foo foo = mock(Foo.class);
		when(em.find(Foo.class, source)).thenReturn(foo);

		ConversionService conversionService = mock(ConversionService.class);
		when(conversionService.canConvert(FooDto.class, Long.class)).thenReturn(false);

		factory.get(dto);
	}
}
