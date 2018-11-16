package com.eip.festevent.dao.morphia;

import com.eip.festevent.utils.Utils;
import com.google.common.collect.Lists;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.List;


@Provider
public class QueryValidationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
/*            QueriesAllowed queryAnnotation = method.getAnnotation(QueriesAllowed.class);
            if (queryAnnotation == null)
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response(keys)).build());
            List<String> fieldsAllowed = Lists.newArrayList(queryAnnotation.fields());
            List<String> operatorsAllowed = Lists.newArrayList(queryAnnotation.operators());
            String keys = method.getParameters()[0].toString();
            for (String key : keys) {
                if (!key.contains(" ") && !key.contains("limit") && !key.contains("offset"))
                    key.concat(" =");
                if ((key.contains(" ") && (!fieldsAllowed.contains(key.split(" ")[0]) || !operatorsAllowed.contains(key.split(" ")[1]))) ||
                        !operatorsAllowed.contains(key))
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response("Authentication required : no token found")).build());
        }*/
    }
}

