/*
 * Class to map data field to other named data fields.
 * author: uknow.
 */
var Mapping = $.extendClass(function(){}, {
	options: {
		// Container.
		container: null,
		// Default to form container.
		containerSelector: 'form',
		// Default to common form controls.
		controlSelector: 'input,select,textarea',
		// Filter function to filter controls wanted.
		filter: null,
		// Source attribute.
		defaultIdentifier: 'name',
		// Target attribute.
		identifier: 'data-mapping'
	},
	
	init: function(options) {
		$.extend(true, this.options, options);
	},
	
	map: function() {
		var data = {};
		var $container = this.options.container? $(this.options.container): $(this.options.containerSelector);
		var $controls = null;
		if (this.options.controlSelector) {
			$controls = $container.find(this.options.controlSelector);
		}
		if (this.options.filter) {
			$controls = $controls.filter(function(index, control) {
				return this.options.filter.call(control, control);
			}.bind(this));
		}
		
		$.each($controls, function(index, control) {
			var $control = $(control);
			var name = null;
			if (name = $control.attr(this.options.identifier)) {
				data[name] = $control.val();
			} else if (name = $control.attr(this.options.defaultIdentifier)) {
				data[name] = $control.val();
			} else {
				console.warn('Ignore element [' + control + '] because both [' 
						+ this.options.identifier + '] and [' 
						+ this.options.defaultIdentifier + '] are not defined!');
			}
		}.bind(this));
		
		return data;
	}
});