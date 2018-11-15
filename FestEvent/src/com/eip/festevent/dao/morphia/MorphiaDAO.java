package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DAO;
import com.eip.festevent.dao.DataBase;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class MorphiaDAO<T> extends BasicDAO<T, ObjectId> implements DAO<T> {
	
	protected Query<T> query;
	
	protected MorphiaDAO(DataBase db) {
		super(((MorphiaDB)db).getDatastore());
		query = super.createQuery();
	}

	public void clear() {
		List<T> list = query.asList();
		for (T entity: list) {
			super.delete(entity);
		}
	}
	
	public boolean push(T bean) {
		super.save(bean);
		return true;
	}

	public boolean remove(T bean) {
		super.delete(bean);
		return true;
	}

	public List<T> getAll() {
		return query.asList();
	}

	public T getFirst() {
		return query.get();
	}

	public <K> DAO<T> filter(String query, K value) {
		this.query = this.query.filter(query, value);
		return this;
	}

	public <K> DAO<T> order(String query) {
		this.query = this.query.order(query);
		return this;
	}

	public <K> DAO<T> contains(String field, String query) {
		this.query = this.query.field(field).contains(query);
		return this;
	}

	public <K> DAO<T> limit(int value) {
		this.query = this.query.limit(value);
		return this;
	}

	public <K> DAO<T> offset(int value) {
		this.query = this.query.offset(value);
		return this;
	}

	public <K> DAO<T> or(String field, Object value1, Object value2) {
		this.query.or(
				this.query.criteria(field).equal(value1),
				this.query.criteria(field).equal(value2));
		return this;
	}

	public Query<T> retrievedFields(boolean tf, String value) {
		return this.query.retrievedFields(tf, value);
	}
}
