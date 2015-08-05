// Author: linxiao.teng

// Function to define plugin widget.
(function($){
	$.widget('metro.customized', {
		options: {
			tilesPerRow: 3,
			rows: 2,
			containerClz: '.metro-ui'
		},
		
		$this: null,
		
		_create: function()
		{
			if (this.element.is(this.options.containerClz)) {
				this.$this = this.element;
			} else {
				this.$this = this.element.find(this.options.containerClz);
			}
			
			if (!this.$this) {
				window.alert("container is not found with class " + this.options.containerClz + "!");
				return;
			}
			this.init();
		},
		
		init: function() {
			this.bindEvents();
		},
		
		resize: function() {
			// Set the size for tile, rough computation ! :)
			var height = $(document).height();
			var $tiles = this.$this.find(".tile").height(height / 2 - 120);
			
			// Adjust the icon position.
			var width = $tiles.width();
			$tiles.find('i').css('left', (width - $tiles.find('i').width()) / 2);
			
			// Record the first time window size.
			if (!this.$this.data('icon-font')) {
				this.$this.data({
					'icon-font': parseInt($tiles.find('i').css('fontSize')), 
					'doc-size': {height: parseInt($tiles.find('i').height()), width: parseInt($tiles.find('i').width())}
				});
			}
		},
		
		bindEvents: function()
		{
			// Create effects for tile desc.
			this.$this.find('.tile').hover(function(event) {
				var $this = $(this);
				$this.find('.brand').stop().animate({bottom: '0px'}, 500);
			}, function(event){
				var $this = $(this);
				$this.find('.brand').stop().animate({bottom: '-100%'}, 500);
			}).click(function(){
				window.location.href = $(this).find('a').attr('href');
			});
			
			// Resize the doms if window resize event fires.
			$(window).resize(this.resize.bind(this)).trigger('resize');
		}
	});
})(jQuery);

$(function(){
	$(document).customized({});
});