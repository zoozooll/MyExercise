//省份的编码
var provinceCodes = "110000,120000,130000,140000,150000,210000,220000,230000,310000,320000,330000,340000,350000,360000,370000,410000,420000,430000,440000,450000,460000,500000,510000,520000,530000,540000,610000,620000,630000,640000,650000,710000,810000,820000";
var provinceNames = "北京市,天津市,河北省,山西省,内蒙古自治区,辽宁省,吉林省,黑龙江省,上海市,江苏省,浙江省,安徽省,福建省,江西省,山东省,河南省,湖北省,湖南省,广东省,广西壮族自治区,海南省,重庆市,四川省,贵州省,云南省,西藏自治区,陕西省,甘肃省,青海省,宁夏回族自治区,新疆维吾尔自治区,台湾省,香港特别行政区,澳门特别行政区";

/********************************************************
/*
/* 根据省份的编码取得省份的名称。
/* 如果省份的编码不存在返回空的字符串。
/*
/* Author: Alan Liu(刘新福)
/* Created on 2003-12-07
/*******************************************************/
function getProvinceNameByCode(code)
{
    var name = "";
    var provinceCodeList = provinceCodes.split(',');
    var provinceNameList = provinceNames.split(',');

    for(var i = 0; i < provinceCodeList.length; i++)
    {
        if(code == provinceCodeList[i])
        {
            name = provinceNameList[i];
            break;
        }
    }
    return name;
}

/***********************************************************************
/*
/* 初始化省份下拉列表。
/*
/* @province 要初始化的HTML页面的<select>元素。
/* @initValue 该<select>元素的初始显示的<option>。
/*
/* Author: Alan Liu(刘新福)
/* Created on 2003-12-07 /
**********************************************************************/
function initProvince(province, initValue)
{
	var provinceCodeList  = provinceCodes.split(',');
	var provinceNameList = provinceNames.split(',');

	for(var i = 0; i < provinceCodeList.length; i++)
	{
		var option = new Option ( provinceNameList[i],provinceCodeList[i]);
		province.options[province.options.length] = option;
		if(provinceCodeList[i] == initValue)
		{
			option.selected = true;
		}		
	}
}
