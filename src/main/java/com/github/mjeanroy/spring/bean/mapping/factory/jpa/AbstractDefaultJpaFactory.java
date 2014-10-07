package com.github.mjeanroy.spring.bean.mapping.factory.jpa;

import com.github.mjeanroy.spring.bean.mapping.factory.ObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;

/**
 * Default implementation for jpa factory.
 *
 * This implementation use application context to find appropriate entity manager
 * factory. This entity manager factory will be used to create entity manager.
 *
 * This implementation will try to use instance of {@link org.springframework.core.convert.ConversionService}
 * to get entity id from source object. This service will be found from
 * spring application context.
 *
 * This class is an abstract since it must be sub-classed to use default constructor.
 *
 * @param <T> Type of entity.
 * @param <PK> Type entity primary key.
 */
@Component
public abstract class AbstractDefaultJpaFactory<T, PK extends Serializable> extends AbstractJpaObjectFactory<T, PK> implements ObjectFactory<T>, ApplicationContextAware {

	/**
	 * Spring application context that will be used to retrieve
	 * entity manager factory.
	 *
	 * Volatile since it can be accessed by several thread
	 * Even if spring initialization should take care about thread safety, I keep volatile since
	 * factory can be created outside spring container.
	 */
	private volatile ApplicationContext appContext;

	/**
	 * Converter implementation that can be used to map source object
	 * to entity primary key value.
	 */
	protected final Converter<Object, PK> pkConverter;

	/**
	 * Build new jpa factory.
	 * Generic types will be detected at class instantiation.
	 */
	public AbstractDefaultJpaFactory() {
		super();
		this.pkConverter = null;
	}

	/**
	 * Build new jpa factory.
	 * Generic types will be detected at class instantiation.
	 *
	 * @param converter Explicit converter that will be used to get entity id from source.
	 */
	public AbstractDefaultJpaFactory(Converter<Object, PK> converter) {
		super();
		this.pkConverter = converter;
	}

	/**
	 * Build new jpa factory.
	 *
	 * @param klass Entity klass.
	 * @param pkClass Primary key class.
	 */
	public AbstractDefaultJpaFactory(Class<T> klass, Class<PK> pkClass) {
		super(klass, pkClass);
		this.pkConverter = null;
	}

	/**
	 * Build new jpa factory.
	 *
	 * @param klass Entity klass.
	 * @param pkClass Primary key class.
	 * @param converter Explicit converter that will be used to get entity id from source.
	 */
	public AbstractDefaultJpaFactory(Class<T> klass, Class<PK> pkClass, Converter<Object, PK> converter) {
		super(klass, pkClass);
		this.pkConverter = converter;
	}

	@Override
	protected EntityManager getEntityManager() {
		return getEmf().createEntityManager();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PK parseId(Object source) {
		final PK id;

		if (source == null) {
			id = null;
		}
		else if (source.getClass().equals(pkClass)) {
			id = (PK) source;
		}
		else if (pkConverter != null) {
			id = pkConverter.convert(source);
		}
		else {
			// Try to get conversion service as last chance
			ConversionService conversionService = appContext.getBean(ConversionService.class);
			if (conversionService != null && conversionService.canConvert(source.getClass(), pkClass)) {
				id = conversionService.convert(source, pkClass);
			}
			else {
				throw new UnsupportedOperationException("Unable to read id of target entity from source object");
			}
		}

		return id;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}

	/**
	 * Get instance of {@link EntityManagerFactory} retrieved from
	 * spring application context.
	 *
	 * This entity manager factory will be used to create entity manager instance.
	 *
	 * @return Entity manager factory.
	 */
	protected EntityManagerFactory getEmf() {
		return appContext.getBean(EntityManagerFactory.class);
	}
}
