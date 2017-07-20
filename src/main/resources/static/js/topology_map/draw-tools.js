// #FFFFFF to '255,255,255'
function toRGB(strRGB){
	if(!strRGB) return ;
  var sR="";
  var sG="";
  var sB="";
  var nR=0;
  var nG=0;
  var nB=0;
  if(strRGB.indexOf("#")==0){
    strRGB=strRGB.substring(1);
    if(!isNaN(parseInt(strRGB,16))){  //16进制转换成10进制
      if(strRGB.length==3){
        strRGBstrRGB=strRGB.toLowerCase()
        sR=strRGB.substring(0,1)+strRGB.substring(0,1);
        sG=strRGB.substring(1,2)+strRGB.substring(1,2);
        sB=strRGB.substring(2)+strRGB.substring(2);
      }
      else if(strRGB.length<=6){
        sR=strRGB.substring(0,2);
        sG=strRGB.substring(2,4);
        sB=strRGB.substring(4);
      }
      nR=parseInt(sR,16);
      nG=parseInt(sG,16);
      nB=parseInt(sB,16);
      return nR+","+nG+","+nB;
    }
    else{
      return "#"+strRGB;
    }
  }
  else
  {
    return strRGB;
  }
}