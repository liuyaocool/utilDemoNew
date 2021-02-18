$(window).load(function (){

});

// 自动生成UUID
function newUUID() {
    var guid = "";
    for (var i = 1; i <= 32; i++) {
        var n = Math.floor(Math.random() * 16.0).toString(16);
        guid += n;
        if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
            guid += "-";
    }
    return guid;
}

function doRequest(opts) {
	
	var _opts = opts;
	
	_opts.type = _opts.type ||  "POST";								// 默认以POST方式提交
	_opts.contentType = _opts.contentType || 'application/json;charset=UTF-8'; // 默认以JSON方式传递参数
	_opts.dataType = _opts.dataType || 'json';						// 默认以JSON方式接收数据
	_opts.async = _opts.async === false ? false : true;             // 默认异步
	_opts.isEncrypt = _opts.isEncrypt === false ? false : true; 	// 默认前后端之间数据加密
	_opts.fileRequest = _opts.fileRequest === true ? true : false;  //是否为文件上传
	
	_opts.headers = {
		'X-Requested-With' : 'XMLHttpRequest',
		'CSRFToken' : $("meta[name='_csrf']").attr("content")
	};
	var _ajax;
	if(_opts.fileRequest){
		_ajax = $.ajax({
			url : _opts.url,
			headers : _opts.headers ,
			type : _opts.type,
			async: _opts.async,						// 不要异步执行
			contentType : false ,
			data : _opts.data,
			processData:false,
			success : _success,
			error : _error
		});
	}else{
		_ajax = $.ajax({
			url : _opts.url,
			headers : _opts.headers ,
			async: _opts.async,						// 不要异步执行
			type : _opts.type,
			contentType : _opts.contentType ,
			dataType : _opts.dataType,
			data : _paramsHandle(),
			dataFilter : _dataFilter,
			isEncrypt : _opts.isEncrypt,
			success : _success,
			error : _error
		});
	}
	
	
	return _ajax;
	
	function _paramsHandle(){

		var params = _opts.data;
		
		if (!_opts.contentType || _opts.contentType === 'application/json;charset=UTF-8') {
			params = JSON.stringify(_opts.data);
		}
		
		if(!_opts.isEncrypt){
			return params;
		}
		
		return encrypt(params);	
	}
	
	function _dataFilter(d){
		if(!d) { return; }
		var decrypted;
		if(_opts.dataType == "json"){
			decrypted = decrypt(JSON.parse(d));
		}
		else{
			if(_opts.isEncrypt){			
				decrypted = decrypt(d);
			}
			else{
				decrypted = d;
			}
		}
		return decrypted ? decrypted : d;
		
	}
	
	
	function _success(res, state){

		if (!res.success && (res.errorCode == -1 || res.errorCode == -2 || res.errorCode == -3 || res.errorCode == -4)) {
			top.window.location.href = res.url;
			return;
		}

        if(_opts.fileRequest){
			var data = JSON.parse(decrypt(res));
            _opts.callback(data);
            return;
        }
		
		if (_opts.callback) {
			_opts.callback(res);
		}
	}
	
	function _error(res, state, xhr){
		if (state == 'error') {
			if (_opts.error) {
				_opts.error(res);
			}else{
				showMsg("程序终止。");
			}
			// top.window.location.reload();
		}
	}
	
}

// 获取指定form中的所有的<input>对象 name,value 封装为json对象
function form2Json(formId) {
	var d = {};
	var t = $('#' + formId).serializeArray();
	$.each(t, function() {
//		d[this.name] = this.value;
		d[this.name] = HtmlUtil.htmlEncode(this.value);
	});
	return d;
}

/**
 * 加密
 * 
 * @param content
 *            待加密内容
 * @returns
 */
function encrypt(content){  
	
	var keySpec = CryptoJS.enc.Utf8.parse(CONSTANTS.CIPHER.KEY);
	var ivSpec = CryptoJS.enc.Utf8.parse(CONSTANTS.CIPHER.IV);
	var encrypted =CryptoJS.AES.encrypt(content, keySpec,  { iv : ivSpec });
	
    return encrypted.toString();  
}  

/**
 * 解密
 * 
 * @param content
 *            待解密内容
 * @returns
 */
function decrypt(content){ 

	var keySpec = CryptoJS.enc.Utf8.parse(CONSTANTS.CIPHER.KEY);
	var ivSpec = CryptoJS.enc.Utf8.parse(CONSTANTS.CIPHER.IV);
    var decrypted = CryptoJS.AES.decrypt( content , keySpec, { iv: ivSpec});
    return CryptoJS.enc.Utf8.stringify(decrypted).toString();
    
} 

// 请求后台方法
function lyDoRequest(url, data, async, gridId){
    if(typeof(async) == "undefined" || async != true){
        async = false;
    }
    var returnData;
    if(typeof(data) != "undefined"){
        //去除前后空格
        for(i in data){
            if(null != data[i] && (typeof(data[i]) == "string")){
                data[i] = (data[i] + "").trim();
            }
        }
    }
    doRequest({
        'url':url,
        'data': data,
        'isEncrypt': true,       //传输加密
        'async': async,          //false:不允许异步
        'callback': function (res) {
            returnData = res;
            if(typeof(gridId) != "undefined"){
                $("#" + gridId).datagrid("reload");
            }
        }
    });
    return returnData;
}

//获得文件反显路径 ly
function getObjectURL(fileDomId) {
    var file = document.getElementById(fileDomId).files[0];
    var url = null;
    if (undefined != window.createObjectURL){
        url = window.createObjectURL(file);
    } else if (undefined != window.URL){
        url = window.URL.createObjectURL(file);
    } else if (undefined != window.webkitURL){
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

//获得文件扩展名 ly
function getFileType(fileDomId){

    var file = document.getElementById(fileDomId).files[0];
    var fileName = file.name;
    var resultArray = fileName.split(".");
    return resultArray[resultArray.length-1];
}

//获得项目path路径
function getPath(){

    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result; // == "/kjps"

}

// 时间格式化
Date.prototype.format =function(format)
{
    var o = {
        "M+" : this.getMonth()+1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth()+3)/3), // quarter
        "S" : this.getMilliseconds() // millisecond
    }
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
        (this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o)if(new RegExp("("+ k +")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length==1? o[k] :
                ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
};

//添加等待遮盖
function addLoadingCover() {
	var loading = "<div id='loading_ly' style='position:fixed;top: 0;left: 0;height: 100%;width: 100%;background-color: rgba(0,0,0,0.3);z-index: 9999;'>" +
					"	<div style='padding:5px 10px;border:2px solid;position:absolute; background-color: #FFFFFF; top:40%; left: 30%;width: 200px;height: 30px'>" +
					"		<img src='" + getPath() + "/resources/images/working.gif' style='width:30px;height:30px;'/>" +
					"		<span style='position:absolute; top: 6px;font-size: 20px; font-weight: bold'>&nbsp;正在处理中。。。</span>" +
					"	</div>" +
				  "</div>";
	$("body").append(loading);
};
//移除等待遮盖
function removeLoadingCover() {
	$("#loading_ly").remove();
}

//提示框
function showMsg(msg) {

	var msgStr = "<div id='msg_ly' style='display:none;z-index:999;width:230px;height:130px;background-color:#C5B479;position:absolute;top:30%;left:30%'>" +
        "<div style='padding:4px 10px;width:210px;background-color:#F0D572;top:0px;left:0px'>提示</div>" +
        "<div style='padding: 10px 20px;'>" + msg + "</div></div>";

	$("body").append(msgStr);
	$("#msg_ly").slideDown(100);
	setTimeout(function () {
        $("#msg_ly").slideUp(100);
        setTimeout(function () {
            $("#msg_ly").remove();
        }, 200)
    },2000)
}

//获得项目path路径--wkf
function getPath(){

    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;

}

//深度克隆方法 -- 递归
function deepCopy(obj){
    if(typeof obj != 'object'){
        return obj;
    }
    var newobj;
    if (isArrayFn(obj)){
        newobj = [];
        for (var i = 0, length = obj.length; i < length; i++) {
            newobj.push(deepCopy(obj[i]));
        }
    } else {
        newobj = {};
        for (var attr in obj) {
            newobj[attr] = deepCopy(obj[attr]);
        }
    }
    return newobj;
}

//判断是否是数组
function isArrayFn(value){
    if (typeof Array.isArray === "function") {
        return Array.isArray(value);
    }else{
        return Object.prototype.toString.call(value) === "[object Array]";
    }
}


//数字转换成汉字
function numberToChinese(number) {

    number *= 1;

    var numberMap = [];
    numberMap[1] = "一";
    numberMap[2] = "二";
    numberMap[3] = "三";
    numberMap[4] = "四";
    numberMap[5] = "五";
    numberMap[6] = "六";
    numberMap[7] = "七";
    numberMap[8] = "八";
    numberMap[9] = "九";
    numberMap[0] = "";

    var numDw = [];
    numDw[1] = "";
    numDw[2] = "十";
    numDw[3] = "百";
    numDw[4] = "千";
    numDw[5] = "万";
    numDw[6] = "十万";
    numDw[7] = "百万";
    numDw[8] = "千万";
    numDw[9] = "亿";

    if (number > 9 && number < 20)
        return "十" + numberMap[number%10];

    var chinese = "";
    var headChinese = "";
    if (number > 99999 && number < 200000) {
        var headNum = Math.floor(number/10000);
        headChinese = "十" + numberMap[headNum % 10] + "万";
        number = number%10000;
        if (number/1000 < 1){
            headChinese += "零";
        }
    }

    for (var i = 1; number >= 1; i++) {

        var singleNum = number % 10;
        if (0 == singleNum) {

            if (("" != chinese) && ("零" != chinese.split("")[0])) {
                chinese = "零" + chinese;
            }
        } else {
            chinese = numberMap[singleNum] + numDw[i] + chinese;
        }
        number = Math.floor(number / 10);
    }
    return headChinese + chinese;
}

// $.alerts = {
//     alert: function (title, message, callback) {
//         if (title == null) title = 'Alert';
//         $.alerts._show(title, message, null, 'alert', function (result) {
//             if (callback) callback(result);
//         });
//     },
//
//     confirm: function (title, message, callback) {
//         if (title == null) title = 'Confirm';
//         $.alerts._show(title, message, null, 'confirm', function (result) {
//             if (callback) callback(result);
//         });
//     }
// }

//小窗口展示
function showTips(jqSelector) {
    $(jqSelector).mouseout(function () {
        $("#lyTips").hide();
    }).mousemove(showLy);
    function showLy(ev) {
        if ($("#lyTips").length <= 0) {
            var tipsDom = "<div id='lyTips' style='border:1px solid black;position:absolute;max-width:325px;z-index:999;background-color:lightyellow;border-radius:5px;padding:9px;font-size:15px;display:none;'></div>";
            $("body").append(tipsDom);
        }
        var content = $(this).text();
        $("#lyTips").html(content).show();
        var X = ev.pageX;
        var Y = ev.pageY;
        var wdHeight = $(window).height();
        var wdWidth = $(window).width();
        // console.log($(".showTips").height());
        var tipHeight = $("#lyTips").height();
        var tipWidth = $("#lyTips").width();

        var size = 20;
        if (wdHeight-Y < tipHeight){
            $("#lyTips").css("top", "");
            $("#lyTips").css("bottom", wdHeight-Y+size);
        }else {
            $("#lyTips").css("bottom", "");
            $("#lyTips").css("top", Y+size);
        }
        if (wdWidth-X < tipWidth){
            $("#lyTips").css("left", "");
            $("#lyTips").css("right", wdWidth-X+size);
        }else {
            $("#lyTips").css("right", "");
            $("#lyTips").css("left", X+size);
        }
    }
}
