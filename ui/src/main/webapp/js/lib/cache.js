/*
 * Centralized cache for browser data cache.
 * @author uknow.
 */

var Cache = (function() {
	// Default group.
	var defaultGroup = 'default';
	
	// Cache instance.
	var cache = {
		'default': {}
	};
	return {
		// Get cache instance.
		// tip: not recommend to directly access the obj for safety purpose.
		getCache: function() {
			return cache;
		},
		
		// Get cache group instance.
		getCacheGroup: function(groupId) {
			return cache[groupId];
		},
		
		// Whether contains certain cache group.
		containsGroup: function(groupId) {
			return cache[groupId]? true: false;
		},
		
		// Whether a group contains certain key.
		containsGroupKey: function(key, groupId) {
			if (arguments.length == 2) {
				if (!this.containsGroup(groupId)) {
					return false;
				}
				return cache[groupId][key]? true: false;
			} else {
				return cache[defaultGroup][key]? true: false;
			}
			
		},
		
		// Add cache value to one group.
		addGroupCache: function(key, value, groupId) {
			if (arguments.length < 2) {
				console.error(arguments + ' is not a valid record to put into cache!');
				return;
			} 
			var group = groupId? groupId: defaultGroup;
			if (!this.containsGroup(group)) {
				cache[group] = {}
			}
			var existed = cache[group][key];
			cache[group][key] = value;
			return existed;
		},
		
		// Get cache value of certain group.
		getGroupCache: function(key, groupId) {
			var group = groupId? groupId: defaultGroup;
			if (!this.containsGroup(group)) {
				console.error('cache group ' + group + ' is not existed!');
				return;
			}
			return cache[group][key];
		},
		
		// Remove cache value in certain group.
		removeGroupCache: function(key, groupId) {
			var group = groupId? groupId: defaultGroup;
			if (this.containsGroup(group)){
				delete cache[group][key];
			} else {
				console.error('cache group ' + groupId + ' is not existed!');
			}
		},
		
		// Add cache group data.
		addCacheGroup: function(data, groupId) {
			var existed = cache[groupId];
			cache[groupId] = data;
			return existed;
		},
		
		// Remove all the caches of certain group.
		removeCacheGroup: function(groupId) {
			delete cache[groupId];
		},
		
		// Convert data obj array to map.
		convertToMap: function(data, keyField, valueField) {
			var map = {};
			for (var index in data) {
				map[data[index][keyField]] = data[index][valueField];
			}
			return map;
		}
	};
})();
