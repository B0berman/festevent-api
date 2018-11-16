package com.eip.festevent.authentication;

import com.eip.festevent.utils.Utils;
import com.eip.festevent.beans.User;
import com.eip.festevent.dao.DAOManager;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;


@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method method = resourceInfo.getResourceMethod();
		if (method.isAnnotationPresent(Authenticated.class)) {
			String roleAllowed = "ALL";
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				RolesAllowed roleAnnotation = method.getAnnotation(RolesAllowed.class);
				roleAllowed = roleAnnotation.value()[0];
			}


			String token = requestContext.getHeaderString("token");
			User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
			if (token == null) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response("Authentication required : no token found")).build());
			}
			else if (user == null) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response("Wrong access token.")).build());
			}
			else if (!roleAllowed.equals("ALL") && !roleAllowed.equals(user.getRole())) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response("Permission denied : " + roleAllowed + " allowed only.")).build());
			}	
		}
	}
}