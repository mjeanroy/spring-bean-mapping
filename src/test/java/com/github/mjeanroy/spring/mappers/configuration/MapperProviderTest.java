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

package com.github.mjeanroy.spring.mappers.configuration;

import com.github.mjeanroy.spring.mappers.commons.ClassUtils;
import com.github.mjeanroy.spring.mappers.configuration.dozer.DozerConfiguration;
import com.github.mjeanroy.spring.mappers.configuration.modelmapper.ModelMapperConfiguration;
import com.github.mjeanroy.spring.mappers.configuration.orika.OrikaConfiguration;
import com.github.mjeanroy.spring.mappers.configuration.spring.SpringMapperConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClassUtils.class)
public class MapperProviderTest {

	@Before
	public void setUp() {
		PowerMockito.mockStatic(ClassUtils.class);
	}

	@Test
	public void it_should_get_dozer_configuration_class() {
		Class<?> klass = MapperProvider.DOZER.configurationClass();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(DozerConfiguration.class);
	}

	@Test
	public void it_should_get_model_mapper_configuration_class() {
		Class<?> klass = MapperProvider.MODEL_MAPPER.configurationClass();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(ModelMapperConfiguration.class);
	}

	@Test
	public void it_should_get_orika_configuration_class() {
		Class<?> klass = MapperProvider.ORIKA.configurationClass();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(OrikaConfiguration.class);
	}

	@Test
	public void it_should_get_spring_configuration_class() {
		Class<?> klass = MapperProvider.SPRING.configurationClass();
		assertThat(klass)
				.isNotNull()
				.isEqualTo(SpringMapperConfiguration.class);
	}

	@Test
	public void it_should_load_dozer_by_default() {
		mockStaticClasses(true, true, true);
		Class<?> klass = MapperProvider.AUTO.configurationClass();

		assertThat(klass)
				.isNotNull()
				.isEqualTo(DozerConfiguration.class);
	}

	@Test
	public void it_should_load_model_mapper_as_second_choice() {
		mockStaticClasses(false, true, true);
		Class<?> klass = MapperProvider.AUTO.configurationClass();

		assertThat(klass)
				.isNotNull()
				.isEqualTo(ModelMapperConfiguration.class);
	}

	@Test
	public void it_should_load_orika_as_third_choice() {
		mockStaticClasses(false, false, true);
		Class<?> klass = MapperProvider.AUTO.configurationClass();

		assertThat(klass)
				.isNotNull()
				.isEqualTo(OrikaConfiguration.class);
	}

	@Test
	public void it_should_load_spring_configuration_as_fallback() {
		mockStaticClasses(false, false, false);
		Class<?> klass = MapperProvider.AUTO.configurationClass();

		assertThat(klass)
				.isNotNull()
				.isEqualTo(SpringMapperConfiguration.class);
	}

	private void mockStaticClasses(boolean dozer, boolean modelMapper, boolean orika) {
		when(ClassUtils.isPresent("org.dozer.DozerBeanMapper")).thenReturn(dozer);
		when(ClassUtils.isPresent("org.modelmapper.ModelMapper")).thenReturn(modelMapper);
		when(ClassUtils.isPresent("ma.glasnost.orika.MapperFacade")).thenReturn(orika);
	}
}
