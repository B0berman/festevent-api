package com.eip.festevent.dao.morphia;

import com.eip.festevent.dao.DAO;
import com.google.common.collect.Lists;

import java.util.List;

public class QueryHelper<T> {

    protected DAO<T> dao;
    protected List<String> keys;
    protected List<String> values;

    public QueryHelper(DAO<T> dao, List<String> keys, List<String> values) {
        this.dao = dao;
        this.keys = keys;
        this.values = values;
    }

    public boolean isValidQuery(Class<T> c) {
        if (c.isAnnotationPresent(QueriesAllowed.class)) {
            List<String> allowed = Lists.newArrayList(c.getAnnotation(QueriesAllowed.class).values());

            for (String key : keys) {
                if (!key.contains(" ") && !key.contains("limit") && !key.contains("offset"))
                    key.concat(" =");
                if (key.contains(" ") && !allowed.contains(key.split(" ")[0]))
                    return false;
            }
        }
        return true;
    }

    public void performQueries() {
        int i = 0;
        for (String key : keys) {
            String[] split = key.split(" ");
            if (key.equals("limit"))
                dao = dao.limit(new Integer(values.get(i)).intValue());
            if (key.equals("offset"))
                dao = dao.offset(new Integer(values.get(i)).intValue());
            else
                switch (split[1]) {
                    case "contains":
                        dao = dao.contains(split[0], values.get(i));
                        break;
                    case "order":
                        if (values.get(i).equals("ASC"))
                            dao = dao.order(split[0]);
                        else if (values.get(i).equals("DESC"))
                            dao = dao.order("-" + split[0]);
                        break;
                    default:
                        dao = dao.filter(key, values.get(i));
                }
            i++;
        }
    }

    public DAO<T> getDao() {
        return dao;
    }
}
