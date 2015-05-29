package dk.game.card.message;


/**
 *
 * @author Jörgen Persson
 */
public class GameMessage {
    public static final String LOGIN_REQUEST = "loginreq";
    public static final String DEAL_REQUEST = "dealreq";
    public static final String GAME_START_REQUEST = "startreq";
    public static final String GAME_STOP_REQUEST = "stopreq";
    public static final String JOIN_REQUEST = "joinreq";
    public static final String PLAY_CARD_REQUEST = "playcardreq";
    
    public static final String GAME_TIME_RESPONSE = "gameTimeRes";
    public static final String LOGOUT_REQUEST = "dreq";
    public static final String LOGIN_RESPONSE = "loginres";
    public static final String RESULT_RESPONSE = "rres";
    public static final String HAND_RESPONSE = "handres";
    public static final String PLAY_CARD_RESPONSE = "playcardres";
    
    private String type;

    private String communicationId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(String communicationId) {
        this.communicationId = communicationId;
    }
}
