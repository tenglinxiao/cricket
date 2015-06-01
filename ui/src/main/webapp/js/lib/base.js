// Define method named extendClass supporting class inheritance.
$.extend($, {
	extendClass: function(parentClz, subClz) {
		if (typeof(parentClz) != 'function' || typeof(subClz) != 'object') {
			window.alert("parentClz parameter MUST be a function & subClz parameter MUST be an object!");
			return;
		}
		
		if (!subClz.init) {
			window.alert("Init method is a MUST have method!");
			return;
		}
		
		sub = function() {
			$.extend(this, subClz);
			this.super = this.__proto__;
			this.init.apply(this, arguments);
		}
		$.extend(sub.prototype, new parentClz);
		return sub;
	}
});

// Base class.
var Base = function() {
	if (!Function.prototype.bind) {
		// Detect the bind function definition on Function prototype.
		// Add it to prototype if function is not defined. 
		Function.prototype.bind = function(__this) {
			func = this;
			return function() {
				func.apply(__this, arguments);
			}
		};
	}
}

// Wrapped class supporting basic ops.
Base = $.extendClass(Base, {
	options: {},
	
	init: function(options) {
		// Merge options with the existed ones.
		$.extend(true, this.options, options);
		
		// Set up all the controls.
		this.setupControls();
		
		// Bind all the events necessary.
		this.bindEvents();
		
		// Call this method to finish the initialization phase.
		this.doneInitialization();
	},
	
	setupControls: function(){},
	
	bindEvents: function(){},
	
	doneInitialization: function(){},
	
	ajaxCall: function(settings) {
		$.ajax($.extend(true, settings, {
			headers: {
				Accept: 'application/json',
				'Content-Type': 'application/json'
			},
			cache: false,
			dataType: 'json',
			timeout: 3000,
			error: function(request, status, error) {
				this.renderError(request, status, error);
			}.bind(this)
		}))
	},
	
	renderError: function(request, status, error) {
		window.alert('status:' + status + '  ' + 'status_code:' + request.status);
	}
});

