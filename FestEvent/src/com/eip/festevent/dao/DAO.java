package com.eip.festevent.dao;

import org.mongodb.morphia.query.Query;

import java.util.List;


public interface DAO<T> {
	boolean push(T bean);
	boolean remove(T bean);
	
	List<T> getAll();
	T getFirst();
	<K> DAO<T> filter(String query, K value);
	void clear();
	Query<T> retrievedFields(boolean query, String value);
	<K> DAO<T> order(String query);
	<K> DAO<T> contains(String field, String query);
	<K> DAO<T> limit(int value);
	<K> DAO<T> offset(int value);
	<K> DAO<T> or(String field, Object value1, Object value2);
}