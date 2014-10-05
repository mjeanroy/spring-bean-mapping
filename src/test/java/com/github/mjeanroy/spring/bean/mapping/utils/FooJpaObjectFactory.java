package com.github.mjeanroy.spring.bean.mapping.utils;

import com.github.mjeanroy.spring.bean.mapping.commons.Function;
import com.github.mjeanroy.spring.bean.mapping.factory.jpa.JpaObjectFactory;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;

public class FooJpaObjectFactory extends JpaObjectFactory<Foo, Long> {

	public final EntityManager entityManager;

	public FooJpaObjectFactory() {
		entityManager = mock(EntityManager.class);
	}

	public FooJpaObjectFactory(Function<FooDto, Long> function) {
		super(function);
		entityManager = mock(EntityManager.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
