var Monitor = $.extendClass(Base, {
	options: {
		containerSelector: 'body',
		context: window.location.href,
		params: {
			serviceName: 'cpm.monitor.realtime'
		}
	},
	
	$container: null,
	
	currentTime: null,
	
	stuck: false,
	
	lastPackage: null,
	
	requestQueue: new Array(3),
	
	dataTables: [],
	
	counter: null,
	
	aggregateConstraints: [{
		groupKeys: ['cityid'],
		fieldsExcluded: ['butype', 'slotid']
	}, {
		groupKeys: [{
			name: 'slotid',
			value: function() {
				return this.slotid;
			}
		}],
		fieldsExcluded: ['butype', 'cityid']
	}, {
		groupKeys: ['slotid'],
		fieldsExcluded: ['butype', 'cityid']
	}],
	
	interrupted: false,
	
	$chart: null,
	
	init: function(options) {
		$.extend(this.options, options);
		
		// Get container.
		this.$container = $(this.options.containerSelector);
		
		// Parse for context url.
		this.options.context = this.options.context.substring(0, this.options.context.lastIndexOf('/') + 1);
		
		// Get current tick time for query.
		this.currentTime = this.getTickTime();
		
		this.counter = new Counter({
			limit: 3,
			action: function() {
				this.hideSpinner();
			}.bind(this)
		});
		
		// Call parent init method.
		this.super(options);
	},
	
	setupControls: function() {
		this.showSpinner();
		this.setupDataTables();
		this.setupFilters();
		
		var __self = this;
		
		// Highcharts global settings.
		Highcharts.setOptions({
//			chart: {
//				backgroundColor: 'transparent',
//	            borderWidth: 0,
//	            plotBackgroundColor: 'transparent',
//	            plotBorderWidth: 0
//			},
//			title: {
//            	style: {
//            		'color': '#DDD'
//            	}
//	        },
//	        legend: {
//	        	itemStyle: {
//	    			'color': '#DDD'
//	    		},
//	    		margin: 1
//	        },
//	        xAxis: {
//	        	lineColor: '#DDD',
//	        	tickLength: 3,
//	        	title: {
//	            	style: {
//	            		'color': '#DDD'
//	            	},
//	        		margin: 3
//		        }
//	        },
//	        yAxis: {
//	        	lineColor: '#DDD',
//	        	tickLength: 3,
//	        	labels: {
//	        		x: -2
//	        	},
//	        	title: {
//	            	style: {
//	            		'color': '#DDD'
//	            	},
//	        		margin: 3
//		        }
//	        },
			colors: ["#7cb5ec", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354", "#2b908f", "#f45b5b", "#91e8e1"]
		});

		this.scheduleAjaxCall({
			url: this.options.context + "data",
			data: {
				serviceName: 'cpm.monitor.realtime.today'
			},
			success: function(data) {
				__self.$container.highcharts({
					chart: {
						type: 'spline',
						events: {
							load: function() {
								__self.$chart = $chart = this;
								
								__self.scheduleAjaxCall({
									url: __self.options.context + 'data',
									data: {
										serviceName: 'cpm.monitor.realtime.lastWeekDay'
									},
									timeout: 7000,
									success: function(data) {
										var points = __self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'});
										$chart.series[0].setData(points);
										__self.counter.step();
									}
								});
								
								__self.scheduleAjaxCall({
									url: __self.options.context + 'data',
									data: {
										serviceName: 'cpm.monitor.realtime.yesterday'
									},
									timeout: 7000,
									success: function(data) {
										var points = __self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'});
										$chart.series[1].setData(points);
//										var max = 0;
//										for (var index in points) {
//											if (points[max].y < points[index].y) {
//												max = index;
//											}
//											$chart.series[1].addPoint(points[index]);
//										}
//										$chart.series[1].data[max].update({
//											marker: {
//												enabled: true,
//												symbol: 'circle',
//												fillColor: '#FF0000',
//												lineColor: "#FF0000",
//												radius: 8
//											}
//										});
										__self.counter.step();
									}
								});
								
								__self.scheduleAjaxCall({
									url: __self.options.context + 'data',
									data: {
										serviceName: 'cpm.monitor.realtime.now',
										param_start_time: __self.currentTime
									},
									dataRefresh: {
										param_start_time: function() {
											if (!__self.stuck) {
												__self.currentTime = __self.getTickTime();
											}
											return __self.currentTime;
										}
									},
									isInterrupted: function() {
										return __self.interrupted;
									},
									timeout: 7000,
									success: function(data) {
										if (data.data.length == 0) {
											__self.stuck = true;
											return;
										}
										__self.stuck = false;
										
										if (!__self.lastPackage || __self.lastPackage[0][0].x != data.data[0].updated_time) {
											var points = [
											  __self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'}),
											  __self.convertToPoints(data.data, {x: 'updated_time', y: 'consumption'})
											];
											__self.lastPackage = points;
											for (var index in points) {
												for (var i in points[index]) {
													$chart.series[2 + parseInt(index)].addPoint(points[index][i]);
												}
											}
										}
									}
								});
								
								new Pen({
									points: function(){
										var points = [];
										for (var index in $chart.series) {
											points = points.concat($chart.series[index].data);
										}
										return points;
									},
									converter: function(){
										return {
											origin: this,
											x: $chart.plotLeft + $chart.xAxis[0].toPixels(this.x, true), 
											y: $chart.plotTop + $chart.yAxis[0].toPixels(this.y, true)
										};
									},
									action: function(points){
										points.map(function(point){
											point.origin.remove();
											//point.origin.update({y: null});
										});
									}
								});
							}
						}
					},
					plotOptions: {
						spline: {
							allowPointSelect: true,
							tooltip: {
								headerFormat: '',
								pointFormatter: function() {
									return '<span style="color:' + this.color + '">\u25CF</span> <b>Time: ' + this.time.format('%H:%m') + '</b><br/><b> ' + this.y + ' RMB</b>';
								}
							},
		                    marker: {
		                        radius: 5,
		                        states: {
		                            hover: {
		                                enabled: true,
		                                lineColor: 'rgb(100,100,100)'
		                            }
		                        }
		                    },
		                    states: {
		                        hover: {
		                            marker: {
		                                enabled: false
		                            }
		                        }
		                    },
		                    cursor: 'pointer',
		                    events: {
		                        click: function(event) {
		                            alert('x: ' + event.chartX + ', y: ' + event.chartY);
		                        }
		                    },
							useHTML: true
						}
					},
					title: {
						text: null
					},
					xAxis: {
		                title: {
		                    text: 'Day Time',
		                    align: 'high'
		                },
						min: 0,
						tickInterval: 2,
						max: 24,
						crosshair: true,
						labels: {
							formatter: function() {
								return this.value < 10? '0' + this.value + ':00': this.value + ':00'
							}
						}
					},
					yAxis: {
						title: {
							text: 'RMB'
						},
						min: 0
					},
					series: [{
						name: 'Charge(' + new Date().lastNDay(7, true).format('%y-%M-%d') + ')',
						data: []
					}, {
						name: 'Charge(' + new Date().yesterday().format('%y-%M-%d') + ')',
						data: []
					}, {
						name: 'Charge(' + new Date().format('%y-%M-%d') + ')',
						data: __self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'})
					}, {
						name: 'Consumption(' + new Date().format('%y-%M-%d') + ')',
						data: __self.convertToPoints(data.data, {x: 'updated_time', y: 'consumption'})
					}]
				});
			}
		});
		
	},
	
	attachEvents: function() {
		var __self = this;
		$('#cpm_panel .filter button[type=submit]').on('click', function(event) {
			event.preventDefault();
			var params = {};
			
			// Fetch all the values of filters.
			$(this).parents('.filter').find('select').each(function(index, elem){
				$elem = $(elem);
				var values = $elem.val();
				if (values.length == 1 && values[0] == "ALL") {
					values = [-1];
				} else if (values.indexOf('ALL') != -1) {
					values = values.filter(function(e){
						if (e == 'ALL') return false;
						return true;
					});
				}
				
				params['param_' + $elem.attr('id')] = values.join(',');
			});
			
			// Fetch date value.
			params['param_date'] = $(this).parents('.filter').find('input.date').val();
			
			// Set service name for data fetching.
			params['serviceName'] = 'cpm.monitor.realtime.conditions';
			
			// Interrupt scheduling ajax call if the filter is used.
			__self.interrupted = true;
			
			__self.scheduleAjaxCall({
				url: __self.options.context + 'data',
				data: params,
				dataRefresh: {
					param_date: function() {
						return new Date(params.param_date).lastNDay(7, true).format('%y-%M-%d');
					}
				},
				timeout: 7000,
				success: function(data) {
					__self.$chart.series[0].update({name: 'Charge('+ new Date(params.param_date).lastNDay(7, true).format('%y-%M-%d') +')'});
					__self.$chart.series[0].setData(__self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'}));
				}
			});
			
			__self.scheduleAjaxCall({
				url: __self.options.context + 'data',
				data: params,
				dataRefresh: {
					param_date: function() {
						return new Date(params.param_date).yesterday().format('%y-%M-%d');
					}
				},
				timeout: 7000,
				success: function(data) {
					__self.$chart.series[1].update({name: 'Charge('+ new Date(params.param_date).yesterday().format('%y-%M-%d') +')'});
					__self.$chart.series[1].setData(__self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'}));
				}
			});
			
			__self.scheduleAjaxCall({
				url: __self.options.context + 'data',
				data: params,
				timeout: 7000,
				success: function(data) {
					__self.$chart.series[2].update({name: 'Charge('+ params.param_date +')'});
					__self.$chart.series[2].setData(__self.convertToPoints(data.data, {x: 'updated_time', y: 'charge'}));
					__self.$chart.series[3].update({name: 'Consumption('+ params.param_date +')'});
					__self.$chart.series[3].setData(__self.convertToPoints(data.data, {x: 'updated_time', y: 'consumption'}));
				}
			});
		});
		
		$('.panel-primary:has(.dataTable)').find('button[type=submit]').each(function(index, elem) {
			$(elem).data('index', index);
		}).on('click', function(event, init){
			event.preventDefault();	
			$(event.target).parents('.panel-body').find('.mask').show();
			var date = $(this).parents('.filter').find('input.date').val();
			var index = $(event.target).data('index');
			var dates = [date, new Date(date).yesterday().format('%y-%M-%d'), new Date(date).lastNDay(7, true).format('%y-%M-%d')];
			var cached = true;
			// Check whether data is already cached.
			for (var i in dates) {
				if (!Cache.containsGroupKey(dates[i])) {
					cached = false;
					break;
				}
			}
			
			// If not cached, then make ajax call to fetch data, else use cache data for rendering.
			if (!cached) {
				// Register an observer.
				var dataTableCounter = new Counter({
					limit: 3,
					action: function() {
						if (init) {
							for (var i = 0; i < __self.dataTables.length; i++) {
								__self.updateDataTable(i, date);
							}
							__self.counter.step();
						} else {
							__self.updateDataTable(index, date);
						}
					}
				});
				
				__self.scheduleAjaxCall({
					url: __self.options.context + 'data',
					data: {
						serviceName: 'cpm.monitor.realtime.statistics',
						param_date: date
					},
					timeout: 10000,
					success: function(data) {
						// Add data in cache.
						Cache.addGroupCache(date, data.data);
						dataTableCounter.step();
					}
				});
				
				__self.scheduleAjaxCall({
					url: __self.options.context + 'data',
					data: {
						serviceName: 'cpm.monitor.realtime.statistics',
						param_date: new Date(date).yesterday().format('%y-%M-%d')
					},
					timeout: 10000,
					success: function(data) {
						// Add data in cache.
						Cache.addGroupCache(new Date(date).yesterday().format('%y-%M-%d'), data.data);
						dataTableCounter.step();
					}
				});
				
				__self.scheduleAjaxCall({
					url: __self.options.context + 'data',
					data: {
						serviceName: 'cpm.monitor.realtime.statistics',
						param_date: new Date(date).lastNDay(7, true).format('%y-%M-%d')
					},
					timeout: 10000,
					success: function(data) {
						// Add data in cache.
						Cache.addGroupCache(new Date(date).lastNDay(7, true).format('%y-%M-%d'), data.data);
						dataTableCounter.step();
					}
				});
			} else {
				__self.updateDataTable(index, date);
			}
		}).eq(0).trigger('click', true);
	},
	
	setupFilters: function() {
		var __self = this;
		
		// Register an observer.
		var filterCounter = new Counter({
			limit: 3,
			action: function() {
				__self.attachEvents.apply(__self);
			}
		});
		
		// Setup all datepicker and set the default value as today.
		$('input.date').datepicker({dateFormat: 'yy-mm-dd'}).datepicker('setDate', new Date());
		
		// Make ajax call to fetch data for slot list.
		this.scheduleAjaxCall({
			url: this.options.context + 'data',
			data: {
				serviceName: 'cpm.monitor.realtime.slotlist'
			},
			timeout: 7000,
			success: function(data) {
				var slots = __self.mapFields(data.data, {id: 'id', text: 'slotname'})
				Cache.addCacheGroup(Cache.convertToMap(slots, 'id', 'text'), 'slots');
				$('select.slot').select2({
					data: slots
				});
				filterCounter.step();
			}
		});
	
		// Make ajax call to fetch data for city list.
		this.scheduleAjaxCall({
			url: this.options.context + 'data',
			data: {
				serviceName: 'cpm.monitor.realtime.citylist'
			},
			timeout: 7000,
			success: function(data) {
				var cities = __self.mapFields(data.data, {id: 'city_id', text: 'city_name'});
				Cache.addCacheGroup(Cache.convertToMap(cities, 'id', 'text'), 'cities');
				$('select.city').select2({
					data: cities
				});
				filterCounter.step();
			}
		});
		
		// Make ajax call to fetch data for bu list.
		this.scheduleAjaxCall({
			url: this.options.context + 'data',
			data: {
				serviceName: 'cpm.monitor.realtime.bulist'
			},
			timeout: 7000,
			success: function(data) {
				var bus = __self.mapFields(data.data, {id: 'buid', text: 'buname'});
				Cache.addCacheGroup(Cache.convertToMap(bus, 'id', 'text'), 'bus');
				$('select.bu').select2({
					data: bus
				});
				filterCounter.step();
			}
		});

	},
	
	// Setup empty data tables
	setupDataTables: function() {
		var __self = this;
		this.dataTables.push($('#city_panel .dataTable table').dataTable({
			//serverSide: true,
			processing: true,
			//info: false,
			//paging: false,
			//ordering: false,
			columns: [
			 {data: 'fdate'},
			 {data: function (row) {
				 return Cache.getGroupCache(row['cityid'], 'cities');
			 }},
			 {data: 'imp'},
			 {data: 'click'},
			 {data: 'ctr'},
			 {data: 'charge'},
			 {data: 'consumption'}
			],
			createdRow: function(row, data, index) {
				$(row).find('td').each(function(index, td){
					var $td = $(td);
					var text = $td.text();
					var pos = -1;
					if ((pos = text.indexOf('(')) != -1) {
						$td.text(text.slice(0, pos)).append($('<span>').text(text.slice(pos)));
					}
				});
			},
			language: {
				search: '搜索',
				info: '显示 _START_ 到 _END_， 共 _TOTAL_ 记录',
				infoEmpty: '没有匹配记录！',
				infoFiltered: '（过滤 _MAX_ 记录）',
				lengthMenu: '显示 _MENU_ 记录',
				emptyTable: '没有记录！',
				zeroRecords: '没有匹配记录！',
				paginate: {
					first: "首页",
					last: "尾页",
					next: "下页",
					previous: "上页"
				}
			},
			data: [] 
		}));
		
		this.dataTables.push($('#slot_all_panel .dataTable table').dataTable({
			//serverSide: true,
			processing: true,
			//info: false,
			//paging: false,
			//ordering: false,
			columns: [
			 {data: 'fdate'},
			 {data: function(row) {
				 var category = __self.getSlotCategory(row['slotid']);
				 return category? category.name: '-';
			 }},
			 {data: 'imp'},
			 {data: 'click'},
			 {data: 'ctr'},
			 {data: 'charge'},
			 {data: 'consumption'}
			],
			createdRow: function(row, data, index) {
				$(row).find('td').each(function(index, td){
					var $td = $(td);
					var text = $td.text();
					var pos = -1;
					if ((pos = text.indexOf('(')) != -1) {
						$td.text(text.slice(0, pos)).append($('<span>').text(text.slice(pos)));
					}
				});
			},
			language: {
				search: '搜索',
				info: '显示 _START_ 到 _END_， 共 _TOTAL_ 记录',
				infoEmpty: '没有匹配记录！',
				infoFiltered: '（过滤 _MAX_ 记录）',
				lengthMenu: '显示 _MENU_ 记录',
				emptyTable: '没有记录！',
				zeroRecords: '没有匹配记录！',
				paginate: {
					first: "首页",
					last: "尾页",
					next: "下页",
					previous: "上页"
				}
			},
			data: []
		}));
		
		this.dataTables.push($('#slot_panel .dataTable table').dataTable({
			//serverSide: true,
			processing: true,
			//info: false,
			//paging: false,
			//ordering: false,
			columns: [
			 {data: 'fdate'},
			 {data: function(row) {
				 var slotName = Cache.getGroupCache(row['slotid'], 'slots')
				 return slotName? slotName: '-';
			 }},
			 {data: 'imp'},
			 {data: 'click'},
			 {data: 'ctr'},
			 {data: 'charge'},
			 {data: 'consumption'}
			],
			createdRow: function(row, data, index) {
				$(row).find('td').each(function(index, td){
					var $td = $(td);
					var text = $td.text();
					var pos = -1;
					if ((pos = text.indexOf('(')) != -1) {
						$td.text(text.slice(0, pos)).append($('<span>').text(text.slice(pos)));
					}
				});
			},
			language: {
				search: '搜索',
				info: '显示 _START_ 到 _END_， 共 _TOTAL_ 记录',
				infoEmpty: '没有匹配记录！',
				infoFiltered: '（过滤 _MAX_ 记录）',
				lengthMenu: '显示 _MENU_ 记录',
				emptyTable: '没有记录！',
				zeroRecords: '没有匹配记录！',
				paginate: {
					first: "首页",
					last: "尾页",
					next: "下页",
					previous: "上页"
				}
			},
			data: []
		}));
	},
	
	updateDataTable: function(index, date) {
		var dates = [date, new Date(date).yesterday().format('%y-%M-%d'), new Date(date).lastNDay(7, true).format('%y-%M-%d')];
		var data = [];
		for (var i in dates) {
			data.push(Cache.getGroupCache(dates[i])); 
		}
		
		// Gathering filter params.
		var params = {};
		$('.panel-primary:has(.dataTable) .filter').eq(index).find('select').each(function(index, elem){
			$elem = $(elem);
			var values = $elem.val();
			if (values.length > 0 && values.indexOf('ALL') != -1) {
				values = values.filter(function(e){
					if (e == 'ALL') return false;
					return true;
				});
			}
			if (values.length > 0) {
				params[$elem.attr('column')] = values;
			}
		});

		var __self = this;
		var aggregate = new Aggregate();
		var $dataTable = this.dataTables[index];
		var dataSet = [];
		
		// Aggregate data for required 3 days.
		for (var i in data) {
			var temp = aggregate.aggregate(data[i], $.extend({}, this.aggregateConstraints[index], {
				isValid: function() {
					for(var p in params) {
						if (params[p] instanceof Array) {
							if (params[p].indexOf(this[p]) == -1) {
								return false;
							}
						} else if(this[p] != params[p]) {
							return false;
						}
					}
					return true;
				},
				aggregate: function(context) {
					var data = context.data;
					var result = $.cloneObj(data[0]);
					result['imp'] = result['click'] = result['charge'] = result['consumption'] = result['pv'] = 0;
					for (var index = 0; index < data.length; index++) {
						result['imp'] += new Number(data[index]['imp']);
						result['click'] += new Number(data[index]['click']);
						result['charge'] += new Number(data[index]['charge']);
						result['consumption'] += new Number(data[index]['consumption']);
						result['pv'] += new Number(data[index]['pv']);
					}
					context.result = result;
				},
				end: function(context) {
					if (context.result['pv']) {
						context.result['ctr'] = context.result['click'] / context.result['pv'] * 100;
					} else {
						context.result['ctr'] = '-';
					}
					this.collect(context.result);
				}
			}));
			
			if (index == 1) {
				temp = aggregate.aggregate(temp, {
					groupKeys: [{
						name: 'slotid',
						value: function(){
							return __self.getSlotCategory(this.slotid).index;
						},
						display: true
					}],
					isValid: function() {
						return __self.getSlotCategory(this.slotid);
					},
					aggregate: function(context) {
						var data = context.data;
						var result = $.cloneObj(data[0]);
						result['imp'] = result['click'] = result['charge'] = result['consumption'] = result['pv'] = 0;
						for (var index = 0; index < data.length; index++) {
							result['imp'] += new Number(data[index]['imp']);
							result['click'] += new Number(data[index]['click']);
							result['charge'] += new Number(data[index]['charge']);
							result['consumption'] += new Number(data[index]['consumption']);
							result['pv'] += new Number(data[index]['pv']);
						}
						context.result = result;
					},
					end: function(context) {
						if (context.result['pv']) {
							context.result['ctr'] = context.result['click'] / context.result['pv'] * 100;
						} else {
							context.result['ctr'] = '-';
						}
						this.collect(context.result);
					}
				});
				
				temp = temp.concat(aggregate.aggregate(temp, {
					groupKeys: [{
						name: 'slotid',
						value: function() {
							return -3;
						},
						display: true
					}],
					isValid: function(){
						return this.slotid > -3;
					},
					aggregate: function(context) {
						var data = context.data;
						var result = $.cloneObj(data[0]);
						result['imp'] = result['click'] = result['charge'] = result['consumption'] = result['pv'] = 0;
						for (var index = 0; index < data.length; index++) {
							result['imp'] += new Number(data[index]['imp']);
							result['click'] += new Number(data[index]['click']);
							result['charge'] += new Number(data[index]['charge']);
							result['consumption'] += new Number(data[index]['consumption']);
							result['pv'] += new Number(data[index]['pv']);
						}
						context.result = result;
					},
					end: function(context) {
						if (context.result['pv']) {
							context.result['ctr'] = context.result['click'] / context.result['pv'] * 100;
						} else {
							context.result['ctr'] = '-';
						}
						this.collect(context.result);
					}
				}));
			}
			
			dataSet.push(temp);
		}
		
		// Finish aggregation for comparation.
		dataSet = dataSet[0].concat(dataSet[1]).concat(dataSet[2]);
		dataSet = aggregate.aggregate(dataSet, $.extend({}, this.aggregateConstraints[index], {
			groupSort: [{
				name: 'fdate',
				asc: false
			}],
			newFields: [{
				name: 'imp2', 
				value: function(){ 
					return this.imp;
				},
				display: true
			}],
			orderBy: [{
				name: 'imp2', 
				converter: 'string2number',
				asc: false
			}],
			aggregate: function(context) {
				var data = context.data;
				var result = $.cloneObj(context.data[0]);
				if (result.fdate != dates[0]) {
					result.fdate = dates[0];
					result['imp2'] = result['imp'] = result['click'] = result['charge'] = result['consumption'] = result['pv'] = 0;
					result['ctr'] = '-';
				}
				var data = dates.map(function(date) {
					var matched = data.filter(function(elem) {
						return elem.fdate == date;
					});
					if (matched.length > 0) {
						return matched[0];
					}
				});
				
				var mapField = function(data, prop) {
					return data.map(function(elem) {
						if (!elem) return null;
						return elem[prop];
					});
				};
				result['imp'] = __self.formatNumber(result['imp']) + __self.computePercentages.apply(__self, mapField(data, 'imp'));
				result['click'] = __self.formatNumber(result['click']) + __self.computePercentages.apply(__self, mapField(data, 'click'));
				result['charge'] = __self.formatNumber(new Number(result['charge']).toFixed(2)) + __self.computePercentages.apply(__self, mapField(data, 'charge'));
				result['consumption'] = __self.formatNumber(new Number(result['consumption']).toFixed(2)) + __self.computePercentages.apply(__self, mapField(data, 'consumption'));
				result['pv'] = __self.formatNumber(result['pv']) + __self.computePercentages.apply(__self, mapField(data, 'pv'));
				result['ctr'] = (result['ctr'] == '-'? result['ctr']: new Number(result['ctr']).toFixed(2) + '%') + __self.computePercentages.apply(__self, mapField(data, 'ctr'));
				context.result = result;
			},
			end: function(context) {
				this.collect(context.result);
			}
		}));

		$dataTable.fnClearTable(0);
		if (dataSet.length > 0) {
			$dataTable.fnAddData(dataSet);
		}
		$dataTable.fnDraw();
		$('div.dataTable').find('.mask').eq(index).hide();
	},
	
	convertToPoints: function(data, map) {
		var points = new Array();
		var lastX = 0;
		var x = 0;
		for (var d in data) {
			x = new Date(data[d][map.x]).toDayPoint();
			if (lastX < x) {
				points.push({
					time: new Date(data[d][map.x]),
					x: x,
					y: Number(data[d][map.y])
				});
				lastX = x;
			}
		}
		return points;
	},
	
	// Map obj fields.
	mapFields: function(data, map) {
		var refactored = new Array();
		for (var d in data) {
			var obj = {};
			for (var prop in map) {
				obj[prop] = data[d][map[prop]];
			}
			refactored.push(obj);
		}
		return refactored;
	},
	
	getSlotCategory: (function() {
		var categories = [{
			name: '站内-推荐',
			scope: [3, 4, 5, 10004, 10007, 11004, 12004, 20004, 21004, -1]
		}, {
			name: '站内-搜索',
			scope: [10005, 10008, 10009, 10010, 10011, 10012, 11009, -2]
		}, {
			name: '站内',
			scope: [3, 4, 5, 10004, 10007, 11004, 12004, 20004, 21004, 10005, 10008, 10009, 10010, 10011, 10012, 11009, -3]
		}, {
			name: '站外',
			scope: [30001, 30002, 30003, 30004, 30005, -4]
		}];
		return function(slot) {
			for (var index in categories) {
				if (categories[index].scope.indexOf(Number.parseInt(slot)) != -1) {
					return {index: -index - 1, name: categories[index].name};
				}
			}
			return null;
		}
	})(),
	
	getTickTime: function() {
		var date = new Date();
		date.setSeconds(0);
		return date.format();
	},
	
	handleError: function(request, status, error) {
		this.hideSpinner();
		console.error(request, status, error);
	},
	
	showSpinner: function() {
		var $mask = $('#mask');
		var $body = $('body');
		var $spinner = $(new Spinner().spin().el).appendTo($mask);
		$mask.show();
		$(window).resize(function(event){
			$mask.height($body.height());
		}).scroll(function(event){
			$spinner.css('top', $body.scrollTop() + $(window).height() / 2);
		}).trigger('resize').trigger('scroll');
		
		$('div.dataTable').find('.mask').each(function(index, elem) {
			$(new Spinner().spin().el).appendTo($(elem));
		});
	},
	
	hideSpinner: function() {
		$('#mask').hide();
	},
	
	formatNumber: function(number) {
		var numberStr = number;
		if (typeof(numberStr) != 'string') {
			numberStr = String(numberStr);
		}
		var dotPosition = numberStr.indexOf('.');
		if (dotPosition == -1) {
			dotPosition = numberStr.length;
		}
		if (dotPosition <= 3) {
			return numberStr;
		}
		var format = '';
		var start = 0;
		for (var end = dotPosition % 3; end <= dotPosition; end+=3) {
			format = format.concat(numberStr.slice(start, end));
			if (end != 0 && end != dotPosition) {
				format = format.concat(',');
			}
			start = end;
		}
		format = format.concat(numberStr.slice(dotPosition, numberStr.length));
		return format;
	},
	
	computePercentages: function() {
		if (arguments.length > 2) {
			var percentage = '(';
			for (var index = 1; index < arguments.length; index++) {
				percentage += this.computePercentage(arguments[index], arguments[0]);
				if (index < arguments.length - 1) {
					percentage += ',';
				}
			}
			percentage += ')';
			return percentage;
		}
	},
	
	computePercentage: function(base, data) {
		base = new Number(base);
		data = new Number(data);
		if (isNaN(base) || isNaN(data) || base == 0) {
			return '-';
		}
		return ((data - base) * 100 / base).toFixed(2) + '%';
	}
});


$(function(){
	new Monitor({containerSelector: '#monitorChart'});
})