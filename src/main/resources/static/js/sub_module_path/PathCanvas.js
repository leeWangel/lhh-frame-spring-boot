var PathCanvas = function(canvasId,initEvent){
    var endPoint = null;
    //虚拟点(拖动时用到)
    var virtualPoint = null;
    //画
    function draw(mapCanvas, point) {
        var ctx = mapCanvas.ctx;
        var points = mapCanvas.points;
        ctx.clearRect(0,0,mapCanvas.canvas.width,mapCanvas.canvas.height);
        mapCanvas.drawImage();
        //开始一个新的绘制路径
        ctx.beginPath();
        ctx.lineWidth = mapCanvas.lineWidth;
        ctx.strokeStyle = mapCanvas.strokeStyle;
        if(points && points.length>1) {
            for (var i =0;i<points.length;i++) {
                var j = i+1;
                if(j==points.length) break;
                ctx.moveTo(points[i].x,points[i].y);
                ctx.lineTo(points[j].x,points[j].y);
                ctx.stroke();
            }
        }
        if(point) {
            ctx.moveTo(endPoint.x,endPoint.y);
            ctx.lineTo(point.x,point.y);
            ctx.stroke();
        }
        //关闭当前的绘制路径
        ctx.closePath();
    }
    function isInRange(points, point) {
        if(!points || points.length==0 || !point) return false;
        var minX = 0;
        var minY = 0;
        var maxX = 0;
        var maxY = 0;
        for(var i = 0; i<points.length; i++) {
            var p = points[i];
            if(minX==0 || minX>p.x) minX = p.x;
            if(maxX==0 || maxX<p.x) maxX = p.x;
            if(minY==0 || minY>p.y) minY = p.y;
            if(maxY==0 || maxY<p.y) maxY = p.y;
        }
        return point.x<=maxX && point.x>=minX && point.y<=maxY && point.y>=minY;
    }
    //拖动
    function drag(points, currentPoint, pathCanvas) {
        if(!points || points.length==0 || !currentPoint) return false;
        var canvasWidth = pathCanvas.canvas.width;
        var canvasHeight = pathCanvas.canvas.height;
        //TODO 因背景图有可能更换，会影响画布的大小，先注释
        // for(var i = 0; i<points.length; i++) {
        //     var p = points[i];
        //     var x = p.x + currentPoint.x-virtualPoint.x;
        //     var y = p.y + currentPoint.y-virtualPoint.y;
        //     if(x>canvasWidth || y>canvasHeight || x<0 || y<0) {
        //         virtualPoint = currentPoint;
        //         return false;
        //     }
        // }
        for(var i = 0; i<points.length; i++) {
            var p = points[i];
            p.x += currentPoint.x-virtualPoint.x;
            p.y += currentPoint.y-virtualPoint.y;
        }
        virtualPoint = currentPoint;
        return true;
    }
    
    var myMapCanvas = {
        init:function(canvasId,initEvent) {
            var canvas = document.getElementById(canvasId);
            if(!canvas.getContext) {
                throw new Error("浏览器不支持canvas画布");
            }
            this.setWidth(canvas.width);
            this.setHeight(canvas.height);
            this.lineWidth = 1;
            this.strokeStyle = 'rgb(0,0,0)';
            this.penActive = false;
            this.ctx = canvas.getContext('2d');
            this.canvas = canvas;
            this.points = [];
            if(initEvent){
            	this.event();
            }
            this.image = null;
            return this;
        },
        setWidth:function(width){
            if(!width || width==0) return;
            canvasWidth = width;
        },
        setHeight:function(height){
            if(!height || height==0) return;
            canvasHeight = height;
        },
        onKeyDown:function(e) {
            var keyID = e.keyCode ? e.keyCode :e.which;
            if(keyID === 68) this.penActive = true;
        },
        onKeyUp:function(e) {
            var keyID = e.keyCode ? e.keyCode :e.which;
            if(keyID === 68) this.penActive = false;
        },
        onMouseDown:function(e){
            var p = {x:e.offsetX, y:e.offsetY};

            //TODO 因背景图有可能更换，会影响画布的大小，先注释
            // if(!isInRange(this.points, p)) return;
            virtualPoint = p;
        },
        onMouseUp:function(e) {
            virtualPoint = null;
        },
        onClick:function(e){
            if(!this.penActive) return;
            endPoint = {x:e.offsetX, y:e.offsetY};
            this.points.push(endPoint);
        },
        onContextMenu:function(e){
            e.preventDefault();
            if(!this.penActive) return;
            endPoint = null;
            draw(this, null);
        },
        onMouseMove:function(e) {
            var p = {x:e.offsetX, y:e.offsetY};
            if(this.penActive) {
                if(endPoint) draw(this, p);
            } else {
                if(!virtualPoint) return;
                var isInRange = drag(this.points, p, this);
                if(!isInRange) return;
                draw(this, null);
            }
        },
        event:function(){
            this.canvas.addEventListener("mousedown",this.onMouseDown.bind(this),false);
            this.canvas.addEventListener("mouseup",this.onMouseUp.bind(this),false);
            this.canvas.addEventListener("click",this.onClick.bind(this),false);
            this.canvas.addEventListener("contextmenu",this.onContextMenu.bind(this),false);
            this.canvas.addEventListener("mousemove",this.onMouseMove.bind(this),false);
            window.addEventListener('keydown', this.onKeyDown.bind(this), true);
            window.addEventListener('keyup', this.onKeyUp.bind(this), true);
        },
        clear:function(){
            endPoint = null;
            this.points.splice(0, this.points.length);
            this.ctx.clearRect(0,0,this.canvas.width,this.canvas.height);
            this.drawImage();
        },
        drawImage:function(){
            if(this.image!=null) this.ctx.drawImage(this.image,0,0,this.canvas.width,this.canvas.height);
        },
        load:function(obj){
            this.lineWidth = obj.lineWidth;
            this.strokeStyle = obj.strokeStyle;
            this.points = JSON.parse(obj.points);
            if(!this.points) this.points = [];
            draw(this, null);
        }
    };
    return myMapCanvas.init(canvasId,initEvent);
}
