// Define method named extendClass supporting class inheritance.
$.extend($, {
	extendClass: function(parentClz, subClz) {
		// Make sure the parentClz is a function & subClz is a object.
		if (typeof(parentClz) != 'function' || typeof(subClz) != 'object') {
			window.alert("parentClz parameter MUST be a function & subClz parameter MUST be an object!");
			return;
		}
		
		// Make sure init method is offered.
		if (!subClz.init) {
			window.alert("Init method is a MUST have method!");
			return;
		}
		
		// Mark each function a property __name__ stores the function name.
		$.each(subClz, function(property, func){
			if (typeof(func) == 'function') {
				func.prototype.__name__ = property;
			}
		});
		
		// Create sub function for each class declared.
		sub = function() {
			$.extend(this, subClz);
		
			this.super = function() {
				func = arguments.callee.caller.prototype.__name__;
				if (this.__proto__[func]) {
					this.__proto__[func].apply(this.__proto__, arguments);
				} else {
					console.error('no method named [' + func + '] defined!');
				}
			}.bind(this);
			
			if (arguments.callee.caller != $.extendClass) {
				// Let all the objs in chain has the same this pointer. 
				parent = this;
				while (parent.__proto__ instanceof Base) { 
					parent = parent.__proto__; 
				}
				parent.__this__ = this;

				// Call init method.
				this.init.apply(this, arguments);
			}
		}
		// create parent class obj as the prototype for new class.
		sub.prototype = new parentClz;
		
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
	
	this.$ = function(){
		if (arguments.length == 0) {
			return $(document.body);
		}
		arg = arguments[0];
		if (arg instanceof $) {
			return arg;
		}
		return $(arg);
	}; 
}

// Wrapped class supporting basic ops.
Base = $.extendClass(Base, {
	options: {},
	
	init: function(options) {
		// Merge options with the existed ones.
		$.extend(this.options, options);
		
		self = this.__this__;
		
		// Set up all the controls.
		self.setupControls();
		
		// Bind all the events necessary.
		self.bindEvents();
		
		// Call this method to finish the initialization phase.
		self.doneInitialization();
		
		// Trigger event done init.
		$(self).trigger('doneInit');
	},
	
	setupControls: function() {},
	
	bindEvents: function() {
		if (this.options.events && this.options.events.doneInit) {
			this.bind('doneInit', this.options.events.doneInit);
		}
	},
	
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

