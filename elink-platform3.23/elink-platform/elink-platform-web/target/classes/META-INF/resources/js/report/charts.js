//http://echarts.baidu.com/index.html
function getRotateBarOption(title, dataAxis, data, util) {
	return {
		title : {
			text : title,
			subtext : '',
			textStyle : {
				color : '#ffffff',
			}
		},
		tooltip : {
			trigger : 'axis'
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'value',
			axisLabel : {
				formatter : '{value}' + util
			},
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			}
		},
		grid: {
	        left:60
	    },
		yAxis : {
			type : 'category',
			axisLabel : {
				interval : 0,
				show : true,
				formatter: function (value, index) {
					return getFormatNumber(value);
				},
				textStyle : {
					"fontFamily" : "微软雅黑",
					"fontSize" : 12
				}
			},
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			},
			data : dataAxis
		},
		series : [ {
			name : '共计',
			type : 'bar',
			itemStyle : {
				normal : {
					color : new echarts.graphic.LinearGradient(0, 0, 0, 1, [ {
						offset : 0,
						color : '#83bff6'
					}, {
						offset : 0.5,
						color : '#188df0'
					}, {
						offset : 1,
						color : '#188df0'
					} ])
				},
				emphasis : {
					color : new echarts.graphic.LinearGradient(0, 0, 0, 1, [ {
						offset : 0,
						color : '#2378f7'
					}, {
						offset : 0.7,
						color : '#2378f7'
					}, {
						offset : 1,
						color : '#83bff6'
					} ])
				}
			},
			data : data
		} ]
	};
}

function getBarOption(title, dataAxis, data,toolboxDisable) {
	var toolbox = {};
	if(!toolboxDisable){
		toolbox = {
			feature : {
				saveAsImage : {}
			}
		};
	}
	return {
		grid : {
			show : 'true',
			borderWidth : '0'
		},
		title : {
			text : title,
			textStyle : {
				color : '#ffffff',
			}
		},
		tooltip : {
			trigger : 'axis',
			axisPointer : { // 坐标轴指示器，坐标轴触发有效
				type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			},

			formatter : function(params) {
				var tar = params[0];
				return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
			}
		},
		toolbox : toolbox,
		xAxis : {
			type : "category",
			splitLine : {
				show : false
			},
			axisLabel : {
				interval : 0,
				rotate : 30,
				show : true,
				textStyle : {
					fontFamily : "微软雅黑",
					fontSize : 12,
					color : '#fff'
				}
			},
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			},
			data : dataAxis
		},
		grid: {
	        left:60
	    },
		yAxis : {
			type : 'value',
			minInterval : 1,
			splitLine : {
				show : false
			},
			axisLabel : {
				show : true,
				formatter: function (value, index) {
					   return getFormatNumber(value);
				},
				textStyle : {
					color : '#fff'
				}
			},
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			}
		},
		series : [ {
			name : '共计',
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'inside'
					}
				}
			},
			data : data
		} ]
	};
}

function getPieOption(title, legend, data,toolboxDisable) {
	var toolbox = {};
	if(!toolboxDisable){
		toolbox = {
			feature : {
				saveAsImage : {}
			}
		};
	}
	return {
		title : {
			text : title,
			textStyle : {
				color : '#ffffff',
			},
			x : 'center'
		},
		legend : {
			orient : 'vertical',
			x : 'right',
			textStyle : {
				color : '#ffffff',
			},
			data : legend
		},
		calculable : false,
		tooltip : {
			trigger : 'item',
			formatter : "{b} : {c} ({d}%)"
		},
		toolbox : toolbox,
		series : [ {
			type : 'pie',
			radius : [ '40%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				}
			},
			data : data
		} ]
	};
}

function getLineOption(title, seriesName, data, util) {
	return {
		title : {
			text : title,
			textStyle : {
				color : '#ffffff',
			}
		},
		tooltip : {
			trigger : 'axis',
			formatter : "{c}"+util
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'time',
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			},  
		},
		grid: {
	        left:60
	    },
		yAxis : {
			type : 'value',
			formatter: function (value, index) {
				   return getFormatNumber(value) + util;
			},
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			},
			max: function(value) {
			    return parseInt(value.max + value.max/5);
			}
		},
		series : [ {
			name : seriesName,
			type : 'line',
			showSymbol : false,
			data : data,
			markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		} ]
	};
}

function getLineCategoryOption(title, dataAxis, data, util) {
	return {
		title : {
			text : title,
			textStyle : {
				color : '#ffffff',
			}
		},
		tooltip : {
			trigger : 'axis'
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'category',
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			},
			data : dataAxis
		},
		grid: {
	        left:60
	    },
		yAxis : {
			type : 'value',
			axisLabel : {
				formatter: function (value, index) {
					 return getFormatNumber(value)+ util;
				},
			},
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			}
		},
		series : [ {
			name : '共计',
			type : 'line',
			data : data
		} ]
	};
}

function getStepLineOption(title, data) {
	return {
		title : {
			text : title,
			textStyle : {
				color : '#ffffff',
			}
		},
		tooltip : {
			trigger : 'axis'
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'time',
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			}
		},
		grid: {
	        left:60
	    },
		yAxis : {
			type : 'value',
			minInterval : 1,
			axisLine: { 
				show: true,
				formatter: function (value, index) {
					return getFormatNumber(value);
				},
				lineStyle: {
					color: '#4b8df8'
				}  
			}
		},
		series : [ {
			type : 'line',
			step : 'start',
			data : data
		} ]
	};
}

function getStateTimeLineOption(title, xAxisData, yAxisData) {
	return {
		title : {
			text : title,
			textStyle : {
				color : '#ffffff',
			}
		},
		tooltip : {
			trigger : 'item',
			formatter : '{c}'
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'time',
			axisLine: { 
				show: true,
				lineStyle: {
					color: '#4b8df8'
				}  
			}
		},
		grid: {
	        left:60
	    },
		yAxis : {
			type : 'category',
			axisLine: { 
				show: true,
				formatter: function (value, index) {
				   return getFormatNumber(value);
				},
				lineStyle: {
					color: '#4b8df8'
				}  
			},
			data : yAxisData
		},
		series : [ {
			type : 'line',
			step : 'start',
			data : xAxisData
		} ]
	};
}