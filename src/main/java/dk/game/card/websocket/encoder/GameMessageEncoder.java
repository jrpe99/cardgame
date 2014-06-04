package dk.game.card.websocket.encoder;

import dk.game.card.message.GameMessage;
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class GameMessageEncoder implements Encoder.Text<GameMessage> {

    @Override
    public String encode(GameMessage object) throws EncodeException {
        try {
            return object.toJson();
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
