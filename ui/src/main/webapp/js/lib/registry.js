/*
 * Registry class aiming for taking actions when satisfy certain conditions. 
 * @author uknow.
 */
var Observer = $.extendClass(function(){}, {
	options: {
		when: function() { return true;},
		action: function(){}
	},
	
	context: null,
	
	init: function(options) {
		$.extend(this.options, options);
	},
	
	// Update action when it's notified.
	update: function() {
		if (!this.options.when || this.options.when.apply(this.context, arguments)) {
			this.options.action.apply(this.context, arguments);
		}
	}
});

var Observable = $.extendClass(function(){}, {
	options: {
		context: {},
		step: function() {},
		reset: function(){}
	},
	observers: [],
	
	init: function(options) {
		$.extend(this.options, options);
	},
	
	// Add observer.
	addObserver: function(observer) {
		observer.context = this.options.context;
		this.observers.push(observer);
	},
	
	// Notify changes to observers.
	notify: function() {
		for (var index in this.observers) {
			this.observers[index].update.apply(this.observers[index], arguments);
		}
	},
	
	// Make step change to observable.
	onstep: function() {
		if (arguments[0] instanceof $.Event) {
			arguments = Array.prototype.slice.call(arguments, 1);
		}
		if (this.options.step) {
			this.options.step.apply(this.options.context, arguments);
		}
		this.notify.apply(this, arguments);
	},
	
	reset: function() {
		if (this.options.reset) {
			this.options.reset.apply(this.options.context);
		}
	}
});


var Counter = $.extendClass(Observable, {
	options: {
		context: null,
		limit: 0,
		increment: 1,
		step: function() {
			this.times += this.options.increment;
		},
		when: function() {
			return this.times == this.options.limit;
		},
		reset: function() {
			this.times = 0;
		},
		action: function() {}
	},
	times: 0,
	init: function(options) {
		if (options.step || options.when || options.reset) {
			delete options.context;
			delete options.step;
			delete options.when;
			delete options.reset;
		}

		$.extend(this.options, {context: this}, options);
		this.addObserver(new Observer(this.options));
	},
	step: function() {
		this.onstep.apply(this, arguments);
	}
});

$.extend($.fn, {
	register: function(options) {
		var registry = this.data('__registry');
		if (!registry) {
			registry = [];
			this.bind('step', function() {
				var id = arguments[1];
				var registry = this.data('__registry');
				if (id) {
					if (registry[id - 1]) {
						registry[id - 1].onstep.apply(registry[id - 1], arguments);
					}
				} else {
					for (var index in registry) {
						registry[index].onstep.apply(registry[index], arguments);
					}
				}
			}.bind(this));
		}
		var observable = new Observable({context: options.context? options.context: this, step: options.step, reset: options.reset});
		var observer = new Observer({when: options.when, action: options.action});
		observable.addObserver(observer);
		var id = registry.push(observable);
		this.data('__registry', registry);
		return id;
	},
	
	resetObservable: function(id) {
		if (id) {
			if (registry[id - 1]) {
				registry[id - 1].reset.apply(registry[id - 1]);
			}
		} else {
			for (var index in registry) {
				registry[index].reset.apply(registry[index]);
			}
		}
	},
	
	registerObserver: function(id, options) {
		var registry = this.data('__registry');
		if (registry[id - 1]) {
			registry[id - 1].addObserver(new Observer({when: options.when, action: options.action}));
			this.data('__registry', registry);
		} else {
			console.error('observable obj is not available!');
		}
	},
	
	unregister: function(id) {
		if (id) {
			var registry = this.data('__registry');
			for (var index in registry) {
				if (index == id - 1) {
					delete registry[index];
					break;
				}
			}
			this.data('__registry', registry);
		} else {
			this.data('__registry', []);
		}
	}
});

// Use the Registry in a way without attaching obj. 
var Registry = (function(){
	var registry = [];
	return {
		trigger: function(id) {
			if (registry[id - 1]) {
				registry[id - 1].onstep.apply(registry[id - 1]);
			} else {
				console.error('observable obj is not available!');
			}
		},
		
		register: function(options) {
			var observer = new Observer({when: options.when, action: options.action});
			var observable = new Observable({context: options.context, step: options.step});
			observable.addObserver(observer);
			return registry.push(observable);
		},
		
		registerObservable: function (id, options){
			if (registry[id - 1]) {
				registry[id - 1].addObserver(new Observer({when: options.when, action: options.action}) );
			} else {
				console.error('observable obj is not available!');
			}
		},
		
		unregister: function(id) {
			if (id) {
				for (var index in registry) {
					if (index == id - 1) {
						delete registry[index];
					}
				}
			} 
		}
	};
})();