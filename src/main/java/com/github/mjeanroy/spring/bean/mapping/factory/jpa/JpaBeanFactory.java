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

import com.github.mjeanroy.spring.bean.mapping.commons.Function;
import com.github.mjeanroy.spring.bean.mapping.factory.AbstractBeanFactory;

/**
 * Factory that can be used to create jpa entity.
 *
 * This factory is used as:
 *
 * - If object given in {@link #get(Object)} method is null, a new instance is
 *   created by reflection.
 *
 * - If object is not null and an instance of {@link com.github.mjeanroy.spring.bean.mapping.commons.Function} has been set in constructor,
 *   it will be called to get primary key value.
 *
 * - If object is not null and implements {@link IdentifiableObject}, primary key
 *   is obtained and result of {@link EntityManager#find(Class, Object)} is returned.
 *
 * @param <T> Type of entities.
 * @param <PK> Type of entities' primary key.
 */
public abstract class JpaBeanFactory<T, PK extends Serializable> extends AbstractBeanFactory<T> {

	/**
	 * Function that can be used to get primary key value
	 * used to find target entity.
	 */
	private final Function<Object, PK> function;

	/**
	 * Create new factory.
	 *
	 * Source objects must implements {@link IdentifiableObject} to get
	 * id of target object.
	 *
	 * Class of objects to create will be auto-detected at factory instantiation.
	 */
	public JpaBeanFactory() {
		super();
		this.function = null;
	}

	/**
	 * Create new factory.
	 *
	 * Source objects must implements {@link IdentifiableObject} to get
	 * id of target object.
	 *
	 * @param klass Class of objects to create.
	 */
	public JpaBeanFactory(Class<T> klass) {
		super(klass);
		this.function = null;
	}

	/**
	 * Create new factory.
	 * Given function will be used to get id of target entities.
	 * Class of objects to create will be auto-detected at factory instantiation.
	 *
	 * @param function Function.
	 */
	@SuppressWarnings("unchecked")
	public JpaBeanFactory(Function<? extends Object, PK> function) {
		super();
		this.function = (Function<Object, PK>) function;
	}

	/**
	 * Create new factory.
	 * Given function will be used to get id of target entities.
	 *
	 * @param klass Class of objects to create.
	 * @param function Function.
	 */
	@SuppressWarnings("unchecked")
	public JpaBeanFactory(Class<T> klass, Function<? extends Object, PK> function) {
		super(klass);
		this.function = (Function<Object, PK>) function;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Object source) {
		if (source == null) {
			return get();
		}

		final T destination;
		final PK id;

		// Try to get id field
		if (function != null) {
			id = function.apply(source);
		}
		else if (source instanceof IdentifiableObject) {
			id = ((IdentifiableObject<PK>) source).getId();
		}
		else {
			throw new UnsupportedOperationException("Unable to get jpa id from source object");
		}

		// Try to retrieve entity, or instantiate if id is null
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
}
