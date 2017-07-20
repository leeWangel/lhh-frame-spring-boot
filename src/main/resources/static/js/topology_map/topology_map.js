//创建画布
var mytopo;

$('.color').click(function(){
	$('.color').removeClass('selected');
	$(this).addClass('selected');
	mytopo.updateColor($(this).attr('showcolor'));
	mytopo.setCurrentColor($(this).attr('showcolor'));
});
$('.line').click(function(){
	$('.line').removeClass('selected');
	$(this).addClass('selected');
	mytopo.updateWidth($(this).attr('showwidth'));
	mytopo.setCurrentWidth($(this).attr('showwidth'));
});
$('#clearbtn').click(function(){
	mytopo.clear();
});

//加载感应带数据
function loadLineInfo(){
	var mapId = $('#mapId').val();
	var subId = $('#subidfield').val();
	var ch = $('#chfield').val();
	$.ajax({
        type: 'POST',
        url: '/topology_map_path/query',
        data:{
        	subModuleId:subId,
            ch:ch,
            mapId:mapId
        },
        success: function (result) {
        	if (result.code == 0 && result.entity!=null) {
        		//设置form表单值
        		$('input[name="name"]').val(result.entity.name);
        		$('input[name="id"]').val(result.entity.id);
        		$('.line').removeClass('selected');
        		$('.line[showwidth='+result.entity.lineWidth+']').addClass('selected');
        		$('.color').removeClass('selected');
        		$('.color[showcolor="'+result.entity.strokeStyle+'"]').addClass('selected');
        		//加载jtopo图
                var lines = [result.entity];
                if(mytopo){
                	mytopo.clear();
                	mytopo.initLines(lines);
                	mytopo.initEvent(mytopo);
                }else{
                	mytopo = new drawTopo(false,false,'select');
                	mytopo.initLines(lines);
                	mytopo.initEvent(mytopo);
                }
                mytopo.setCurrentWidth(result.entity.lineWidth);
                mytopo.setCurrentColor(result.entity.strokeStyle);
        	}else{
        		if(mytopo){
        			mytopo.clear();
        		}else{
        			mytopo = new drawTopo(false,false,'select');
        			mytopo.initEvent(mytopo);
        		}
        		$('input[name="name"]').val('');
        		$('input[name="id"]').val('');
        	}
        },
        error: function (result, type) {
            if(mytopo){
    			mytopo.clear();
    		}else{
    			mytopo = new drawTopo(false,false,'select');
    			mytopo.initEvent(mytopo);
    		}
            $('input[name="name"]').val('');
            $('input[name="id"]').val('');
        }
    });
}

//清除
function clearline(){
	var id = $('input[name="id"]').val();
	if(id==null || id==''){
		$('input[name="name"]').val('');
		$('input[name="id"]').val('');
		if(mytopo){
			mytopo.clear();
		}
	}else{
		$.ajax({
	        type: 'POST',
	        url: '/topology_map_path/delete',
	        data:{
	        	id:id
	        },
	        success: function (result) {
	        	if (result.code == 0 ) {
	        		$('input[name="name"]').val('');
	        		$('input[name="id"]').val('');
	        		if(mytopo){
	        			mytopo.clear();
	        		}
	        	}else{
	        		layer.msg('清除失败！', {icon: 2});
	        	}
	        },
	        error: function (result, type) {
	        	layer.msg('清除失败！', {icon: 2});
	        }
	    });
	}
}
