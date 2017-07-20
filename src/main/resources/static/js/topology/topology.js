
function Topology(){
    this.config = {
        lineColorMap: {
            down: 'red',
            normal: 'green',
            high: 'yellow'
        }
    };
    this.tipInfo = {
        type:'',//line or node
        data:{}
    }
}

Topology.prototype.draw = function(){
    var self = this;
    self.initForce();//初始布局
    self.createElem();//创建元素
    Topology.bindEvent.apply(self);//事件绑定
};

Topology.prototype.initForce = function(){
    var self = this;
    //Initialize a default force layout, using the nodes and edges in dataset
    self.force = d3.layout.force()
        .nodes(dataset.nodes)
        .links(dataset.paths)
        .size([w, h])
        .linkDistance(function(){
            return 300;
        })
        .charge([-100])
        .on("tick",tick)
        .on("end",tickEnd)
        .start();

    this.drag = this.force.drag()
        .on("dragstart",function(d){
            d3.event.sourceEvent.stopPropagation();
        })
        .on("drag",function(d){
            //self.stopTick(5);
        })
        .on("dragend",function(d){
            updateNodePos(d);
        })

    function tick(){
        self.link.selectAll("line")
            .each(function(d){
                var elem = d3.select(this),
                    offset = self.getOffset(d,elem,6);
                elem
                    .attr("x1", function(d,i) {
                        return d.source.x + offset.offsetX;
                    })
                    .attr("y1", function(d,i) {
                        return d.source.y + offset.offsetY;
                    })
                    .attr("x2", function(d,i) {
                        return d.target.x + offset.offsetX;
                    })
                    .attr("y2", function(d,i) {
                        return d.target.y + offset.offsetY;
                    });
            })

        self.node
            .attr("transform", function(d) { 
            	return "translate(" + d.x + "," + d.y + ")"; 
            });

        if(isFirstLoad){
            isFirstLoad = false;
            self.refresh(self);
        }

    }
//
//	d3.select(".container")
//	.attr("transform","translate(" + chattrax + "," + chattray + ")"+
//		"scale("+chatscale+")");

    function tickEnd(){
//		if(isFirstLoad){
//			isFirstLoad = false;
//			self.refresh(self);
//		}
    }
}

Topology.prototype.createElem = function(){
    var self = this;
    //Create SVG element
    var svg = d3.select("#topology_container")
        .append("svg")
        .attr("id","network-topology")
        .attr("width", w)
        .attr("height", h);
//	.call(this.zoom);

    var container = svg.append("g")
        .attr("class","container");
    self.linkContainer = container.append("g")
        .attr("id","line_container");

    self.nodeContainer = container.append("g")
        .attr("id","node_container");

    self.link = self.linkContainer.selectAll(".link")
        .data(dataset.paths)
        .enter()
        .append("g")
        .attr("class","link")
        .each(function(d){
            self.addLines(d3.select(this),d);
        });

    self.node = self.nodeContainer.selectAll(".node")
        .data(dataset.nodes)
        .enter()
        .append("g")
        .attr("class", "node")
        .call(this.drag);

    self.addNodeChildren(self.node);
}

Topology.prototype.getLinePos = function(index){
    var direction,num;
    if(index%2 == 1){
        direction = 'up';
    }else{
        direction = 'down';
    }
    num = Math.floor((index + 1) / 2);
    return direction + "_" + num;
};

//计算线相对于中心线的偏移量(x坐标偏移量和y坐标偏移量)
Topology.prototype.getOffset = function(d,elem,space){
    var linePos = elem.attr("data-pos"),
        x1 = d.source.x,
        y1 = d.source.y,
        x2 = d.target.x,
        y2 = d.target.y,
        x,y,posArr,stepOffsetX,stepOffsetY,offsetX,offsetY;

    x = Math.abs(x2 - x1);
    y = Math.abs(y2 - y1);
    stepOffsetX = Math.ceil(space * y / Math.sqrt(Math.pow(x,2) + Math.pow(y,2)));
    stepOffsetY = Math.ceil(space * x / Math.sqrt(Math.pow(x,2) + Math.pow(y,2)));

    posArr = linePos.split("_");
    offsetX = parseInt(posArr[1]) * stepOffsetX;
    offsetY = parseInt(posArr[1]) * stepOffsetY;

    if((x2 - x1) * (y2 - y1) > 0){
        if(posArr[0] == 'up'){
            offsetX = -offsetX;
        }else{
            offsetY = -offsetY;
        }
    }else{
        if(posArr[0] == 'up'){
            offsetX = -offsetX;
            offsetY = -offsetY;
        }
    }

    return {offsetX:offsetX,offsetY:offsetY};
};


//给路径添加一条或多条线
Topology.prototype.addLines = function(pathG,d){
    var position,line;

    for(var i = 0;i < d.lines.length;i++){
        position = this.getLinePos(i);

        line = pathG.append("line")
            .attr("stroke",this.config.lineColorMap[d.lines[i].traffic_status])
            .attr("data-pos",position+'_'+i);
    }
};

//节点添加内容
Topology.prototype.addNodeChildren = function(node){
    var self = this;

    node.append("image")
        .attr("class","node-icon")
        .attr("transform","translate("+-26+","+-26+")")
        .each(function(d){
            self.setImage(d3.select(this),d.type,d.info);
        });

    node.append("text")
        .each(function(d){
            if(d.type=="collection"){
                d3.select(this).attr("transform","translate("+-20+","+42+")");
            }else{
                d3.select(this).attr("transform","translate("+-20+","+34+")");
            }
        })
        .text(function(d){
            return d.name;
        })
}

Topology.prototype.addAlarm = function(subId){
	var self = this;
	self.node.each(function(n,index){
		if(subId==n.node_id){
			d3.select("#node_container .node:nth-child("+(index+1)+")").attr('class','node alarmnode');
		}
	});
}

Topology.prototype.cancelAlarm = function(){
	d3.select("#node_container .alarmnode").attr('class','node');
}

//添加节点图标
Topology.prototype.setImage = function(elem,type,info){
	if(info.id=='0'){//云主控
		elem
        .attr("xlink:href","images/cloud.png")
        .attr("width",50)
        .attr("height",50);
		return;
	}
    switch(type){
        case "switchDevice":
            elem
                .attr("xlink:href","images/Control.png")
                .attr("width",50)
                .attr("height",50);
            break;
        case "collection":
        	var imgName = typemap[info.subModuleType.id];
            elem
                .attr("xlink:href","/attachment/image?module=sub_module_type_img&fileName="+imgName)
                .attr("width",50)
                .attr("height",50);
    }
};


Topology.prototype.stopTick = function(n){
    for(var i = 0;i < n;++i){
        this.force.tick();
    }
    this.force.stop();
};

//编辑信息
Topology.prototype.editTipInfo = function(jqElem){
    var text = jqElem.val(),
        label = jqElem.prev().prev().text(),
        label = label.substring(0,label.length - 1),

        orgParam = {};

    orgParam[label] = text;

    orgParam.type = self.tipInfo.type;

    for(var key in self.tipInfo.data){
        if(key != label){
            if(key == "ip" || key == "hostname"){
                orgParam[key] = self.tipInfo.data[key];
            }
        }
    }
}

//更新拓扑图数据
Topology.prototype.refresh = function(self){

    updateTopo();
//    d3.select(".container")
//        .attr("transform","translate(" + chattrax + "," + chattray + ")"+
//            "scale("+chatscale+")");
    function updateTopo(){
        //self.getAlarm();
        self.force
            .links(dataset.paths)
            .nodes(dataset.nodes);

        self.link.remove();

        self.link = self.linkContainer.selectAll(".link")
            .data(dataset.paths)
            .enter()
            .append("g")
            .attr("class","link")
            .each(function(d){
                self.addLines(d3.select(this),d);
            });


        self.node.remove();
        self.node = self.nodeContainer.selectAll(".node")
            .data(dataset.nodes)
            .enter()
            .append("g")
            .attr("class", "node")
            .call(self.drag);

        self.addNodeChildren(self.node);

        self.force.start();
    }
}

//事件绑定
Topology.bindEvent = function(){
    var selfColor,elem,lineData,linePos,nodeInfo
    self = this;

    d3.select("#node_container")
        .on("click",function(){
            if(d3.event.defaultPrevented) return;
            d3.event.stopPropagation();

            var point = {
                x:d3.event.pageX,
                y:d3.event.pageY
            }
            if(d3.select(d3.event.target).datum().type=='collection'){//子控
                editNode(d3.select(d3.event.target).datum().info);
            }else{//主控
            	editMaster(d3.select(d3.event.target).datum().info);
            }
        });

    $("#network-topology").click(function(){
//		Topology.hideTip();
    })

    $("#addnode").click(function(){
        createNewNode();
    });
}

var topology = new Topology();
//topology.draw();
