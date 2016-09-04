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

package com.github.mjeanroy.spring.mappers.commons;

import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassUtilsTest {

	@Test
	public void it_should_check_if_class_is_present() {
		assertThat(ClassUtils.isPresent(getClass().getName())).isTrue();
		assertThat(ClassUtils.isPresent("foo.bar")).isFalse();
	}

	@Test
	public void it_should_select_annotation_value() {
		final String annotationName = "annotation";
		final Map<String, Object> attributes = singletonMap(annotationName, (Object) "bar");
		final AnnotationMetadata metadata = mock(AnnotationMetadata.class);

		when(metadata.getAnnotationAttributes(CustomAnnotation.class.getName())).thenReturn(attributes);

		final String result = ClassUtils.getAnnotationValue(metadata, CustomAnnotation.class, annotationName, "default");

		assertThat(result)
				.isNotNull()
				.isEqualTo("bar");
	}

	@Test
	public void it_should_select_default_annotation_value() {
		final String annotationName = "annotation";
		final String defaultValues = "default";
		final Map<String, Object> attributes = singletonMap("foo", (Object) "bar");
		final AnnotationMetadata metadata = mock(AnnotationMetadata.class);

		when(metadata.getAnnotationAttributes(CustomAnnotation.class.getName())).thenReturn(attributes);

		final String result = ClassUtils.getAnnotationValue(metadata, CustomAnnotation.class, annotationName, defaultValues);

		assertThat(result)
				.isNotNull()
				.isSameAs(defaultValues);
	}

	public @interface CustomAnnotation {
	}
}
