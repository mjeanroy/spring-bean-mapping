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

package com.github.mjeanroy.spring.bean.mapping.factory.jpa;

import javax.persistence.EntityManager;
import java.io.Serializable;

import com.github.mjeanroy.spring.bean.mapping.factory.AbstractObjectFactory;

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
public abstract class AbstractJpaObjectFactory<T, PK extends Serializable> extends AbstractObjectFactory<T> {

	/**
	 * Create new factory.
	 *
	 * Source objects must implements {@link IdentifiableObject} to get
	 * id of target object.
	 *
	 * Class of objects to create will be auto-detected at factory instantiation.
	 */
	public AbstractJpaObjectFactory() {
		super();
	}

	/**
	 * Create new factory.
	 *
	 * Source objects must implements {@link IdentifiableObject} to get
	 * id of target object.
	 *
	 * @param klass Class of objects to create.
	 */
	public AbstractJpaObjectFactory(Class<T> klass) {
		super(klass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Object source) {
		if (source == null) {
			return get();
		}

		// Try to get id field
		final PK id = parseId(source);

		// Try to retrieve entity, or instantiate if id is null
		final T destination;
		if (id != null) {
			destination = findById(id);
		} else {
			destination = instantiate(source);
		}

		return destination == null ?
				instantiate(source) : // Fail ?
				destination;
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
		return get();
	}

	/**
	 * Get entity by its id.
	 * Default is to return the result of {@link EntityManager#find(Class, Object)} method.
	 *
	 * @param id Target entity id.
	 * @return Target entity, null if entity is not found.
	 */
	protected T findById(PK id) {
		return getEntityManager().find(klass, id);
	}

	/**
	 * Get entity manager to use to retrieve target jpa entities.
	 *
	 * @return Entity manager.
	 */
	protected abstract EntityManager getEntityManager();

	/**
	 * Parse id of target entity from source object.
	 * If returned id is null, target entity will be created using default
	 * constructor, otherwise id will be used with {@link javax.persistence.EntityManager#find(Class, Object)} method.
	 *
	 * @param source Source object.
	 * @return Id.
	 */
	protected abstract PK parseId(Object source);
}
