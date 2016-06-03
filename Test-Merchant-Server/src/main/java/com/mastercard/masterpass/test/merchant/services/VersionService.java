package com.mastercard.masterpass.test.merchant.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/version")
public class VersionService {

    @GET
    public String getVersion() {
        // Return date and time of deployment
        return "12/02/2016 11.31AM";
    }
}
