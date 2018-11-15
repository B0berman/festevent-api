package com.eip.festevent.services;

import com.eip.festevent.authentication.Authenticated;
import com.eip.festevent.beans.Event;
import com.eip.festevent.beans.Group;
import com.eip.festevent.beans.User;
import com.eip.festevent.dao.DAOManager;
import com.eip.festevent.utils.Utils;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(tags = {"Groups"})
@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupService {

    @GET
    @Path("/all")
    public List<Group> getGroups() {
        return DAOManager.getFactory().getGroupDAO().getAll();
    }

    @GET
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user groups.", response = Event.class, responseContainer = "List")
    public Response getUserGroups(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        List<Group> result = DAOManager.getFactory().getGroupDAO().filter("members in", user).getAll();
        if (result.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).entity(new Utils.Response("User doesn't have any group.")).build();
        return Response.ok(result).build();
    }

    @GET
    @Path("/members")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get group members.", response = Event.class, responseContainer = "List")
    public Response getMembers(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                              @ApiParam(value = "id of event", required = true) @QueryParam("id") String id) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        Group grp = DAOManager.getFactory().getGroupDAO().filter("id", id)/*filter("members in", user)*/.getFirst();
 //       if (!grp.getMembers().contains(user))
 //           return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("You're not member of the group.")).build();
        return Response.ok(grp.getMembers()).build();
    }

    @PUT
    @Path("/member/add")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Add member to a group.", response = Event.class, responseContainer = "List")
    public Response addMember(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                              @ApiParam(value = "id of group", required = true) @QueryParam("id") String gid,
                              @ApiParam(value = "email of user", required = true) @QueryParam("email") String email) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        User user = DAOManager.getFactory().getUserDAO().filter("email", email).getFirst();
        if (user == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid email.")).build();
        Group grp = DAOManager.getFactory().getGroupDAO().filter("id", gid)/*filter("members in", user)*/.getFirst();
        if (!grp.getMembers().contains(sender))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("You're not member of the group.")).build();
        if (grp.getMembers().contains(user))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("User is already in this group.")).build();

        grp.addMember(user);
        DAOManager.getFactory().getGroupDAO().push(grp);
        return Response.ok().build();
    }

    @POST
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Create a new group.", response = Response.class)
    public Response createGroup(final Group entity, @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        if (entity == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Please fill the event fields.")).build();
        if (entity.getName().isEmpty() || entity.getName() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Please fill the name fields.")).build();
        entity.addMember(user);
        entity.setCreator(user);
        DAOManager.getFactory().getGroupDAO().push(entity);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/leave")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request") })
    @ApiOperation(value = "Leave a group.", response = Response.class)
    public Response leaveGroup(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                               @ApiParam(value = "id of event", required = true) @QueryParam("id") String id) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        Group grp = DAOManager.getFactory().getGroupDAO().filter("id", id)/*filter("members in", user)*/.getFirst();
        if (!grp.getMembers().contains(user))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("You're not member of the group.")).build();
        grp.removeMember(user);
        return Response.ok().build();
    }

    @DELETE
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Delete a group.", response = Response.class)
    public Response deleteGroup(final Group entity) {
        DAOManager.getFactory().getGroupDAO().remove(entity);
        return Response.status(Response.Status.OK).build();
    }

}
