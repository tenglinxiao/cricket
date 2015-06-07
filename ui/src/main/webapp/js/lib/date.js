$.extend(Date.prototype, {
	format: function(){
		var formatStr = arguments.length > 0 && arguments[0]? arguments[0]: '%y-%M-%d %H-%m-%s' ;		
		var matches = formatStr.match(/%[yMdHms]/g);
		for (var index in matches) {
			value = '';
			switch (matches[index]) {
			case '%y': value = this.getFullYear(); break;
			case '%M': value = this.getMonth() + 1; break;
			case '%d': value = this.getDate(); break;
			case '%H': value = this.getHours(); break;
			case '%m': value = this.getMinutes(); break;
			case '%s': value = this.getSeconds(); break;
			}
			formatStr = formatStr.replace(matches[index], value < 10? "0" + value: value)
		}
		return formatStr;
	},
	
	getMonthDays: function() {
		switch(arguments.length == 0? this.getMonth() + 1: arguments[0]) {
		case 1: case 3:case 5: case 7:case 8: case 10: case 12: return 31;
		case 4: case 6: case 9: case 11: return 30;
		case 2: if (this.getFullYear() % 4 == 0 && this.getFullYear() % 100 != 0) {
					return 29
				} else {
					return 28;
				}
		}
	},
	
	yesterday: function() {
		var date = new Date(this.getTime());
		if (date.getDate() == 1) {
			date.setMonth(date.getMonth() - 1);
			date.setDate(date.getMonthDays());
		} else {
			date.setDate(date.getDate() - 1);
		}
		return date;
	},
	
	lastNDays: function(days, format, excludesToday) {
		if (days) {
			var dates = new Array();
			var date = new Date();
			if (!excludesToday) {
				days -= 1;
				dates.push(date.format(format));
			}
			for (var i = 0; i < days; i++) {
				date = date.yesterday();
				dates.push(date.format(format));
			} 
			return dates;
		} else {
			console.error('days must be offered!');
		}
	},
	
	lastMonthDays: function(format, excludesToday) {
		var dates = new Array();
		if (!excludesToday) {
			dates.push(this.format(format));
		}
		var yesterday = this.yesterday();
		while (yesterday.getDate() != this.getDate()) {
			dates.push(yesterday.format(format));
			yesterday = yesterday.yesterday();
		}
		return dates;
	},
	
	toDayPoint: function() {
		return (this.getHours() + this.getMinutes() / 60 + this.getSeconds() / 3600).toFixed(2);
	}
});