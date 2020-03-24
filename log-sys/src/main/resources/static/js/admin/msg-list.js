layui.extend({
	admin: '{/}../../js/admin'
});
var pageCurr;

	layui.use(['table', 'jquery', 'form', 'util', 'admin'], function () {
		var table = layui.table,
			$ = layui.jquery,
			form = layui.form,
			admin = layui.admin
			, util = layui.util;

		tableIns = table.render({
			id: 'idTest',
			elem: '#logList'
			, url: '/log/getMsgs'
			, method: 'post' //默认：get请求
			, cellMinWidth: 80
			, height: 'full-250'
			, limits: [20, 50, 100, 1000]
			, limit: 20
			, event: true
			, page: true,
			request: {
				pageName: 'page' //页码的参数名称，默认：page
				, limitName: 'limit' //每页数据量的参数名，默认：limit
			}, response: {
				statusName: 'code' //数据状态的字段名称，默认：code
				, statusCode: 200 //成功的状态码，默认：0
				, countName: 'totals' //数据总数的字段名称，默认：count
				, dataName: 'list' //数据列表的字段名称，默认：data
			},
			cols: [[

				{type: 'checkbox'},
				{field: 'code', width: 120, title: '消息代码', align: 'center', sort: true},
				{field: 'imei', width: 160, title: 'IMEI', align: 'center', sort: true},
				{field: 'channelId', width: 100, title: '通道ID', align: 'center', sort: true},
				{field: 'sender', width: 160, title: '发送者', align: 'center', sort: true},
				{field: 'receiver', width: 160, title: '接收者', align: 'center', sort: true},
				{field: 'content', width: 1000, title: '消息内容', align: 'center', sort: true},
				{
					field: 'sendTime', title: '发送时间', width: 200, align: 'center', sort: true, templet: function (d) {
						if (d.sendTime == null) {
							return "";
						}
						return util.toDateString(d.sendTime, "yyyy-MM-dd HH:mm:ss");
					}
				}
			]],
			done: function (res, curr, count) {
				//如果是异步请求数据方式，res即为你接口返回的信息。
				//如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				//console.log(res);
				//得到当前页码
				//console.log(curr);
				//得到数据总量
				//console.log(count);
				pageCurr = curr;
				// AutoTableHeight();

			}

		});
		/*
         *数据表格中form表单元素是动态插入,所以需要更新渲染下
         * http://www.layui.com/doc/modules/form.html#render
         * */
		$(function () {
			form.render();
		});

		var $ = layui.$, active = {

		};


		$('.demoTable .layui-btn').on('click', function () {
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