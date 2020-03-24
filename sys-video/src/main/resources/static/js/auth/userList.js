/**
 * 用户管理
 */
var pageCurr;
$(function() {
    layui.use(['table','util','form'], function(){
        var table = layui.table
            ,form = layui.form
             ,util = layui.util;
        tableIns=table.render({
            id: 'idTest',
            elem: '#uesrList'
            ,url:'/user/getUsers'
            ,method: 'post' //默认：get请求
            ,cellMinWidth: 80
            ,page: true,
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            },response:{
                statusName: 'code' //数据状态的字段名称，默认：code
                ,statusCode: 200 //成功的状态码，默认：0
                ,countName: 'totals' //数据总数的字段名称，默认：count
                ,dataName: 'list' //数据列表的字段名称，默认：data
            }
            ,cols: [[
                {type:'checkbox'}
                ,{field:'id', title:'ID',width:80, unresize: true, sort: true}
                ,{field:'username', title:'用户名'}
                ,{field:'realname', title:'真实姓名'}
                ,{field:'rolename', title: '角色名称', minWidth:80}
                ,{field:'mobile', title:'手机号码'}
                ,{field:'department', title:'部门'}
                ,{field:'email', title: '邮箱'}
                ,{field:'isLock', title: '账号状态', minWidth:80,templet:function (d) {
                        return d.isLock==0?"禁用":"启用";
                    }}
                ,{field:'createTime', title: '创建时间',width: 200,templet:function (d) {
                        return   DateUtils.formatDate(d.createTime) ;
                    }}
                ,{field:'updateTime', title: '更新时间',width: 200,templet:function (d) {
                        return DateUtils.formatDate(d.updateTime);
                        }}
                ,{fixed:'right', title:'操作',width:140,align:'center', toolbar:'#optBar'}
            ]]
            ,  done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });
        //监听表格复选框选择
        table.on('checkbox(userTable)', function(obj){
            console.log(obj.checked); //当前是否选中状态
        });

        //监听工具条
        table.on('tool(userTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                delUser(data,data.id,data.username);
            } else if(obj.event === 'edit'){
                //编辑
                getUserAndRoles(data,data.id);
            }
        });
        //监听提交
        form.on('submit(userSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });

        var $ = layui.$, active = {
            /**启用*/
            unlockUser: function(){
                var checkStatus = table.checkStatus('idTest')
                    ,data = checkStatus.data;
                if(data.length<1){
                    layer.alert("请选中一条记录！");
                    return false;
                }
                var dataStr = JSON.stringify(data);
                $.ajax({
                    type: "POST",
                    data: {data : dataStr},
                    url: "/user/unlockUser",
                    success: function (data) {
                        if(isLogin(data)){
                            if (data == "ok") {
                                layer.alert("操作成功",function(){
                                    if($("#id").val()==currentUser){
                                        //如果是自己，直接重新登录
                                        parent.location.reload();
                                    }else{
                                        layer.closeAll();
                                        //加载页面
                                        load(data);
                                    }
                                });
                            } else {
                                layer.alert(data,function(){
                                    layer.closeAll();
                                    //加载load方法
                                    load(data);//自定义
                                });
                            }
                        }
                    },
                    error: function () {
                        layer.alert("操作请求错误，请您稍后再试",function(){
                            layer.closeAll();
                            //加载load方法
                            load(obj);//自定义
                        });
                    }
                });
                // layer.alert(JSON.stringify(data));
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            },
            /**禁用*/
            lockUser: function(){ //获取选中数据
                var checkStatus = table.checkStatus('idTest')
                    ,data = checkStatus.data;
                if(data.length<1){
                    layer.alert("请选中一条记录！");
                    return false;
                }
                var dataStr = JSON.stringify(data);
                $.ajax({
                    type: "POST",
                    data: {data : dataStr},
                    url: "/user/lockUser",
                    success: function (data) {
                        if(isLogin(data)){
                            if (data == "ok") {
                                layer.alert("操作成功",function(){
                                    if($("#id").val()==currentUser){
                                        //如果是自己，直接重新登录
                                        parent.location.reload();
                                    }else{
                                        layer.closeAll();
                                        load(data);
                                    }
                                });
                            } else {
                                layer.alert(data,function(){
                                    layer.closeAll();
                                    //加载load方法
                                    load(data);//自定义
                                });
                            }
                        }
                    },
                    error: function () {
                        layer.alert("操作请求错误，请您稍后再试",function(){
                            layer.closeAll();
                            //加载load方法
                            load(obj);//自定义
                        });
                    }
                });
                // layer.alert(JSON.stringify(data));
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            },
            /**批量删除*/
            delUser: function(){ //获取选中数据
                var checkStatus = table.checkStatus('idTest')
                    ,data = checkStatus.data;
                if(data.length<1){
                    layer.alert("请选中一条记录！");
                    return false;
                }
                var dataStr = JSON.stringify(data);
                $.ajax({
                    type: "POST",
                    data: {data : dataStr},
                    url: "/user/deleteUser",
                    success: function (data) {
                        if(isLogin(data)){
                            if (data == "ok") {
                                layer.alert("操作成功",function(){
                                    if($("#id").val()==currentUser){
                                        //如果是自己，直接重新登录
                                        parent.location.reload();
                                    }else{
                                        layer.closeAll();
                                        load(data);
                                    }
                                });
                            } else {
                                layer.alert(data,function(){
                                    layer.closeAll();
                                    //加载load方法
                                    load(data);//自定义
                                });
                            }
                        }
                    },
                    error: function () {
                        layer.alert("操作请求错误，请您稍后再试",function(){
                            layer.closeAll();
                            //加载load方法
                            load(obj);//自定义
                        });
                    }
                });
                // layer.alert(JSON.stringify(data));
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            },
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });



    });
    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#insertTimeStart'
        });
        laydate.render({
            elem: '#insertTimeEnd'
        });
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });
});



//提交表单
function formSubmit(obj){
    var currentUser=$("#currentUser").html();
    if(checkRole()){
        if($("#id").val()==currentUser){
            layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
                btn: ['返回','确认'] //按钮
            },function(){
                layer.closeAll();
            },function() {
                layer.closeAll();//关闭所有弹框
                submitAjax(obj,currentUser);
            });
        }else{
            submitAjax(obj,currentUser);
        }
    }
}
function submitAjax(obj,currentUser){
    $.ajax({
        type: "POST",
        data: $("#userForm").serialize(),
        url: "/user/setUser",
        success: function (data) {
            if(isLogin(data)){
                if (data == "ok") {
                    layer.alert("操作成功",function(){
                        if($("#id").val()==currentUser){
                            //如果是自己，直接重新登录
                            parent.location.reload();
                        }else{
                            layer.closeAll();
                            cleanUser();
                            //$("#id").val("");
                            //加载页面
                            load(obj);
                        }
                    });
                } else {
                    layer.alert(data,function(){
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                }
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}

function checkRole(){
    //选中的角色
    var array = new Array();
    var roleCheckd=$(".layui-form-checked");
    //获取选中的权限id
    for(var i=0;i<roleCheckd.length;i++){
        array.push($(roleCheckd.get(i)).prev().val());
    }
    //校验是否授权
    var roleIds = array.join(",");
    if(roleIds==null || roleIds==''){
        layer.alert("请您给该用户添加对应的角色！")
        return false;
    }
    $("#roleIds").val(roleIds);
    return true;
}
/**
 *新增用户
 * */
function addUser(){
    $.get("/auth/getRoles",function(data){
        if(isLogin(data)){
            if(data!=null){
                //显示角色数据
                $("#roleDiv").empty();
                $.each(data, function (index, item) {
                    // <input type="checkbox" name="roleId" title="发呆" lay-skin="primary"/>
                    var roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.rolename+" lay-skin='primary'/>");
                    //未选中
                    /*<div class="layui-unselect layui-form-checkbox" lay-skin="primary">
                        <span>发呆</span><i class="layui-icon">&#xe626;</i>
                        </div>*/
                    //选中
                    // <div class="layui-unselect layui-form-checkbox layui-form-checked" lay-skin="primary">
                    // <span>写作</span><i class="layui-icon">&#xe627;</i></div>
                    var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                        "<span>"+item.rolename+"</span><i class='layui-icon'>&#xe626;</i>" +
                        "</div>");
                    $("#roleDiv").append(roleInput).append(div);
                })
                openUser(null,"新增用户");
                //重新渲染下form表单 否则复选框无效
                layui.form.render('checkbox');
            }else{
                //弹出错误提示
                layer.alert("获取角色数据有误，请您稍后再试",function () {
                    layer.closeAll();
                });
            }
        }
    });
}
function openUser(id,title){
    if(id==null || id==""){
        $("#id").val("");
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setUser'),
        end:function(){
            cleanUser();
        }
    });
}


/**
 * 编辑用户信息
 * */
function getUserAndRoles(obj,id) {
    //如果已经，提醒不可编辑和删除
    if(obj.job){
    }else{
        //回显数据
        $.get("/user/getUserAndRoles",{"id":id},function(data){
            if(isLogin(data)){
                if(data.msg=="ok" && data.user!=null){
                    var existRole='';
                    if(data.user.userRoles !=null ){
                        $.each(data.user.userRoles, function (index, item) {
                            existRole+=item.roleId+',';
                        });
                    }
                    $("#id").val(data.user.id==null?'':data.user.id);
                    $("#version").val(data.user.version==null?'':data.user.version);
                    $("#username").val(data.user.username==null?'':data.user.username);
                    $("#mobile").val(data.user.mobile==null?'':data.user.mobile);
                    $("#email").val(data.user.email==null?'':data.user.email);
                    //显示角色数据
                    $("#roleDiv").empty();
                    $.each(data.roles, function (index, item) {
                        var roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.rolename+" lay-skin='primary'/>");
                        var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                            "<span>"+item.rolename+"</span><i class='layui-icon'>&#xe626;</i>" +
                            "</div>");
                        if(existRole!='' && existRole.indexOf(item.id)>=0){
                             roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.rolename+" lay-skin='primary' checked='checked'/>");
                             div=$("<div class='layui-unselect layui-form-checkbox  layui-form-checked' lay-skin='primary'>" +
                                "<span>"+item.rolename+"</span><i class='layui-icon'>&#xe627;</i>" +
                                "</div>");
                        }
                        $("#roleDiv").append(roleInput).append(div);
                    });
                    openUser(id,"设置用户");
                    //重新渲染下form表单中的复选框 否则复选框选中效果无效
                    // layui.form.render();
                    layui.form.render('checkbox');
                }else{
                    //弹出错误提示
                    layer.alert(data.msg,function () {
                        layer.closeAll();
                    });
                }
            }
        });
    }
}


/**
 * 单个删除
 * */
function delUser(obj,id,name) {
    var currentUser=$("#currentUser").html();
    var version=obj.version;
    if(null!=id){
        if(currentUser==id){
            layer.alert("对不起，您不能执行删除自己的操作！");
        }else{
            layer.confirm('您确定要删除'+name+'用户吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
                $.post("/user/delUser",{"id":id,"version":version},function(data){
                    if(isLogin(data)){
                        if(data=="ok"){
                            //回调弹框
                            layer.alert("删除成功！",function(){
                                layer.closeAll();
                                //加载load方法
                                load(obj);//自定义
                            });
                        }else{
                            layer.alert(data,function(){
                                layer.closeAll();
                                //加载load方法
                                load(obj);//自定义
                            });
                        }
                    }
                });
            }, function(){
                layer.closeAll();
            });
        }
    }
}


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
/**
 * 清空表单
 * */
function cleanUser(){
    //$("#id").val("");
    $("#username").val("");
    $("#mobile").val("");
    $("#email").val("");
    $("#password").val("");
}
