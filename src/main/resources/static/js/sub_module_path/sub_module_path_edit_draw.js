var pathCanvas = new PathCanvas("pathCanvasID",true);

$('#clearBtn').bind('click',function(){
    pathCanvas.clear();
});
function drawImage() {
    pathCanvas.image = document.getElementById("LAY_demo_upload");
    pathCanvas.drawImage();
}






