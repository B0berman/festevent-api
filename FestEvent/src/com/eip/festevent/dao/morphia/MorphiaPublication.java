package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DataBase;
import com.eip.festevent.beans.Publication;

public class MorphiaPublication extends MorphiaDAO<Publication> {
	public MorphiaPublication(DataBase db) {
		super(db);
	}
}
