
//字符串前自动补零  ly 2018/06/21
String.prototype.lyPadStart = function (size, pad){
    pad += "";
    size *= 1;
    var pads ="";
    for(var i = 0; i < (size-this.length); i++){
        pads += pad;
    }
    return pads + this;
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
        return Array.isArray(value); //ECMAScript5将Array.isArray()正式引入JavaScript 存在兼容性问题
    }else{
        return Object.prototype.toString.call(value) === "[object Array]"; //兼容性高
    }
}


//数字转换成汉字
function numberToChinese(number) {

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

//获得文件扩展名 ly
function getFileType(fileDomId){

    var file = document.getElementById(fileDomId).files[0];
    var fileName = file.name;
    var resultArray = fileName.split(".");
    return resultArray[resultArray.length-1];
}

//获得文件名
function getFileName(fileDomId) {
    var name = document.getElementById(fileDomId).files[0].name;
    var last = name.lastIndexOf(".");
    return name.substring(0, last);
}

//获得文件反显路径 即时反显
function getObjectURL(fileId) {
    var file;
    if ("string" == typeof (fileId)) { //domId
        file = document.getElementById(fileId).files[0];
    } else {
        file = fileId; //file对象
    }
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

// 获得 当前时间 param: boolean isTime
function getNowDate(isTime){
    var date = new Date();
    var dateTime = date.getFullYear();
//		dateTime += "-" + ((date.getMonth() + 1).toString().padStart(2, '0'));
    dateTime += "-" + ((date.getMonth() + 1).toString().lyPadStart(2, '0'));
    dateTime += "-" + (date.getDate().toString().lyPadStart(2, '0'));

    if(true == isTime){  //获得时分秒

        dateTime += " " + (date.getHours().toString().lyPadStart(2, '0'));
        dateTime += ":" + (date.getMinutes().toString().lyPadStart(2, '0'));
        dateTime += ":" + (date.getSeconds().toString().lyPadStart(2, '0'));
    }
    return dateTime;
}

//延时加载 fun：要延时的方法 time:时间／毫秒
function timeOut(fun, time) {
    setTimeout(function () {
        fun();
    }, time*1);
}

/**
 * 日期方法
 * @param date 日期对象
 * @param day 增加的天数
 * @param format 日期格式
 * @returns {*}
 */
function dateFormat(date, day, format) {
    if (typeof date != "object"){ date = new Date(); }
    if (!format){ format = "yyyy-MM-dd"; }
    var timeNum = date.getTime();
    if (day){
        timeNum += parseFloat(day) * 86400000;
    }
    date = new Date(timeNum);
    var year = date.getFullYear(),
        month = date.getMonth() + 1,
        day = date.getDate(),
        h24 = date.getHours(),
        h12 = h24 > 12 ? h24 - 12 : h24,
        mm = date.getMinutes(),
        ss = date.getSeconds();
    format = format.replace("yyyy", year);
    format = format.replace("MM", month < 10 ? "0" + month : month);
    format = format.replace("dd", day < 10 ? "0" + day : day);
    format = format.replace("HH", h24 < 10 ? "0" + h24 : h24);
    format = format.replace("hh", h12 < 10 ? "0" + h12 : h12);
    format = format.replace("mm", mm < 10 ? "0" + mm : mm);
    format = format.replace("ss", ss < 10 ? "0" + ss : ss);
    return format;
}


function rgbToHex(r, g, b){
    var hex = ((r<<16) | (g<<8) | b).toString(16);
    return "#" + new Array(Math.abs(hex.length-7)).join("0") + hex;
}

// hex to rgb
function hexToRgb(hex){
    var rgb = [];
    for(var i=1; i<7; i+=2){
        rgb.push(parseInt("0x" + hex.slice(i,i+2)));
    }
    return rgb;
}

/**
 * 计算渐变过渡色
 * 例([0,0,0], [255,255,255], 100) 取100个:(]
 */
function gradient_rgb (sColor, eColor, step){
    // 计算R\G\B每一步的差值
    var stepOne = [
        (eColor[0] - sColor[0]) / step,
        (eColor[1] - sColor[1]) / step,
        (eColor[2] - sColor[2]) / step
    ]
    //获得结果集
    var colorArr = [];
    for(var i = 1; i <= step; i++){
        colorArr.push([
            stepOne[0]*i+sColor[0],
            stepOne[1]*i+sColor[1],
            stepOne[2]*i+sColor[2],
        ]);
    }
    return colorArr;
}


/**
 * 计算渐变过渡色('#ec9089', '#c12927', 100, 24) 取100个:[) 第24个
 */
function gradient (startColor,endColor,step, index){
    // 将 hex 转换为rgb
    var sColor = hexToRgb(startColor),
        eColor = hexToRgb(endColor);
    // 计算R\G\B每一步的差值
    var rStep = (eColor[0] - sColor[0]) / step,
        gStep = (eColor[1] - sColor[1]) / step,
        bStep = (eColor[2] - sColor[2]) / step;
    //若确定取第几个
    if ((index  = parseInt(index)) > 0) {
        return hex(index);
    }
    //获得结果集
    var gradientColorArr = [];
    for(var i = 0; i < step; i++){
        // 计算每一步的hex值
        gradientColorArr.push(hex(i));
    }
    return gradientColorArr;

    //创建16进制坐标
    function hex(num) {
        return rgbToHex(
            parseInt(rStep * num + sColor[0]),
            parseInt(gStep * num + sColor[1]),
            parseInt(bStep * num + sColor[2])
        )
    }
}

/**
 * cookie方法
 * @param method set/get/clear
 * @param cookieKey 浏览器中的key
 * @param key cookieKey中的key
 * @param value 值
 * @returns {string}
 */
function kjpsCookie(method, cookieKey, key, value) {
    var path = "kjps";
    var ck = document.cookie;
    ck = ck.substring(ck.indexOf(cookieKey) + cookieKey.length + 1);
    ck = ck.substring(0, ck.indexOf(";"));
    switch (method){
        case "set":
            if (!ck || document.cookie.indexOf(cookieKey+"=") < 0){
                ck = key + "=" + value;
            } else if (ck.indexOf(key + "=") >= 0) {
                ck = ck.replace(key + "=" + getCk(), key + "=" + value);
            } else {
                ck += "&" + key + "=" + value;
            }
            document.cookie = cookieKey+"=" + ck + "; path=/"+path;
            break;
        case "get":
            if (ck && ck.indexOf(key + "=") >= 0) {
                return getCk();
            }else {
                return "";
            }
        case "clear":
            document.cookie = cookieKey+"=; path=/"+path;
            break;
        default: break;
    }

    function getCk() {
        var val = ck.substring(ck.indexOf(key) + (key.length + 1));
        if (val.indexOf("&") < 0){
            return val;
        }
        return val.substring(0, val.indexOf("&"));
    }
}


var HtmlUtil = {
    /*1.用浏览器内部转换器实现html转码*/
    htmlEncode:function (html){
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement ("div");
        //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
        (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
        //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
        var output = temp.innerHTML;
        temp = null;
        return output;
    },
    /*2.用浏览器内部转换器实现html解码*/
    htmlDecode:function (text){
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
        temp.innerHTML = text;
        //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
        var output = temp.innerText || temp.textContent;
        temp = null;
        return output;
    },
    /*3.用正则表达式实现html转码*/
    htmlEncodeByRegExp:function (str){
        var s = "";
        if(str.length == 0) return "";
        s = str.replace(/&/g,"&amp;");
        s = s.replace(/</g,"&lt;");
        s = s.replace(/>/g,"&gt;");
        s = s.replace(/ /g,"&nbsp;");
        s = s.replace(/\'/g,"&#39;");
        s = s.replace(/\"/g,"&quot;");
        return s;
    },
    /*4.用正则表达式实现html解码*/
    htmlDecodeByRegExp:function (str){
        var s = "";
        if(str.length == 0) return "";
        s = str.replace(/&amp;/g,"&");
        s = s.replace(/&lt;/g,"<");
        s = s.replace(/&gt;/g,">");
        s = s.replace(/&nbsp;/g," ");
        s = s.replace(/&#39;/g,"\'");
        s = s.replace(/&quot;/g,"\"");
        return s;
    }
};


function randomAccess(min,max){
    return Math.floor(Math.random() * (min - max) + max)
}

// 解码
function decodeUnicode(str) {
   //Unicode显示方式是\u4e00
   str = "\\u"+str
   str = str.replace(/\\/g, "%");
    //转换中文
   str = unescape(str);
    //将其他受影响的转换回原来
   str = str.replace(/%/g, "\\");
   return str;
}

/*
*@param Number NameLength 要获取的名字长度
*/
function getRandomName(NameLength){
    let name = ""
    for(let i = 0;i<NameLength;i++){
        let unicodeNum  = ""
        unicodeNum = randomAccess(0x4e00,0x9fa5).toString(16)
        name += decodeUnicode(unicodeNum)
    }
    return name
}