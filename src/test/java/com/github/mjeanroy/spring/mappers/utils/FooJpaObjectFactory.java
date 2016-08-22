package com.github.mjeanroy.spring.mappers.utils;

import com.github.mjeanroy.spring.mappers.factory.jpa.AbstractJpaObjectFactory;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;

public class FooJpaObjectFactory extends AbstractJpaObjectFactory<Foo, FooDto, Long> {

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
