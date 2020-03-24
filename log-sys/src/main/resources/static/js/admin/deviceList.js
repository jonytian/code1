layui.extend({
	admin: '{/}../../js/admin'
});
var pageCurr;
layui.use(['table', 'jquery','form','util', 'admin'], function() {
	var table = layui.table,
		$ = layui.jquery,
		form = layui.form,
		admin = layui.admin
	    ,util = layui.util;

	tableIns = 	table.render({
		id: 'idTest',
		elem: '#deviceList'
		,url:'/device/getDevices'
		,method: 'post' //默认：get请求
		,cellMinWidth: 80
		, height: 'full-250'
		,limits: [20,50,100,1000]
		,limit: 20
		,event: true
		,page: true,
		request: {
			pageName: 'page' //页码的参数名称，默认：page
			,limitName: 'limit' //每页数据量的参数名，默认：limit
		},response:{
			statusName: 'code' //数据状态的字段名称，默认：code
			,statusCode: 200 //成功的状态码，默认：0
			,countName: 'totals' //数据总数的字段名称，默认：count
			,dataName: 'list' //数据列表的字段名称，默认：data
		},
		cols : [ [  //日期，箱号，整机SN，IMEI，ICCID，BT号段，wifi号段
			{type:'checkbox'},
			{field : 'imei',width : 120,title : 'IMEI', align: 'center', sort : true},
			{field : 'projectName', width : 210, title : '项目名称', align: 'center', sort : true},
			{field:'connectTime', title: '连接时间',width: 200,align: 'center' , sort : true,templet:function (d) {
					if (d.connectTime == null) {return "";}
					return util.toDateString(d.connectTime, "yyyy-MM-dd HH:mm:ss");
				}},
			{field:'disconnectTime', title: '断开时间',width: 200,align: 'center' , sort : true,templet:function (d) {
					if (d.disconnectTime == null) {return "";}
					return util.toDateString(d.disconnectTime, "yyyy-MM-dd HH:mm:ss");
				}},
			{field : 'kicked', width : 200, title : '是否踢出', align: 'center', sort : true,templet:function (d) {
					return d.kicked == 1 ? "是":"否";
				}},
			{field : 'online', width : 200, title : '是否在线', align: 'center', sort : true,templet:function (d) {
					return d.online == 1 ? "是":"否";
				}},
			{field : 'restart', width : 200, title : '是否重启', align: 'center', sort : true,templet:function (d) {
					return d.restart == 1 ? "是":"否";
				}},
			{field : 'mtklogStatus', width : 200, title : 'MTKLOG状态', align: 'center', sort : true,templet:function (d) {
					return d.mtklogStatus == 1 ? "运行":"关闭";
				}},
			{field:'createTime', title: '创建时间',width: 200,align: 'center' , sort : true,templet:function (d) {
					if (d.createTime == null) {return "";}
					return util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
				}}
		] ],
        done: function(res, curr, count){
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            //console.log(res);
            //得到当前页码
            //console.log(curr);
            //得到数据总量
            //console.log(count);
            pageCurr=curr;
            // AutoTableHeight();

        }

	});
	/*
	 *数据表格中form表单元素是动态插入,所以需要更新渲染下
	 * http://www.layui.com/doc/modules/form.html#render
	 * */
	$(function(){
		form.render();
	});

	var $ = layui.$, active = {
		restart: function(){ //获取选中数据
			var checkStatus = table.checkStatus('idTest')
				,data = checkStatus.data;
			if(data.length == 0){
				layer.msg('请选择要操作的数据行');
				return;
			}
			console.log(JSON.stringify(data));
			layer.confirm('确定要操作吗?', function(index) {
				$.ajax({
					url:'/message/restart',
					type:'POST',
					data:JSON.stringify(data),
					dataType:"json",
					contentType: "application/json",
					success:function (res) {
						if(res && res.code == 0){
							layer.msg(res.data);
						}else{
							layer.msg(res.data);
						}
					},
					error:function (res) {
						layer.msg("请求错误");
					}
				});
			});

		}
		,grab: function(){ //获取选中数目
			var checkStatus = table.checkStatus('idTest')
				,data = checkStatus.data;
			if(data.length == 0){
				layer.msg('请选择要操作的数据行');
				return;
			}
			console.log(JSON.stringify(data));
			layer.confirm('确定要操作吗?', function(index) {
				$.ajax({
					url:'/message/grab',
					type:'POST',
					data:JSON.stringify(data),
					dataType:"json",
					contentType: "application/json",
					success:function (res) {
						if(res && res.code == 0){
							layer.msg(res.data);
						}else{
							layer.msg(res.data);
						}
					},
					error:function (res) {
						layer.msg("请求错误");
					}
				});
			});
		}

	};

	$('.demoTable .layui-btn').on('click', function(){
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});


});

/**
 * 监听搜索
 * */
layui.use(['form', 'laydate'], function () {
	var form = layui.form, layer = layui.layer
		, laydate = layui.laydate;
	//日期
	laydate.render({
		elem: '#insertTimeStart'
	});
	laydate.render({
		elem: '#insertTimeEnd'
	});
	//TODO 数据校验
	//监听搜索框
	form.on('submit(search)', function (data) {
		//重新加载table
		load(data);
		return false;
	});
});


/**
 * 重新刷新页面
 * */
function load(obj){
	//重新加载table
	tableIns.reload({
		where: obj.field
		, page: {
			curr: pageCurr //从当前页码开始
		}
	});
}