var Scheduler = $.extendClass(iNettuts, {
	options: {},
	init: function(options){
		this.super(options);
	},
	globalInit: function() {
		Highcharts.setOptions({
	        chart: {
	            backgroundColor: 'transparent',
	            borderWidth: 0,
	            plotBackgroundColor: 'transparent',
	            plotShadow: true,
	            plotBorderWidth: 0
	        },
	        title: {
            	style: {
            		'color': 'white'
            	},
            	margin: 5
	        },
	        legend: {
	        	itemStyle: {
	    			'color': '#DDD'
	    		},
	    		margin: 5
	        },
	        xAxis: {
	        	lineColor: '#DDD',
	        	tickLength: 5
	        },
	        yAxis: {
	        	lineColor: '#DDD',
	        	tickLength: 5
	        }
		});
	},
	
	setupControls: function() {
		this.super();
		this.globalInit();
		$('#container').highcharts({
	        chart: {
	            type: 'bar'
	        },
	        title: {
	            text: 'Fruit Consumption'
	        },
	        xAxis: {
	            categories: ['Apples', 'Bananas', 'Oranges']
	            //lineColor: 'white',
	            //tickLength: 5
	        },
	        yAxis: {
	            title: {
	                text: 'Fruit eaten'
	            },
		        //lineColor: 'white',
		        //tickLength: 5,
		        lineWidth: 1
	        },
	        series: [{
	            name: 'Jane',
	            data: [1, 0, 4]
	        }, {
	            name: 'John',
	            data: [5, 7, 3]
	        }]
	    });
	}
});

$(document).ready(function(){
	new Scheduler();
});
