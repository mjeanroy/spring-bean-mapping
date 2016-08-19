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
import com.github.mjeanroy.spring.mappers.utils.Foo;
import com.github.mjeanroy.spring.mappers.utils.FooDto;
import com.github.mjeanroy.spring.mappers.utils.FooMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractObjectMapperTest {

	protected abstract FooMapper fooMapper();

	@Test
	public void it_should_detect_generic_classes_and_instantie_default_factory() throws Exception {
		FooMapper fooMapper = fooMapper();

		ObjectFactory factory = (ObjectFactory) readField(fooMapper, "factory", true);
		Class klassT = (Class) readField(fooMapper, "klassT", true);
		Class klassU = (Class) readField(fooMapper, "klassU", true);

		assertThat(factory).isNull();
		assertThat(klassT).isNotNull().isSameAs(Foo.class);
		assertThat(klassU).isNotNull().isSameAs(FooDto.class);
	}

	@Test
	public void it_should_map_null_to_null() throws Exception {
		FooMapper fooMapper = fooMapper();
		FooDto fooDto = fooMapper.map((Foo) null);
		assertThat(fooDto).isNull();
	}

	@Test
	public void it_should_map_one_object_to_the_other() throws Exception {
		Long id = 1L;
		String name = "foo";
		Foo foo = new Foo(id, name);
		FooMapper fooMapper = fooMapper();

		FooDto fooDto = fooMapper.map(foo);

		assertThat(fooDto).isNotNull();
		assertThat(fooDto.getId()).isEqualTo(1L);
		assertThat(fooDto.getName()).isNotNull().isEqualTo(name);
	}

	@Test
	public void it_should_transform_source_iterable_to_destination_iterable() throws Exception {
		Foo foo1 = new Foo(1L, "foo1");
		Foo foo2 = new Foo(2L, "foo2");
		List<Foo> foos = asList(foo1, foo2);

		FooMapper fooMapper = fooMapper();

		Iterable<FooDto> foosDto = fooMapper.map(foos);

		assertThat(foosDto).isNotNull();

		checkBeforeIteration(foosDto, foos);

		List<FooDto> results = new ArrayList<FooDto>();
		for (FooDto fooDto : foosDto) {
			results.add(fooDto);
		}

		assertThat(results).isNotNull().hasSize(2);

		checkAfterIteration(results, foos);

		FooDto fooDto1 = results.get(0);
		assertThat(fooDto1.getId()).isEqualTo(foo1.getId());
		assertThat(fooDto1.getName()).isEqualTo(foo1.getName());

		FooDto fooDto2 = results.get(1);
		assertThat(fooDto2.getId()).isEqualTo(foo2.getId());
		assertThat(fooDto2.getName()).isEqualTo(foo2.getName());
	}

	@Test
	public void it_should_transform_source_map_to_destination_map() throws Exception {
		Foo foo1 = new Foo(1L, "foo1");
		Foo foo2 = new Foo(2L, "foo2");

		Map<Long, Foo> foos = new HashMap<Long, Foo>();
		foos.put(foo1.getId(), foo1);
		foos.put(foo2.getId(), foo2);

		FooMapper fooMapper = fooMapper();
		Map<Long, FooDto> foosDto = fooMapper.map(foos);

		assertThat(foosDto).isNotNull().isNotEmpty();
		assertThat(foosDto.size()).isEqualTo(foos.size());

		FooDto dto1 = foosDto.get(foo1.getId());
		assertThat(dto1.getId()).isEqualTo(foo1.getId());
		assertThat(dto1.getName()).isEqualTo(foo1.getName());

		FooDto dto2 = foosDto.get(foo2.getId());
		assertThat(dto2.getId()).isEqualTo(foo2.getId());
		assertThat(dto2.getName()).isEqualTo(foo2.getName());
	}

	protected abstract void checkBeforeIteration(Iterable<FooDto> fooDtos, List<Foo> foos);

	protected abstract void checkAfterIteration(List<FooDto> fooDtos, List<Foo> foos);
}
