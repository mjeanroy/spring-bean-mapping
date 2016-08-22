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
import com.github.mjeanroy.spring.mappers.factory.reflection.ReflectionObjectFactory;
import com.github.mjeanroy.spring.mappers.iterables.Iterables;
import com.github.mjeanroy.spring.mappers.iterables.LazyUnmodifiableCollectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.spring.mappers.commons.PreConditions.notNull;

/**
 * Simple abstraction that defines commons methods to object mapper implementations.
 * These mappers offers class level type checking with generic and can be used to
 * map iterables source objects.
 *
 * @param <T> Type of source objects.
 * @param <U> Type of destination objects.
 */
public abstract class AbstractObjectMapper<T, U> implements ObjectMapper<T, U> {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractObjectMapper.class);

	/**
	 * Mapper that will be used internally to map source object
	 * to destination object.
	 */
	private final Mapper mapper;

	/**
	 * Class of source objects.
	 */
	private final Class<T> klassT;

	/**
	 * Class of destination objects.
	 */
	private final Class<U> klassU;

	/**
	 * Factory used to instantiate destination objects.
	 */
	private final ObjectFactory<U, T> factory;

	/**
	 * Create new mapper.
	 * Generic types will be detected at object creation.
	 *
	 * @param mapper Mapper used to map source to destination.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractObjectMapper(Mapper mapper) {
		this.mapper = notNull(mapper, "Mapper must not be null");

		Class<?>[] klasses = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractObjectMapper.class);
		this.klassT = (Class<T>) klasses[0];
		this.klassU = (Class<U>) klasses[1];
		this.factory = new ReflectionObjectFactory<>(klassU);
	}

	/**
	 * Create new mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 */
	protected AbstractObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU) {
		this.mapper = notNull(mapper, "Mapper must not be null");
		this.klassT = notNull(klassT, "Class T must bot be null");
		this.klassU = notNull(klassU, "Class U must bot be null");
		this.factory = new ReflectionObjectFactory<>(klassU);
	}

	/**
	 * Create new mapper.
	 *
	 * @param mapper Mapper used to map source to destination.
	 * @param klassT Source type.
	 * @param klassU Destination type.
	 * @param factory Factory used to instantiate destination object.
	 */
	protected AbstractObjectMapper(Mapper mapper, Class<T> klassT, Class<U> klassU, ObjectFactory<U, T> factory) {
		this.mapper = notNull(mapper, "Mapper must not be null");
		this.klassT = notNull(klassT, "Class T must bot be null");
		this.klassU = notNull(klassU, "Class U must bot be null");
		this.factory = notNull(factory, "Factory must bot be null");
	}

	/**
	 * Get mapper implementation.
	 *
	 * @return Mapper.
	 */
	protected Mapper getMapper() {
		return mapper;
	}

	@Override
	public U map(T source) {
		log.debug("Map source object: {}", source);
		return source == null ? null : doMap(source);
	}

	protected U doMap(T source) {
		log.debug("Creating destination object using mapper: {}", mapper);
		log.debug("  - Factory: {}", factory);
		log.debug("  - Target class: {}", klassU);
		return mapper.map(source, factory);
	}

	@Override
	public Iterable<U> map(Iterable<T> sources) {
		log.debug("Mapping source of iterables");

		// Copy to list implementation, this is not a real lazy implementation since
		// original list must be copied in memory even if it is not already a collection
		// If original sources elements is already a collection, this is not really
		// important, since it is already in memory !
		List<T> list = Iterables.toList(sources);
		return new LazyUnmodifiableCollectionMapper<>(list, this);
	}

	@Override
	public <K> Map<K, U> map(Map<K, T> sources) {
		log.debug("Mapping source values of map object");

		Map<K, U> map = initMap(sources);
		for (Map.Entry<K, T> entry : sources.entrySet()) {
			final T source = entry.getValue();
			log.trace("  --> Mapping destination key from: {}", source);

			final U destination = map(source);
			log.trace("  --> Result: {}", destination);

			map.put(entry.getKey(), destination);
		}

		return map;
	}

	/**
	 * Build destination map.
	 * Source map is used to build destination map (to get target
	 * size).
	 *
	 * The default is to create an empty instance of {@link LinkedHashMap} to maintain
	 * original order of sources iteration.
	 *
	 * This method can be overridden to build map in another way.
	 *
	 * @param sources Source map.
	 * @param <K> Type of key in map.
	 * @return Destination map (empty).
	 */
	protected <K> Map<K, U> initMap(Map<K, T> sources) {
		log.debug("Initialize map of target objects");
		return new LinkedHashMap<>(sources.size());
	}
}
