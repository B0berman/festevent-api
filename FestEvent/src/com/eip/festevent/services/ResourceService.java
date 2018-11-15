package com.eip.festevent.services;

import com.eip.festevent.utils.Utils;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(tags = {"Resources"})
@Path("/resources")
@Produces({"image/png", "image/jpg", "image/gif"})
public class ResourceService {

    @GET
    @Path("/image/{key}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized") })
    @ApiOperation(value = "Get user profil image with token.", response = Response.class)
    public Response getProfilImage(@ApiParam(value = "Token of sender", required = true) @PathParam("key") final String key) {
        byte[] imageData = Utils.getImage(key);
        if (imageData == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Image not found").build();

        return Response.ok(imageData).build();
    }

}
