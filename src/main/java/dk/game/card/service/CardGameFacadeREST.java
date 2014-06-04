package dk.game.card.service;

import dk.game.card.GameState;
import dk.game.card.message.response.GameStatus;
import dk.game.card.websocket.endpoint.GameServer;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author jorperss
 */
@Stateless
@Path("status")
public class CardGameFacadeREST {

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create() {
        System.out.println("POST");
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit() {
        System.out.println("PUT");
    }

    @DELETE
    public void remove() {
        System.out.println("DELETE");
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public GameStatus getState() {
        System.out.println("GET");
        GameStatus status = new GameStatus();
        status.setStatus(GameServer.game.getState().toString());
        return status;
    }
}
