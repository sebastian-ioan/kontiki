package org.vizuina;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Mandala on 04-Mar-17.
 */
@Component
@Path("/test")
public class KontikiController {


    @GET
    @Produces("application/json")
    public String health() {
        return new String("Jersey: Up and Running!");
    }
}
