$(function(){
	//获取统计信息
	var statisticsInfo = {};
	$.ajax({
			type: 'POST',
			url: 'getDeviceStatistics',
			data:{},
			dataType: 'json',
			async: false,
			success: function(data){
				layer.msg('统计图数据加载完成!',{icon:1,time:1000});
				statisticsInfo = data;
				console.log(data);
			},
			error:function(data) {
				console.log(data.msg);
			},
	});

	var myChart1;
	var myChart2;
	var domCode = document.getElementById('sidebar-code');
	var domGraphic = document.getElementById('graphic');
	var domMain1 = document.getElementById('main1');
	var domMain2 = document.getElementById('main2');
	var domMain3 = document.getElementById('main3');
	var domMain4 = document.getElementById('main4');
	var domMessage = document.getElementById('wrong-message');
	var iconResize = document.getElementById('icon-resize');
	var needRefresh = false;

	var enVersion = location.hash.indexOf('-en') != -1;
	var hash = location.hash.replace('-en','');
	hash = hash.replace('#','') || (needMap() ? 'default' : 'macarons');
	hash += enVersion ? '-en' : '';

	var curTheme;
	function requireCallback (ec, defaultTheme) {
	    curTheme = {};
	    echarts = ec;
	    refresh();
	    window.onresize = myChart1.resize;
	}

	function autoResize() {
	    if ($(iconResize).hasClass('glyphicon-resize-full')) {
	        focusCode();
	        iconResize.className = 'glyphicon glyphicon-resize-small';
	    }
	    else {
	        focusGraphic();
	        iconResize.className = 'glyphicon glyphicon-resize-full';
	    }
	}

	function focusCode() {
	    domCode.className = 'col-md-8 ani';
	    domGraphic.className = 'col-md-4 ani';
	}

	function focusGraphic() {
	    domCode.className = 'col-md-4 ani';
	    domGraphic.className = 'col-md-8 ani';
	    if (needRefresh) {
	        myChart1.showLoading();
	        myChart2.showLoading();
	        setTimeout(refresh, 1000);
	    }
	}

	function refresh(isBtnRefresh){
	    if (isBtnRefresh) {
	        needRefresh = true;
	        focusGraphic();
	        return;
	    }
	    needRefresh = false;
	    if (myChart1 && myChart1.dispose) {
	        myChart1.dispose();
	        myChart2.dispose();
	    }
	    myChart1 = echarts.init(domMain1, curTheme);
	    myChart2 = echarts.init(domMain2, curTheme);
	    myChart3 = echarts.init(domMain3, curTheme);
	    myChart4 = echarts.init(domMain4, curTheme);
	    window.onresize = myChart1.resize;
	
	//设备在线统计
	statisticsInfo.deviceTotalNum = statisticsInfo.deviceTotalNum || 0;
	statisticsInfo.deviceOnlineNum = statisticsInfo.deviceOnlineNum || 0;
	statisticsInfo.deviceOfflineNum = statisticsInfo.deviceTotalNum - statisticsInfo.deviceOnlineNum;
	var percN= new Number((statisticsInfo.deviceOnlineNum/statisticsInfo.deviceTotalNum)*100).toFixed(2)
	statisticsInfo.perc = !isNaN(percN) ? percN :'-';
	$("#num1").text(statisticsInfo.deviceTotalNum);
	$("#num2").text(statisticsInfo.deviceOnlineNum);
	$("#num3").text(statisticsInfo.deviceOfflineNum);
	var pie1 =  {
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}%"
	    },
	    toolbox: {
	        show : false
	    },
	    series : [
	        {
	            name:'设备在线统计',
	            type:'gauge',
	            startAngle: 180,
	            endAngle: 0,
	            center : ['50%', '75%'],    // 默认全局居中
	            radius : 100,
	            axisLine: {            // 坐标轴线
	                lineStyle: {       // 属性lineStyle控制线条样式
						color: [[0.2, 'red'],[0.8, 'blue'],[1, 'green']], 
	                    width: 50
	                }
	            },
	            axisTick: {            // 坐标轴小标记
	                splitNumber: 10,   // 每份split细分多少段
	                length :12,        // 属性length控制线长
	            },
	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
	                formatter: function(v){
	                    switch (v+''){
	                        case '10': return '低';
	                        case '50': return '中';
	                        case '90': return '高';
	                        default: return '';
	                    }
	                },
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: '#fff',
	                    fontSize: 15,
	                    fontWeight: 'bolder'
	                }
	            },
	            pointer: {
	                width:6,
	                length: '60%',
	                color: 'rgba(255, 255, 128, 0.5)'
	            },
	            title : {
	                show : true,
	                offsetCenter: [0, '-130%'],       // x, y，单位px
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: '#fff',
	                    fontSize: 18,
	                    fontWeight: 'bold',
						fontFamily: "Microsoft YaHei",
	                }
	            },
	            detail : {
	                show : true,
	                backgroundColor: 'rgba(0,0,0,0)',
	                borderWidth: 0,
	                borderColor: '#ccc',
	                width: 100,
	                height: 40,
	                offsetCenter: [0, -40],       // x, y，单位px
	                formatter:'{value}%\n在线',
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    fontSize : 15,
						color: '#fff',
						fontWeight: 'bold'
	                }
	            },
	            data:[{value: statisticsInfo.perc, name: '设备在线统计'}]
	        }
	    ]
	};

	//刷新在线统计
	clearInterval(timeTicket1);
	var timeTicket1 = setInterval(function (){
	    pie1.series[0].data[0].value = (Math.random()*100).toFixed(2) - 0;
	    myChart1.setOption(pie1,true);
	},5000000)

	var pie2 = {
	    title : {
	        text: '故障分析统计', 
			textStyle:{
				color: '#fff',
				fontWeight: 'bold'
			},
	        subtext: '',
	        x:'center'
	    },
	    tooltip : {
			show:false,
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient : 'horizontal',
			textStyle:{
				color: '#fff',
				fontWeight: 'bold'
			},
	        x : 'left',
	        y : '30',
	        data:['断电','断网','设备故障','其它']
	    },
	    calculable : true,
	    series : [
	        {
	            name:'面积模式',
	            type:'pie',
	            radius : [14, 50],
	            center : ['50%', 140],
	            roseType : 'area',
	            x: '50%',               // for funnel
	            max: 40,                // for funnel
	            sort : 'ascending',     // for funnel
	            data:[
	                {value:10, name:'断电'},
	                {value:5, name:'断网'},
	                {value:15, name:'设备故障'},
	                {value:3, name:'其它'},
	            ],
				itemStyle: {
	                emphasis: {  
	                    shadowBlur: 3,  
	                    shadowOffsetX: 0,  
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'  
	                },
					normal: {
					  label: {
						show: true,
						formatter: '{d}%\n{c}'
					  },
					  labelLine: {
						show: true,
						length:0.001
					  }
					}
	            } 
	        }
	    ]
	};

	//终端在线分析统计数据处理
	var deviceList = statisticsInfo.deviceList;
	var onNums = [0,0,0,0,0];
	var offNums = [0,0,0,0,0];
	for(var d=0; d<deviceList.length; d++)
	{
		var type = deviceList[d].deviceType-0;
		type = type < 4 ? type:4;//四种统计
		onNums[deviceList[d].deviceType-0] += deviceList[d].deviceOnlineNum;
		offNums[deviceList[d].deviceType-0] += deviceList[d].deviceTotalNum - deviceList[d].deviceOnlineNum;
	}
	var pie3 = {
	    title : {
	        text: '终端在线分析统计',
			textStyle:{
				color: '#fff',
				fontWeight: 'bold'
			},
	        subtext: '',
	        x:'center'
	    },
	    tooltip : {
	        show: false,
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: 'horizontal',
			textStyle:{
				color: '#fff',
				fontWeight: 'bold'
			},
	        x: 'center',
			y: 30,
	        data: ['在线','离线']//['电子警察','治安卡口','治安监控','人脸设备','其它']
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : ['电子\n警察','治安\n卡口','治安\n监控','人脸\n设备','其它'],
			　　splitLine:{
			　　　　show:false
			　　},
				axisLabel: {
					interval:0, 
					textStyle:{
						color: '#fff'
					},
				}
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
			　　splitLine:{
			　　　　show:false
			　　},
				axisLabel: {
					textStyle:{
						color: '#fff',
						fontWeight: 'bold'
					},
				}
	        }
	    ],
		grid:{
			x:30,
			y:55,
			x2:15,
			y2:35,
			left: 12,
			right:12
		},
	    series : [
			{
	            name:'总数',
	            type:'bar',
			    itemStyle: {
	                normal: {
	                    label: {
	                        show: true,
	                        position: 'top',
	                        formatter: '{c}'
	                    }
	                }
	            },
	            data:onNums
	        },
	        {
	            name:'在线',
	            type:'bar',
			    itemStyle: {
	                normal: {
	                    label: {
	                        show: true,
	                        position: 'top',
	                        formatter: '{c}'
	                    }
	                }
	            },
	            data:offNums
	        }
	    ]
	};
	
	//各单位在线数据处理
	var coutryList = statisticsInfo.coutryList;
	var cSortSrc = [];//原始排名
	var countryNames = [];
	var countryTotalNums = [];
	for(var i=0; i<coutryList.length; i++){
		cSortSrc.push(coutryList[i].countryTotalNum);
		coutryList[i].countryName = coutryList[i].countryName || "";
		countryNames.push( i%2!=0 ?coutryList[i].countryName:'\n'+coutryList[i].countryName);
		countryTotalNums.push(coutryList[i].countryTotalNum);
	}
	cSortSrc = cSortSrc.sort().reverse();
	var cSortData = [];//排名
	for(var i=0; i<coutryList.length; i++){
		cSortData.push(cSortSrc.indexOf(coutryList[i].countryTotalNum)+1);
	}
	
	var pie4 = {
		title:{
			text: '各单位在线数量排名',
			x:'center',
			textStyle:{
				color: '#fff',
				fontWeight: 'bold'
			},
		},
	    legend: {
	        orient: 'horizontal',
			textStyle:{
				color: '#fff',
				fontWeight: 'bold'
			},
	        x: 'right',
			y: 10,
	        data: ['数量','排名']
	    },
		grid:{
			x:30,
			y:55,
			x2:25,
			y2:55,
			left: 12,
			right:12
		},
	    xAxis: {
	        type: 'category',
	        axisLabel: {  
			   interval:0,  
			   //rotate:-45,
				textStyle:{
					color: '#fff'
				},
			} ,
		　　splitLine:{
		　　　　show:false
		　　},
	        data: countryNames
	    },
	    yAxis: [{
				type: 'value',
				name: '数量',
			　　splitLine:{
			　　　　show:false
			　　},
				axisLabel: {
					textStyle:{
						color: '#fff',
					},
				}
			},{
				type: 'value',
				name: '排名',
			　　splitLine:{
			　　　　show:false
			　　},
				axisLabel: {
					textStyle:{
						color: '#fff',
					},
				}
			},
		],
	    series: [{
	            name:'数量',
				data: countryTotalNums,
				itemStyle: {
	                normal: {
	                    label: {
	                        show: true,
	                        position: 'top',
	                        formatter: '{c}'
	                    }
	                }
	            },
				type: 'bar'
			},{
	            name:'排名',
				yAxisIndex:1,
				data: cSortData,
				itemStyle: {
	                normal: {
	                    label: {
	                        show: true,
	                        position: 'top',
	                        formatter: '{c}'
	                    }
	                }
	            },
				type: 'bar'
			},
		]
	};
	                                        
	                                       
	    myChart1.setOption(pie1, true);
	    myChart2.setOption(pie2, true);
	    myChart3.setOption(pie3, true);
	    myChart4.setOption(pie4, true);
	}

	function needMap() {
	    var href = location.href;
	    return href.indexOf('map') != -1
	           || href.indexOf('mix3') != -1
	           || href.indexOf('mix5') != -1
	           || href.indexOf('dataRange') != -1;

	}

	var echarts;
	var developMode = false;

	if (developMode) {
	    window.esl = null;
	    window.define = null;
	    window.require = null;
	    (function () {
	        var script = document.createElement('script');
	        script.async = true;

	        var pathname = location.pathname;

	        var pathSegs = pathname.slice(pathname.indexOf('doc')).split('/');
	        var pathLevelArr = new Array(pathSegs.length - 1);
	        script.src = pathLevelArr.join('../') + 'asset/js/esl/esl.js';
	        if (script.readyState) {
	            script.onreadystatechange = fireLoad;
	        }
	        else {
	            script.onload = fireLoad;
	        }
	        (document.getElementsByTagName('head')[0] || document.body).appendChild(script);
	        
	        function fireLoad() {
	            script.onload = script.onreadystatechange = null;
	            setTimeout(loadedListener,100);
	        }
	        function loadedListener() {
	            // for develop
	            require.config({
	                packages: [
	                    {
	                        name: 'echarts',
	                        location: '../../src',
	                        main: 'echarts'
	                    },
	                    {
	                        name: 'zrender',
	                        // location: 'http://ecomfe.github.io/zrender/src',
	                        location: '../../../zrender/src',
	                        main: 'zrender'
	                    }
	                ]
	            });
	            launchExample();
	        }
	    })();
	}
	else {
	    // for echarts online home page
	    require.config({
	        paths: {
	            echarts: '/echarts-2.2.7/doc/example/www/js'
	        }
	    });
	    launchExample();
	}

	var isExampleLaunched;
	function launchExample() {
	    if (isExampleLaunched) {
	        return;
	    }
	    // 按需加载
	    isExampleLaunched = 1;
	    require(
	        [
	            'echarts',
	            'echarts/theme/blue' ,
	            'echarts/chart/line',
	            'echarts/chart/bar',
	            'echarts/chart/funnel',
	            'echarts/chart/gauge',
	            'echarts/chart/pie',
	            needMap() ? 'echarts/chart/map' : 'echarts'
	        ],
	        requireCallback
	    );
	}
});


