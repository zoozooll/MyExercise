var total;//定义flash影片总桢数
var frame_number;//定义flash影片当前桢数
//以下是滚动条图片拖动程序
var dragapproved=false;
var z,x,y

//动态显示播放影片的当前桢/总桢数(进度条显示)
function showcount(){
    //已测可用ChangeProgressMethod.consoleFlashProgress(5);
    total = movie.TotalFrames;
 	frame_number=movie.CurrentFrame();
    frame_number++;
    var progressSize = 100*(frame_number/movie.TotalFrames());
    ChangeProgressMethod.consoleFlashProgress(progressSize);
}

//播放影片 
function Play(){
 	movie.Play();
}
//暂停播放
function Pause(){
 movie.StopPlay();
}

//开始载入flash影片
function loadSWF(fsrc,fwidth,fheight){
 movie.LoadMovie(0, fsrc);
 movie.width=fwidth;
 movie.height=fheight;
 frame_number=movie.CurrentFrame();
}