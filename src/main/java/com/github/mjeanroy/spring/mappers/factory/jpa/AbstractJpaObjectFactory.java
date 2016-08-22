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

import com.github.mjeanroy.spring.mappers.commons.ClassUtils;
import com.github.mjeanroy.spring.mappers.factory.AbstractObjectFactory;
import com.github.mjeanroy.spring.mappers.factory.ObjectFactory;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static com.github.mjeanroy.spring.mappers.commons.PreConditions.notNull;

/**
 * Factory that can be used to create jpa entity.
 *
 * This factory is used as:
 *
 * - If object given in {@link #get(Object)} method is null, a new instance is
 *   created by reflection.
 * - Otherwise, method {@link #parseId(Object)} is used to get id of target entity.
 *
 * @param <T> Type of entities.
 * @param <PK> Type of entities' primary key.
 */
public abstract class AbstractJpaObjectFactory<T, PK extends Serializable> extends AbstractObjectFactory<T> implements ObjectFactory<T> {

	/**
	 * Class of entities' primary key.
	 */
	private final Class<PK> pkClass;

	/**
	 * Create new factory.
	 * Class of objects to create will be auto-detected at factory instantiation.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractJpaObjectFactory() {
		super();
		pkClass = (Class<PK>) ClassUtils.getGenericType(getClass(), 1);
	}

	/**
	 * Create new factory.
	 *
	 * @param klass Class of objects to create.
	 * @param pkClass Class of primary key of target entities.
	 */
	protected AbstractJpaObjectFactory(Class<T> klass, Class<PK> pkClass) {
		super(klass);
		this.pkClass = notNull(pkClass, "PK class must not be null");
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Object source) {
		if (source == null) {
			return null;
		}

		// Try to get id field
		final PK id = parseId(source);

		// Try to retrieve entity, or instantiate if id is null
		final T destination;
		if (id != null) {
			destination = findById(id);
		} else {
			destination = super.get(source);
		}

		return destination == null ?
				instantiate(source) : // Fail ?
				destination;
	}

	/**
	 * Get class of primary key of target class.
	 *
	 * @return Primary key class.
	 */
	protected Class<PK> getPkClass() {
		return pkClass;
	}

	/**
	 * Instantiate target object when target id is null or,
	 * it has not been found in persistent database.
	 *
	 * Default is to instantiate entity using default constructor.
	 *
	 * @param source Source object, can be used to instantiate target entity.
	 * @return Target entity.
	 */
	protected T instantiate(Object source) {
		return get(source);
	}

	/**
	 * Get entity by its id.
	 * Default is to return the result of {@link EntityManager#find(Class, Object)} method.
	 *
	 * @param id Target entity id.
	 * @return Target entity, null if entity is not found.
	 */
	protected T findById(PK id) {
		return getEntityManager().find(getTargetClass(), id);
	}

	/**
	 * Get entity manager to use to retrieve target jpa entities.
	 *
	 * @return Entity manager.
	 */
	protected abstract EntityManager getEntityManager();

	/**
	 * Parse id of target entity map source object.
	 * If returned id is null, target entity will be created using default
	 * constructor, otherwise id will be used with {@link javax.persistence.EntityManager#find(Class, Object)} method.
	 *
	 * @param source Source object.
	 * @return Id.
	 */
	protected abstract PK parseId(Object source);
}
