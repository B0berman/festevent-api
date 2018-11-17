package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DataBase;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaDB implements DataBase {

    protected Morphia morphia;
    protected MongoClient client;
    private Datastore datastore;

    protected String		url = "localhost";
    protected String		dbName = "dbeip";

    public boolean connect() {
        client = new MongoClient(url);
        morphia = new Morphia();
        datastore = morphia.createDatastore(client, dbName);
        return true;
    }

    public boolean disconnect() {
        client.close();
        return true;
    }

    public Datastore getDatastore() {
        return datastore;
    }

}