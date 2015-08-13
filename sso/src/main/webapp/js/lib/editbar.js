/**
 * Created by tenglinxiao on 3/8/15.
 */

var EditBar = $.extendClass(Base, {
    options: {
        containerSelector: null,
        container: null,
        buttons: [{
            name: 'edit',
            text: '编辑',
            class: 'btn btn-success',
            click: function() {
            	$('#editModal').modal().find('.modal-footer .btn-primary').click(function(event) {
            		$(event.target).parents('.modal').modal('hide');
            	}).end();
            }
        }, {
            name: 'remove',
            text: '删除',
            class: 'btn btn-danger',
            click: function(){
            	$('#removeModal').modal().find('.modal-footer .btn-primary').click(function(event) {
            		$(event.target).parents('.modal').modal('hide');
            	}).end();
            }
        }]

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
            switch (def.name) {
                case 'edit':
                    __self.createEditButton(def);
                    break;
                case 'remove':
                    __self.createRemoveButton(def);
                    break;
            }
        });
    },

    createEditButton: function(def) {
        $('<button>').attr('type', 'button').addClass(def.class).text(def.text).bind('click', def.click).appendTo(this.$container);
    },

    createRemoveButton: function(def) {
        $('<button>').attr('type', 'button').addClass(def.class).text(def.text).bind('click', def.click).appendTo(this.$container);
    }
});