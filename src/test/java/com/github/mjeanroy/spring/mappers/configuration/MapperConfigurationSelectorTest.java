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

import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapperConfigurationSelectorTest {

	@Test
	public void it_should_load_provider() {
		final Class<?> configurationClass = MapperConfigurationSelectorTest.class;
		final MapperConfigurationSelector selector = new MapperConfigurationSelector();
		final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
		final MapperProvider provider = mock(MapperProvider.class);
		final Map<String, Object> annotationAttributes = singletonMap("provider", (Object) provider);

		when(metadata.getAnnotationAttributes(EnableMapper.class.getName())).thenReturn(annotationAttributes);
		when(provider.configurationClass()).thenReturn(configurationClass);

		final String[] klasses = selector.selectImports(metadata);

		assertThat(klasses)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.containsOnly(configurationClass.getName());

		verify(metadata).getAnnotationAttributes(EnableMapper.class.getName());
		verify(provider).configurationClass();
	}
}
