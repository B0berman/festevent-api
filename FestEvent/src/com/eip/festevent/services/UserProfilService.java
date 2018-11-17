package com.eip.festevent.services;

import com.eip.festevent.authentication.Authenticated;
import com.eip.festevent.beans.Media;
import com.eip.festevent.beans.Publication;
import com.eip.festevent.beans.User;
import com.eip.festevent.dao.DAO;
import com.eip.festevent.dao.DAOManager;
import com.eip.festevent.dao.morphia.QueryHelper;
import com.eip.festevent.utils.RandomString;
import com.eip.festevent.utils.Utils;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.mindrot.jbcrypt.BCrypt;


import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(tags = {"Users"})
@Path("/profil")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserProfilService {

    @GET
    @Path("/all")
    public Response getUsers() {
        List<User> list = DAOManager.getFactory().getUserDAO().getAll();
        return Response.ok(list).build();
    }

    @GET
    @Authenticated
    @Path("/research")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Search user", response = User.class, responseContainer = "List", notes = "Queries :\\n keys: \"email, firstName, lastName\"\\nvalues: \"contains, order, limit, offset, =\"")
    public Response searchUser(@ApiParam(value = "filter keys", required = true) @QueryParam("key") List<String> keys,
                                            @ApiParam(value = "filter values", required = true) @QueryParam("value") List<String> values,
                                            @ApiParam(value = "token of sender", required = true) @HeaderParam("token") final String token) {
        DAO<User> dao = DAOManager.getFactory().getUserDAO();
        QueryHelper<User> helper = new QueryHelper<User>(dao, keys, values);
        if (helper.isValidQuery(User.class))
            helper.performQueries();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid queries.")).build();
        List<User> result = helper.getDao().getAll();
        return Response.ok(result).build();
    }

    @GET
    @Path("/publications")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user publicatons.", response = Publication.class, responseContainer = "List")
    public Response getUserPublications(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();

        List<Publication> publications = DAOManager.getFactory().getPublicationDAO().filter("publisher", user).getAll();
        return Response.ok(publications).build();
    }


    @GET
    @Path("/admins")
    @Authenticated
    @RolesAllowed("ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get all user administrators", response = User.class, responseContainer = "List", notes = "Returns a list of User.")
    public Response getAdmins(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        List<User> admins = DAOManager.getFactory().getUserDAO().filter("role", "ADMIN").getAll();
        return Response.ok(admins).build();
    }

    @GET
    @Path("/friends")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user friends.", response = User.class, responseContainer = "List", notes = "Returns a list of Profil.")
    public Response getFriends(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        List<User> result = DAOManager.getFactory().getUserDAO().filter("friends in", user.getEmail()).getAll();
        // get friends
        return Response.ok(result).build();
    }

    @GET
    @Path("/pictures")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user pictures.", response = Media.class, responseContainer = "List", notes = "Returns a list of Media.")
    public Response getPictures(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        return Response.ok(user.getPictures()).build();
    }

    @GET
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user profil with token.", response = User.class)
    public Response getUser(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token) {
        User user = DAOManager.getFactory().getUserDAO()
                .filter("accessToken", token)
                .getFirst();
        return Response.ok(user).build();
    }

    @POST
    @Authenticated
    @Path("/profil-image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "set user profil image.", response = Response.class)
    public Response setProfilImage(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token,
                                   @ApiParam(value = "Media object", required = true) final Media media) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        if (Utils.writeToFileServer(media.getBytes(), media.getId()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Image upload failed.")).build();
        user.removePicture(user.getProfilPicture());
        media.setUrl("92.222.82.30:8080/eip-dev/resources/image" + media.getId());
        user.setProfilPicture(media);
        DAOManager.getFactory().getUserDAO().push(user);
        return Response.ok(media).build();
    }

    @PUT
    @Path("/password-forget")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request") } )
    @ApiOperation(value = "Reset lost password.", response = Response.class)
    public Response resetPassword(@QueryParam("email") final String email, @QueryParam("login") final String login) {
        User user = DAOManager.getFactory().getUserDAO().filter("authentication.login", login).getFirst();
        if (user == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid login")).build();
        if (!user.getEmail().equals(email))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Login and email don't match")).build();
        RandomString random = new RandomString(8);
        String newPwd = random.nextString();

        String salt = BCrypt.gensalt(12);
        String hashed = BCrypt.hashpw(newPwd, salt);
        user.setPassword(hashed);
        DAOManager.getFactory().getUserDAO().push(user);

        String res = Utils.sendResetPwdMail(email, "Dear " + user.getFirstName()
                + ",\n\n\tHere is your new password : " + newPwd + "\n\n Do not give your password or login.\nThe team Festevent thanks you!");
        return Response.ok(new Utils.Response(res)).build();
    }

    //Modification du user
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Modify user informations", response = Response.class)
    public Response setUser(final User user, @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token) {
        User user2 = DAOManager.getFactory().getUserDAO()
                .filter("accessToken", token)
                .getFirst();
        List<String> errors = Lists.newArrayList();
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            user2.setLastName(user.getLastName());
            errors.add("Incorrect or empty LastName");
        }
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            user2.setFirstName(user.getFirstName());
            errors.add("Incorrect or empty FirstName");
        }
        user2.setLocationLatitude(user.getLocationLatitude());
        user2.setLocationLongitude(user.getLocationLongitude());
        if (user.getEmail() != null && !user.getEmail().isEmpty() && Utils.checkEmail(user.getEmail())) {
            user2.setEmail(user.getEmail());
        } else {
            errors.add("Incorrect or empty email");
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            user2.setPhone(user.getPhone());
        }
        if (user.getBirthdate() != null)
            user2.setBirthdate(user.getBirthdate());
        if (user.getLocationLatitude() != 0.0)
            user2.setLocationLatitude(user.getLocationLatitude());
        if (user.getLocationLongitude() != 0.0)
            user2.setLocationLongitude(user.getLocationLongitude());
        if (user.getProfilPicture() != null)
            user2.setProfilPicture(user.getProfilPicture());

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        DAOManager.getFactory().getUserDAO().push(user2);
        return Response.ok().build();
    }

    //Suppression du user
    @DELETE
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Delete sender account.", response = Response.class)
    public Response deleteUser(@ApiParam(value = "Token of sender", required = true) @HeaderParam("accessToken") final String token) {
        User user = DAOManager.getFactory().getUserDAO()
                .filter("accessToken", token)
                .getFirst();
        // delete in friends
        List<User> users = DAOManager.getFactory().getUserDAO().filter("friends in", user.getEmail()).getAll();
        for (User u : users) {
            u.removeFriend(user.getEmail());
            DAOManager.getFactory().getUserDAO().push(u);
        }
        DAOManager.getFactory().getEventDAO().filter("creator", user).clear();
        DAOManager.getFactory().getPublicationDAO().filter("publisher", user).clear();
        DAOManager.getFactory().getUserDAO().remove(user);
        return Response.ok().build();
    }
}
