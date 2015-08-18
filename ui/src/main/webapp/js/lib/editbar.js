/**
 * Created by tenglinxiao on 3/8/15.
 * @author uknow
 */

var EditBar = $.extendClass(Base, {
    options: {
        containerSelector: null,
        container: null,
        buttons: []
    },

    $container: null,

    init: function(options) {
        $.extend(true, this.options, options);
        if (!this.options.containerSelector && !this.options.container) {
            window.alert('Either container selector or container obj must be offered!');
            return;
        }
        this.$container = this.options.containerSelector? $(this.options.containerSelector): this.$(this.options.container);
        this.setupControls();
    },

    setupControls: function() {
    	var __self = this;
        $.each(this.options.buttons, function(index, def) {
            __self.createButton(def);
        });
    },

    createButton: function(def) {
        $('<button>').attr('type', 'button').addClass(def.cls).text(def.text).bind('click', def.click).appendTo(this.$container);
    }
});