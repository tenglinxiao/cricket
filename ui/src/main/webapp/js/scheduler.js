var Scheduler = $.extendClass(iNettuts, {
	options: {
		style: {
			height: '250px'
		} 
	},
	
	init: function(options){
		$.extend(this.options, options);
		this.super(this.options);
	},
	
	globalInit: function() {
		Highcharts.setOptions({
	        chart: {
	            backgroundColor: 'transparent',
	            borderWidth: 0,
	            plotBackgroundColor: 'transparent',
	            plotBorderWidth: 0
	        },
	        title: {
            	style: {
            		'color': '#DDD'
            	},
            	margin: 1
	        },
	        legend: {
	        	itemStyle: {
	    			'color': '#DDD'
	    		},
	    		margin: 1
	        },
	        xAxis: {
	        	lineColor: '#DDD',
	        	tickLength: 3,
	        	title: {
	            	style: {
	            		'color': '#DDD'
	            	},
	        		margin: 3
		        }
	        },
	        yAxis: {
	        	lineColor: '#DDD',
	        	tickLength: 3,
	        	labels: {
	        		x: -2
	        	},
	        	title: {
	            	style: {
	            		'color': '#DDD'
	            	},
	        		margin: 3
		        }
	        },
	        colors: ["#7cb5ec", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354", "#2b908f", "#f45b5b", "#91e8e1"]
		});
	},
	
	setupControls: function() {
		this.super();
		this.globalInit();
		$this = this;
		
		var holdPlace = function(value, number) {
			var arr = new Array();
			for (var index = 0; index < number; index++) {
				arr.push(value);
			}
			return arr;
		};
		
		this.setupWidgetContent.call(this.__proto__, $('#intro'), function(){
			$('<div>').css($this.options.style).css({width: this.width(), height: '250px'}).highcharts({
				chart: {
					type: 'spline',
					alignTicks: false,
					events: {
						load: function(){
							var successTimes = 0;
							var categories = holdPlace('-', 20);
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/system', 
								success: function(data) {
									var timestamp = new Date().format('%H:%m:%s');
									if (successTimes < 20) {
										categories[successTimes] = timestamp;
										this.xAxis[0].setCategories(categories);
										this.series[0].data[successTimes].update({y: data.data.CpuUsage.percentage});
										this.series[1].data[successTimes].update({y: data.data.MemoryUsage.percentage});
										successTimes++;
									} else {
										this.series[0].removePoint(0);
										this.series[1].removePoint(0);
										this.series[0].addPoint([timestamp, data.data.CpuUsage.percentage]);
										this.series[1].addPoint([timestamp, data.data.MemoryUsage.percentage]);
									}
								}.bind(this)
							}, 3000);
							
						}
					}
				},
		        title: {
		            text: 'System Health'
		        },
		        legend: {
		            borderWidth: 0
		        },
		        xAxis: [{
		        	categories: holdPlace('-', 20),
		            crosshair: true
		        }],
		        yAxis: [{
		        	gridLineWidth: 1,
		            title: {
		                text: 'CPU'
		            },
			        min: 0,
			        max: 1
		        }, {
		        	gridLineWidth: 1,
		            title: {
		                text: 'Memory'
		            },
			        min: 0,
			        max: 1,
			        labels: {
		        		x: 2
		        	},
			        opposite: true
		        }],
		        series: [{
		            name: 'CPU Usage',
		            yAxis: 0,
		            data: holdPlace(null, 20)
		        }, {
		            name: 'Memory Usage',
		            yAxis: 1,
		            data: holdPlace(null, 20)
		        }]
		    }).appendTo(this);
		});
		
		this.setupWidgetContent.call(this.__proto__, $('#column_1 .widget:eq(1)'), function(){
			var scheduled = 0;
			$('<div>').css($this.options.style).css({width: this.width()/2, float: 'left', height: '300px'}).highcharts({
				chart: {
		            type: 'solidgauge',
		            alignTicks: false,
		            events: {
		            	load: function(){
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/scheduled', 
								success: function(data) {
									this.yAxis[0].setExtremes(0, data.data.length, true);
									scheduled = data.data.length;
								}.bind(this)
							}, 3000);
							
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/scheduling', 
								success: function(data) {
									this.series[0].setData([data.data.length]);
								}.bind(this)
							}, 3000);
						}
		            }
		        },
		        title: "Jobs Scheduled/Scheduling",
		        pane: {
		            center: ['50%', '70%'],
		            size: '100%',
		            startAngle: -90,
		            endAngle: 90,
		            background: {
		                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
		                innerRadius: '60%',
		                outerRadius: '100%',
		                shape: 'arc'
		            }
		        },

		        tooltip: {
		            enabled: false
		        },

		        // The value axis
		        yAxis: {
		            stops: [
		                [0.1, '#55BF3B'], // green
		                [0.5, '#DDDF0D'], // yellow
		                [0.9, '#DF5353'] // red
		            ],
		            lineWidth: 0,
		            tickInterval: 1,
		            minorTickInterval: null,
		            tickPixelInterval: 400,
		            tickWidth: 2,
		            title: {
		                y: 0,
		                text: 'Jobs'
		            },
		            labels: {
		                y: 10
		            },
		            min: 0
		        },

		        plotOptions: {
		            solidgauge: {
		                dataLabels: {
		                    y: 5,
		                    borderWidth: 0,
		                    useHTML: true
		                }
		            }
		        },
		        
		        credits: {
		            enabled: false
		        },
		        
		        series: [{
		            name: 'Scheduling/Scheduled',
		            data: [0],
		            dataLabels: {
		                formatter: function() {
		                	return '<div style="text-align:center"><span style="font-size:15px;color:#DDD">' + this.y + '/</span>' +
	                        '<span id="scheduled" style="font-size:8px;color:silver">' + scheduled + '</span>' + 
	                        '<span style="font-size:15px;color:#DDD"> on scheduling</span></div>'
		                }
		            }
		        }]
		    }).appendTo(this);
			this.find('ul').css({width: this.width() / 2 - 20});
		});

		this.setupWidgetContent.call(this.__proto__, $('#column_2 .widget:eq(0)'), function(){
			pieWidth = Math.floor(this.width() / 2 - parseInt(this.css('paddingLeft')));
			$('<div>').css($this.options.style).css({width: pieWidth, float: 'left', height: '300px'}).highcharts({
		        chart: {
		            type: 'pie',
		            events: {
		            	load: function(){
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/sla_sle', 
								success: function(data) {
									var chartData = new Array();
									var resultset = data.data;
									var min = 1;
									for (var index in resultset) {
										var point = { 
											name: resultset[index].group + "." + resultset[index].name,
										    y: (resultset[index].total - resultset[index].total_sla) / resultset[index].total
										};
										if (min > point.y) {
											min = point.y;
										}
										chartData.push(point);
									}
									
									for (var index in chartData) {
										if (chartData[index].y == min) {
											chartData[index].selected = chartData[index].sliced = true;
										}
									}

									this.series[0].setData(chartData);
								}.bind(this)
							});
						}
		            }
		        },
		        
		        plotOptions: {
		        	pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
		        },
		        
		        title: {
		            text: 'Jobs SLA'
		        },
		   
		        series: [{
	                type: 'pie',
	                name: 'SLA',
	                data: []
	            }]
		    }).appendTo(this);
			
			$('<div>').css($this.options.style).css({width: pieWidth, 'float': 'right', height: '300px'}).highcharts({
		        chart: {
		            type: 'pie',
		            events: {
		            	load: function(){
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/sla_sle', 
								success: function(data) {
									var chartData = new Array();
									var resultset = data.data;
									var min = 1;
									for (var index in resultset) {
										var point = { 
											name: resultset[index].group + "." + resultset[index].name,
										    y: (resultset[index].total - resultset[index].total_sle) / resultset[index].total
										};
										if (min > point.y) {
											min = point.y;
										}
										chartData.push(point);
									}
									
									for (var index in chartData) {
										if (chartData[index].y == min) {
											chartData[index].selected = chartData[index].sliced = true;
										}
									}
									
									this.series[0].setData(chartData);
								}.bind(this)
							});
						}
		            }
		        },
		        
		        plotOptions: {
		        	pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
		        },
		        
		        title: {
		            text: 'Jobs SLE'
		        },
		   
		        series: [{
	                type: 'pie',
	                name: 'SLE',
	                data: []
	            }]
		    }).appendTo(this);
		});

		this.setupWidgetContent.call(this.__proto__, $('#column_2 .widget:eq(1)'), function(){
			$('<div>').css($this.options.style).css({width: this.width(), height: '350px'}).highcharts({
		        chart: {
		            type: 'column',
		            events: {
		            	load: function(){
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/failures', 
								success: function(data) {
									var resultset = data.data;
									for (var index in resultset) {
										this.addSeries({ 
											name: resultset[index].group + "." + resultset[index].name,
										    data: [resultset[index].total - resultset[index].failures, resultset[index].failures]
										});
									}
								}.bind(this)
							});
						}
		            }
		        },
		        title: {
		            text: 'Success & Failure'
		        },
		        plotOptions: {
		        	column: {
		                pointPadding: 0.2,
		                borderWidth: 0
		            }
		        },
		        xAxis: {
		            categories: ['Success', 'Failure'],
		            crosshair: true
		        },
		        yAxis: {
		            title: {
		                text: 'Times'
		            },
			        min: 0
		        },
		        series: []
		    }).appendTo(this);
		});
		
		this.setupWidgetContent.call(this.__proto__, $('#column_3 .widget:eq(0)'), function(){
			$('<div>').css($this.options.style).css({width: this.width(), height: '350px'}).highcharts({
				chart: {
					events: {
		            	load: function(){
		            		var fillData = function(dates, data) {
		            			var dataset = new Array();
		            			var i = 0;
		            			for (var index = 0; index < dates.length; index++) {
		            				if (data[i].start_date == dates[index]) {
		            					dataset.push([dates[index], data[i].time_cost]);
		            					if (i < data.length - 1) {
		            						i++;
		            					}
		            				} else {
		            					dataset.push([dates[index], null]);
		            				}
		            			}
		            			return dataset;
		            		};
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/timecost', 
								success: function(data) {
									var dates = new Date().lastMonthDays();
									var resultset = data.data;
									var dataset = new Array();
									var id = 0;
									for (var index in resultset) {
										// If it's beginning of a new group elements or it's the end of elements, sumarize the previous group data.
										if (id != resultset[index].id || index == resultset.length - 1) {
											if (id != 0) {
												if (index == resultset.length - 1) {
													dataset.push(resultset[index]);
												}
												var processed = fillData(dates, dataset);
												this.addSeries({ 
													name: dataset[0].group + "." + dataset[0].name,
												    data: processed
												});
											}
											id = resultset[index].id;
											dataset = [];
										}
										dataset.push(resultset[index]);
									}
								}.bind(this)
							});
						}
		            }
				},
		        title: {
		            text: 'Time Cost'
		        },
		        legend: {
		            borderWidth: 0
		        },
		        xAxis: {
		            crosshair: true,
		            categories: new Date().lastMonthDays('%M-%d')
		        },
		        yAxis: {
		            title: {
		                text: 'Seconds'
		            }
		        },
		        series: []
		    }).appendTo(this);
		});
		
		this.setupWidgetContent.call(this.__proto__, $('#column_3 .widget:eq(1)'), function(){
			$('<div>').css($this.options.style).css({width: this.width(), height: "300px"}).highcharts({
				chart: {
		            type: 'columnrange',
		            events: {
		            	load: function(){
							$this.selfUpdate({
								url: '/ui/proxy/scheduler/rest/timeinterval', 
								success: function(data) {
									var resultset = data.data;
									var dataset = new Array();
									var categories = new Array();
									for (var index in resultset) {
										categories.push(resultset[index].group + "." + resultset[index].name);
									}
									this.xAxis[0].setCategories(categories);
									for (var index in resultset) {
										this.addSeries({
											name: resultset[index].group + '.' + resultset[index].name,
											data: [{
												low: new Date(resultset[index].start_time).toDayPoint(), 
										    	high: new Date(resultset[index].start_time + resultset[index].time_cost * 1000).toDayPoint(),
										    	startTime: resultset[index].start_time,
										    	x: index
											}],
											tooltip: {
								                pointFormatter: function() {
								                	return '<span style="color:' + this.color + '">\u25CF</span> <b>StartTime: ' + new Date(this.startTime).format('%H:%m:%d') + '</b><br/>' +
								                	'<span style="color:' + this.color + '">\u25CF</span> <b>End Time: ' + Date.parseDayPoint(this.high) + '</b>';
								                }
								                	
									        }
										});
									}									
								}.bind(this)
							});
						}
		            }
		        },
		        
		        plotOptions: {
		            columnrange: {
		                dataLabels: {
		                    enabled: true,
		                    formatter: function () {
		                        return this.y ;
		                    }
		                }
		            }
		        },

		        title: {
		            text: 'Job Time Range'
		        },
		        
		        legend: {
		            borderWidth: 0
		        },
		        
		        yAxis: {
		        	 min: 0,
		        	 max: 24,
		        	 tickInterval: 1,
		        	 title: {
		        		 text: 'Time'
		        	 }
		        },
		        series: []
		    }).appendTo(this);
		});
	}
});

$(document).ready(function(){
	new Scheduler();
});
