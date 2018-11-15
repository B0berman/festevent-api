package com.eip.festevent.dao;


import com.eip.festevent.dao.morphia.MorphiaDAOFactory;
import com.eip.festevent.dao.morphia.MorphiaDB;

public final class DAOManager {

	private final static DAOManager INSTANCE = new DAOManager();
	
	private final MorphiaDB morphiaDB;
	private final MorphiaDAOFactory morphiaDAOFactory;
	
	private DAOManager() {
		morphiaDB = new MorphiaDB();
		morphiaDB.connect();
		morphiaDAOFactory = new MorphiaDAOFactory(morphiaDB);
	}
	
	public static DataBase getDB() {
		return INSTANCE.morphiaDB;
	}


	public static DAOFactory getFactory() {
		return INSTANCE.morphiaDAOFactory;
	}

}