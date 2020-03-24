/**
 * 新增角色
 */
//选中的复选框
var nodeIds = [];
$(function() {

    layui.use(['form' ,'tree','layer'], function(){
        var form = layui.form;
        var layer=layui.layer;

        //监听提交
        getTreeData();

        form.on('submit(roleSubmit)', function(data){
            //校验 TODO
            var array = new Array();
            var permCheckd=$(".layui-form-checked");
            //获取选中的权限id
            for(var i=0;i<permCheckd.length;i++){
                array.push($(permCheckd.get(i)).prev().val());
            }
            //校验是否授权
            var permIds = array.join(",");
            // console.log("permIds"+permIds)
            if(permIds==null || permIds==''){
                layer.alert("请给该角色添加权限菜单！")
                return false;
            }
            $("#permIds").val(permIds);

            $.ajax({
                type: "POST",
                data: $("#roleForm").serialize(),
                url: "/auth/addRole",
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
function getTreeData() {
    $.ajax({
        type: "get",
        url: "/auth/findPerms",
        success: function (data) {
            if (data !=null) {
                initTree(data);
            } else {
                layer.alert(data);
            }
        },
        error: function () {
            layer.alert("获取数据错误，请您稍后再试");
        }
    });
}

function initTree(data){
    //显示角色数据
    $("#perm").empty();
    $("#op_perm").empty();
    $.each(data, function (index, item) {
        var roleInput = $("<input type='checkbox' name='permId' value=" + item.id + " title=" + item.name + " lay-skin='primary'/>");
        var div = $("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
            "<span>" + item.name + "</span><i class='layui-icon'>&#xe626;</i>" +
            "</div>");
        if (item.istype.trim() == "基本权限") {
            $("#perm").append(roleInput).append(div);
        }
        ;
        if (item.istype.trim() == "操作权限") {
            $("#op_perm").append(roleInput).append(div);
        }
        ;
    });
    //重新渲染下form表单 否则复选框无效
    layui.form.render('checkbox');
}
/**
 * 获取所有子节点的id数组
 * @param obj
 * @returns {Array}
 */
function getChildNode( obj ){
    if(obj!=null){
        if( obj.children.length > 0 ){
            $.each( obj.children, function(k, v){
                console.log( v.id );
                nodeIds.push( v.id );
                getChildNode( v );
            });
        }
    }
    return nodeIds;
}


/**
 * list转化为tree结构的json数据
 */
function listToTreeJson(data){
    //data不能为null，且是数组
    if(data!=null && (data instanceof Array)){
        //递归转化
        var getJsonTree=function(data,parentId){
            var itemArr=[];
            for(var i=0;i < data.length;i++){
                var node=data[i];
                if(node.pId==parentId && parentId!=null){
                    var newNode={name:node.name,spread:true,id:node.id,pid:node.pId,children:getJsonTree(data,node.id)};
                    itemArr.push(newNode);
                }
            }
            return itemArr;
        }
        // return JSON.stringify(getJsonTree(data,''));
        return getJsonTree(data,0);
    }
    // console.log(JSON.stringify(getJsonTree(data,'')));
}
