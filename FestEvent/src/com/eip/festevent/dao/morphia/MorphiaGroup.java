package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DataBase;
import com.eip.festevent.beans.Group;

public class MorphiaGroup  extends MorphiaDAO<Group> {
    public MorphiaGroup(DataBase db) {
        super(db);
    }
}