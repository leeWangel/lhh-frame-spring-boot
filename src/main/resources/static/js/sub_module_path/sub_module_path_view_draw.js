
var pathCanvas = new PathCanvas("pathCanvasID",false);

function drawImage() {
    pathCanvas.image = document.getElementById("LAY_demo_upload");
    pathCanvas.drawImage();
}

window.requestAnimFrame = (function() {  
    return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||  
        function( /* function FrameRequestCallback */ callback, /* DOMElement Element */ element) {  
            return window.setTimeout(callback, 1000/6);  
        };  
})();  

var lastTime,deltaTime;  
var cantinue = true;

function initanim(){
	lastTime = Date.now();  
    gameloop();  
}

function cancelanim(){
	cantinue = false;
}

function gameloop(){  
	if(cantinue){
		window.requestAnimationFrame(gameloop1);  
		var now = Date.now();  
		deltaTime = now - lastTime;  
		lastTime = now;  
		pathCanvas.clear();
	}
} 

function gameloop1(){
	if(cantinue){
		window.requestAnimationFrame(gameloop);  
	    var now = Date.now();  
	    deltaTime = now - lastTime;  
	    lastTime = now;  
	    pathCanvas.load(obj);
	}
}



//initanim();
 

