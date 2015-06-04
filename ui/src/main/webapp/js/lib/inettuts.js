var iNettuts = $.extendClass(Base, {
    options : {
        columns : '.column',
        widgetSelector: '.widget',
        handleSelector: '.widget-head',
        contentSelector: '.widget-content',
        editBoxSelector: '.edit-box',
        widgetDefault : {
            movable: true,
            removable: true,
            collapsible: true,
            editable: true,
            colorClasses : ['color-yellow', 'color-red', 'color-blue', 'color-white', 'color-orange', 'color-green']
        },
        widgetIndividual : {
            intro : {
                movable: false,
                removable: false,
                collapsible: false,
                editable: false
            }
        }
    },

    init : function(options) {
    	$.extend(true, this.options, options);
    	this.super(options);
    },
    
    getWidgetSettings: function (id) {
    	options = this.options;
    	
    	// If id exists & individual settings are defined in the options, then use the settings defined, otherwise use the default settings.
        return (id && options.widgetIndividual[id])? $.extend({}, options.widgetDefault, options.widgetIndividual[id]): options.widgetDefault;
    },
    
    setupControls: function () {
    	options = this.options;
        
        $(options.widgetSelector, $(options.columns)).each(function(index, widget) {
            thisWidgetSettings = this.getWidgetSettings(widget.id);
            $widget = $(widget);
            
            if (thisWidgetSettings.removable) {
                $('<a href="#" class="remove">CLOSE</a>').click(function() {
                    if (confirm('This widget will be removed, ok?')) {
                    	$widget.animate({
                            opacity: 0    
                        }, function() {
                        	$widget.wrap('<div/>').parent().slideUp(function() {
                        		$widget.remove();
                            });
                        });
                    }
                    return false;
                }).appendTo($(options.handleSelector, widget));
            }

            if (thisWidgetSettings.editable) {
                $('<a href="#" class="edit">EDIT</a>').click(function() {
                	$this = $(this);
                	if (!$this.data('toggle')) {
	                    $this.css({backgroundPosition: '-66px 0', width: '55px'})
	                        .parents(options.widgetSelector)
	                            .find('.edit-box').show().find('input').focus();
                	} else {
	                    $this.css({backgroundPosition: '', width: ''})
	                        .parents(options.widgetSelector)
                            .find('.edit-box').hide();
                	}
                	$this.data('toggle', !$this.data('toggle'));
                    return false;
                }).appendTo($(options.handleSelector, widget));
                
                if (thisWidgetSettings.editBox) {
                	thisWidgetSettings.editBox.apply($('<div class="edit-box" style="display:none;"/>')
    	                    .insertAfter($(options.handleSelector, widget)));
                }
            }
            
            if (thisWidgetSettings.collapsible) {
                $('<a href="#" class="collapse">COLLAPSE</a>').click(function() {
                	$this = $(this);
                	if (!$this.data('toggle')) {
	                    $this.css({backgroundPosition: '-38px 0'})
	                        .parents(options.widgetSelector)
	                            .find(options.contentSelector).hide();
                	} else {
	                    $this.css({backgroundPosition: ''})
	                        .parents(options.widgetSelector)
	                            .find(options.contentSelector).show();
                    }
                	$this.data('toggle', !$this.data('toggle'));
                    return false;
                }).prependTo($(options.handleSelector, widget));
            }
            
            if (thisWidgetSettings.content) {
            	thisWidgetSettings.content.apply($(options.contentSelector, widget));
            }
        }.bind(this));
        
        this.makeSortable();
    },
    
    setupWidgetContent: function(widget, setup, empty) {
    	$content = this.$(widget).find(this.options.contentSelector);
    	setup.apply(empty? $content.empty(): $content);
    },
    
    setupWidgetEditBox: function(widget, setup){
    	setup.apply(this.$(widget).find(this.options.editBoxSelector).empty());
    },
    
    makeSortable : function () {
        options = this.options,
        $sortableItems = (function () {
            var notSortable = '';
            $(options.widgetSelector, $(options.columns)).each(function(i, widget) {
                if (!this.getWidgetSettings(widget.id).movable) {
                    if (!widget.id) {
                        widget.id = 'widget-no-id-' + i;
                    }
                    if (notSortable.length != 0) {
                    	notSortable += ',';
                    }
                    notSortable += '#' + widget.id;
                }
            }.bind(this));
            return $('> li:not(' + notSortable + ')', options.columns);
        }.bind(this))();
        
        $sortableItems.find(options.handleSelector).css({
            cursor: 'move'
        }).mousedown(function(e) {
            $sortableItems.css({width:''});
            $(this).parent().css({
                width: $(this).parent().width() + 'px'
            });
        }).mouseup(function() {
            if(!$(this).parent().hasClass('dragging')) {
                $(this).parent().css({width:''});
            } else {
                $(options.columns).sortable('disable');
            }
        });

        $(options.columns).sortable({
            items: $sortableItems,
            connectWith: $(options.columns),
            handle: options.handleSelector,
            placeholder: 'widget-placeholder',
            forcePlaceholderSize: true,
            revert: 300,
            delay: 100,
            opacity: 0.8,
            containment: 'document',
            start: function (e, ui) {
                $(ui.helper).addClass('dragging');
            },
            stop: function (e, ui) {
                $(ui.item).css({width:''}).removeClass('dragging');
                $(options.columns).sortable('enable');
            }
        });
    }
});
