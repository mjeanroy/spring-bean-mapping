package com.github.mjeanroy.spring.mappers.utils;

import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;

import com.github.mjeanroy.spring.mappers.factory.jpa.AbstractJpaObjectFactory;

public class FooJpaObjectFactory extends AbstractJpaObjectFactory<Foo, Long> {

	public final EntityManager entityManager;

	public FooJpaObjectFactory() {
		entityManager = mock(EntityManager.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	protected Long parseId(Object source) {
		return ((FooDto) source).getId();
	}
}
