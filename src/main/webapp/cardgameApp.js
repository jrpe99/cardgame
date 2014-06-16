
var models = moduleCache.module("models");
var views = moduleCache.module("views");

/*
var CardGameRouter = Backbone.Router.extend({
});
*/
var websocket;
var action = new models.ActionModel();
var cardListView = new views.CardListView();
var playerListView = new views.PlayerListView();

new views.JoinActionView();
new views.StartGameActionView();
new views.DealActionView();
new views.StopGameActionView();
new views.StatusActionView();

initWebSocket(cardListView, playerListView);

