package com.eip.festevent.services;


import com.eip.festevent.beans.Media;
import com.eip.festevent.beans.User;
import com.eip.festevent.dao.DAOManager;
import com.eip.festevent.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(tags = {"Signup"})
@Path("/signup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegisterService {

    @POST
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 201, message = "Created") })
    @ApiOperation(value = "Register a new user.", response = Response.class)
    public Response createUser(final User user) {

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Empty request.")).build();
        }

        if (!Utils.checkEmail(user.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong email.")).build();
        }
        Utils.normalizeEmail(user.getEmail());

        if (!Utils.checkProperNoun(user.getFirstName())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response( "Wrong name.")).build();
        }
        Utils.normalizeProperNoun(user.getFirstName());

        if (!Utils.checkProperNoun(user.getLastName())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong name.")).build();
        }
        Utils.normalizeProperNoun(user.getLastName());

        if (!Utils.checkPassword(user.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong password.")).build();
        }

        User existingUser = DAOManager.getFactory().getUserDAO()
                .filter("email", user.getEmail())
                .getFirst();
        if (existingUser != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("This email is already taken." + user.getPassword())).build();
        }

        if (user.getProfilPicture() != null) {
            if (Utils.writeToFileServer(user.getProfilPicture().getBytes(), user.getProfilPicture().getId()))
                return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Image upload failed.")).build();
            user.getProfilPicture().setUrl("92.222.82.30:8080/eip-dev/resources/image" + user.getProfilPicture().getId());
        }
        String salt = BCrypt.gensalt(12);
        String password = user.getPassword();
        String hashed = BCrypt.hashpw(password, salt);

        user.setPassword(hashed);
        if (user.getRole() == null || user.getRole().isEmpty())
            user.setRole("STANDARD");
        user.signIn();
        if (!user.getEmail().equals(user.getEmail()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("email in profil and user must be equals.")).build();
        DAOManager.getFactory().getUserDAO().push(user);
        return Response.status(Response.Status.CREATED).header("accessToken", user.getAccessToken()).build();
    }
}
