/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.game.card;

import dk.game.card.decoder.GameMessageDecoder;
import dk.game.card.message.GameMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jorperss
 */
public class TestJSON {
    public static void mainJP(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        String s = "{type:\"lreq\",communicationId:0,data:\"JP\"}";
        GameMessage msg;
        try {
            msg = mapper.readValue(s, GameMessage.class);
        } catch (IOException ex) {
            Logger.getLogger(GameMessageDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
