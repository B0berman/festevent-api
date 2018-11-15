package com.eip.festevent.services;

import com.eip.festevent.authentication.Authenticated;
import com.eip.festevent.beans.FriendRequest;
import com.eip.festevent.beans.User;
import com.eip.festevent.dao.DAOManager;
import com.eip.festevent.utils.Utils;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(tags = {"Friends"})
@Path("/friends")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FriendService {

    @POST
    @Authenticated
    @Path("/request-cancel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Cancel a friend request.", response = Response.class)
    public Response cancelFriendRequest(@ApiParam(value = "email of target", required = true) @QueryParam("email") String email,
                                        @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        for (FriendRequest fr : sender.getFriendsRequestsSent()) {
            if (fr.getTarget().equals(email)) {
                User target = DAOManager.getFactory().getUserDAO().filter("email", email).getFirst();
                if (target == null)
                    return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong email.")).build();
                target.rmFriendRequestReceived(sender.getEmail());
                DAOManager.getFactory().getUserDAO().push(target);
                sender.rmFriendRequestSent(email);
                DAOManager.getFactory().getUserDAO().push(sender);
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Request not found.")).build();
    }

    @GET
    @Path("/requests-sent")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user friend requests sent.", response = FriendRequest.class, responseContainer = "List")
    public Response getFriendsRequestsSent(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        return Response.ok(user.getFriendsRequestsSent()).build();
    }

    @GET
    @Path("/requests-received")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user friend requests received.", response = FriendRequest.class, responseContainer = "List")
    public Response getFriendsRequestsReceived(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        return Response.ok(user.getFriendsRequestsReceived()).build();
    }

    @POST
    @Path("/request")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Send friend request to a user.", response = Response.class)
    public Response newFriendRequest(@ApiParam(value = "email of target", required = true) @QueryParam("email") String email,
                                     @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        User user = DAOManager.getFactory().getUserDAO().filter("email", email).getFirst();
        if (user == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong email.")).build();
        if (user.equals(sender))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("You cannot be your friend.")).build();
        if (sender.getFriends().contains(user.getEmail()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Cannot add a friend you already have.")).build();
        if (sender.getFriendsRequestsSent().contains(user.getEmail()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Request already sent.")).build();

        sender.addFriendRequestSent(user.getEmail());
        user.addFriendRequestReceived(sender.getEmail());
        DAOManager.getFactory().getUserDAO().push(sender);
        DAOManager.getFactory().getUserDAO().push(user);
        // Notification
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/request-answer")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Answer to a friend request.", response = Response.class)
    public Response newFriendAnswer(@ApiParam(value = "Email of friend request sender", required = true) @QueryParam("email") String email,
                                    @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                    @ApiParam(value = "Request answer", required = true) @QueryParam("answer") boolean answer) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        User user = DAOManager.getFactory().getUserDAO().filter("email", email).getFirst();
        if (user == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong email.")).build();
        if (!user.rmFriendRequestSent(sender.getEmail()) ||
                !sender.rmFriendRequestReceived(user.getEmail()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Request not found")).build();
        if (answer) {
            sender.addFriend(user.getEmail());
            user.addFriend(sender.getEmail());
            DAOManager.getFactory().getUserDAO().push(sender);
            DAOManager.getFactory().getUserDAO().push(user);
        }
        DAOManager.getFactory().getUserDAO().push(sender);
        DAOManager.getFactory().getUserDAO().push(user);
        return Response.ok().build();
    }

    @DELETE
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Delete user friend by email.", response = Response.class)
    public Response deleteFriend(@ApiParam(value = "email of friend to delete", required = true) @QueryParam("email") String email,
                                 @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        User user  = DAOManager.getFactory().getUserDAO().filter("email", email).getFirst();
        if (user == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("wrong email")).build();
        user.removeFriend(sender.getEmail());
        sender.removeFriend(user.getEmail());
        DAOManager.getFactory().getUserDAO().push(sender);
        DAOManager.getFactory().getUserDAO().push(user);
        return Response.ok().build();
    }
}
