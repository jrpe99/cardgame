package dk.game.card.message.request;

import dk.game.card.message.GameMessage;

/**
 *
 * @author jorperss
 */
public class LoginRequestMessage extends GameMessage {

    private String loginId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    
}
