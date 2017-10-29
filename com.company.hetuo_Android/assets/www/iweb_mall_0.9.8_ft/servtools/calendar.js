function PopupCalendar(InstanceName)
{
    this.instanceName=InstanceName;
    this.separator="-"
    this.oBtnTodayTitle="Today"
    this.oBtnCancelTitle="Cancel"
    this.oBtnCleanTitle="Clean"
    this.weekDaySting=new Array("S","M","T","W","T","F","S");
    this.monthSting=new Array("January","February","March","April","May","June","July","August","September","October","November","December");
    this.Width=200;
    this.currDate=new Date();
    this.today=new Date();
    this.startYear=2009;
    this.endYear=2050;
    this.divBorderCss="1px solid #BCD0DE";
    this.tableBorderColor="#CCCCCC"
    this.Init=CalendarInit;
    this.Fill=CalendarFill;
    this.Refresh=CalendarRefresh;
    this.Restore=CalendarRestore;
    this.oTaget=null;
    this.oPreviousCell=null;
    this.sDIVID=InstanceName+"oDiv";
    this.sTABLEID=InstanceName+"oTable";
    this.sMONTHID=InstanceName+"oMonth";
    this.sYEARID=InstanceName+"oYear";
    
}
function CalendarInit()             ///Create panel
{
    var sMonth,sYear
    sMonth=this.currDate.getMonth();
    sYear=this.currDate.getYear();
    if ( document.all){
       htmlAll="<div id='"+this.sDIVID+"' style='display:none;position:absolute;margin-top:10px;width:130px;border:"+this.divBorderCss+";padding:1px;background-color:#FFFFFF;z-index:100';>";
    }else{
       htmlAll="<div id='"+this.sDIVID+"' style='display:none;position:absolute;margin-top:10px;width:130px;border:"+this.divBorderCss+";padding:1px;background-color:#FFFFFF;z-index:100';>";
    }

    htmloMonth="<select id='"+this.sMONTHID+"' onchange=CalendarMonthChange("+this.instanceName+") style='width:60px;background-color:#D0F5FF;'>";
    for(i=0;i<12;i++)
    {           
        htmloMonth+="<option value='"+i+"'>"+this.monthSting[i]+"</option>";
    }
    htmloMonth+="</select>";
    /// Year
    htmloYear="<select id='"+this.sYEARID+"' onchange=CalendarYearChange("+this.instanceName+") style='width:60px;background-color:#D0F5FF;'>";
    for(i=this.startYear;i<=this.endYear;i++)
    {
        htmloYear+="<option value='"+i+"'>"+i+"</option>";
    }
    htmloYear+="</select>";
    /// Day
    htmloDayTable="<table id='"+this.sTABLEID+"' style='z-index:100' width='130' border=0 cellpadding=0 cellspacing=1 bgcolor='"+this.tableBorderColor+"'>";
    htmloDayTable+="<tbody bgcolor='#ffffff' style='font-size:10px;'>";
    for(i=0;i<=6;i++)
    {
        if(i==0)
            htmloDayTable+="<tr bgcolor='#98B8CD'>";
        else
            htmloDayTable+="<tr>";
        for(j=0;j<7;j++)
        {
            if(i==0)
            {
                htmloDayTable+="<td height='14' align='center' valign='middle' style='cursor:pointer;font-size:12px;'>";
                htmloDayTable+=this.weekDaySting[j]+"</td>"
            }
            else
            {
                htmloDayTable+="<td height='14' align='center' valign='middle' style='cursor:pointer;font-size:12px;'";
                htmloDayTable+=" onmouseover=CalendarCellsMsOver("+this.instanceName+")";
                htmloDayTable+=" onmouseout=CalendarCellsMsOut("+this.instanceName+")";
                htmloDayTable+=" onclick=CalendarCellsClick(this,"+this.instanceName+")>";
                htmloDayTable+="&nbsp;</td>"
            }
        }
        htmloDayTable+="</tr>"; 
    }
    htmloDayTable+="</tbody></table>";
    /// Today Button
    htmloButton="<div align='center' style='padding:1px'>"
    htmloButton+="<button style='width:35px;height:16px;border:1px solid #BCD0DE;background-color:#eeeeee;cursor:pointer;font-size:12px;'"
    htmloButton+=" onclick=CalendarTodayClick("+this.instanceName+")>"+this.oBtnTodayTitle+"</button>&nbsp;"
    htmloButton+="<button style='width:35px;height:16px;border:1px solid #BCD0DE;background-color:#eeeeee;cursor:pointer;font-size:12px;'"
    htmloButton+="onclick=clearInput("+this.instanceName+")>"+this.oBtnCleanTitle+"</button>&nbsp;"
    htmloButton+="<button style='width:35px;height:16px;border:1px solid #BCD0DE;background-color:#eeeeee;cursor:pointer;font-size:12px;'"
    htmloButton+=" onclick=CalendarCancel("+this.instanceName+")>"+this.oBtnCancelTitle+"</button> "
    htmloButton+="</div>"
    /// All
    htmlAll=htmlAll+htmloYear+htmloMonth+htmloDayTable+htmloButton+"</div>";
    document.write(htmlAll);
    this.Fill();    
}
function CalendarFill()         ///
{
    var sMonth,sYear,sWeekDay,sToday,oTable,currRow,MaxDay,sDaySn,sIndex,rowIndex,cellIndex,oSelectMonth,oSelectYear
    sMonth=this.currDate.getMonth();
    //dt = new Date();
    //alert(dt);
    sYear=this.currDate.getFullYear();
    sWeekDay=(new Date(sYear,sMonth,1)).getDay();
    sToday=this.currDate.getDate();
    //oTable=document.all[this.sTABLEID];
    oTable = document.getElementById(this.sTABLEID);
    currRow=oTable.rows[1];
    MaxDay=CalendarGetMaxDay(sYear,sMonth);
    
    //oSelectMonth=document.all[this.sMONTHID]
    oSelectMonth = document.getElementById(this.sMONTHID);
    oSelectMonth.selectedIndex=sMonth;
    //oSelectYear=document.all[this.sYEARID]
    oSelectYear = document.getElementById(this.sYEARID);
    //alert(oSelectYear.options[0].value);
    
    for(i=0;i<oSelectYear.length;i++)
    {
        if(parseInt(oSelectYear.options[i].value)==sYear)oSelectYear.selectedIndex=i;
    }
    ////
    for(sDaySn=1,sIndex=sWeekDay;sIndex<=6;sDaySn++,sIndex++)
    {

        if(sDaySn==sToday)
        {
            currRow.cells[sIndex].innerHTML="<font color=red><i><b>"+sDaySn+"</b></i></font>";
            this.oPreviousCell=currRow.cells[sIndex];
        }
        else
        {
            currRow.cells[sIndex].innerHTML=sDaySn;
            currRow.cells[sIndex].style.color="#666666";    
        }
        CalendarCellSetCss(0,currRow.cells[sIndex]);
    }
    for(rowIndex=2;rowIndex<=6;rowIndex++)
    {
        if(sDaySn>MaxDay)break;
        currRow=oTable.rows[rowIndex];
        for(cellIndex=0;cellIndex<currRow.cells.length;cellIndex++)
        {
            if(sDaySn==sToday)
            {
                currRow.cells[cellIndex].innerHTML="<font color=red><i><b>"+sDaySn+"</b></i></font>";
                this.oPreviousCell=currRow.cells[cellIndex];
            }
            else
            {
                currRow.cells[cellIndex].innerHTML=sDaySn; 
                currRow.cells[cellIndex].style.color="#666666"; 
            }
            CalendarCellSetCss(0,currRow.cells[cellIndex]);
            sDaySn++;
            if(sDaySn>MaxDay)break; 
        }
    }
}
function CalendarRestore()                  /// Clear Data
{   
    var oTable

    oTable = document.getElementById(this.sTABLEID);
    for(i=1;i<oTable.rows.length;i++)
    {
        for(j=0;j<oTable.rows[i].cells.length;j++)
        {
            CalendarCellSetCss(0,oTable.rows[i].cells[j]);
            oTable.rows[i].cells[j].innerHTML="&nbsp;";
        }
    }   
}
function CalendarRefresh(newDate)                   ///
{
    this.currDate=newDate;
    this.Restore(); 
    this.Fill();    
}
function CalendarCellsMsOver(oInstance)             /// Cell MouseOver
{
    var myCell
    myCell=event.srcElement;                        //????
    CalendarCellSetCss(0,oInstance.oPreviousCell);
    if(myCell)
    {
        CalendarCellSetCss(1,myCell);
        oInstance.oPreviousCell=myCell;
    }
}
function CalendarCellsMsOut(oInstance)              ////// Cell MouseOut
{
    var myCell
    myCell=event.srcElement;                        //??????
    CalendarCellSetCss(0,myCell);   
}
function CalendarCellsClick(oCell,oInstance)
{
    //alert(oCell.innerText);
    var sDay,sMonth,sYear,newDate
    sYear=oInstance.currDate.getFullYear();
    //alert(sYear);
    //alert('dddddddddddddd');
    sMonth=oInstance.currDate.getMonth();
    //alert(sMonth);
    sDay=oInstance.currDate.getDate();
    //alert('|'+ oCell.innerHTML + '|');
    
    if (document.all){
       txt = oCell.innerText;
    }else{
       txt = oCell.innerHTML;
       if ( txt == '&nbsp;' ){
    txt = " ";
    }
    else if(txt.indexOf('<b>') != -1){
        txt = txt.substring(txt.indexOf('<b>')+3,txt.length);
        txt = txt.substring(0,txt.indexOf('</b>'));
        }
    }
    if(txt != " ")
    {
        sDay=txt;//parseInt(oCell.innerText);
        if(sDay!=oInstance.currDate.getDate())
        {
            newDate=new Date(sYear,sMonth,sDay);
            oInstance.Refresh(newDate);
        }
    }
    sDateString=sYear+oInstance.separator+CalendarDblNum(sMonth+1)+oInstance.separator+CalendarDblNum(sDay);        ///return sDateString
    //alert(sDateString);
    if(oInstance.oTaget.tagName=="INPUT")
    {
        oInstance.oTaget.value=sDateString;
    }
    //document.all[oInstance.sDIVID].style.display="none";        
    document.getElementById(oInstance.sDIVID).style.display="none";
}

function clearInput(oInstance){
if(oInstance.oTaget.tagName=="INPUT")
    {
        oInstance.oTaget.value="";
    }
    CalendarCancel(oInstance);
}


function CalendarYearChange(oInstance)              /// Year Change
{
    var sDay,sMonth,sYear,newDate
    sDay=oInstance.currDate.getDate();
    sMonth=oInstance.currDate.getMonth();
    //sYear=document.all[oInstance.sYEARID].value
    sYear = document.getElementById(oInstance.sYEARID).value
    newDate=new Date(sYear,sMonth,sDay);
    oInstance.Refresh(newDate);
}
function CalendarMonthChange(oInstance)             /// Month Change
{
    var sDay,sMonth,sYear,newDate
    sDay=oInstance.currDate.getDate();
    //sMonth=document.all[oInstance.sMONTHID].value
    sMonth=document.getElementById(oInstance.sMONTHID).value
    sYear=oInstance.currDate.getYear();
    newDate=new Date(sYear,sMonth,sDay);
    oInstance.Refresh(newDate); 
}
function CalendarTodayClick(oInstance)              /// "Today" button Change
{   
    oInstance.Refresh(new Date());      
}
function getDateString(oInputSrc,oInstance)
{
    if(oInputSrc&&oInstance) 
    {
       // CalendarDiv=document.all[oInstance.sDIVID];
        CalendarDiv = document.getElementById(oInstance.sDIVID);
        oInstance.oTaget=oInputSrc;
        if(document.all){
          //CalendarDiv.style.pixelLeft=CalendargetPos(oInputSrc,"Left")+90+oInputSrc.offsetWidth;
          //CalendarDiv.style.pixelTop=CalendargetPos(oInputSrc,"Top")-25+oInputSrc.offsetHeight;
          CalendarDiv.style.pixelLeft=CalendargetPos(oInputSrc,"Left");
          CalendarDiv.style.pixelTop=CalendargetPos(oInputSrc,"Top") + oInputSrc.offsetHeight;
        }else{
          //CalendarDiv.style.left=CalendargetPos(oInputSrc,"Left") - 134+oInputSrc.offsetWidth + "px";
          CalendarDiv.style.left=CalendargetPos(oInputSrc,"Left") + "px";
          CalendarDiv.style.top=CalendargetPos(oInputSrc,"Top") + oInputSrc.offsetHeight + "px";
        }
        CalendarDiv.style.display=(CalendarDiv.style.display=="none")?"":"none";
    }   
}
function CalendarCellSetCss(sMode,oCell)            /// Set Cell Css
{
    // sMode
    // 0: OnMouserOut 1: OnMouseOver 
    if(sMode)
    {
        oCell.style.border="1px solid #5589AA";
        oCell.style.backgroundColor="#BCD0DE";
    }
    else
    {
        oCell.style.border="1px solid #FFFFFF";
        oCell.style.backgroundColor="#FFFFFF";
    }   
}
function CalendarGetMaxDay(nowYear,nowMonth)            /// Get MaxDay of current month
{
    var nextMonth,nextYear,currDate,nextDate,theMaxDay
    nextMonth=nowMonth+1;
    if(nextMonth>11)
    {
        nextYear=nowYear+1;
        nextMonth=0;
    }
    else    
    {
        nextYear=nowYear;   
    }
    currDate=new Date(nowYear,nowMonth,1);
    nextDate=new Date(nextYear,nextMonth,1);
    theMaxDay=(nextDate-currDate)/(24*60*60*1000);
    return theMaxDay;
}
function CalendargetPos(el,ePro)                /// Get Absolute Position
{
    var ePos=0;
    while(el!=null)
    {       
        ePos+=el["offset"+ePro];
        el=el.offsetParent;
    }
    return ePos;
}
function CalendarDblNum(num)
{
    if(num<10) 
        return "0"+num;
    else
        return num;
}
function CalendarCancel(oInstance)          ///Cancel
{
    CalendarDiv = document.getElementById(oInstance.sDIVID);
    CalendarDiv.style.display="none";       
}

var oCalendarChs=new PopupCalendar("oCalendarChs");
oCalendarChs.weekDaySting=new Array("\u65e5","\u4e00","\u4e8c","\u4e09","\u56db","\u4e94","\u516d");
oCalendarChs.monthSting=new Array("\u4e00\u6708","\u4e8c\u6708","\u4e09\u6708","\u56db\u6708","\u4e94\u6708","\u516d\u6708","\u4e03\u6708","\u516b\u6708","\u4e5d\u6708","\u5341\u6708","\u5341\u4e00\u6708","\u5341\u4e8c\u6708");
oCalendarChs.oBtnTodayTitle="\u4eca\u5929";
oCalendarChs.oBtnCancelTitle="\u53d6\u6d88";
oCalendarChs.oBtnCleanTitle="\u6e05\u7a7a";
oCalendarChs.Init();
