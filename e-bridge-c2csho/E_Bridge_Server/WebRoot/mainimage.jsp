<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <!--大图轮换区-->
					<div id="topstory">
						<div id=highlight>
							<div id=featured>
								<!--标签开始 -->
								<div class=image id=image_xixi-01>
									<a title=08广告创意风暴（十） href="http://www.d500.com.cn/"
										target=_blank> <img alt=08广告创意风暴（十）
											src="images/47492_253130.jpg" /> </a>
									<div class=word>
										<h3>
											08广告创意风暴（十）
										</h3>
										<p>
											视觉中国网站编辑收集整理08年最新的广告创意，以连载的形式的呈现的大家的
										</p>
									</div>
								</div>
								<!--标签结束 -->
								<div class=image id=image_xixi-02>
									<a class=open title=13家经典“大牌”的logo进化
										href="http://www.d500.com.cn/" target=_blank><img
											class=full alt=13家经典“大牌”的logo进化 src="images/47426_252416.jpg" />
									</a>
									<div class=word>
										<h3>
											13家经典“大牌”的logo进化
										</h3>
										<p>
											看看这些颇有影响力的品牌早期使用的logo，是不是嗅到时代的气息了呢？
										</p>
									</div>
								</div>
								<div class=image id=image_xixi-03>
									<a class=open title="视觉专访：悉尼奥运会首席设计顾问Michael Bryce"
										href="http://www.d500.com.cn/" target=_blank><img
											class=full alt="视觉专访：悉尼奥运会首席设计顾问Michael Bryce"
											src="images/47526_253407.jpg" /> </a>
									<div class=word>
										<h3>
											视觉专访：悉尼奥运会首席设计顾问Michael Bryce
										</h3>
										<p>
											Michael Bryce是全球著名的澳大利亚设计师，他在从建筑设计
										</p>
									</div>
								</div>
								<div class=image id=image_xixi-04>
									<a class=open title=adidas“没有不可能”(四)
										href="http://www.d500.com.cn/" target=_blank><img
											class=full alt=adidas“没有不可能”(四) src="images/47469_252798.jpg" />
									</a>
									<div class=word>
										<h3>
											adidas“没有不可能”(四)
										</h3>
										<p>
											运动是一种游戏，一种积极的释放，更是一种生活态度。在运动中解放自己的心灵
										</p>
									</div>
								</div>
								<div class=image id=image_xixi-05>
									<a class=open title=adidas“没有不可能”(三)
										href="http://www.d500.com.cn/" target=_blank><img
											class=full alt=adidas“没有不可能”(三) src="images/47468_252784.jpg" />
									</a>
									<div class=word>
										<h3>
											adidas“没有不可能”(三)
										</h3>
										<p>
											运动是一种游戏，一种积极的释放，更是一种生活态度。在运动中解放自己的心灵
										</p>
									</div>
								</div>
								<div class=image id=image_xixi-06>
									<a class=open title=adidas“没有不可能”(二)
										href="http://www.d500.com.cn/" target=_blank><img
											class=full alt=adidas“没有不可能”(二) src="images/47464_252767.jpg" />
									</a>
									<div class=word>
										<h3>
											adidas“没有不可能”(二)
										</h3>
										<p>
											运动是一种游戏，一种积极的释放，更是一种生活态度。在运动中解放自己的心灵
										</p>
									</div>
								</div>
								<div class=image id=image_xixi-07>
									<a class=open title="iphone ipod该换衫了？定制壁纸给你换！"
										href="http://www.d500.com.cn/" target=_blank><img
											class=full alt="iphone ipod该换衫了？定制壁纸给你换！"
											src="images/47457_252699.jpg" /> </a>
									<div class=word>
										<h3>
											iphone ipod该换衫了？定制壁纸给你换！
										</h3>
										<p>
											当我们拥有了ipod或者iphone，也必须享用那些自带壁纸和桌面主题么
										</p>
									</div>
								</div>
							</div>
							<div id=thumbs>
								<ul>
									<li class="first btnPrev">
										<img id=play_prev src="images/btn_prev.gif">
									</li>
									<li class=slideshowItem>
										<a id=thumb_xixi-01 href="#image_xixi-01"><img height=20
												src="images/e8bbb9f5e00523d5528615a835201266_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class=slideshowItem>
										<a id=thumb_xixi-02 href="#image_xixi-02"><img height=20
												src="images/7bb09aff47d9393658b0385f2aabf5a5_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class=slideshowItem>
										<a id=thumb_xixi-03 href="#image_xixi-03"><img height=20
												src="images/3e85fb19e8c09f630f68a9b5120fa264_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class=slideshowItem>
										<a id=thumb_xixi-04 href="#image_xixi-04"><img height=20
												src="images/4d39c524100fd0385e7c9de82efd8e33_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class=slideshowItem>
										<a id=thumb_xixi-05 href="#image_xixi-05"><img height=20
												src="images/354a2840f556bfeaf96c84a00cbe09ac_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class=slideshowItem>
										<a id="thumb_xixi-06" href="#image_xixi-06"><img height=20
												src="images/c00d230fbc41140319a1ff901fdbe9c4_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class="last_img slideshowItem">
										<a id="thumb_xixi-07" href="#image_xixi-07"><img height=20
												src="images/55f1457fa8f165d95fa3ee8f5eb4422a_1_48_20.jpg"
												width=48> </a>
									</li>
									<li class="last btnNext" style="position: absolute;">
										<img id=play_next src="images/btn_next.gif"/>
										<div class=clear></div>
									</li>
								</ul>
							</div>
							<script type=text/javascript>
var target = ["xixi-01","xixi-02","xixi-03","xixi-04","xixi-05","xixi-06","xixi-07"];
</script>
						</div>

										
						</div>
  </body>
</html>
