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
			$.extend(true, this, subClz);
		
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
				while (parent.__prototype__ instanceof Object) {
					parent.__proto__ = $.extend(true, {}, parent.__prototype__);
					parent = parent.__proto__; 
				}
				parent.__this__ = this;

				// Call init method.
				this.init.apply(this, arguments);
			}
		}
		
		// Create parent class obj as the prototype for new class.
		sub.prototype.__prototype__ = new parentClz;
		
		return sub;
	},
	
	cloneObj: function(object) {
		if (typeof(object) != 'object') {
			return object;
		}
		var target = object instanceof Array? []: {};
		for (var prop in object) {
			if (typeof(object[prop]) != 'object') {
				target[prop] = object[prop];
			} else {
				target[prop] = $.cloneObj(object[prop]);
			}
		}
		return target;
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
		
		var __this__ = this.__this__;
		
		// Set up all the controls.
		__this__.setupControls();
		
		// Bind all the events necessary.
		__this__.bindEvents();
		
		// Call this method to finish the initialization phase.
		__this__.doneInitialization();
		
		// Trigger event done init.
		$(__this__).trigger('doneInit');
	},
	
	setupControls: function() {},
	
	bindEvents: function() {
		if (this.options.events && this.options.events.doneInit) {
			this.bind('doneInit', this.options.events.doneInit);
		}
	},
	
	doneInitialization: function(){},
	
	// Ajax call with default settings.
	ajaxCall: function(settings) {
		$.ajax($.extend(true, settings, {
			headers: {
				Accept: 'application/json',
				'Content-Type': 'application/json; charset=utf-8'
			},
			cache: false,
			dataType: 'json',
			error: function(request, status, error) {
				this.handleError(request, status, error);
			}.bind(this)
		}))
	},
	
	// Schedule ajax call with interval settings.
	scheduleAjaxCall: function(settings, interval) {
		var updateHandler = null;
		(updateHandler= function(){
			var data = {};
			if (settings.dataRefresh) {
				for (var prop in settings.dataRefresh) {
					data[prop] = settings.dataRefresh[prop].apply(this);
				}
			}

			this.ajaxCall($.extend(true, {timeout: 3000}, settings, {
				data: settings.data instanceof Object? data: settings.data,
				success: function() {
					var interrupted = false;
					if (settings.isInterrupted) {
						interrupted = settings.isInterrupted.apply(this);
					}
					if (!interrupted) {
						settings.success.apply(this, arguments);
						if (interval) {
							setTimeout(updateHandler, interval);
						}
					}
				}.bind(this)
			}));
		}.bind(this))();
	},
	
	// Default implementation for handling ajax errors.
	handleError: function(request, status, error) {
		window.alert('status:' + status + '  ' + 'status_code:' + request.status);
	}
});

