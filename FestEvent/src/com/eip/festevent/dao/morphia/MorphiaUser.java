package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DataBase;
import com.eip.festevent.beans.User;

public class MorphiaUser extends MorphiaDAO<User> {
	public MorphiaUser(DataBase db) {
		super(db);
	}
}
