/**
 *基于D3.js拓扑图
 *
 */

///Width and height
// var w = $("#topology_container").width();
var w = 720;
var h = 480;

var chatscale = 1;
var chattrax=0;
var chattray=0;

var isFirstLoad = true,
    dataset, alarms;

var dataset = {};
var subtypes = [];
var typemap = {};

$('input[name="zoom"]').blur(function(){
	var zoom = $(this).val();
	var max = parseFloat($(this).attr('max'));
	var min = parseFloat($(this).attr('min'));
	if(zoom>max){
		$(this).val(max);
	}
	if(zoom<min){
		$(this).val(min);
	}
	var newZoom = $(this).val();
	 $.ajax({
	  type: 'POST',
	  url: '/topology/updateZoom',
	  data:{
		  zoom:newZoom
	  },
	  success: function (result) {
	      changeZoom(newZoom);
	  }
	});
	
	
});

function initSubModuleType(types){
	subtypes = types;
	$.each(types,function(index,node){
		typemap[node.id]=node.imagePath;
	});
}

//初始化页面
function initchart(masterlist,sublist){
	  dataset.nodes = [];
	  dataset.paths = [];
	  //master
	  if(masterlist.length>0){
		  $.each(masterlist,function(index,node){
			  dataset.nodes.push({
			        node_id:node.id,
			        type:'switchDevice',
			        name:node.name,
			        x:node.topoX,
			        y:node.topoY,
			        fixed:true,
			        info:node
			    });
		  })
	  }
	  //sub
	  if(sublist.length>0){
		  $.each(sublist,function(index,node){
			  dataset.nodes.push({
	                node_id:node.id,
	                type:'collection',
	                name:node.devEui,
	                x:node.topoX,
	                y:node.topoY,
	                fixed:true,
	                info:node
	            });
			  
			  dataset.paths.push({
	                source: findSourceOrder(masterlist,node),
	                target: masterlist.length+index,
	                lines:[{traffic_status:"normal"}]
	            })
		  })
	  }
    topology.draw();
}

function findSourceOrder(masterlist,sub){
	var order = 0;
	if(!sub.masterModule){
		return order;
	}
	$.each(masterlist,function(index,node){
		  if(node.id==sub.masterModule.id){
			  order = index;
			  return false;
		  }
	  });
	return order;
}

//新增主控
function addMaster(){
	top.layer.open({
        type:2,
        area:['450px','380px'],
        resize:false,
        title:'主控制器新增',
        content:'/topology/masterModuleAdd'
    });
}

//新增子控
function createNewNode(){
	var masterId = $('#master_module_edit input[name="id"]').val();
	openAddsubView(masterId);
}

//新增云子控
function addCloudSub(){
	openAddsubView('0');
}

function openAddsubView(masterId){
	top.layer.open({
        type:2,
        area:['450px','300px'],
        resize:false,
        title:'子控制器添加',
        content:'/sub_module/add_topo_view?masterId='+masterId
    });
}

//编辑节点信息
function editNode(nodeInfo){
	$('#master_module_edit').css('display','none');
	$('#cloud_sub_edit').css('display','none');
	$('#sub_module_edit').css('display','block');
	//form赋值
	$('#sub_module_edit input[name="id"]').val(nodeInfo.id);
	$('#sub_module_edit input[name="devEui"]').val(nodeInfo.devEui);
	$('#sub_module_edit select[name="subModuleTypeId"]').val(nodeInfo.subModuleType.id);
	$('#sub_module_edit input[name="status"][value="'+nodeInfo.status+'"]').attr("checked",true);
	layui.form().render('select');
	layui.form().render('radio');
}

//编辑主控信息
function editMaster(masterInfo){
	if(masterInfo.id=='0'){
		$('#master_module_edit').css('display','none');
		$('#cloud_sub_edit').css('display','block');
		$('#sub_module_edit').css('display','none');
	}else{
		$('#master_module_edit').css('display','block');
		$('#cloud_sub_edit').css('display','none');
		$('#sub_module_edit').css('display','none');
		//form赋值
		$('#master_module_edit input[name="id"]').val(masterInfo.id);
		$('#master_module_edit input[name="name"]').val(masterInfo.name);
		$('#master_module_edit input[name="a"]').val(masterInfo.a);
		$('#master_module_edit input[name="a"]').val(masterInfo.a);
		$('#master_module_edit input[name="portName"]').val(masterInfo.portName);
		$('#master_module_edit input[name="devId"]').val(masterInfo.devId);
		$('#master_module_edit input[name="status"][value="'+masterInfo.status+'"]').attr("checked",true);
		layui.form().render('radio');
	}
}

//保存节点位置信息
function updateNodePos(n){
    if(n.type=='collection'){
        $.ajax({
            type: 'POST',
            url: '/sub_module/updatePos',
            data:{
                id:n.info.id,
                x:n.x,
                y:n.y
            },
            success: function (result) {
            },
            error: function (result, type) {
                console.log(result);
            }
        });
    }else{
        $.ajax({
            type: 'POST',
            url: '/master_module/updatePos',
            data:{
                id:n.info.id,
                x:n.x,
                y:n.y
            },
            success: function (result) {
                console.log(result);
            },
            error: function (result, type) {
                console.log(result);
            }
        });
    }
}

//删除主控
function deleteMaster(){
	var id = $('#master_module_edit input[name="id"]').val();
	$.ajax({
        type: 'POST',
        url: '/master_module/delete',
        data: {ids: id },
        dataType: 'json',
        success: function (result) {
            if (result.code == 0) {
                layer.msg('删除成功', {icon: 1,time: 1000});
                setTimeout(function(){
                	parent.refreshIframe();
                },800);
            }
            else {
                layer.msg('删除失败！'+result.msg, {time:1000,icon: 2});
            }
        },
        error: function(result, type) {
            layer.msg('删除失败！', { time:1000,icon: 2 });
        }
    });
}

//删除子控
function deleteSub(){
	var id = $('#sub_module_edit input[name="id"]').val();
	$.ajax({
        type: 'POST',
        url: '/sub_module/delete',
        data:{
        	ids:id
        },
        success: function (result) {
            if (result.code == 0) {
                layer.msg('删除成功', {icon: 1, time:1000});
                setTimeout(function(){
                	parent.refreshIframe();                
                },800);
            }
            else {
                layer.msg('删除失败！'+result.msg, {icon: 2});
            }
        },
        error: function (result, type) {
            layer.msg('删除失败！', { icon: 2 });
        }
    });
}

function changeZoom(zoom){
	d3.select(".container")
    .attr("transform","translate(0,0)"+
        "scale("+zoom+")");
}

//查看子控地图详情
function findDetail(){
	var subModuleTypeId = $('#sub_module_edit select[name="subModuleTypeId"]').val();
	var subId = $('#sub_module_edit input[name="id"]').val();
	var chsize = 1;
	var ch = 1;
	$.each(subtypes,function(index,node){
		if(node.id==subModuleTypeId){
			chsize = node.chSize;
			return false;
		}
	});
	if(chsize>1){
		layer.prompt({
			  formType: 0,
			  value: '1',
			  title: '请输入频道号',
			}, function(value, index, elem){
			  if(!validNum(value)){
				  layer.msg('频道号为整数！');
			  }else if(parseInt(value)<1 || parseInt(value)>chsize){
				  layer.msg('频道号输入错误！');
			  }else{
				  ch = value;
				  layer.close(index);
				  top.layer.open({
				        type:2,
				        area:['1000px','620px'],
				        resize:false,
				        title:'感应带地图',
				        content:'/topology/view_submodule_path?subid='+subId +'&ch='+ch
				    });
			  }
			});
	}else{
		top.layer.open({
			type:2,
			area:['1000px','620px'],
			resize:false,
			title:'感应带地图',
			content:'/topology/view_submodule_path?subid='+subId
		});
	}
}

function validNum(str){
	var r = /^\+?[1-9][0-9]*$/;　　//正整数
	return flag=r.test(str);
}