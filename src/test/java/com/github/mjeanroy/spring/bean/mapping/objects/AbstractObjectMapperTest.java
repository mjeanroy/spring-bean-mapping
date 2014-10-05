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

import com.github.mjeanroy.spring.bean.mapping.factory.ObjectFactory;
import com.github.mjeanroy.spring.bean.mapping.utils.Foo;
import com.github.mjeanroy.spring.bean.mapping.utils.FooDto;
import com.github.mjeanroy.spring.bean.mapping.utils.FooMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		FooDto fooDto = fooMapper.convert((Foo) null);
		assertThat(fooDto).isNull();
	}

	@Test
	public void it_should_map_one_object_to_the_other() throws Exception {
		Long id = 1L;
		String name = "foo";
		Foo foo = new Foo(id, name);
		FooMapper fooMapper = fooMapper();

		FooDto fooDto = fooMapper.convert(foo);

		assertThat(fooDto).isNotNull();
		assertThat(fooDto.getId()).isEqualTo(1L);
		assertThat(fooDto.getName()).isNotNull().isEqualTo(name);
	}

	@Test
	public void it_should_iterable_to_iterable() throws Exception {
		Long id1 = 1L;
		String name1 = "foo1";

		Long id2 = 2L;
		String name2 = "foo2";

		Foo foo1 = new Foo(id1, name1);
		Foo foo2 = new Foo(id2, name2);
		List<Foo> foos = asList(foo1, foo2);

		FooMapper fooMapper = fooMapper();

		Iterable<FooDto> foosDto = fooMapper.convert(foos);

		assertThat(foosDto).isNotNull();

		checkBeforeIteration(foosDto, foos);

		List<FooDto> results = new ArrayList<FooDto>();
		for (FooDto fooDto : foosDto) {
			results.add(fooDto);
		}

		assertThat(results).isNotNull().hasSize(2);

		checkAfterIteration(results, foos);

		FooDto fooDto1 = results.get(0);
		assertThat(fooDto1.getId()).isEqualTo(id1);
		assertThat(fooDto1.getName()).isEqualTo(name1);

		FooDto fooDto2 = results.get(1);
		assertThat(fooDto2.getId()).isEqualTo(id2);
		assertThat(fooDto2.getName()).isEqualTo(name2);
	}

	protected abstract void checkBeforeIteration(Iterable<FooDto> fooDtos, List<Foo> foos);

	protected abstract void checkAfterIteration(List<FooDto> fooDtos, List<Foo> foos);
}
