package edu.mayo.phenoportal.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface GuvnorClient {
    @POST
    @Path("/packages/{packageName}/assets")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_ATOM_XML)
    String addAsset(@PathParam("packageName") String pkg, @HeaderParam("Authorization") String auth, @HeaderParam("Slug") String slug, byte[] bytes);
}
