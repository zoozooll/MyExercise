//ʡ�ݵı���
var provinceCodes = "110000,120000,130000,140000,150000,210000,220000,230000,310000,320000,330000,340000,350000,360000,370000,410000,420000,430000,440000,450000,460000,500000,510000,520000,530000,540000,610000,620000,630000,640000,650000,710000,810000,820000";
var provinceNames = "������,�����,�ӱ�ʡ,ɽ��ʡ,���ɹ�������,����ʡ,����ʡ,������ʡ,�Ϻ���,����ʡ,�㽭ʡ,����ʡ,����ʡ,����ʡ,ɽ��ʡ,����ʡ,����ʡ,����ʡ,�㶫ʡ,����׳��������,����ʡ,������,�Ĵ�ʡ,����ʡ,����ʡ,����������,����ʡ,����ʡ,�ຣʡ,���Ļ���������,�½�ά���������,̨��ʡ,����ر�������,�����ر�������";

/********************************************************
/*
/* ����ʡ�ݵı���ȡ��ʡ�ݵ����ơ�
/* ���ʡ�ݵı��벻���ڷ��ؿյ��ַ�����
/*
/* Author: Alan Liu(���¸�)
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
/* ��ʼ��ʡ�������б�
/*
/* @province Ҫ��ʼ����HTMLҳ���<select>Ԫ�ء�
/* @initValue ��<select>Ԫ�صĳ�ʼ��ʾ��<option>��
/*
/* Author: Alan Liu(���¸�)
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
