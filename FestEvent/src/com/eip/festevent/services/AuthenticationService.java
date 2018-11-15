package com.eip.festevent.services;

import com.eip.festevent.utils.Utils;
import com.eip.festevent.beans.User;
import com.eip.festevent.dao.DAOManager;
import io.swagger.annotations.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;


@Api(tags = {"Authentication"})
@Produces(MediaType.APPLICATION_JSON)
@Path("/signin")
public class AuthenticationService {

	@POST
	@ApiResponses(value = { 
			@ApiResponse(code = 202, message = "Accepted", responseHeaders = @ResponseHeader(name = "accessToken", description = "Basic Auth token", response = String.class)),
			@ApiResponse(code = 400, message = "Bad request") })
	@ApiOperation(value = "Sign in to FestEvent.", response = Response.class)
	public Response singIn(@ApiParam(value = "{\"credential\":\"login:password\"}", required = true)  @HeaderParam("Authorization") final String authCredentials) {
		if (authCredentials == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		final String encodedUserPassword = authCredentials.replaceFirst("Basic"+ " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		String login = "";
		String password = "";
		if (tokenizer.hasMoreTokens()) {
			login = tokenizer.nextToken();
		} if (tokenizer.hasMoreTokens()) {
			password = tokenizer.nextToken();
		}

		if (!Utils.checkPassword(password)) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		User user = DAOManager.getFactory().getUserDAO().filter("authentication.login", login).filter("deleted", false).getFirst();
		if (user == null) {
			return Response.status(Status.BAD_REQUEST).entity(new Utils.Response("This user does not exist.")).build();
		}
		if (BCrypt.checkpw(password, user.getPassword())) {
			user.signIn();
			DAOManager.getFactory().getUserDAO().push(user);
			return Response.status(Status.ACCEPTED).header("accessToken", user.getAccessToken()).build();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}
}
