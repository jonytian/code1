/**
 * 角色更新信息
 */
$(function() {
    /**获取角色授权关系信息并赋值*/
    if(flag=="updateRole"){
        $.ajax({
            type: "get",
            url: "/auth/findPerms",
            success: function (data) {
                if (data !=null) {
                    //显示角色数据
                    $("#perm1").empty();
                    $("#op_perm1").empty();
                    $.each(data, function (index, item) {
                        var roleInput = $("<input type='checkbox' name='permId1' value=" + item.id + " title=" + item.name + " lay-skin='primary'/>");
                        var div = $("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                            "<span>" + item.name + "</span><i class='layui-icon'>&#xe626;</i>" +
                            "</div>");
                        if (item.istype.trim() == "基本权限") {
                            $("#perm1").append(roleInput).append(div);
                        }
                        ;
                        if (item.istype.trim() == "操作权限") {
                            $("#op_perm1").append(roleInput).append(div);
                        }
                        ;
                    });
                    //重新渲染下form表单 否则复选框无效
                    layui.form.render('checkbox');
                } else {
                    layer.alert(data);
                }
            },
            error: function () {
                layer.alert("获取数据错误，请您稍后再试");
            }
        });
    }


    layui.use(['form' ,'layer'], function(){
        var form = layui.form;
        var layer=layui.layer;

        //监听提交
        form.on('submit(updateRoleSumbit)', function(data){
            //获取选中的权限
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var nodes = treeObj.getCheckedNodes(true);
            //选中的复选框
            var nodeIds =new Array();
            for (var i = 0; i < nodes.length; i++) {
                nodeIds.push(nodes[i].id);
            }
            //校验是否授权
            var permList = nodeIds.join(",");
            // console.log("permList:"+permList)
            if(permList==null || permList==''){
                layer.alert("请给该角色添加权限菜单！")
                return false;
            }
            $("#rolePermIds").val(permList);
            $.ajax({
                type: "POST",
                data: $("#updateRoleForm").serialize(),
                url: "/auth/setRole",
                success: function (data) {
                    if (data == "ok") {
                        layer.alert("操作成功",function(){
                            layer.closeAll();
                            load();
                        });
                    } else {
                        layer.alert(data);
                    }
                },
                error: function (data) {
                    layer.alert("操作请求错误，请您稍后再试");
                }
            });
            return false;
        });
        form.render();
    });
});


