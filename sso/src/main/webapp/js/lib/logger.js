/*
 * Factory function for logger.
 * @author: uknow.
 */
var Logger = (function(){
	var levels = ['debug', 'info', 'warn', 'error'];
	var defaultLevel = 2;
	return {
		getLogger: function(level) {
			var __level = defaultLevel;
			if (level && levels.indexOf(level) != -1) {
				__level = levels.indexOf(level);
			}
			return {
				debug: function() {
					if (__level == 0) {console.debug.apply(console, arguments);}
				},
				info: function() {
					if (__level <= 1) {console.info.apply(console, arguments);}
				},
				warn: function() {
					if (__level <= 2) {console.warn.apply(console, arguments);}
				},
				error: function() {
					console.error.apply(console, arguments);
				}
			};
		}
	};
})();