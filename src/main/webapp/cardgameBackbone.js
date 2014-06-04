var ActionModel = Backbone.Model.extend({
    initialize: function(){
        this.on("change:time", function(model){
            document.getElementById("timeID").value = this.get("time");
        });
    }    
});

var GameStatusModel = Backbone.Model.extend({
    urlRoot: '/cardgame/webresources/status',
    defaults: {
        status: ''
    },
    initialize: function() {
        this.fetch({
            success: function(status) {
                alert(status.get("status"));
            }
        });
    }   
});

var HandModel = Backbone.Model.extend({});
var hand = new HandModel();

var HandView = Backbone.View.extend({
    el: $("#hand"),
    initialize: function(){
        this.render();
    },
    render: function(){
        // Compile the template using underscore
        var template = _.template( $("#tpl-hand").html(), {} );
        // Load the compiled HTML into the Backbone "el"
        this.$el.html( template );
    }
});
//var handView = new HandView();

var CardModel = Backbone.Model.extend({});
var CardList = Backbone.Collection.extend({
    model: CardModel
});
var CardListView = Backbone.View.extend({
    el: $("#cardList"),
    collection: cardList,
    initialize: function() {
        this.collection = new CardList();
        this.collection.add([
            {card : "b1fv"},
            {card : "b1fv"},
            {card : "b1fv"},
            {card : "b1fv"},
            {card : "b1fv"}
        ]);
        this.showCardList();
    },
    // PUBLIC
    updateCardList: function(cardList) {
        this.removeCardList();
        this.collection.reset();
        for(var i = 0; i < cardList.length; i++) {
            this.collection.add({card : cardList[i]});
        }
        this.showCardList();
        return this;
    },
    showCardList: function() {
        var self = this;
        this.collection.each(function(model) {
            self.createCard(model);
        });
        return this;
    },
    // PRIVATE
    createCard: function(model) {
        var template = _.template( $("#tpl-card").html(), {card : model.get("card") + ".png"} );
        this.$el.append(template);
    },
    removeCard: function(model) {
        var template = _.template( $("#tpl-cardClear").html(), {} );
        this.$el.html(template);
    },
    removeCardList: function() {
        var self = this;
        this.collection.each(function(model) {
            self.removeCard(model);
        });
    }
});

