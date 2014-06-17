package dk.game.card.websocket.encoder;

import dk.game.card.message.GameMessage;
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class GameMessageEncoder implements Encoder.Text<GameMessage> {

    @Override
    public String encode(GameMessage object) throws EncodeException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(object);
            System.out.println("Send JSON string: \n");
            System.out.println(json);
            return json;
        } catch (IOException ex) {
            throw new EncodeException(ex, "Encoding failed");
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
