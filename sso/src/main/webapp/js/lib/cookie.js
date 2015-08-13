/*
 * Extend the cookie related function onto jQuery obj.
 * @author: uknow.
 */
$.extend($, (function(){
	var allowedOptions = ['domain', 'path', 'expires', 'httpOnly', 'secure'];
	var defaultOptions = {
		expires: new Date().nextNDay(7, true)
	};
	
	// Function for parsing json value.
	var getJsonValue = function(value) {
		try {
			return JSON.parse(value);
		} catch (error) {
			return value;
		}
	};
	return {
		// Fetch all cookies.
		getCookies: function() {
			var kvs = [];
			if (document.cookie) {
				var cookies = document.cookie.split(';');
				for (var index in cookies) {
					var kv = cookies[index].trim().split('=');
					kvs.push({name: kv[0] , value: getJsonValue(kv[1])});
				}
			}
			return kvs;
		},
		// Get one cookie with name.
		getCookie: function(name) {
			var cookies = $.getCookies();
			for (var index in cookies) {
				if (cookies[index]['name'] == name) {
					return cookies[index];
				}
			}
			return null;
		},
		// Set cookie.
		setCookie: function(name, value, options) {
			// Check the valid options if defined.
			if (options) {
				$.each(options, function(prop, value) {
					// Remove invalid options.
					if (allowedOptions.indexOf(prop) == -1) {
						delete options[prop];
					}
				});
			}
			
			// Allow json object as value for cookie.
			var cookie = name + '=' + JSON.stringify(value);
			var merged = $.extend({}, defaultOptions, options);
			for (var prop in merged) {
				cookie += ';' + prop + '=' + merged[prop];
			}
			document.cookie = cookie;
		},
		// Delete cookie.
		removeCookie: function(name) {
			document.cookie = name + "=null;expires=" + new Date().yesterday(); 
		},
		// Find the root domain for the current context.
		getRootDomain: function() {
			return window.location.href.match('https?://([^/:]+)')[1];
		}
	}
})());