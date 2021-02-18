
String.prototype.endWith=function(endStr){
    var d=this.length-endStr.length;
    return (d>=0&&this.lastIndexOf(endStr)==d)
}
$(function () {
    PowerBtnControl();
});
$(window).load(function btnInit(){
	divResize();
});

//展示提示信息
function showPrompt(jsObj, msg) {


}

//div自适应窗口
function divResize(){
	var windowWidth = $(window).width();
	var windowHeight = $(window).height();
	$("#mainDiv").width(windowWidth-4);
	$("#mainDiv").height(windowHeight-4);
}
//将特殊字符进行转义存放到数据库中
function decodeObject(obj){
	for(var p in obj){
		var v = obj[p];
		if(v instanceof Object){
			decodeObject(v);
		}else{
			obj[p] = HtmlUtil.htmlDecode(v);
		}
	}
}

//把特殊的符号转成html   如空格转成&nbsp;
function encodeObject(obj){
	for(var p in obj){
		var v = obj[p];
		if(v instanceof Object){
			encodeObject(v);
		}else{
			obj[p] = HtmlUtil.htmlEncode(v);
		}
	}
}

/**
 * 获取全部字典项，并将其赋予一个全局变量
 * @author LC
 * @returns
 */
var DIC = {};//定义一个全局变量，存储所有的字典项
function initDic(){
	var dic;
	doRequest({
		url: "/kjps/system/dicItem/getAllDic",
		isEncrypt : true, // 传输加密
		async : false, // 不允许异步
		callback : function(res) {
			dic = res.rows;
		}	
	});
	var father = dic[0].dicId;	//父字典项编码
	var fat = {};				//父字典项数组
	for(var i=0; i<dic.length; i++){
		if(dic[i].dicId == father){
			fat[dic[i].itemCode] = dic[i].itemName;
		}else{
			DIC[father] = fat;
			fat = {};					//重置父字典项数组
			father = dic[i].dicId;		//重置父字典项编码
			fat[dic[i].itemCode] = dic[i].itemName;
		}
	}
	DIC[father] = fat;//把所有字典项拼成key-value的形式
//	var d = {};
//	d["cs1"]="测试一";
//	d["cs2"]="测试二";
//	d["cs3"]="测试三";
//	dic["d"] = d;
//	var d1 = {};
//	d1["cs11"]="测试一1";
//	d1["cs21"]="测试二1";
//	d1["cs31"]="测试三1";
//	dic["d1"] = d1;
//	debugger
//	for(var str in DIC){ 
//		if(str == "DIC_ZYDL"){
//			var child = DIC[str];
//		} 
//	}
}

/**
 * 检验批量上传文件是否为PDF
 * @param fileId
 * @returns
 */
function checkPDFDatas(fileId){
	var fileDir = document.getElementById(fileId).files;
	var flag = true;
	$(fileDir).each(function(index,element){
		var name = element.name;
		var suffix = name.substr(name.lastIndexOf("."));  
		if("" == name){  
			ts("选择需要的PDF文件！");  
			flag = false;
			return false;  
		}  
		if(".pdf" != suffix && ".PDF" != suffix){  
			ts("选择pdf格式的文件导入！");  
			flag = false;
			return false;  
		}  
	});
	return flag;
}
/**
 * 检验批量上传文件是否为PPT
 * @param fileId
 * @returns
 */
function checkPPTDatas(fileId){
	var fileDir = document.getElementById(fileId).files;
	var flag = true;
	$(fileDir).each(function(index,element){
		var name = element.name;
		var suffix = name.substr(name.lastIndexOf("."));  
		if("" == name){  
			ts("选择需要的PPT文件！");  
			flag = false;
			return false;  
		}  
		if(".ppt" != suffix && ".pptx" != suffix && ".PPT" != suffix && ".PPTX" != suffix){    
			ts("选择PPT文件导入！");  
			flag = false;
			return false;  
		}  
	});
	return flag;
}
/**
 * 检验上传文件是否为PDF
 * @param fileId
 * @returns
 */
function checkPDFData(fileId){  
   var fileDir = $("#"+fileId).val();  
   var suffix = fileDir.substr(fileDir.lastIndexOf("."));  
   if("" == fileDir){  
       ts("选择需要的PDF文件！");  
       return false;  
   }  
   if(".pdf" != suffix && ".PDF" != suffix){  
       ts("选择pdf格式的文件导入！");  
       return false;  
   }  
   return true;  
}

/**
 * 检验上传文件是否为doc或docx文档
 * @param fileId
 * @returns
 */
function checkWordData(fileId){  
   var fileDir = $("#"+fileId).val();  
   var suffix = fileDir.substr(fileDir.lastIndexOf("."));  
   if("" == fileDir){  
       ts("选择需要的DOCX格式Word文件！");  
       return false;  
   }  
   if(".docx" != suffix && ".DOCX" != suffix){  
       ts("选择DOCX格式的文件导入！");  
       return false;  
   }  
   return true;  
}

/**
 * 检验上传文件是否为ppt或pptx文档
 * @param fileId
 * @returns
 */
function checkPPTData(fileId){
	var fileDir = $("#"+fileId).val();  
	   var suffix = fileDir.substr(fileDir.lastIndexOf("."));  
	   if("" == fileDir){  
	       ts("选择需要的PPT文件！");  
	       return false;  
	   }  
	   if(".ppt" != suffix && ".pptx" != suffix && ".PPT" != suffix && ".PPTX" != suffix){  
	       ts("选择PPT式的文件导入！");  
	       return false;  
	   }  
	   return true; 
}

/**
 * 重置查询框
 */
function clearSearchForm(formId,Grid){
	$('#'+formId).form('reset');
	$('#'+Grid).datagrid('reload');
}
/**
 * 单位代码树
 * 不能选择根节点
 * @param treeId  input-id
 * @returns
 */
function initCompany(treeId){
	initCompanys(treeId,'../system/org/orgList',false);
}

/**
 * 单位代码树（只能选择推荐单位）
 * 不能选择根节点
 * @param treeId  input-id
 * @returns
 */
function initCompany1(treeId){
	initCompanys(treeId,'../system/org/orgList',true);
}

/**
 * 单位代码树
 * 不能选择根节点
 * @param treeId  input-id
 * @returns
 */
function initCompanys(treeId,url,type){
	doRequest({
		url : url,
		async : false, // 不允许异步
		callback : function(res) {
			orows=res.rows;
			data = orgChangeTree(orows); //TreeGrid Action数据转换
			orgRows = orgConvert(data);//Tree children 数据转换 
			$('#'+treeId).combotree('loadData',orgRows);
			$('#'+treeId).combotree({   
		        onBeforeSelect: function(node) {  
		        	if(node.children!=null&&node.children.length!=0){
		        		if(node.children[0].children!=null&&node.children[0].children.length!=0){
		        			return false;
		        		}else{
		        			var father = $(this).tree("getParent",node.target);
		        			if(father != null){
		        				if($(this).tree("getParent",father.target) == null){	//正数第二级单位不能选中
		        					//清除选中  
//				            	 	$('#orgId1').treegrid("unselect");
		        					return false;
		        				}else{
		        					if(type&&node.attributes['tjdw']=="否"){ //不是推荐单位的不能选中
		        						return false;
		        					}
		        				}
		        			}else{														//正数第一级单位不能选中
		        				//清除选中  
//			            		$('#orgId1').treegrid("unselect");
		        				return false;
		        			}
		        		}
		        	}else if(type&&node.attributes['tjdw']=="否"){ //不是推荐单位的不能选中
		        		return false;
					}
		        },  
		        onClick: function(node) {  
		        	if(node.children!=null&&node.children.length!=0){
		        		if(node.children[0].children!=null&&node.children[0].children.length!=0){
		        			 $('#'+treeId).combo('showPanel');
		        		}else{
		        			var father = $(this).tree("getParent",node.target);
		        			if(father != null){
		        				if($(this).tree("getParent",father.target) == null){	//正数第二级单位不能选中
		        					//清除选中  
//				            	 	$('#orgId1').treegrid("unselect");
		        					$('#'+treeId).combo('showPanel');
		        				}else{
		        					if(type&&node.attributes['tjdw']=="否"){ //不是推荐单位的不能选中
		        						$('#'+treeId).combo('showPanel');
		        					}
		        				}
		        			}else{														//正数第一级单位不能选中
		        				//清除选中  
//			            		$('#orgId1').treegrid("unselect");
		        				$('#'+treeId).combo('showPanel');
		        			}
		        		}
		        	}else if(type&&node.attributes['tjdw']=="否"){ //不是推荐单位的不能选中
						$('#'+treeId).combo('showPanel');
					}
		        }  
		    }); 
		}
	});
}

//TreeGrid Action 数据转换
function orgChangeTree(rows) {
	$.each(rows, function(i) {
		var parentId = rows[i].pid;
		if (parentId == "0") { return;}
		rows[i]._parentId = parentId;
	});
	return rows;
} 
function orgConvert(rows) {
    var nodes = [];
    var toDo = [];
	 
	function exists(rows, pid) {
	    for (var i = 0; i < rows.length; i++) {
			if (rows[i].orgId == pid)
				return true;
		}
		return false;
	}
	for (var i = 0; i < rows.length; i++) {
	    var row = rows[i];
		if (!exists(rows, row.pid)) { 
			nodes.push({
				id : row.orgId,
				text : row.orgName,
				state:'closed',
				attributes:{'tjdw':row.fkDicIsRecommendQualified}
			});
		}
	}
	for (var i = 0; i < nodes.length; i++) {
		toDo.push(nodes[i]);
	}
	while (toDo.length) {
		var node = toDo.shift(); // 父节点   
		// 找出一个父的所有子节点   
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (row.pid == node.id) {
				var child = {
						id : row.orgId,
						text : row.orgName,
						state:'open',
						attributes:{'tjdw':row.fkDicIsRecommendQualified}
				};
				for ( var ii in orows) {//此节点有子节点时改变状态
					if (orows[ii].pid==row.orgId) {
						child.state='closed';
						break;
					}
				}
				
				if (node.children) {
					node.children.push(child);
				} else {
					node.children = [ child ];
				}
				toDo.push(child);
			}
		}
	}
	return nodes;
}

/**
 * 科技成果分类代码树
 * 只能选择最底层节点
 * @param treeId    input-id
 * @returns
 */
function initFiled(treeId){
	doRequest({
		url: "/kjps/base/catalogCode/initComoTree",
		isEncrypt : true, // 传输加密
		async : false, // 不允许异步
		callback : function(res) {
			orows=res.rows;
			data = orgChangeTree1(orows); //TreeGrid Action数据转换
			orgRows = orgConvert1(data);//Tree children 数据转换 
			$('#'+treeId).combotree('loadData',orgRows);
			$('#'+treeId).combotree({   
		        onBeforeSelect: function(node) {  
		            if (!$(this).tree('isLeaf', node.target)) {  
		                return false;  
		            }  
		        },  
		        onClick: function(node) {  
		            if (!$(this).tree('isLeaf', node.target)) {  
		                $('#'+treeId).combo('showPanel');  
		            }  
		        }  
		    }); 
		}	
	});
}


/**
 * 科技成果分类代码树
 * 可以选择后两级节点
 * @param treeId    input-id
 * @returns
 */
function initFiled1(treeId){
	doRequest({
		url: "/kjps/base/catalogCode/initComoTree",
		isEncrypt : true, // 传输加密
		async : false, // 不允许异步
		callback : function(res) {
			orows=res.rows;
			data = orgChangeTree1(orows); //TreeGrid Action数据转换
			orgRows = orgConvert1(data);//Tree children 数据转换 
			$('#'+treeId).combotree('loadData',orgRows);
			$('#'+treeId).combotree({   
				 onBeforeSelect: function(node) {  
		        	if(node.children!=null&&node.children.length!=0){
		        		if(node.children[0].children!=null&&node.children[0].children.length!=0){
		        			return false;
		        		}
		        	}
		        },  
		        onClick: function(node) {  
		        	if(node.children!=null&&node.children.length!=0){
		        		if(node.children[0].children!=null&&node.children[0].children.length!=0){
		        			 $('#'+treeId).combo('showPanel');
		        		}
		        	}
		        }  
		    }); 
		}	
	});
}

//TreeGrid Action 数据转换
function orgChangeTree1(rows) {
	$.each(rows, function(i) {
		var parentId = rows[i].superiorId;
		if (parentId == null||parentId == '') { return;}
		rows[i]._parentId = parentId;
	});
	return rows;
} 
function orgConvert1(rows) {
    var nodes = [];
    var toDo = [];
	function exists(rows, superiorId) {
	    for (var i = 0; i < rows.length; i++) {
			if (rows[i].id == superiorId)
				return true;
		}
		return false;
	}
	for (var i = 0; i < rows.length; i++) {
	    var row = rows[i];
		if (!exists(rows, row.superiorId)) { 
			nodes.push({
				id : row.id,
				text : row.code+row.name,
				state:'closed'
			});
		}
	}
	for (var i = 0; i < nodes.length; i++) {
		toDo.push(nodes[i]);
	}
	while (toDo.length) {
		var node = toDo.shift(); // 父节点   
		// 找出一个父的所有子节点   
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (row.superiorId == node.id) {
				var child = {
						id : row.id,
						text : row.code+row.name,
						state:'open'
				};
				for ( var ii in orows) {//此节点有子节点时改变状态
					if (orows[ii].superiorId==row.id) {
						child.state='closed';
						break;
					}
				}
				
				if (node.children) {
					node.children.push(child);
				} else {
					node.children = [ child ];
				}
				toDo.push(child);
			}
		}
	}
	return nodes;
}

/**
 * 获取字典项列表
 * @param dicCodes  字典编码
 * @param boxId     下拉框id值
 * @returns
 */
function checkBox(dicCodes,boxId){
	var itemMap = new Array();
	var child;
//	doRequest({ 
//		'url': '/kjps/system/dic/listByCodes',
//		'data':{ dicCodes: [dicCodes] },
//		'isEncrypt': true,                      //传输加密 
//		'async': false,                         //不允许异步
//		'callback': function (res) {
//			// TODO 数据校验
//			itemMap = formatDic2Map(res.rows);
//		}
//	});
	
	for(var str in DIC){ 				//遍历DIC中的所有父字典项
		if(str == dicCodes){
			child = DIC[str];			//父字典项对应上，则取出其对应的所有子字典项
		} 
	}
	itemMap.push({"code":"","description":"请选择"});
	for(var str in child){ 										//将字典项set到下拉选项中
		itemMap.push({"code":str,"description":child[str]});
	}
	$('#'+boxId).combobox({
        data: itemMap,                        
        valueField: 'code',
        textField: 'description',
        editable: false,
        required:true
    });
}

function checkBox2(dicCodes,boxId){
	var itemMap = new Array();
	var child;
	for(var str in DIC){ 				//遍历DIC中的所有父字典项
		if(str == dicCodes){
			child = DIC[str];			//父字典项对应上，则取出其对应的所有子字典项
		} 
	}
	itemMap.push({"code":"","description":"请选择"});
	for(var str in child){ 										//将字典项set到下拉选项中
		itemMap.push({"code":str,"description":child[str]});
	}
	$('#'+boxId).combobox({
        data: itemMap,                        
        valueField: 'code',
        textField: 'description',
        editable: false
    });
}

//字典数据格式化处理
function formatDic2Map(dics) {
	if (!dics) {
		return;
	}
	// 将字典数组格式数据格式转换为Map
	var itemMap = new Array();
	itemMap.push({"code":"","description":"请选择"});
	for (var i = 0; i < dics.length; i++) {
		var d = dics[i];
		if (!d.dicItems) {
			continue;
		}
		// 将字典项数据格式转换为Map
		for (var j = 0; j < d.dicItems.length; j++) {
			itemMap.push({"code":d.dicItems[j].itemCode,"description":d.dicItems[j].itemName});
		}
	}
	return itemMap;
}

/**
 * 根据字典项编码获取字典项名称
 * @param value
 * @returns
 */
function getDicNameByCode(value){
	if(value == null || value == ""){
		return;
	}
	var name;
//	var data = {};
//	data["itemCode"] = value;
//	doRequest({ 
//		'url': '/kjps/system/dicItem/listByItem',
//		'data':data,
//		'isEncrypt': true,                      //传输加密 
//		'async': false,                         //不允许异步
//		'callback': function (res) {
//			// TODO 数据校验 
//			if(res.rows != null && res.rows.length == 1){
//				name = res.rows[0].itemName;
//			}else{
//				name = "";
//			}
//		}
//	});
	for(var fat in DIC){			//遍历DIC中的各个父字典项
		var sonS = DIC[fat];
		for(var son in sonS){ 		//对每个父字典项遍历其所有子字典项
			if(value == son){		//若传入的字典项编码和其中一个对应	
				name = sonS[son];	//将字典项名字传回
			}
		}
	}
	return name;
}

/**
 * 根据字典项名称获取字典项编码
 * @param value
 * @returns
 */
function getDicCodeByName(value){
	if(value == null || value == ""){
		return;
	}
	var name;
//	var data = {};
//	data["itemName"] = value;
//	doRequest({ 
//		'url': '/kjps/system/dicItem/listByItem',
//		'data':data,
//		'isEncrypt': true,                      //传输加密 
//		'async': false,                         //不允许异步
//		'callback': function (res) {
//			// TODO 数据校验
//			if(res.rows != null && res.rows.length == 1){
//				name = res.rows[0].itemCode;
//			}else{
//				name = "";
//			}
//		}
//	});
	for(var fat in DIC){			//遍历DIC中的各个父字典项
		var sonS = DIC[fat];
		for(var son in sonS){ 		//对每个父字典项遍历其所有子字典项
			if(value == sonS[son]){		//若传入的字典项名称和其中一个对应	
				name = son;	//将字典项编码传回
			}
		}
	}
	return name;
}

/**
 * 根据组织Id获取组织名称
 * @param value
 * @returns
 */
function getOrgNameById(value){
	if(value == null || value == ""){
		return;
	}
	var name = "";
	var data = {};
	data["orgId"] = value;
	doRequest({ 
		'url': '../system/org/getOrg',
		'data':data,
		'isEncrypt': true,                      //传输加密 
		'async': false,                         //不允许异步
		'callback': function (res) {
			// TODO 数据校验
			if(res.rows != null && res.rows.length == 1){
				name = res.rows[0].orgName;
			}else{//如果name为错误的id，则不显示，若为非组织中的单位，则显示原来值
//				if(value.length == 36 && value.indexOf("-")>=0){
				if(value.length == 36 && /^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$/i.test(value)){
					name = "";
				}else{
					name = value;
				}
			}
		}
	});
	return name;
}

/**
 * 根据组织名称获取组织ID
 * @param value
 * @returns
 */
function getOrgIdByName(value){
	if(value == null || value == ""){
		return;
	}
	var id;
	var data = {};
	data["orgName"] = value;
	doRequest({ 
		'url': '/kjps/system/org/getOrg',
		'data':data,
		'isEncrypt': true,                      //传输加密 
		'async': false,                         //不允许异步
		'callback': function (res) {
			// TODO 数据校验
			if(res.rows != null && res.rows.length == 1){
				id = res.rows[0].orgId;
			}else{
				id = "";
			}
		}
	});
	return id;
}

/**
 * 根据科技成果ID获取code+name
 * @param value
 * @returns
 */
function getFieldNameById(value){
	if(value == null || value == ""){
		return;
	}
	var name;
	var data = {};
	data["id"] = value;
	doRequest({ 
		'url': '../base/catalogCode/getCatalog',
		'data':data,
		'isEncrypt': true,                      //传输加密 
		'async': false,                         //不允许异步
		'callback': function (res) {
			// TODO 数据校验
			if(res.rows != null && res.rows.length == 1){
				name = res.rows[0].code + res.rows[0].name;
			}else{
				name = "";
			}
		}
	});
	return name;
}

/**
 * 根据科技成果code+name获取ID
 * @param value
 * @returns
 */
function getFieldIdByName(value){
	if(value == null || value == ""){
		return;
	}
	var id;
	var data = {};
	data["name"] = value;
	doRequest({ 
		'url': '../base/catalogCode/getCatalog',
		'data':data,
		'isEncrypt': true,                      //传输加密 
		'async': false,                         //不允许异步
		'callback': function (res) {
			// TODO 数据校验
			if(res.rows != null && res.rows.length == 1){
				id = res.rows[0].id;
			}else{
				id = "";
			}
		}
	});
	return id;
}


function doRequest(opts, contentType) {
	var _opts = opts;
	
	_opts.type = _opts.type ||  "POST";								// 默认以POST方式提交
	_opts.contentType = _opts.contentType || 'application/json;charset=UTF-8'; // 默认以JSON方式传递参数
	_opts.dataType = _opts.dataType || 'json';						// 默认以JSON方式接收数据
	_opts.async = _opts.async === false ? false : true;             // 默认异步
	_opts.isEncrypt = _opts.isEncrypt === false ? false : true; 	// 默认前后端之间数据加密
	_opts.fileRequest = _opts.fileRequest === true ? true : false;  //是否为文件上传
    _opts.loading = _opts.loading === false ? false : true;  //默认添加加载效果
    _opts.fileLoading = _opts.fileLoading === true ? true : false;  //是否添加上传读条动画

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
			xhr : _xhr,
			beforeSend : _beforeSend,
			complete : _complete,
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
			beforeSend : _beforeSend,
			complete : _complete,
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
	function _xhr(){
		if (_opts.fileRequest) {
	        //取得xmlhttp异步监听
	        var xhr = $.ajaxSettings.xhr();
	        if(onprogress && xhr.upload) {
	            xhr.upload.addEventListener('progress' , onprogress, false);
	            return xhr;
	        }
		}
    }
	
	function _beforeSend() {
		if (_opts.fileRequest) {
			if (!document.getElementById("uploading_ly")) {
				$("<div id='uploading_ly'><div class='mp4Schedule'><span>0%</span><div style='width:0%;'><img src='" + getPath() + "/resources/images/working.gif'></div></div></div>").appendTo("body");
			}
		}
		if(_opts.loading){
			$("<div class=\"datagrid-mask\"  style=\"z-index:9998;\"></div>").css({display:"block"}).appendTo("body");
	        var msg = $("<div class=\"datagrid-mask-msg\" style=\"display:block;left:50%;margin-top:0px;z-index: 9999;\"></div>").html("正在处理，请稍候。。。").appendTo("body");
	        msg._outerHeight(40);
	        msg.css({ marginLeft: (-msg.outerWidth() / 2), lineHeight: (msg.height() + "px") });
		}
    }
    function _complete() {
    	if (_opts.fileRequest) {
    		$(".mp4Schedule div").css("width", "0%");
            $(".mp4Schedule span").text('0%(0kb/0kb)');
        	$("#uploading_ly").remove();
		}
        if(_opts.loading) {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }
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
				ts('错误', res);
			}
			// top.window.location.reload();
		}
	}

	function onprogress(evt){
	    var loaded = evt.loaded;                  //已经上传大小情况
	    var tot = evt.total;                      //附件总大小
	    var per = Math.floor(100*loaded/tot);     //已经上传的百分比 
	    $(".mp4Schedule div").css("width", per +'%');
        $(".mp4Schedule span").text(per +'%'+'('+Math.round(loaded/1024)+'kb/'+Math.round(tot/1024)+'kb)');
	}
}


function cancelUpload(){
	console.log(fileDown);
	fileDown.abort();
	$("#uploading_ly").remove();
}

//表单数据项验证
function validate(formId){
	if(!$('#'+formId).form('validate')){
		return false;
	}else{
		return true;
	}
}

/**
 * 结束时间不能早于开始时间
 * @param beginTime
 * @param endTime
 * @returns
 */
function dateSelect(beginTime,endTime){
	$("#"+beginTime).datebox({ 
		 onSelect : function(beginDate){ 
			 $('#'+endTime).datebox().datebox('calendar').calendar({ 
				 validator: function(date){ 
					 return beginDate<=date;
				 } 
			 }); 
		 } 
	});
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

// 将(枚举的)json对象转为json数组k,v
function getArr(json) {
	var arr = [];// 定义返回数组
	for ( var item in json) {
		var jsona = {};
		jsona.k = item;
		jsona.v = json[item];
		arr.push(jsona);
	}
	return arr;
}
function ts(ms) {
	$.messager.show({
		title : '提示',
		msg : ms,
		timeout : 500,
		style : {}
	});
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
}

function timeFormatter(d){
	if(d !=null){		
		return new Date(d).toLocaleString();  
	}
	else{
		return "";
	}
}

function isNull(d){
	if(d !=null){		
		return d;  
	}
	else{
		return "";
	}
}

//获取时间

function getNowFormatDate() {

    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();

    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
  /*  var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds(); //yyyy-MM-dd HH:MM:SS*/    
   
    var currentdate = month + "/" + strDate+ "/" + date.getFullYear() + " " + date.getHours() + seperator2 + date.getMinutes()
    + seperator2 + date.getSeconds(); //M/dd/yyyy HH:MM:SS
    return currentdate;

}

//重计算行号
function reComRowNo(tableId,pageNumber,pageSize){
	var start = (pageNumber - 1) * pageSize; 
    var end = start + pageSize;
//	var rowNumbers = $("#"+tableId).find('.datagrid-td-rownumber');
//	var rowNumbers = $(".datagrid-body").find('.datagrid-td-rownumber');
	var rowNumbers = $("#"+tableId).parent().find('.datagrid-td-rownumber');
	
	//重新计算行号
    $(rowNumbers).each(function(index){
    	
        var row = index + 1 + parseInt(start);
        $(rowNumbers[index]).find("div").html("");
        $(rowNumbers[index]).find("div").html(row);
        
    });
}

/**
 * tab样式变换
 */
function changeTabsBerfore(name){
    $("#"+ name).children(".tabs-header").find(".tabs-wrap").find("li").find("a").css("border","transparent"); //边线透明
    //遍历li 操作
    $("#"+ name).children(".tabs-header").find(".tabs-wrap").find("li").each(function(i){
        //点击事件
        $(this).click(function(){
            $("#"+ name).children(".tabs-header").find(".tabs-wrap").find("li").each(function(j){
                if(j == i){
                    if($(this).is(".tabs-first")){
                        $(this).find("a").css("background-image","url('../resources/index/images/tabfirstUnsel.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");
                    }
                    else if($(this).is(".tabs-last")){
                        $(this).find("a").css("background-image","url('../resources/index/images/tablastSel.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");    
                    }
                    else{
                        $(this).find("a").css("background-image","url('../resources/index/images/tablisSelect.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");
                    }
                    
                    
                    $(this).find("a").find(".tabs-title").each(function(){
                        var title = '';
                            title +='<span class="fa-stack" style="margin-top:16px;color:#4A91F0">';
                            title += '<i class="fa fa-circle-thin fa-stack-2x"></i>';
                            title += '<i class="fa fa-circle fa-stack-1x"></i>';
                            title +='</span><br>';
                        title += $(this).text();
                            
                        $(this).html(title);
                    });
                }
                else if(j<i){
                    if($(this).is(".tabs-first")){
                        $(this).find("a").css("background-image","url('../resources/index/images/tabfirstSel.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");
                    }
                    else{
                        $(this).find("a").css("background-image","url('../resources/index/images/tablisallsel.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");
                    }
                    
                    var title = '';
                    $(this).find("a").find(".tabs-title").each(function(){
                        title = '<i class = "fa fa-circle" style="margin-top:22px;color:#4A91F0"></i><br>'
                        title += $(this).text();
                    $(this).html(title);
                    });
                }
                else if(j>i){
                    if($(this).is(".tabs-last")){
                        $(this).find("a").css("background-image","url('../resources/index/images/tablastUnsel.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");    
                    }
                    else{
                        $(this).find("a").css("background-image","url('../resources/index/images/tablisUnsel.png')");
                        $(this).find("a").css("background-repeat","no-repeat");
                        $(this).find("a").css("background-size","100% 100%");
                        $(this).find("a").css("background-color","#F8FAFB");
                    }
                    
                    var title = '';
                    $(this).find("a").find(".tabs-title").each(function(){
                        title = '<i class = "fa fa-circle" style="margin-top:22px;color:#DFDFDF"></i><br>'
                        title += $(this).text();
                    $(this).html(title);
                    });
                }
            });
        });
    }); 
    //tabstitle 变换
    $("#"+ name).children(".tabs-header").find(".tabs-wrap").find("li").each(function(j){
        var title = '';
        //判断是否被选中
        if($(this).is(".tabs-selected")){
            title = '';
            //增加 a 标签背景
            if($(this).is(".tabs-first")){
                $(this).find("a").css("background-image","url('../resources/index/images/tabfirstUnsel.png')");
                $(this).find("a").css("background-repeat","no-repeat");
                $(this).find("a").css("background-size","100% 100%");
                $(this).find("a").css("background-color","#F8FAFB");

            }
            $(this).find("a").find(".tabs-title").each(function(){
                    title +='<span class="fa-stack" style="margin-top:16px;color:#4A91F0">';//激活：4A91F0 未激活：DFDFDF
                    title += '<i class="fa fa-circle-thin fa-stack-2x"></i>';
                    title += '<i class="fa fa-circle fa-stack-1x"></i>';
                    title +='</span><br>';
                    title += $(this).text();        
                $(this).html(title);
            });
        }
        else{
            if($(this).is(".tabs-last")){
                $(this).find("a").css("background-image","url('../resources/index/images/tablastUnsel.png')");
                $(this).find("a").css("background-repeat","no-repeat");
                $(this).find("a").css("background-size","100% 100%");
                $(this).find("a").css("background-color","#F8FAFB");    
            }
            else{
                $(this).find("a").css("background-image","url('../resources/index/images/tablisUnsel.png')");
                $(this).find("a").css("background-repeat","no-repeat");
                $(this).find("a").css("background-size","100% 100%");
                $(this).find("a").css("background-color","#F8FAFB");
            }
            title = '';
            $(this).find("a").find(".tabs-title").each(function(){
                title = '<i class = "fa fa-circle" style="margin-top:22px;color:#DFDFDF"></i><br>'
                title += $(this).text();
            $(this).html(title);
        });
        }
    });
}

function changeTabsAfter(name){
    $("#"+ name).children(".tabs-header").find(".tabs-wrap").find("li").find("a").css("line-height","30px"); //更换行高
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

/**
 * 清楚文本框内容
 */
$.extend($.fn.textbox.methods, {
	addClearBtn: function(jq, iconCls){
		var t = $(this);
		var opts = t.textbox('options');
		opts.icons = opts.icons || [];
		opts.icons.unshift({
			iconCls: iconCls,
			handler: function(e){
				$(e.data.target).textbox('clear').textbox('textbox').focus();
				$(this).css('visibility','hidden');
			}
		});
		return jq.each(function(){
			t.textbox();
			if (!t.textbox('getText')){
				t.textbox('getIcon',0).css('visibility','hidden');
			}
			t.textbox('textbox').bind('keyup', function(){
				var icon = t.textbox('getIcon',0);
				if ($(this).val()){
					icon.css('visibility','visible');
				} else {
					icon.css('visibility','hidden');
				}
			});
		});
	}
});

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
                if (res.msg!=null) {
                    ts(res.msg);
				}
            }
        }
    });
    return returnData;
}

//获得文件反显路径 ly
function getObjectURL(fileId) {
    var file;
    if ("string" == typeof (fileId)) {
        file = document.getElementById(fileId).files[0];
    } else {
    	file = fileId;
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

/**
 * @method domresize
 * @description 自动调整表格高度
 * @date 2018-07-22
 * @author 杨荣泽
 * @param gridId 表格id
 * @returns
 */
function domresize(gridId,obj){
var auto = $.extend({auto:100},obj);
$('#'+gridId).datagrid('resize',{
height:$("body").height()-$('#toolbar').height()-auto.auto
});
}

/**
 * @method gridWindowAuto
 * @description 屏幕自适应
 * @date 2018-07-22
 * @author 杨荣泽
 * @param gridId 表格id
 * @returns
 */
function gridWindowAuto(gridId,auto){
	domresize(gridId,{auto:auto});
	$(window).resize(function(){
		domresize(gridId,{auto:auto});
	});
}

function isEmpty(str) {
	return str == null ? "" : str.trim();
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
//获得项目path路径--wkf
function getPath(){

    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result; // == "/kjps"

}
//动态添加div遮罩层
function createDiv(parent) {
    parent = parent ? document.getElementById(parent) : document.body;
    var html = '<div id="coverLoad">'+
				  '<div id="loading">'+
				    '<div style="width:180px;height:18px;margin:17px auto;color:121212;">'+
				       '<img src="../resources/images/load.gif">&nbsp;&nbsp;正在处理，请稍待。。。'+
				    '</div>'+
				  '</div>'+
				'</div>';		    
    parent.insertAdjacentHTML('beforeend', html);
    var a = $("#coverLoad").height();//获取父类div的高度
	$("#loading").css('margin-top',a/2);
};

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

//添加等待遮盖
function addLoadingCover(func) {
	$("<div class=\"datagrid-mask\"  style=\"z-index:9998;\"></div>").css({display:"block"}).appendTo("body");
    var msg = $("<div class=\"datagrid-mask-msg\" style=\"display:block;left:50%;margin-top:0px;z-index: 9999;\"></div>").html("正在处理，请稍候。。。").appendTo("body");
    msg._outerHeight(40);
    msg.css({ marginLeft: (-msg.outerWidth() / 2), lineHeight: (msg.height() + "px") });
}
//移除等待遮盖
function removeLoadingCover() {
	$(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}

//权限按钮显示
function PowerBtnControl() {
    var gridBtns = [];
    doRequest({
        url: getPath() + "/getPowerSelector",
        async: false,
        callback: function (res) {
            var url = window.location.pathname.replace(getPath() + "/", "");
            var powers = res.rows;
            if (!powers) return;
            for (var i = 0, len = powers.length; i < len; i++) {
            	if (null == powers[i].powerCode) continue;
                if (powers[i].powerCode.indexOf(url) >= 0) {
                    var btnSelector = powers[i].buttonId;
                    if (!btnSelector) continue;
                    if (btnSelector.indexOf("#") >= 0) {
                        $(btnSelector).show();
                    } else {
                        gridBtns.push(btnSelector);
                    }
                }
            }
        }
    });

    //表格按钮显示需延迟加载 必须在表格加载完成后执行
    gridBtnControl = function () {
        // setTimeout(function () {
            for (var i = 0, len = gridBtns.length; i < len; i++) {
                $(gridBtns[i]).show();
            }
            if (gridBtns.length > 0)
                $(gridBtns[0]).parent().css("overflow","visible");
        // }, 100)
    }
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

//解决精度损失的乘法 ly
Number.prototype.multiplyBy = function (a) {
	//若果a是数字
	if (a*1 >= 0 || a*1 < 0){
		var _num = 1;
		var numTimes = 0;//两个数 小数点后边总共有几位数
		if ((this+"").indexOf(".") > 0){
            numTimes += (this+"").length - (this+"").indexOf(".") - 1;
		}
		if ((a + "").indexOf(".") > 0){
            numTimes += (a+"").length - (a+"").indexOf(".") - 1;
		}

		for (;numTimes > 0; numTimes--){
			_num *= 10;
		}
        var numInt = (this+"").replace(".","") * 1;
        var aInt = (a + "").replace(".", "") * 1;
        // this.value(numInt * aInt / _num);
        // console.log(numInt * aInt / _num);
        return numInt * aInt / _num;
	} else {
		console.log("'" + a + "'不是数字");
		return 0;
	}
};

//无精度损失乘法
function multiply(a,b) {
    if ((a*1 >= 0 || a*1 < 0) || (b*1 >= 0 || b*1 < 0)){
        var _num = 1;
        var numTimes = 0;//两个数 小数点后边总共有几位数
        if ((b+"").indexOf(".") > 0){
            numTimes += (b+"").length - (b+"").indexOf(".") - 1;
        }
        if ((a + "").indexOf(".") > 0){
            numTimes += (a+"").length - (a+"").indexOf(".") - 1;
        }

        for (;numTimes > 0; numTimes--){
            _num *= 10;
        }
        var numInt = (b+"").replace(".","") * 1;
        var aInt = (a + "").replace(".", "") * 1;
        console.log(numInt * aInt / _num);
        return numInt * aInt / _num;
    } else {
        console.log(a + "," + b + "必须都是数字");
        return 0;
    }
}

//导入请求
function excelRequest(url, func) {
    fileUploadRequest({
        url: url,
        acceptType: ".xls,.xlsx",
        filename: "excel",
        func: func
    });
}

//文件上传请求
function fileUploadRequest(param) {
	if (document.getElementById("lyExcel")) { $("#lyExcel").remove();}
	var dom = "<form id='lyExcel' method='post' enctype='multipart/form-data' hidden>" +
		"<input id='lyExcelFile' type='file' name='" + param.filename + "' accept='" + param.acceptType + "'></form>";
	$("body").append(dom);
	document.getElementById("lyExcel").onchange = function (ev) {
		var data = new FormData($("#lyExcel")[0]);
		doRequest({
			url:param.url,
			data: data,
			loading : false,//false:不允许异步
			fileRequest: true,
			callback: function (res) {
				$("#lyExcelFile").val("");
				param.func(res);
			}
		});
	};
	$("#lyExcelFile").click();
}

//导出请求
function exportRequest(url) {
    if (!document.getElementById("lyExcelExport")) {
        var dom = "<form id='lyExcelExport' action='" + url + "' method='get' hidden></form>";
        $("body").append(dom);
    }
    $("#lyExcelExport").form('submit');
}

//导出请求
function exportRequest2(url,data) {
    if (!document.getElementById("lyExcelExport")) {
        var dom = "<form id='lyExcelExport' action='" + url + "' method='get' hidden><input id='name' type='hidden' name='name' value='"+data+"' ></form>";
        $("body").append(dom);
    }else{
    	 $("#name").val(data);
    }
    $("#lyExcelExport").form('submit');
    $("#lyExcelExport").remove() ;
}

function dlgClose(dlgId) {
	if (typeof dlgId != "string") dlgId = "dlg";
    $('#' + dlgId).dialog('close');
}

//获得差的绝对值
function getDiffer(a,b) {
	return a>b ? a-b : b-a;
}

/**
 * 字典初始化
 * @param dicId 字典id
 * @param comboId easyui下拉框id
 */
function dicInit(dicId,comboId) {
    doRequest({
        url: getPath() + "/getDicItem",
        data: {dicId: dicId},
        callback: function (res) {
            $("#" + comboId).combobox({
                data: res.rows,
                valueField: 'systemId',
                textField: 'dicItemName',
                // value: res.rows[0].systemId,
                panelHeight: "fit",
                onHidePanel: function() {
                    var valueField = $(this).combobox("options").valueField;
                    var val = $(this).combobox("getValue");  //当前combobox的值
                    var allData = $(this).combobox("getData");   //获取combobox所有数据
                    var result = true;      //为true说明输入的值在下拉框数据中不存在
                    for (var i = 0; i < allData.length; i++) {
                        if (val == allData[i][valueField]) {
                            result = false;
                        }
                    }
                    if (result) {
                        $(this).combobox("clear");
                    }
                },
                onChange: function (value) {}
            });
        }

    })
}