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
	        }
		});
	},
	
	setupControls: function() {
		this.super();
		this.globalInit();
		$this = this;
		
		this.setupWidgetContent.call(this.__proto__, $('#column_1 .widget:eq(1)'), function(){
			$('<div>').css($this.options.style).css({width: this.width()/2, float: 'left'}).highcharts({
				chart: {
		            type: 'solidgauge'
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
		            minorTickInterval: null,
		            tickPixelInterval: 400,
		            tickWidth: 2,
		            title: {
		                y: 0,
		                text: 'Jobs'
		            },
		            labels: {
		                y: 16
		            },
		            min: 0,
		            max: 200
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
		            name: 'Speed',
		            data: [10],
		            dataLabels: {
		                format: '<div style="text-align:center"><span style="font-size:15px;color:#DDD">{y}/</span>' +
		                       '<span style="font-size:8px;color:silver">200</span>' + 
		                       '<span style="font-size:15px;color:#DDD"> on scheduling</span></div>'
		            }
		        }]
		    }).appendTo(this);
		});

		this.setupWidgetContent.call(this.__proto__, $('#column_2 .widget:eq(0)'), function(){
			pieWidth = Math.floor(this.width() / 2 - parseInt(this.css('paddingLeft')));
			$('<div>').css($this.options.style).css({width: pieWidth, float: 'left'}).highcharts({
		        chart: {
		            type: 'pie'
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
	                data: [
	                    ['Firefox',   45.0],
	                    ['IE',       26.8],
	                    {
	                        name: 'Chrome',
	                        y: 12.8,
	                        sliced: true,
	                        selected: true
	                    },
	                    ['Safari',    8.5],
	                    ['Opera',     6.2],
	                    ['Others',   0.7]
	                ]
	            }]
		    }).appendTo(this);
			
			$('<div>').css($this.options.style).css({width: pieWidth, 'float': 'right'}).highcharts({
		        chart: {
		            type: 'pie'
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
	                data: [
	                    ['Firefox',   45.0],
	                    ['IE',       26.8],
	                    {
	                        name: 'Chrome',
	                        y: 12.8,
	                        sliced: true,
	                        selected: true
	                    },
	                    ['Safari',    8.5],
	                    ['Opera',     6.2],
	                    ['Others',   0.7]
	                ]
	            }]
		    }).appendTo(this);
		});

		this.setupWidgetContent.call(this.__proto__, $('#column_2 .widget:eq(1)'), function(){
			$('<div>').css($this.options.style).css({'width': this.width()}).highcharts({
		        chart: {
		            type: 'column'
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
		            categories: ['Sucess', 'Failure'],
		            crosshair: true
		        },
		        yAxis: {
		            title: {
		                text: 'Times'
		            },
			        min: 0
		        },
		        series: [{
		            name: 'Jane',
		            data: [1, 4]
		        }, {
		            name: 'John',
		            data: [5, 7]
		        }, {
		            name: 'John',
		            data: [5, 3]
		        }, {
		            name: 'John',
		            data: [4, 3]
		        }]
		    }).appendTo(this);
		});
		
		this.setupWidgetContent.call(this.__proto__, $('#column_3 .widget:eq(0)'), function(){
			$('<div>').css($this.options.style).css({'width': this.width()}).highcharts({
		        title: {
		            text: 'Time Cost'
		        },
		        legend: {
		            //layout: 'vertical',
		            //align: 'bottom',
		            //verticalAlign: 'middle',
		            borderWidth: 0
		        },
		        xAxis: {
		            categories: ['1', '2','4','5','6'],
		            crosshair: true
		        },
		        yAxis: {
		            title: {
		                text: 'Seconds'
		            },
			        min: 0
		        },
		        series: [{
		            name: 'Tokyo',
		            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
		        }, {
		            name: 'New York',
		            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
		        }, {
		            name: 'Berlin',
		            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
		        }, {
		            name: 'London',
		            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
		        }]
		    }).appendTo(this);
		});
		
		this.setupWidgetContent.call(this.__proto__, $('#column_3 .widget:eq(1)'), function(){
			$('<div>').css($this.options.style).css({'width': this.width()}).highcharts({
				chart: {
		            type: 'columnrange',
		        },
		        
		        plotOptions: {
		            columnrange: {
		                dataLabels: {
		                    enabled: true
//		                    formatter: function () {
//		                        return this.y ;
//		                    }
		                }
		            }
		        },

		        title: {
		            text: 'Job Time Interval'
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
		        series: [{
		            name: 'TimeInterval',
		            data: [
		                [-9.7, 9.4],
		                [-8.7, 6.5],
		                [-3.5, 9.4],
		                [-1.4, 19.9],
		                [0.0, 22.6],
		                [2.9, 22.5],
		                [9.2, 22.7],
		                [7.3, 23.5],
		                [4.4, 18.0],
		                [-3.1, 11.4],
		                [-5.2, 10.4],
		                [-13.5, 9.8]
		            ]
		        }]
		    }).appendTo(this);
		});
	}
});

$(document).ready(function(){
	new Scheduler();
});
