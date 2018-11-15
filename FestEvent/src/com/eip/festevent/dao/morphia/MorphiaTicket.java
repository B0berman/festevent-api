package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DataBase;
import com.eip.festevent.beans.Ticket;

public class MorphiaTicket extends MorphiaDAO<Ticket> {
	public MorphiaTicket(DataBase db) {
		super(db);
	}

}
