/**********************************************
/*
/* 根据名称取得元素的引用。
/*
/* Author: Alan Liu(刘新福)
/* Created on 2003-12-07
/**********************************************/
function getElementByName(name)
{
    var elements = document.getElementsByName(name);
    return elements[0];
}