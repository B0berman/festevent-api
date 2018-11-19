package com.visitcardpro.beans;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class Service {


    // Covoit, Logement, geoloc (boussole, rdv), alert, billeterie
    @Id
    protected String id = ObjectId.get().toString();

    protected String name;
    protected boolean activeBeforeStart = true;

    @Reference
    protected Event event;
}
