package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DataBase;
import com.eip.festevent.beans.Event;

public class MorphiaEvent  extends MorphiaDAO<Event> {
	public MorphiaEvent(DataBase db) {
		super(db);
	}
}
