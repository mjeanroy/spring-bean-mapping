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

package com.github.mjeanroy.spring.bean.mapping.configuration;

import com.github.mjeanroy.spring.bean.mapping.commons.ClassUtils;
import com.github.mjeanroy.spring.bean.mapping.configuration.dozer.DozerMapperConfiguration;
import com.github.mjeanroy.spring.bean.mapping.configuration.modelmapper.ModelMapperMapperConfiguration;
import com.github.mjeanroy.spring.bean.mapping.configuration.orika.OrikaMapperConfiguration;
import com.github.mjeanroy.spring.bean.mapping.configuration.spring.SpringMapperConfiguration;

/**
 * Set of mapper provider.
 * Each provider use a dedicated configuration class.
 * Provider {@link #AUTO} will look for best available implementation on classpath
 * and return corresponding configuration class.
 */
public enum MapperProvider {

	/**
	 * Mapper using dozer implementation.
	 * Dozer dependency must be available on classpath.
	 */
	DOZER {
		@Override
		public Class configurationClass() {
			return DozerMapperConfiguration.class;
		}
	},

	/**
	 * Mapper using ModelMapper implementation.
	 * ModelMapper dependency must be available on classpath.
	 */
	MODEL_MAPPER {
		@Override
		public Class configurationClass() {
			return ModelMapperMapperConfiguration.class;
		}
	},

	/**
	 * Mapper using Orika implementation.
	 * Orika dependency must be available on classpath.
	 */
	ORIKA {
		@Override
		public Class configurationClass() {
			return OrikaMapperConfiguration.class;
		}
	},

	/**
	 * Mapper using spring internal method to implement bean mapping.
	 * This require does not depend on other external dependency (except spring framework).
	 */
	SPRING {
		@Override
		public Class configurationClass() {
			return SpringMapperConfiguration.class;
		}
	},

	/**
	 * This provider will look for best provider implementation available on classpath.
	 * It checks providers in following order:
	 * - Dozer.
	 * - ModelMapper.
	 * - Orika.
	 * - Spring mapper will be used as a default implementation.
	 */
	AUTO {
		@Override
		public Class configurationClass() {
			if (ClassUtils.isPresent("org.dozer.DozerBeanMapper")) {
				return DOZER.configurationClass();
			} else if (ClassUtils.isPresent("org.modelmapper.ModelMapper")) {
				return MODEL_MAPPER.configurationClass();
			} else if (ClassUtils.isPresent("ma.glasnost.orika.MapperFacade")) {
				return ORIKA.configurationClass();
			} else {
				return SPRING.configurationClass();
			}
		}
	};

	/**
	 * Get configuration class to use.
	 *
	 * @return Configuration class.
	 */
	public abstract Class configurationClass();
}
