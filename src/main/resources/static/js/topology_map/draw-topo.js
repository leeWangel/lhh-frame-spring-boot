
var drawTopo = function(canPaint,istip,mode){
    var canvas = document.getElementById('canvas'); //舞台
    var stage = new JTopo.Stage(canvas);//场景
    //显示工具
    var scene = new JTopo.Scene(stage);
    scene.mode=mode;
    scene.areaSelect = false;

    var currentColor;//当前颜色
    var currentWidth;//当前宽度
    var mousePos;//鼠标位置
    var endnode;  // the last node
    var endnode2;   // the last two node
    var currentNode; //right button clicked selected
    var currentLink; //right button clicked selected
    var rootnode;

    var myTopo = {
        init:function(){
        	rootnode = null;
            currentColor = $('.color .selected').attr('showcolor');
            currentWidth = $('.line .selected').attr('showwidth');
            return this;
        },
        initEvent:function(self){
            scene.mouseup(function(event){
                if(event.button == 0){
                    if(canPaint){
                        self.addNode(mousePos.x , mousePos.y , currentColor);//addNode use the pic's pix
                    }
                }
            });
            scene.keydown(function(event){
                var keyID = event.keyCode ? event.keyCode :event.which;
                if(keyID === 39 || keyID === 68) { // right arrow and D 
                    if(canPaint == false){
                        canPaint = 1;
                    }
                    if(canPaint == 1 ){
                        if(rootnode == null){
                            rootnode = self.setRoot((mousePos.x - 5) , ( mousePos.y - 5),currentColor);
                        }else{
                            self.addNode((mousePos.x - 3), ( mousePos.y - 3) , currentColor);//addNode use the pic's pix
                        }
                        canPaint++;
                    }
                }  
            });
            scene.mousemove(function (event){
                mousePos = self.getPointOnCanvas(event.pageX, event.pageY);
                if (canPaint){
                    endnode.setLocation(mousePos.x - 3, mousePos.y - 3);
                } 
            });
            scene.keyup(function(event){
                var keyID = event.keyCode ? event.keyCode :event.which;
                if(keyID === 39 || keyID === 68) { // right arrow and D 
                    if(canPaint == 2) {
                        if (endnode.isRoot){
                            rootnode = null;
                        }
                        scene.remove(endnode);
                        endnode = endnode2;
                    }
                    canPaint = false;
                }
            });
        },
        initLines:function(lines){
            var self = this;
            if(lines.length>0){
                $.each(lines,function(index,line){
                    var color = toRGB(line.strokeStyle);
                    var width = line.lineWidth;
                    var points = JSON.parse(line.points);
                    var thisroot = self.setRoot(points[0].x, points[0].y,color);
                    if(istip){
                    	if(line.iserr){
                    		self.addTip(thisroot,'\n '+line.errText+' \n','255,0,0');
                    	}else{
                    		self.addTip(thisroot,'\n '+line.name+':'+'正常 \n','0,255,0');
                    	}
                    }
                    for (var n = 1; n < points.length ; n++) {
                        self.addNode(points[n].x, points[n].y, color,width);
                    }
                })
            }
        },
        updateColor:function(color){
            color = toRGB(color);
            var elements = scene.getDisplayedElements();
            $.each(elements,function(index,ele){
                ele.strokeColor=color;
                if(ele.elementType=='node'){
                    ele.fillColor=color;
                }
            });
        },
        updateWidth:function(width){
            var elements = scene.getDisplayedElements();
            $.each(elements,function(index,ele){
                if(ele.elementType=='link'){
                    ele.lineWidth=width;
                }
            });
        },
        clear:function(){
            scene.clear();
            rootnode = null;
        },
        setRoot:function(x, y,color){
            color = toRGB(color);
            var root = new JTopo.CircleNode();
            root.setLocation(x, y);
            root.setSize(10,10);
            root.fillColor = color;
            root.isRoot = true;
            scene.add(root);
            endnode = root;
            rootnode = root;
            return root;
        },
        addNode:function(x, y,color,width){
            if(!width){
                width = currentWidth;
            }
            color = toRGB(color);
            var node = new JTopo.Node();
            node.setSize(6, 6);
            node.setLocation(x, y);
            node.fillColor = color;
            scene.add(node);
            var link = new JTopo.Link(endnode,node);
            link.strokeColor = color;
            link.lineWidth = width;
            scene.add(link);
            //displayProp(endnode);
            endnode2 = endnode;
            endnode = node;
            return node;
        },
        getPointOnCanvas:function(x, y) { 
            var bbox = canvas.getBoundingClientRect(); 
            return { x: x - bbox.left * (canvas.width / bbox.width), 
            y: y - bbox.top * (canvas.height / bbox.height) + document.body.getClientRects()[0].top
            }; 
        },
        setCurrentColor:function(color){
            currentColor = color;
        },
        setCurrentWidth:function(width){
            currentWidth = width;
        },
        getPoints:function(){
            console.log('get in path');
            var path = [];
            var node = rootnode;
            // add nodes front the root
            while(node){
                path.unshift({'x':parseFloat(node.x).toFixed(2), 'y':parseFloat(node.y).toFixed(2)});
                if(node.inLinks.length){
                    node = node.inLinks[0].nodeA;
                } else{
                    break;
                }
            }
            node = rootnode;
            // add nodes after the root
            while(node){
                if(node.outLinks.length){
                    node = node.outLinks[0].nodeZ;
                }else{
                    break;
                }
                path.push({'x':parseFloat(node.x).toFixed(2), 'y':parseFloat(node.y).toFixed(2)});
            }
            console.log('path');
            console.log(JSON.stringify(path));
            //alert( JSON.stringify(path));
            return path;
        },
        addTip:function(node,text,color){
            node.alarm=text;
            node.paintAlarmText = function(a) {
                if (null != this.alarm && "" != this.alarm) {
                    var b = color,
                            c = this.alarmAlpha || .5;
                    a.beginPath(),
                            a.font = this.alarmFont || "10px 微软雅黑";
                    var textArray = this.alarm.split('\n');
                    var rowCnt = textArray.length;
                    var i = 0,imax  = rowCnt,maxLength = 0;maxText = textArray[0];
                    for(;i<imax;i++){
                        var nowText = textArray[i],textLength = nowText.length;
                        if(textLength >=maxLength){
                            maxLength = textLength;
                            maxText = nowText;
                        }
                    }
                    var maxWidth = a.measureText(maxText).width;
                    var lineHeight = a.measureText("元").width;
                    //算出alarm的最大的宽度
                    var d =((a.measureText(this.alarm).width/rowCnt +6)>maxWidth?(a.measureText(this.alarm).width/rowCnt +6):maxWidth);
                    var e = a.measureText("田").width ,
                            f = this.width / 2 - d / 2,
                            g = (-this.height / 2 - e*rowCnt ) -8;
                            e=(e)*rowCnt;
                    //绘制alarm的边框
                    a.strokeStyle = "rgba(" + b + ", " + c + ")",
                            a.fillStyle = "rgba(" + b + ", " + c + ")",
                            a.lineCap = "round",
                            a.lineWidth = 1,
                            a.moveTo(f, g),
                            a.lineTo(f + d, g),
                            a.lineTo(f + d, g + e),
                            a.lineTo(f + d / 2 + 6, g + e),
                            a.lineTo(f + d / 2, g + e + 8),
                            a.lineTo(f + d / 2 - 6, g + e),
                            a.lineTo(f, g + e),
                            a.lineTo(f, g),
                            a.fill(),
                            a.stroke(),
                            a.closePath(),
                            a.beginPath(),
                            a.strokeStyle = "rgba(" + this.fontColor + ", " + this.alpha + ")",
                            a.fillStyle = "rgba(" + this.fontColor + ", " + this.alpha + ")",
                            (function(a,b,x,y,textArray){
                                for(var j= 0;j<textArray.length;j++){
                                    var words = textArray[j];
                                    a.fillText(words,x,y);
                                    y+= lineHeight;
                                }
                            })(a,this,f,g+8,textArray),
                            a.closePath()
                }
            }
        }
    }
    return myTopo.init();
}