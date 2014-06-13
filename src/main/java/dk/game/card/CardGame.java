package dk.game.card;

import dk.game.card.deck.Dealer;
import dk.game.card.deck.Deck;
import dk.game.card.deck.Hand;
import dk.game.card.deck.ScoreCalculator;
import dk.game.card.message.request.JoinRequest;
import dk.game.card.message.request.LoginRequestMessage;
import dk.game.card.message.request.PlayCardRequest;
import dk.game.card.message.response.HandResponse;
import dk.game.card.message.response.LoginResponseMessage;
import dk.game.card.message.response.PlayCardResponse;
import dk.game.card.user.GameUser;
import dk.game.card.websocket.util.WebSocketHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import javax.websocket.Session;

public class CardGame {

    private GameState state;

    public GameState getState() {
        return state;
    }

    /*
     * ID of the game used for communication
     */
    private final String id = "1";

    /*
     * List of remote clients (Peers)
     */
    private List<Session> sessionList = new ArrayList<>();

    private Timer gameRunningTimer;
    
    private Deck deck = null;

    public CardGame() {
        this.state = GameState.PRE_GAME;
    }

    public synchronized void removeSession(Session arc) {
        sessionList.remove(arc);
        if(sessionList.isEmpty()) {
            switchStateToGameFinished();
        }
    }

    public void handleLoginRequest(LoginRequestMessage messsage, Session session) {
        synchronized (id) {
        }
    }

    public void handlePlayCardRequest(PlayCardRequest message, Session session) {
        synchronized (id) {
            GameUser user = (GameUser)session.getUserProperties().get("user");
            user.getHand().removeCard(message.getCard());
            sessionList.forEach(s -> {
                String cardUrl = message.getCard();
                String card = cardUrl.substring(cardUrl.lastIndexOf("/")+1, cardUrl.lastIndexOf("."));
                PlayCardResponse playedCard = new PlayCardResponse(user.getGameId(), card);
                WebSocketHelper.send(s, playedCard);
            });
        }
    }

    public void handleJoinRequest(JoinRequest message, Session session) {
        synchronized (id) {
            LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
            String welcomeMessage = "";
            if (state == GameState.PRE_GAME && getRemoteClients().size() < 5) {
                GameUser gameUser = null;
                if (!getRemoteClients().contains(session)) {
                    gameUser = new GameUser(message.getGameId());
                    session.getUserProperties().put("user", gameUser);
                    welcomeMessage = "Welcome " + gameUser.getGameId() + " !";
                    this.sessionList.add(session);
                } else {
                    gameUser = (GameUser)session.getUserProperties().get("user");
                    welcomeMessage = "Welcome! You have joined the game :-)";
                }
                loginResponseMessage.setMessage(welcomeMessage);
                loginResponseMessage.setLoginId(gameUser.getGameId());
                loginResponseMessage.setLoginStatus(true);
                WebSocketHelper.send(session, loginResponseMessage);
            } else {
                welcomeMessage = "Not possible to join the game :-(";
                WebSocketHelper.send(session, loginResponseMessage);
            }
        }
    }

    public void handleDealRequest(Session arc) {
        synchronized (id) {
            if (state == GameState.GAME_RUNNING) {
                int numberOfUsers = sessionList.size();
                deck = new Deck();
                List<Hand> hands = new Dealer().deal(deck, numberOfUsers, 5);
                sessionList.forEach(s -> {
                    GameUser user = (GameUser)s.getUserProperties().get("user");
                    Hand hand = hands.get(0);
                    ScoreCalculator.score(hand);
                    user.setHand(hand);
                    hands.remove(0);
                    HandResponse handResp = new HandResponse();
                    handResp.setScore(hand.getScore().toString());
                    hand.getCards().stream().sorted().forEach(card -> {
                        handResp.addCard(card.getSuit(), card.getRank());
                    });
                    
                    WebSocketHelper.send(s, handResp);
                });
            }
        }
    }

    private void sendGameUpdateMessage() {
        /*
        CardGameMessage.PriceUpdateResponseMessage purm = new CardGameMessage.PriceUpdateResponseMessage(id, "" + bestBid);
        for (Session arc : getRemoteClients()) {
            try {
                arc.getBasicRemote().sendObject(purm);
            } catch (IOException | EncodeException e) {
                Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        */
    }

    public void switchStateToGameFinished() {
        synchronized (id) {
            state = GameState.PRE_GAME;
        }
        stopGameTimeBroadcast();
    }

    public void switchStateToGameStarted() {
        if(state == GameState.PRE_GAME) {
            synchronized (id) {
                state = GameState.GAME_RUNNING;
            }
            startGameTimeBroadcast();
        }
    }

    private void startGameTimeBroadcast() {
        if(gameRunningTimer == null) {
            synchronized (id) {
                state = GameState.GAME_RUNNING;
            }
            gameRunningTimer = new Timer();
            gameRunningTimer.schedule(new GameTimeBroadcasterTask(this), 0, 1000);
        }
    }

    private void stopGameTimeBroadcast() {
        if(gameRunningTimer != null) {
            gameRunningTimer.cancel();
            gameRunningTimer = null;
        }
    }

    public String getId() {
        return id;
    }

    public List<Session> getRemoteClients() {
        return Collections.unmodifiableList(sessionList);
    }
}
