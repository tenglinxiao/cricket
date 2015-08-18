/**
 * Created by uknow on 28/7/15.
 */

var Scheduler = $.extendClass(Base, {
    options: {

    },

    context: null,

    init: function(options) {
        $.extend(this.options, options);
        this.context = window.location.href.substring(0, window.location.href.lastIndexOf("/"));
        this.super(options);
    },

    setupControls: function() {
        var __self = this;
        
        // Add customized validator.
        $.formUtils.addValidator({
        	name: 'radio_pane',
        	validatorFunction: function(value, $el, config, language, $form) {
        		if ($el.attr('disabled') || value) {
        			return true;
        		}
        		return false;
        	},
        	errorMessageKey: 'radio_pane_error',
        	errorMessage: 'Job Jar或者Shell脚本路径必须指定！'
        });
        
        // Apply the validator.
        $.validate({
    		onSuccess: function($form) {
    			var mapping = new Mapping({
    				container: $form,
    				filter: function() {
    					var $this = $(this);
    					if ($this.is(':radio,:checkbox')) {
    						return $this.is(':checked');
    					}
    					return !$(this).attr('disabled') ;
    				}
    			});
    			__self.scheduleAjaxCall({
    				url: __self.context + $form.attr('action'),
    				type: 'POST',
    				data: JSON.stringify(mapping.map()),
    				success: function(data) {
    					var result = $('#result');
    					if (data.status.toLowerCase() == 'ok') {
	    					result.find('.alert-success').addClass('show').removeClass('hide').siblings().addClass('hide').removeClass('show');
	    					$form.get(0).reset();
    					} else {
    						result.find('.alert-danger').find('span').text(data.msg).end()
    							.addClass('show').removeClass('hide').siblings().addClass('hide').removeClass('show');
    					}
    					result.modal('show');
    				}
    			});
    		}
    	});
        
        var dataTable = $('#privateJobs').dataTable({
            processing: false,
            lengthChange: false,
            columns: [
                {data: 'id'},
                {data: 'jobKey.name'},
                {data: 'jobKey.group'},
                {data: 'schedule'},
                {
                	data: function(row, type, set, meta) {
                		return new Date(row.createdTime).format('%y-%M-%d');
                	}
                },
                {
                	data: function(row, type, set, meta) {
                		return new Date(row.createdTime).format('%y-%M-%d');
                	}
                },
                {
                	data: null,
                	defaultContent: '<div class="btn-group btn-group-sm"></div>'
                }
            ],
            data: [],
            createdRow: function(row, data, index) {
                new EditBar({
                    container: $(row).find('td:last div'),
                    buttons: [{
                            name: 'edit',
                            text: '编辑',
                            cls: 'btn btn-success',
                            click: function() {
                                var $this = $(this);
                                $('#editModal').modal().find('.modal-footer .btn-primary').click(function(event) {

                                });
                            }
                        }, {
                            name: 'remove',
                            text: '删除',
                            cls: 'btn btn-danger',
                            click: function(){
                                var $this = $(this);
                                $('#removeModal').modal().find('.modal-footer .btn-primary').click(function(event) {
                                    __self.scheduleAjaxCall({
                                        url: __self.context + '/proxy/scheduler/rest/deleteJob',
                                        type: 'POST',
                                        data: $this.parents('tr').find('td:first').text(),
                                        success: function(data) {
                                            $(event.target).parents('.modal').modal('hide');
                                            var result = $('#result');
                                            if (data.data) {
                                                result.find('.alert-success').addClass('show').removeClass('hide').siblings().addClass('hide').removeClass('show');
                                            } else {
                                                result.find('.alert-danger').find('span').text(data.msg).end()
                                                    .addClass('show').removeClass('hide').siblings().addClass('hide').removeClass('show');
                                            }
                                        }
                                    });
                                });
                            }
                        }]
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
            }
        });

        var allJobs = $('#publicJobs').dataTable({
            processing: false,
            lengthChange: false,
            columns: [
                {data: 'id'},
                {data: 'jobKey.name'},
                {data: 'jobKey.group'},
                {data: 'schedule'},
                {
                    data: function(row, type, set, meta) {
                        return new Date(row.createdTime).format('%y-%M-%d');
                    }
                },
                {
                    data: function(row, type, set, meta) {
                        return new Date(row.createdTime).format('%y-%M-%d');
                    }
                }
            ],
            data: [],
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
            }
        });
        
        // Load data for groups list.
        this.scheduleAjaxCall({
            url: __self.context + '/proxy/scheduler/rest/allGroups',
            success: function(data) {
            	var $groups = $('#jobGroup')
            	$.each(data.data, function(index, elem) {
            		$('<option>').val(elem.name).text(elem.name).appendTo($groups);
            	});
            }
        });

        // Load data for fetching all private jobs.
        this.scheduleAjaxCall({
            url: __self.context + '/proxy/scheduler/rest/privateJobs',
            data: {
                owner: $('#username').text()
            },
            success: function(data) {
            	if (data.data.length > 0) {
            		dataTable.fnAddData(data.data);
            	}
            }
        });
        
        // Loading data for file structure modal.
        this.scheduleAjaxCall({
            url: __self.context + '/proxy/scheduler/rest/readDir',
            success: function(data) {
            	var tree = data.data.tree;
            	if (!tree) {
            		tree = data.data;
            	}

            	$('#tree').treeview({
            		levels: 5,
                	expandIcon: 'glyphicon glyphicon-folder-close',
                	collapseIcon: 'glyphicon glyphicon-folder-open',
                	showTags: true,
                	showBorder: false,
                	data: [this.convert(tree)],
                	onNodeSelected: function(event, node) {
                		var $this = $(event.target);
                		var parent = node;
                		var segments = [node.text];
                		var index = 0;
                		while(parent = $this.treeview('getParent', parent)) {
                			if ($this.is(parent)) {
                				$this.data('path', segments.slice(1).join('/'));
                				break;
                			}
                			segments.unshift(parent.text);
                		}
                	}
                });
            }.bind(this)
        });

        $('#public-tab').on('click', function() {
            var $this = $(this);
            if (!$this.data('initialized')) {
                __self.scheduleAjaxCall({
                    url: __self.context + '/proxy/scheduler/rest/allJobs',
                    success: function(data) {
                        allJobs.fnAddData(data.data);
                        $this.data('initialized', true);
                    }
                });
            }
        });
        
    },
    
    bindEvents: function() {
        var __self = this;
    	var $radios = $('input[name=jobType]');
    	$radios.click(function(event) {
    		var $target = $(event.target);
    		$radios.each(function(index, elem) {
    			if ($target.is(elem)) {
    				$(elem).siblings('input').attr('disabled', null);
    			} else {
    				$(elem).siblings('input').css('borderColor', '').attr('disabled', 'disabled');
    			}
    		});
    	});
    	
    	$('#entryFile').on('focus', function(event) {
    		event.stopPropagation();
    		var $target = $(event.target);
    		$('#explorer').find('.btn-primary').click(function(e) {
    			$target.val($(e.target).parents('.modal').modal('hide').find('#tree').data('path'));
    			$target.trigger('blur');
    		}).end().modal('show');
    	});
    },
    
    convert: function(node, depth) {
    	if (!depth)  depth = 0;
		if (node.children) {
			node.text = node.name;
			node.tags = [node.children.length];
			node.selectable = false;
			node.nodes = node.children.map(function(elem, index){
	    		return this.convert(elem, depth + 1);
	    	}.bind(this));
			if (depth) {
				node.state = {expanded: false};
			}
			return node;
		} else {
			var newNode = {}
			newNode.text = node;
			newNode.icon = 'glyphicon glyphicon-file';
			newNode.selectedIcon = 'glyphicon glyphicon-ok';
			return newNode;
		}
    }
});

$(function(){
    new Scheduler();
});


