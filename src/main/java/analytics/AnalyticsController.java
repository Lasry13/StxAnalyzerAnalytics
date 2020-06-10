package analytics;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("analytics")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyticsController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsController(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @GET
    @Path("getSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSummary() {
        String message = "";
        Response.Status status;
        try {
            message = analyticsRepository.getSummary();
            if(message.isEmpty())
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            status = Response.Status.OK;
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        return Response
                .status(status)
                .entity(message)
                .build();
    }

    @GET
    @Path("getRealTimeGates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRealTimeGates() {
        String message = "";
        Response.Status status;
        try {
            message = analyticsRepository.getRealTimeGates();
            if(message.isEmpty())
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            status = Response.Status.OK;
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        return Response
                .status(status)
                .entity(message)
                .build();
    }

    @GET
    @Path("getSp500")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSp500() {
        String message = "";
        Response.Status status;
        try {
            message = analyticsRepository.getSp500();
            status = Response.Status.OK;
        } catch (Exception e) {
            message = "internal server error";
            status = Response.Status.INTERNAL_SERVER_ERROR;
            logger.error("Unexpected error", e);
        }
        return Response
                .status(status)
                .entity(message)
                .build();
    }
}