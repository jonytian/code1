(function($){
    $.fn.ajaxImageUpload = function(options){

        var defaults = {

            data: null,
            url: '',
            zoom: false,
            allowType: ["gif", "jpeg", "jpg", "bmp",'png'],
            maxNum: 10,
            maxSize: 2, //设置允许上传文件的最大尺寸，单位M
            success:$.noop, //上传成功时的回调函数
            error:$.noop //上传失败时的回调函数

        };

        var thisObj = $(this);
        var config  = $.extend(defaults, options);
        var imageBox  = $(".image-box");
        var inputName = thisObj.attr('name');

        // 设置是否在上传中全局变量
        isUploading  = false;

        thisObj.each(function(i){
            thisObj.change(function(){
                handleFileSelect();
            });
        });

        var handleFileSelect = function(){

            if (typeof FileReader == "undefined") {
                return false;
            }

            // 获取最新的section数量
            var imageNum  = $('.image-section').length;

            var postUrl   = config.url;
            var maxNum    = config.maxNum;
            var maxSize   = config.maxSize;
            var allowType = config.allowType;

            if(!postUrl){
                showMessage('请设置要上传的服务端地址');
                return false;
            }

            if(imageNum + 1 > maxNum ){
                showMessage("上传文件数目不可以超过"+maxNum+"个");
                return;
            }

            var files    = thisObj[0].files;
            var fileObj  = files[0];

            if(!fileObj){
                return false;
            }

            var fileName = fileObj.name;
            var fileSize = (fileObj.size)/(1024*1024);

            if (!isAllowFile(fileName, allowType)) {

                showMessage("文件类型必须是" + allowType.join("，") + "中的一种");
                return false;

            }

            if(fileSize > maxSize){

                showMessage('上传文件不能超过' + maxSize + 'M，当前上传文件的大小为'+fileSize.toFixed(2) + 'M');
                return false;

            }

            if(isUploading == true){

                showMessage('文件正在上传中，请稍候再试！');
                return false;

            }

            // 将上传状态设为正在上传中
            isUploading = true;

            // 执行前置函数
            var callback = config.before;

            if(callback && callback() === false){
                return false;
            }

            ajaxUpload();

        };

        var ajaxUpload = function () {
            var formData = new FormData();
            var fileData = thisObj[0].files;
            if(fileData){
                // 目前仅支持单图上传
                formData.append(inputName, fileData[0]);
            }

            var postData = config.data;
            if (postData) {
                for (var i in postData) {
                    formData.append(i, postData[i]);
                }
            }

            // ajax提交表单对象
            $.ajax({
                url: config.url,
                type: "post",
                data: formData,
                processData: false,
                contentType: false,
                dataType: 'json',
                success:function(json){
                    // 将上传状态设为非上传中
                    isUploading = false;
                    // 执行成功回调函数
                    var callback = config.success;
                    callback(json);
                },
                error:function(e){
                    // 执行失败回调函数
                    var callback = config.error;
                    callback(e);
                }
            });

        };

        //获取上传文件的后缀名
        var getFileExt = function(fileName){
            if (!fileName) {
                return '';
            }

            var _index = fileName.lastIndexOf('.');
            if (_index < 1) {
                return '';
            }

            return fileName.substr(_index+1);
        };

        //是否是允许上传文件格式
        var isAllowFile = function(fileName, allowType){

            var fileExt = getFileExt(fileName).toLowerCase();
            if (!allowType) {
                allowType = ['jpg', 'jpeg', 'png', 'gif', 'bmp'];
            }

            if ($.inArray(fileExt, allowType) != -1) {
                return true;
            }
            return false;
        };

    };

})(jQuery);