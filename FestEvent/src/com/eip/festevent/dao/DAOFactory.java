package com.eip.festevent.dao;

import com.eip.festevent.beans.*;

public abstract class DAOFactory {
	
	protected DataBase db;

	public DAOFactory(DataBase db) {
		this.db = db;
	}

	public abstract DAO<User> getUserDAO();
	public abstract DAO<Publication> getPublicationDAO();
	public abstract DAO<Event> getEventDAO();
	public abstract DAO<Ticket> getTicketDAO();
	public abstract DAO<Group> getGroupDAO();
//	public abstract DAO<StaffGroup> getStaffGroupDAO();
//	public abstract DAO<StaffMember> getStaffMemberDAO();
}