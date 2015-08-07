/**
 * Created by tenglinxiao on 4/8/15.
 * @author uknow.
 */

var Quartz = $.extendClass(function(){}, {
    options: {

    },
    
    cronSchedule: Array(6),
    
    init: function(options) {
        $.extend(this.options, options);
    },

    decode: function() {

    },

    encode: function(schedule) {

    },
    
    everyNSeconds: function(n) {
    	if (n > 1) {
    		this.cronSchedule[0] = '*/' + n;
    	}
    	return this;
    },

    everyNMinutes: function() {
    	if (n > 1) {
    		this.cronSchedule[1] = '*/' + n;
    	}
    	return this;
    },

    everyNHours: function() {
    	if (n > 1) {
    		this.cronSchedule[2] = '*/' + n;
    	}
    	return this;
    },

    everyMonthDay: function(n) {
    	if (n > 0 && n <= 31) {
    		this.cronSchedule[3] = '*/' + n;
    	}
    	return this;
    },
    
    everyMonth: function() {
    	if (n > 0 && n <= 31) {
    		this.cronSchedule[4] = n;
    	}
    	return this;
    },

    everyWeekDay: function(5) {
    	if (n > 1) {
    		this.cronSchedule[0] = n;
    	}
    	return this;
    },
    
    startAt: function(n, field) {
    	// Set the start value to the field only if the field value between 0 and 5
    	if (field >= 0 && field < 6) {
    		if (!this.cronSchedule[field]) {
    			this.cronSchedule[field] = n; 
    		} else {
    			this.cronSchedule[field] = this.cronSchedule[field].replace('*', n);
    		}
    	}
    	return this;
    }
    
    reset: function() {
    	this.cronSchedule = Array(6);
    }
});