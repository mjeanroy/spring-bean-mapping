package com.github.mjeanroy.spring.bean.mapping.utils;

import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;

import com.github.mjeanroy.spring.bean.mapping.commons.Function;
import com.github.mjeanroy.spring.bean.mapping.factory.jpa.JpaBeanFactory;

public class FooJpaBeanFactory extends JpaBeanFactory<Foo, Long> {

	public final EntityManager entityManager;

	public FooJpaBeanFactory() {
		entityManager = mock(EntityManager.class);
	}

	public FooJpaBeanFactory(Function<FooDto, Long> function) {
		super(function);
		entityManager = mock(EntityManager.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
