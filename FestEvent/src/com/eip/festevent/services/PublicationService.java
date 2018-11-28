package com.eip.festevent.services;

import com.eip.festevent.authentication.Authenticated;
import com.eip.festevent.beans.*;
import com.eip.festevent.dao.DAO;
import com.eip.festevent.dao.DAOManager;
import com.eip.festevent.dao.morphia.QueryHelper;
import com.eip.festevent.utils.Utils;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import io.swagger.annotations.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Api(tags = {"Publications"})
@Path("/publications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublicationService {

    @GET()
    @Path("/update")
    public Response update() {
        List<Publication> publications = DAOManager.getFactory().getPublicationDAO().getAll();
        for (Publication publication : publications) {
            if (publication.getPublisher() != null) {
                User user = DAOManager.getFactory().getUserDAO().filter("email", publication.getPublisher().getEmail()).getFirst();
                if (user != null)
                    publication.setPublisher(user);
            }
            if (publication.getEvent() != null) {
                Event event = DAOManager.getFactory().getEventDAO().filter("id", publication.getEvent().getId()).getFirst();
                if (event != null)
                    publication.setEvent(event);
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("/all")
    public List<Publication> getAllPublication() {
        return DAOManager.getFactory().getPublicationDAO().getAll();
    }

    @GET
    @Authenticated
    @Path("/research")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Search publication(s)", response = Publication.class, responseContainer = "List")
    public Response searchPublication(@ApiParam(value = "filter keys", required = true) @QueryParam("key") List<String> keys,
                                      @ApiParam(value = "filter values", required = true) @QueryParam("value") List<String> values,
                                      @ApiParam(value = "token of sender", required = true) @HeaderParam("token") final String token) {
        DAO<Publication> dao = DAOManager.getFactory().getPublicationDAO();
        QueryHelper<Publication> helper = new QueryHelper<Publication>(dao, keys, values);
        if (helper.isValidQuery(Publication.class))
            helper.performQueries();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid queries.")).build();
        List<Publication> result = helper.getDao().getAll();
        return Response.ok(result).build();
    }

    @GET
    @Path("/pictures")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get publication pictures.", response = Media.class, responseContainer = "List")
    public Response getPublicationPictures(@ApiParam(value = "id of event", required = true) @QueryParam("id") String id) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid publication id")).build();
        return Response.ok(publication.getMedias()).build();
    }

    @GET
    @Path("/friends")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get friends publications.", response = Publication.class, responseContainer = "List")
    public Response getFriendsPublications(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token) {
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        List<User> userFriends = DAOManager.getFactory().getUserDAO().filter("friends in", user.getEmail()).getAll();
        List<Publication> result = Lists.newArrayList();

        for (User friend : userFriends) {
            result.addAll(DAOManager.getFactory().getPublicationDAO().filter("publisher.email", friend.getEmail()).getAll());
        }

        Collections.sort(result, new Utils.SortPublicationByDate());

        return Response.ok(result).build();
    }

    @GET
    @Path("/likes")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get publication likes.", response = User.class, responseContainer = "List")
    public Response getPublicationLikes(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                        @ApiParam(value = "id of publication", required = true) @QueryParam("id") final String id) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrond id.")).build();
        return Response.ok(publication.getLikes()).build();
    }

    @GET
    @Path("/comments")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get publication comments.", response = Comment.class, responseContainer = "List")
    public Response getPublicationComments(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                           @ApiParam(value = "id of publication", required = true) @QueryParam("id") final String id) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrond id.")).build();
        return Response.ok(publication.getComments()).build();
    }

    public boolean checkMedias(Publication entity) {
        List<Media> list = Lists.newArrayList();
        User publisher = entity.getPublisher();
        Event event = entity.getEvent();
        if (entity.getMedias() != null && entity.getMedias().size() > 0) {
            for (Media media : entity.getMedias()) {
                if (media.getBytes() != null && media.getBytes().length > 0) {
                    if (!Utils.writeToFileServer(media.getBytes(), media.getId()))
                        return false;
                    media.setUrl("92.222.82.30:8080/eip/festevent-resources/image/" + entity.getId());
                    media.setBytes(null);
                    if (publisher != null)
                        publisher.addPicture(media);
                    if (event != null) {
                        event.addPicture(media);
                    }
                    list.add(media);
                }
            }
            entity.setPublisher(publisher);
            entity.setEvent(event);
            entity.setMedias(list);
        }
        return true;
    }

    @POST
    @Path("/publicate")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @Authenticated
    @ApiOperation(value = "Create a publication.", response = Response.class)
    public Response publicate(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token, final Publication entity) {
        entity.setPublisher(DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst());
        if (entity.getContent() == null || entity.getContent().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Content null or empty.")).build();
        if (!checkMedias(entity))
            return Response.status(Response.Status.BAD_REQUEST).entity(entity.getMedias()).build();
        DAOManager.getFactory().getPublicationDAO().push(entity);
        DAOManager.getFactory().getUserDAO().push(entity.getPublisher());
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @POST
    @Path("/event/publicate-link")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @Authenticated
    @ApiOperation(value = "Create a publication.", response = Response.class)
    public Response publicateLinkEvent(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token, final Publication entity,
                                     @ApiParam(value = "id of event that publicated", required = true) @QueryParam("id") final String eId) {
        User sender = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();

        Event e = DAOManager.getFactory().getEventDAO().filter("id", eId).getFirst();
        entity.setEvent(e);
        entity.setPublisher(sender);
        if (e == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        if (entity.getContent() == null || entity.getContent().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Content null or empty.")).build();
        if (!checkMedias(entity))
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Image upload failed.")).build();

        DAOManager.getFactory().getEventDAO().push(entity.getEvent());
        DAOManager.getFactory().getPublicationDAO().push(entity);
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @POST
    @Path("/event/publicate")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request") })
    @ApiOperation(value = "Create a publication.", response = Response.class)
    public Response publicateAsEvent(final Publication entity, @ApiParam(value = "id of event that publicated", required = true) @QueryParam("id") final String eId) {

        Event e = DAOManager.getFactory().getEventDAO().filter("id", eId).getFirst();
        entity.setEvent(e);
        if (e == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Invalid event id")).build();
        if (entity.getContent() == null || entity.getContent().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Content null or empty.")).build();
        if (entity.getMedias() != null && entity.getMedias().size() > 0) {
            for (Media media : entity.getMedias()) {
                e.addPicture(media);
            }
        }//        if (!checkMedias(entity))
//            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Image upload failed.")).build();
        DAOManager.getFactory().getEventDAO().push(e);
        DAOManager.getFactory().getPublicationDAO().push(entity);
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }


    @PUT
    @Path("/like")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Like a publication.", response = Response.class)
    public Response likePublication(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                    @ApiParam(value = "id of publication", required = true) @QueryParam("id") final String id) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong id")).build();
        for (User profil : publication.getLikes()) {
            if (profil.getEmail().equals(user.getEmail()))
                return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("You already like this publication")).build();
        }
        publication.addLike(user);
        DAOManager.getFactory().getPublicationDAO().push(publication);
        return Response.ok(publication.getLikes()).build();
    }

    @PUT
    @Path("/unlike")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Unlike a publication.", response = Response.class)
    public Response unlikePublication(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                      @ApiParam(value = "id of publication", required = true) @QueryParam("id") final String id) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong publication id")).build();
        for (User profil : publication.getLikes()) {
            if (profil.getEmail().equals(user.getEmail())) {
                publication.removeLike(user);
                DAOManager.getFactory().getPublicationDAO().push(publication);
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("You cannot unlike a publication you don't like")).build();
    }

    @POST
    @Path("/comment")
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Comment a publication.", response = Comment.class)
    public Response commentPublication(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                       @ApiParam(value = "id of publication", required = true) @QueryParam("id") final String id, final Comment entity) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong publication id")).build();
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        entity.setDate(new Date());
        if (entity.getCommenter() == null)
            entity.setCommenter(user);
        publication.addComment(entity);
        DAOManager.getFactory().getPublicationDAO().push(publication);
        return Response.ok().entity(entity).build();
    }

    @PUT
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Modify a publication.", response = Response.class)
    public Response setPublication(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                   final Publication entity) {
        Publication publication = DAOManager.getFactory().getPublicationDAO().filter("id", entity.getId()).getFirst();
        if (publication == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("This publication doesn't exist.")).build();
        if (entity.getContent() == null || entity.getContent().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Content null or empty.")).build();

        DAOManager.getFactory().getPublicationDAO().push(entity);
        return Response.ok().build();
    }

    @DELETE
    @Authenticated
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Delete a publication.", response = Response.class)
    public Response deletePublication(@ApiParam(value = "Token of sender", required = true) @HeaderParam("token") String token,
                                      @ApiParam(value = "id of publication", required = true) @QueryParam("id") final String id) {
        Publication p = DAOManager.getFactory().getPublicationDAO().filter("id", id).getFirst();
        User user = DAOManager.getFactory().getUserDAO().filter("accessToken", token).getFirst();
        if (p == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new Utils.Response("Wrong publication id")).build();
        if (p.getPublisher().equals(user) || user.getRole().equals("ADMIN")) {
            DAOManager.getFactory().getPublicationDAO().remove(p);
            return Response.ok().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(new Utils.Response("Only publisher or admin can delete a publication.")).build();
    }
}
