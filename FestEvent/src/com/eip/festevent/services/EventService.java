package com.eip.festevent.services;


import com.eip.festevent.beans.*;
import com.eip.festevent.dao.DAO;
import com.eip.festevent.dao.morphia.QueryHelper;
import com.eip.festevent.utils.Utils;
import com.eip.festevent.authentication.Authenticated;
import com.eip.festevent.dao.DAOManager;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(tags = {"Events"})
@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventService {
    @GET
    @Path("/all")
    public List<Event> getEvents() {
        return DAOManager.getFactory().getEventDAO().getAll();
    }

    @GET
    @Authenticated
    @Path("/user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get events created by a user.", response = Event.class, responseContainer = "List")
    public Response getUserEvents(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        List<Event> list = DAOManager.getFactory().getEventDAO().filter("creator", user).getAll();
        if (list == null || list.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(list).build();
    }

    @GET
    @Authenticated
    @Path("/research")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get events created by a user.", response = Event.class, responseContainer = "List", notes = "Queries :\\n keys: \"email, firstName, lastName\"\\nvalues: \"contains, order, limit, offset, =, >, <\"")
    public Response searchEvents(@ApiParam(value = "filter keys", required = true) @QueryParam("key") List<String> keys,
                                  @ApiParam(value = "filter values", required = true) @QueryParam("value") List<String> values,
                                  @ApiParam(value = "token of sender", required = true) @HeaderParam("token") final String token) {
        DAO<Event> dao = DAOManager.getFactory().getEventDAO();
        QueryHelper<Event> helper = new QueryHelper<Event>(dao, keys, values);
        if (helper.isValidQuery(Event.class))
            helper.performQueries();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid queries.")).build();
        List<Event> result = helper.getDao().getAll();
        // Recherche à faire
        return Response.ok(result).build();
    }

    @GET
    @Path("/participants")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get event participants.", response = User.class, responseContainer = "List")
    public Response getEventParticipants(@ApiParam(value = "id of event", required = true) @QueryParam("id") String id) {
        Event event = DAOManager.getFactory().getEventDAO().filter("id", id).getFirst();
        if (event == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        List<Ticket> tickets = DAOManager.getFactory().getTicketDAO().filter("event", event).getAll();
        if (tickets.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).entity(new Utils.Response("No participants for this event")).build();
        List<User> result = Lists.newArrayList();
        for (Ticket ticket : tickets) {
            result.add(ticket.getOwner());
        }
        return Response.ok(result).build();
    }

    @GET
    @Path("/pictures")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get event pictures.", response = Media.class, responseContainer = "List")
    public Response getEventPictures(@ApiParam(value = "id of event", required = true) @QueryParam("id") String id) {
        Event event = DAOManager.getFactory().getEventDAO().filter("id", id).getFirst();
        if (event == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        List<Media> result = event.getPictures();
        if (result == null) {
            result = Lists.newArrayList();
            event.setPictures(result);
            DAOManager.getFactory().getEventDAO().push(event);
        }
        return Response.ok(event.getPictures()).build();
    }

    @GET
    @Path("/publications")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get event pictures.", response = Publication.class, responseContainer = "List")
    public Response getEventPublications(@ApiParam(value = "id of event", required = true) @QueryParam("id") String id) {
        Event event = DAOManager.getFactory().getEventDAO().filter("id", id).getFirst();
        if (event == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        List<Publication> result = DAOManager.getFactory().getPublicationDAO().filter("event.id", id).getAll();
        return Response.ok(result).build();
    }

    @GET
    @Path("/tickets")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user tickets.", response = Ticket.class, responseContainer = "List")
    public Response getOnParticipateEvents(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        List<Ticket> result = DAOManager.getFactory().getTicketDAO().filter("owner", sender).getAll();
        if (result.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).entity(new Utils.Response("User doesn't participate to any event")).build();
        return Response.ok(result).build();
    }

    @PUT
    @Authenticated
    @Path("/participate")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Participate to an event.", response = Response.class)
    public Response participate(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token,
                                @ApiParam(value = "id of an event", required = true) @QueryParam("id") final String id) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        Event e = DAOManager.getFactory().getEventDAO().filter("id", id).getFirst();

        if (e == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();

        Ticket ticket = new Ticket();
        ticket.setOwner(user);
        ticket.setEvent(e);
        DAOManager.getFactory().getTicketDAO().push(ticket);
        return Response.ok().build();
    }

    @POST
    @Path("/picture")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Create a new event.", response = Response.class)
    public Response addPicture(final Media entity, @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token,
                               @ApiParam(value = "id of event", required = true) @QueryParam("id") final String id) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        Event event = DAOManager.getFactory().getEventDAO().filter("id", id).getFirst();
        if (event == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        if (!event.getCreator().equals(user))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response("You cannot add a picture to this event")).build();
        if (entity != null) {
            if (Utils.writeToFileServer(entity.getBytes(), entity.getId()))
                return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Image upload failed.")).build();
            entity.setUrl("92.222.82.30:8080/eip-dev/resources/image" + entity.getId());
            event.addPicture(entity);
        }
        DAOManager.getFactory().getEventDAO().push(event);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Create a new event.", response = Response.class)
    public Response createEvent(final Event entity, @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        if (entity == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Please fill the event fields.")).build();
        if (entity.getMainPicture() != null) {
            if (Utils.writeToFileServer(entity.getMainPicture().getBytes(), entity.getMainPicture().getId()))
                return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Image upload failed.")).build();
            entity.getMainPicture().setUrl("92.222.82.30:8080/eip-dev/resources/image" + entity.getMainPicture().getId());
        }
        entity.setCreator(user);
        DAOManager.getFactory().getEventDAO().push(entity);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Modify event informations", response = Response.class)
    public Response setEvent(final Event event, @ApiParam(value = "Token of sender", required = true) @HeaderParam("token") final String token) {
        Event newEvent = DAOManager.getFactory().getEventDAO().filter("id", event.getId()).getFirst();

        if (newEvent == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        DAOManager.getFactory().getEventDAO().push(event);
        return Response.ok().build();
    }


    @PUT
    @Path("/validate")
    @Authenticated
    @RolesAllowed("ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Validate an event to be published.", response = Response.class)
    public Response validateEvent(@ApiParam(value = "id of event", required = true) @QueryParam("id") final String id) {
        Event event = DAOManager.getFactory().getEventDAO().filter("id", id).getFirst();
        if (event == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        event.setValid(true);
        DAOManager.getFactory().getEventDAO().push(event);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Delete an event.", response = Response.class)
    public Response deleteEvent(final Event entity) {
        DAOManager.getFactory().getTicketDAO().filter("event", entity).clear();
        DAOManager.getFactory().getPublicationDAO().filter("event", entity).clear();
        DAOManager.getFactory().getEventDAO().remove(entity);
        return Response.status(Response.Status.OK).build();
    }
}
