/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.game.card.message.response;

import dk.game.card.message.GameMessage;

/**
 *
 * @author jorperss
 */
public class LoginResponseMessage extends GameMessage {

    private String loginId;
    private String message;
    private boolean loginStatus;

    public LoginResponseMessage() {
        this.setType(LOGIN_RESPONSE);
    }
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoggedIn() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }
}
