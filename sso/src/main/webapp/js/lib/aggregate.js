/*
 * Converters used for data convertion.
 * @author: uknow.
 */

var Converter = (function() {
	var converters = {
		string2number: function(str) {
			return Number(str);
		},
		number2string: function(number) {
			return String(number);
		}
	};
	return {
		findConverter: function(key) {
			return converters[key];
		},
		registerConverter: function(key, converter) {
			converters[key] = converter;
		}
	};
})();

/*
 * Aggregate class for client side data aggregation.
 * @author: uknow.
 */
var Aggregate = $.extendClass(function(){}, {
	options: {
		// Debug mode.
		debug: false,
		
		// Partition keys.
		groupKeys: [],
		
		// Sort fields inside each partition.
		groupSort: [],
		
		// New fields created.
		newFields: [],
		
		// Selection fields, Partition keys are included by default.
		fieldsIncluded: [],
		
		// Selection Exclusive fields.
		fieldsExcluded: [],
		
		// Determine whether record is valid for aggregation.
		isValid: function(){
			return true;
		},
		
		// Fields order by.
		orderBy: [],
		
		// Method call when begin the aggregation.
		init: function(globalContext){},
		
		// Function call before each partition's processing.
		start: function(context){
			context.result = {};
		},
		
		// Function call to complete aggregation for each partition.
		aggregate: function(context) {},
		
		// Function call made when finish partition processing.
		end: function(context) {
			this.collect(context.result);
		},
		
		// Method call when the aggregation is done.
		done: function(globalContext){}
	},
	
	// Logger obj.
	logger: null,
	
	// Resultset after aggregation.
	resultSet: [],
	
	// Invalid data filtered out for aggregation.
	exclusiveData: [],
	
	// Constructor.
	init: function(options) {
		// Backup the default options.
		this.__options__ = $.cloneObj(this.options);
		
		// Merge the options with default ones.
		$.extend(true, this.options, options);
		
		// Create logger obj based on options.
		this.logger = Logger.getLogger(this.options.debug? 'debug': '');
	},
	
	// Prepare phase for the aggregation.
	prepare: function() {
		this.logger.debug('Start prepare phase ...');
		
		this.findFields(this.options.groupKeys);
		
		this.findFields(this.options.groupSort);
		
		this.findFields(this.options.orderBy);
		
		this.logger.debug('End prepare phase ...');
	},
	
	// Take the aggregate ops.
	aggregate: function(data, options) {
		// Recover default options if new options is offered when call this method.
		if (options) {
			// Clone the default options obj.
			this.options = $.cloneObj(this.__options__);
			
			// Merge the options with the default options.
			$.extend(true, this.options, options);
			
			// Recreate the logger object.
			this.logger = Logger.getLogger(this.options.debug? 'debug': '');
		}
		
		// Create global context obj.
		var globalContext = {options: this.options};
		
		if (this.options.init) {
			this.options.init.apply(this, globalContext);
		}
		
		this.logger.debug('Enter into partition phase ...');
		
		// Get partitioned data groups.
		var partitioned = this.partition(data, this.options.groupKeys);
		
		this.logger.debug('End of partition phase ...');
		
		// Determine whether use the excluded fields.
		var usedExcludedFields = this.useExcludedFields();
		
		// Get new fields list require to create.
		var newFields = this.newFields(this.options.groupKeys, this.options.newFields);
		
		this.resultSet = [];
		this.exclusiveData = [];
		
		// For each partition, do the aggregation job.
		for (var index in partitioned) {
			this.logger.debug('Processing partition [' + index + ']: ', partitioned[index]);
			
			var partitionData = this.createFields(partitioned[index], newFields);
			
			// Create context obj passed through each phase of the processing.
			var context = {
					// Partitioned group data.
					data: this.sort(partitionData, this.options.groupSort), 
					// Partitioned keys.
					groupKeys: this.options.groupKeys, 
					// Second choice for data collection
					collect: function(elem){
						this.collect(elem);
					}.bind(this)
			};
			// Partition processing start phase.
			this.options.start.call(this, context, globalContext);
			
			// Partition processing phase.
			this.options.aggregate.call(this, context, globalContext);
			
			// Partition processing end phase.
			this.options.end.call(this, context, globalContext);
		}
		
		if (this.options.done) {
			this.options.done.apply(this, globalContext);
		}
		
		this.logger.debug('Resultset after done processing partitions: ', this.resultSet);
		
		this.logger.debug('Sort resultset based on keys: ', this.options.orderBy);
		
		// Sort the records based on the selected keys.
		this.resultSet = this.sort(this.resultSet, this.options.orderBy);
		
		this.logger.debug('Resultset after sorting: ', this.resultSet);
		
		// Clip the selected fields.
		this.resultSet = this.map(this.resultSet, usedExcludedFields? this.options.fieldsExcluded: this.options.fieldsIncluded, usedExcludedFields);
		
		this.logger.debug('Resultset after clipping the fields: ', this.resultSet);
		return this.resultSet;
	},
	
	// Emit one result to resultset.
	collect: function(elem) {
		this.resultSet.push(elem);
	},
	
	// Sort the partition keys in by index order.
	getGroupSequence: function() {
		var groupKeys = [];
		for (var key in this.options.groupKeys) {
			this.options.groupKeys[key]['name'] = key;
			groupKeys.push(this.options.groupKeys[key]);
		}
		return this.sort(groupKeys, ['index']);
	},
	
	// Partition the data into groups.
	partition: function(data, partitionKeys) {
		if (data.length == 0) return data;
		
		// Sort data globally with partitioned keys based on the valid records. 
		data = this.sort(data.filter(function(elem) {
			if (!this.options.isValid || this.options.isValid.apply(elem)) {
				return true;
			}
			this.exclusiveData.push(elem);
			return false;
		}.bind(this)), partitionKeys);
		
		// If no data left after filtering phase, abort the aggregation phase.
		if (data.length == 0) {
			return data;
		}
		
		var temp = data[0];
		var partition = [temp];
		var partitions = [];
		
		// Separate data into partitions.
		for (var index = 1; index < data.length; index++) {
			for (var i in partitionKeys) {
				if ((typeof(partitionKeys[i]) == 'string' && data[index][partitionKeys[i]] != temp[partitionKeys[i]])
						|| (typeof(partitionKeys[i]) == 'object' && partitionKeys[i]['value'].apply(data[index]) != partitionKeys[i]['value'].apply(temp))) {
					partitions.push(partition);
					partition = [];
					temp = data[index];
					break;
				}
			}
			partition.push(data[index]);
		}
		
		// Put partition into partitions array.
		partitions.push(partition);
		return partitions;
	},
	
	// Sort based on keys.
	sort: function(data, sortKeys) {
		// Make sure sort keys are defined. 
		if (sortKeys.length == 0) {
			return data;
		}
		return data.sort(function(prev, next){
			var cmp = 0;
			var p = null, n = null;
			var asc = true;
			for (var index = 0; index < sortKeys.length; index++) {
				cmp = 0;
				
				// If the sort key is string, then use it as field name, otherwise if the sort key is obj & has value function defined, 
				// then apply the function onto the record and use return value as comparator. If neither is offered, then apply converter 
				// function onto field, and use return value as comparator. 
				if (typeof(sortKeys[index]) == 'string') {
					p = prev[sortKeys[index]];
					n = next[sortKeys[index]];
				} else if (sortKeys[index]['value']) {
					p = sortKeys[index]['value'].apply(prev);
					n = sortKeys[index]['value'].apply(next); 
				} else if (sortKeys[index]['converter']) {
					var converter = Converter.findConverter(sortKeys[index]['converter']);
					p = converter.call(prev, prev[sortKeys[index]['name']]);
					n = converter.call(next, next[sortKeys[index]['name']]);
				} else {
					p = prev[sortKeys[index]['name']];
					n = next[sortKeys[index]['name']];
				}
				
				// If no asc option is offered, then set asc = true as default. 
				asc = sortKeys[index]['asc'] === undefined? true: sortKeys[index]['asc'];
				if (typeof(p) == 'number' && typeof(n) == 'number') {
					cmp = p - n;
				} else if (p != n){
					cmp = p > n? 1: -1;
				}
				
				// If comparables are not equal, return cmp value based on the asc option.
				if (cmp) {
					return asc? cmp: -cmp;
				} 
			}
			return 0;
		});
	},
	
	// Map the fields selected.
	map: function(data, fields, excluded) {
		if (!fields || fields.length == 0) {
			return data;
		}
		return data.map(function(elem) {
			for (var prop in elem) {
				if ((excluded && fields.indexOf(prop) != -1) || (!excluded && fields.indexOf(prop) == -1)) {
					delete elem[prop];
				}
			}
			return elem;
		});
	},
	
	findFields: function(fields) {
		for (var index = 0; index < fields.length; index++) {
			var matches = this.options.newFields.filter(function(elem){ 
				if (typeof(fields[index]) == 'object') {
					return elem['name'] == fields[index]['name'];
				}
				return elem['name'] == fields[index];
			});
			if (matches.length != 0) {
				if (typeof(fields[index]) == 'object') {
					$.extend(fields[index], matches[0]);
				} else {
					fields[index] = matches[0];
				}
			}
		}
		return fields;
	},
	
	// Get new fields list that need to create.
	newFields: function() {
		var fields = [];
		for (var index in arguments) {
			fields = fields.concat(arguments[index]);
		}
		return fields.filter(function(field){ 
			return field.display;
		});
	},
	
	// Create new fields on dataset.
	createFields: function(data, fieldDefs) {
		var dataSet = [];
		for (var index in data) {
			var clone = {};
			for (var i in data[index]) {
				clone[i] = data[index][i]; 
			}
			for (var i in fieldDefs) {
				clone = this.createField(clone, fieldDefs[i]);
			}
			dataSet.push(clone);
		}
		return dataSet;
	},
	
	// Create new field on record.
	createField: function(rec, fieldDef){
		if (typeof(fieldDef['value']) == 'function') {
			rec[fieldDef['name']] = fieldDef['value'].apply(rec);
		}
		return rec;
	},
	
	// Determine whether use exclusive fields option.
	useExcludedFields: function() {
		return this.options.fieldsIncluded && this.options.fieldsIncluded.length == 0? true: false;
	}
});
