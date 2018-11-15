package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DAO;
import com.eip.festevent.dao.DAOFactory;
import com.eip.festevent.beans.*;

public class MorphiaDAOFactory extends DAOFactory {
	public MorphiaDAOFactory(MorphiaDB db) {
		super(db);
}

	@Override
	public DAO<User> getUserDAO() {
		return new MorphiaUser(db);
	}

	@Override
	public DAO<Publication> getPublicationDAO() {
		return new MorphiaPublication(db);
	}


	@Override
	public DAO<Event> getEventDAO() {
		return new MorphiaEvent(db);
	}

	@Override
	public DAO<Ticket> getTicketDAO() {
		return new MorphiaTicket(db);
	}

	@Override
	public DAO<Group> getGroupDAO() {
		return new MorphiaGroup(db);
	}

/*	@Override
	public DAO<StaffGroup> getStaffGroupDAO() {
		// TODO Auto-generated method stub
		return new MorphiaStaffGroup(db);
	}

	@Override
	public DAO<StaffMember> getStaffMemberDAO() {
		// TODO Auto-generated method stub
		return new MorphiaStaffMember(db);
	}*/
	
	
}
