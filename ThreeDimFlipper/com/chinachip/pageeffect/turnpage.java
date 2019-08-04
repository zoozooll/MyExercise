/*      */ package com.chinachip.pageeffect;
/*      */ 
/*      */ import android.graphics.Bitmap;
/*      */ import android.graphics.Canvas;
/*      */ import android.graphics.ColorMatrix;
/*      */ import android.graphics.ColorMatrixColorFilter;
/*      */ import android.graphics.Matrix;
/*      */ import android.graphics.Paint;
/*      */ import android.graphics.Path;
/*      */ import android.graphics.PointF;
/*      */ import android.graphics.Region.Op;
/*      */ import android.graphics.drawable.GradientDrawable;
/*      */ import android.graphics.drawable.GradientDrawable.Orientation;
/*      */ 
/*      */ public class turnpage
/*      */ {
/*   22 */   public boolean isLeftUp = false;
/*   23 */   public boolean isLeftDown = false;
/*   24 */   public boolean isRightUp = false;
/*   25 */   public boolean isRightDown = false;
/*      */ 
/*  134 */   public static double count = 0.0D;
/*      */ 
/*  433 */   public static PointF pta = null;
/*  434 */   public static PointF ptf = null;
/*  435 */   public static PointF ptg = null;
/*  436 */   public static PointF pth = null;
/*  437 */   public static PointF pte = null;
/*  438 */   public static PointF ptc = null;
/*  439 */   public static PointF ptj = null;
/*  440 */   public static PointF ptb = null;
/*  441 */   public static PointF ptk = null;
/*  442 */   public static PointF ptd = null;
/*  443 */   public static PointF pti = null;
/*  444 */   public static PointF ptl = null;
/*  445 */   public static PointF ptm = null;
/*      */ 
/*  447 */   public static PointF pta2 = null;
/*  448 */   public static PointF ptd1 = null;
/*  449 */   public static PointF pti1 = null;
/*      */ 
/*  451 */   public static float mTouchToCornerDis = 0.0F;
/*      */ 
/*  490 */   public static Path mPath0 = null;
/*  491 */   public static Path mPath1 = null;
/*      */ 
/*  493 */   public static Path mPath2 = null;
/*  494 */   public static Path mPath3 = null;
/*  495 */   public static Path mPath4 = null;
/*      */ 
/*  498 */   public static Path mPath5 = null;
/*  499 */   public static Path mPath6 = null;
/*  500 */   public static Path mPath7 = null;
/*      */ 
/*  502 */   public static Path mPath8 = null;
/*      */ 
/*  504 */   public static Path mPath9 = null;
/*      */ 
/*  677 */   public static PointF dragCorner = new PointF();
/*  678 */   public static PointF lockCorner = new PointF();
/*  679 */   public static PointF targetCorner = new PointF();
/*      */ 
/*  754 */   public static double MaxLength = 0.0D;
/*  755 */   public static float W = 0.0F;
/*  756 */   public static float H = 0.0F;
/*      */ 
/*  815 */   public static int[] mBackShadowColors = null;
/*  816 */   public static int[] mFrontShadowColors = null;
/*      */   public static GradientDrawable mBackShadowDrawableLR;
/*      */   public static GradientDrawable mBackShadowDrawableRL;
/*      */   public static GradientDrawable mFolderShadowDrawableLR;
/*      */   public static GradientDrawable mFolderShadowDrawableRL;
/*      */   public static GradientDrawable mFrontShadowDrawableHBT;
/*      */   public static GradientDrawable mFrontShadowDrawableHTB;
/*      */   public static GradientDrawable mFrontShadowDrawableVLR;
/*      */   public static GradientDrawable mFrontShadowDrawableVRL;
/* 1048 */   public static Paint mPaint = new Paint();
/* 1049 */   public static ColorMatrix cm = new ColorMatrix();
/* 1050 */   public static float[] array = { 0.55F, 0.0F, 0.0F, 0.0F, 80.0F, 0.0F, 0.55F, 0.0F, 0.0F, 80.0F, 0.0F, 0.0F, 0.55F, 0.0F, 80.0F, 0.0F, 0.0F, 0.0F, 0.2F, 0.0F };
/*      */ 
/*      */   public static PointF g(PointF a, PointF f)
/*      */   {
/*   31 */     PointF g = new PointF();
/*   32 */     g.x = ((a.x + f.x) / 2.0F);
/*   33 */     g.y = ((a.y + f.y) / 2.0F);
/*   34 */     return g;
/*      */   }
/*      */ 
/*      */   public static PointF e(PointF g, PointF f)
/*      */   {
/*   40 */     PointF e = new PointF();
/*      */ 
/*   43 */     g.x -= (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
/*   44 */     e.y = f.y;
/*   45 */     return e;
/*      */   }
/*      */ 
/*      */   public static PointF h(PointF g, PointF f)
/*      */   {
/*   50 */     PointF h = new PointF();
/*   51 */     h.x = f.x;
/*   52 */     float fg = f.y - g.y;
/*   53 */     if (fg < 0.0F)
/*   54 */       fg = -fg;
/*   55 */     g.y -= (f.x - g.x) * (f.x - g.x) / (f.y - g.y);
/*   56 */     return h;
/*      */   }
/*      */ 
/*      */   public static double hypot(PointF a, PointF b)
/*      */   {
/*   62 */     return Math.hypot(a.x - b.x, a.y - b.y);
/*      */   }
/*      */ 
/*      */   public static PointF a1(PointF a, PointF f, PointF c)
/*      */   {
/*   68 */     PointF a1 = new PointF();
/*   69 */     PointF g1 = new PointF();
/*   70 */     PointF o = new PointF(a.x, f.y);
/*   71 */     float cn = 0.0F;
/*   72 */     float nf = 0.0F;
/*      */ 
/*   74 */     cn = (int)((float)Math.pow(hypot(a, o), 2.0D) * (float)hypot(c, f) / Math.pow(hypot(a, f), 2.0D));
/*      */ 
/*   76 */     if (c.x <= a.x)
/*   77 */       nf = f.x - cn;
/*      */     else {
/*   79 */       nf = c.x - cn;
/*      */     }
/*   81 */     float g1n = (float)Math.sqrt(cn * nf);
/*      */ 
/*   83 */     if (c.x < a.x)
/*      */     {
/*   85 */       g1.x = cn;
/*      */     }
/*   87 */     else if (c.x == a.x)
/*      */     {
/*   89 */       if (a.x < f.x)
/*      */       {
/*   91 */         g1.x = cn;
/*      */       }
/*      */       else
/*      */       {
/*   95 */         g1.x = nf;
/*      */       }
/*      */     }
/*      */     else {
/*   99 */       g1.x = nf;
/*      */     }
/*  101 */     if (a.y < f.y)
/*  102 */       f.y -= g1n;
/*      */     else {
/*  104 */       g1.y = g1n;
/*      */     }
/*  106 */     if (c.x < o.x)
/*  107 */       g1.x -= nf / 3.0F;
/*  108 */     else if (c.x == o.x)
/*      */     {
/*  110 */       if (a.x < f.x)
/*      */       {
/*  112 */         g1.x -= nf / 3.0F;
/*      */       }
/*      */       else
/*      */       {
/*  116 */         g1.x += nf / 3.0F;
/*      */       }
/*      */     }
/*      */     else {
/*  120 */       g1.x += nf / 3.0F;
/*      */     }
/*  122 */     if (a.y < f.y)
/*  123 */       g1.y -= g1n / 3.0F;
/*      */     else {
/*  125 */       g1.y += g1n / 3.0F;
/*      */     }
/*      */ 
/*  128 */     return a1;
/*      */   }
/*      */ 
/*      */   public static PointF a2(PointF a, PointF e, PointF f)
/*      */   {
/*  140 */     mTouchToCornerDis = (float)Math.hypot(a.x - f.x, a.y - f.y);
/*  141 */     count = Math.min(mTouchToCornerDis / 4.0F, 12.0F);
/*      */ 
/*  143 */     PointF a2 = new PointF();
/*  144 */     double degree = 0.0D;
/*      */ 
/*  148 */     if (dragCorner.x == 0.0F)
/*      */     {
/*  150 */       if (dragCorner.y == 0.0F)
/*      */       {
/*  153 */         degree = 0.7853981633974483D - Math.atan2(a.y - e.y, a.x - e.x);
/*  154 */         double distance2 = count * 1.414D * Math.sin(degree);
/*  155 */         double distance1 = count * 1.414D * Math.cos(degree);
/*      */ 
/*  157 */         a2.x = (float)(a.x + distance1);
/*  158 */         a2.y = (float)(a.y - distance2);
/*      */       }
/*      */       else
/*      */       {
/*  163 */         degree = 0.7853981633974483D - Math.atan2(e.y - a.y, a.x - e.x);
/*      */ 
/*  165 */         double distance2 = count * 1.414D * Math.sin(degree);
/*  166 */         double distance1 = count * 1.414D * Math.cos(degree);
/*      */ 
/*  168 */         a2.x = (float)(a.x + distance1);
/*  169 */         a2.y = (float)(a.y + distance2);
/*      */       }
/*      */ 
/*      */     }
/*  174 */     else if (dragCorner.y == 0.0F)
/*      */     {
/*  176 */       degree = 0.7853981633974483D - Math.atan2(e.y - a.y, a.x - e.x);
/*      */ 
/*  178 */       double distance2 = count * 1.414D * Math.sin(degree);
/*  179 */       double distance1 = count * 1.414D * Math.cos(degree);
/*      */ 
/*  181 */       a2.x = (float)(a.x + distance1);
/*  182 */       a2.y = (float)(a.y + distance2);
/*      */     }
/*      */     else
/*      */     {
/*  186 */       degree = 0.7853981633974483D - Math.atan2(a.y - e.y, a.x - e.x);
/*  187 */       double distance2 = count * 1.414D * Math.sin(degree);
/*  188 */       double distance1 = count * 1.414D * Math.cos(degree);
/*      */ 
/*  190 */       a2.x = (float)(a.x + distance1);
/*  191 */       a2.y = (float)(a.y - distance2);
/*      */     }
/*      */ 
/*  199 */     return a2;
/*      */   }
/*      */ 
/*      */   public static PointF a2(PointF a, PointF b, PointF k, PointF i, PointF d)
/*      */   {
/*  207 */     PointF a2 = new PointF();
/*  208 */     double distanceBK = Math.sqrt(Math.pow(b.x - k.x, 2.0D) + Math.pow(b.y - k.y, 2.0D));
/*  209 */     double distanceID = Math.sqrt(Math.pow(i.x - d.x, 2.0D) + Math.pow(i.y - d.y, 2.0D));
/*  210 */     a2.x = (float)(d.x - (b.x - a.x) * distanceID / distanceBK);
/*  211 */     a2.y = (float)(d.y - (b.y - a.y) * distanceID / distanceBK);
/*      */ 
/*  213 */     return a2;
/*      */   }
/*      */ 
/*      */   public static PointF d1(PointF l, PointF d, PointF f)
/*      */   {
/*  224 */     PointF d1 = new PointF();
/*  225 */     float a = (d.y - l.y) / (d.x - l.x);
/*  226 */     float c = (d.x * l.y - d.y * l.x) / (d.x - l.x);
/*      */ 
/*  228 */     d1.y = f.y;
/*  229 */     d1.x = ((d1.y - c) / a);
/*  230 */     return d1;
/*      */   }
/*      */ 
/*      */   public static PointF i1(PointF m, PointF i, PointF f)
/*      */   {
/*  238 */     PointF i1 = new PointF();
/*  239 */     float a = (i.y - m.y) / (i.x - m.x);
/*  240 */     float c = (i.x * m.y - i.y * m.x) / (i.x - m.x);
/*      */ 
/*  242 */     i1.x = f.x;
/*  243 */     i1.y = (a * i1.x + c);
/*  244 */     return i1;
/*      */   }
/*      */ 
/*      */   public static PointF targetTrack(PointF p1, PointF p2, float x)
/*      */   {
/*  251 */     PointF point = new PointF();
/*      */ 
/*  253 */     float a = 0.0F; float b = 0.0F;
/*      */ 
/*  255 */     if (p1.x != p2.x)
/*      */     {
/*  257 */       a = (p1.y - p2.y) / (p1.x - p2.x);
/*  258 */       b = (p1.x * p2.y - p2.x * p1.y) / (p1.x - p2.x);
/*      */     }
/*  260 */     point.x = x;
/*  261 */     point.y = (a * x + b);
/*      */ 
/*  263 */     return point;
/*      */   }
/*      */ 
/*      */   private static PointF j(PointF h, PointF f)
/*      */   {
/*  290 */     PointF j = new PointF();
/*  291 */     j.x = f.x;
/*  292 */     h.y -= (f.y - h.y) / 2.0F;
/*  293 */     return j;
/*      */   }
/*      */ 
/*      */   public static Boolean isOver(PointF d, PointF i, float w, float h)
/*      */   {
/*  299 */     PointF a = new PointF();
/*  300 */     PointF b = new PointF();
/*      */ 
/*  302 */     PointF approach = new PointF();
/*  303 */     if (dragCorner.x == 0.0F)
/*      */     {
/*  305 */       if (dragCorner.y == 0.0F)
/*      */       {
/*  307 */         a.x = 10.0F;
/*  308 */         a.y = h;
/*  309 */         b.x = 20.0F;
/*  310 */         b.y = h;
/*  311 */         approach = lineCross(d, i, a, b);
/*  312 */         if (approach.x >= w - 3.0F)
/*      */         {
/*  314 */           return Boolean.valueOf(true);
/*      */         }
/*      */ 
/*  317 */         return Boolean.valueOf(false);
/*      */       }
/*      */ 
/*  321 */       a.x = 10.0F;
/*  322 */       a.y = 0.0F;
/*  323 */       b.x = 20.0F;
/*  324 */       b.y = 0.0F;
/*  325 */       approach = lineCross(d, i, a, b);
/*  326 */       if (approach.x >= w - 1.0F)
/*      */       {
/*  328 */         return Boolean.valueOf(true);
/*      */       }
/*      */ 
/*  331 */       return Boolean.valueOf(false);
/*      */     }
/*      */ 
/*  337 */     if (dragCorner.y == 0.0F)
/*      */     {
/*  339 */       a.x = 10.0F;
/*  340 */       a.y = h;
/*  341 */       b.x = 20.0F;
/*  342 */       b.y = h;
/*  343 */       approach = lineCross(d, i, a, b);
/*  344 */       if (approach.x <= 3.0F)
/*      */       {
/*  346 */         return Boolean.valueOf(true);
/*      */       }
/*      */ 
/*  349 */       return Boolean.valueOf(false);
/*      */     }
/*      */ 
/*  354 */     a.x = 10.0F;
/*  355 */     a.y = 0.0F;
/*  356 */     b.x = 20.0F;
/*  357 */     b.y = 0.0F;
/*  358 */     approach = lineCross(d, i, a, b);
/*  359 */     if (approach.x <= 3.0F)
/*      */     {
/*  361 */       return Boolean.valueOf(true);
/*      */     }
/*      */ 
/*  364 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   private static PointF lineCross(PointF pa, PointF pb, PointF pc, PointF pd)
/*      */   {
/*  373 */     PointF p = new PointF();
/*      */ 
/*  375 */     float x1 = pa.x; float x2 = pb.x; float x3 = pc.x; float x4 = pd.x;
/*  376 */     float y1 = pa.y; float y2 = pb.y; float y3 = pc.y; float y4 = pd.y;
/*      */ 
/*  378 */     float a = 0.0F; float b = 0.0F;
/*      */ 
/*  380 */     if (x1 != x2)
/*      */     {
/*  382 */       a = (y1 - y2) / (x1 - x2);
/*  383 */       b = (x1 * y2 - x2 * y1) / (x1 - x2);
/*      */     }
/*      */ 
/*  387 */     float c = 0.0F; float d = 0.0F;
/*  388 */     if (x3 != x4)
/*      */     {
/*  390 */       c = (y3 - y4) / (x3 - x4);
/*  391 */       d = (x3 * y4 - x4 * y3) / (x3 - x4);
/*      */     }
/*      */ 
/*  394 */     if ((x1 == x2) && (x3 != x4))
/*      */     {
/*  396 */       p.x = x1;
/*  397 */       p.y = (c * x1 + d);
/*      */     }
/*  399 */     else if ((x1 != x2) && (x3 == x4))
/*      */     {
/*  401 */       p.x = x3;
/*  402 */       p.y = (a * x3 + b);
/*      */     }
/*      */     else
/*      */     {
/*  406 */       p.x = 
/*  407 */         (((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1)) / (
/*  407 */         (x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4)));
/*      */ 
/*  409 */       p.y = 
/*  410 */         (((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4)) / (
/*  410 */         (y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4)));
/*      */     }
/*  412 */     return p;
/*      */   }
/*      */ 
/*      */   private static PointF triangleCenter(PointF c, PointF b, PointF e)
/*      */   {
/*  417 */     PointF d = new PointF();
/*  418 */     d.x = (((c.x + b.x) / 2.0F + e.x) / 2.0F);
/*  419 */     d.y = (((c.y + b.y) / 2.0F + e.y) / 2.0F);
/*  420 */     return d;
/*      */   }
/*      */ 
/*      */   public static PointF c(PointF a, PointF f)
/*      */   {
/*  425 */     PointF c = new PointF();
/*  426 */     PointF g = g(a, f);
/*  427 */     PointF e = e(g, f);
/*  428 */     c.y = f.y;
/*  429 */     f.x -= (f.y - ((a.y + f.y) / 2.0F + a.y) / 2.0F) * (f.x - e.x) / (f.y - (a.y + f.y) / 2.0F);
/*  430 */     return c;
/*      */   }
/*      */ 
/*      */   public static void calcPoints(PointF a, PointF f)
/*      */   {
/*  455 */     pta = a;
/*  456 */     ptf = f;
/*      */ 
/*  458 */     ptg = g(pta, ptf);
/*      */ 
/*  461 */     pth = h(ptg, ptf);
/*  462 */     pte = e(ptg, ptf);
/*  463 */     ptc = c(pta, ptf);
/*      */ 
/*  465 */     ptj = j(pth, ptf);
/*      */ 
/*  467 */     ptb = lineCross(pta, pte, ptc, ptj);
/*  468 */     ptk = lineCross(pta, pth, ptc, ptj);
/*      */ 
/*  470 */     ptd = triangleCenter(ptc, ptb, pte);
/*  471 */     pti = triangleCenter(ptj, ptk, pth);
/*      */ 
/*  473 */     ptl = lineCross(ptd, pti, pta, pte);
/*  474 */     ptm = lineCross(ptd, pti, pta, pth);
/*      */ 
/*  477 */     pta2 = a2(pta, pte, ptf);
/*      */ 
/*  481 */     ptd1 = d1(ptl, ptd, ptf);
/*  482 */     pti1 = i1(ptm, pti, ptf);
/*      */   }
/*      */ 
/*      */   public static Path getPath0()
/*      */   {
/*  509 */     Path mPath0 = new Path();
/*      */ 
/*  511 */     mPath0.moveTo(ptj.x, ptj.y);
/*  512 */     mPath0.quadTo(pth.x, pth.y, ptk.x, ptk.y);
/*  513 */     mPath0.lineTo(pta.x, pta.y);
/*  514 */     mPath0.lineTo(ptb.x, ptb.y);
/*  515 */     mPath0.quadTo(pte.x, pte.y, ptc.x, ptc.y);
/*  516 */     mPath0.lineTo(ptf.x, ptf.y);
/*  517 */     mPath0.close();
/*      */ 
/*  519 */     return mPath0;
/*      */   }
/*      */ 
/*      */   public static Path getPath1()
/*      */   {
/*  524 */     Path mPath1 = new Path();
/*      */ 
/*  526 */     mPath1.moveTo(pti.x, pti.y);
/*  527 */     mPath1.quadTo(ptm.x, ptm.y, ptk.x, ptk.y);
/*  528 */     mPath1.lineTo(pta.x, pta.y);
/*  529 */     mPath1.lineTo(ptb.x, ptb.y);
/*  530 */     mPath1.quadTo(ptl.x, ptl.y, ptd.x, ptd.y);
/*  531 */     mPath1.lineTo(pti.x, pti.y);
/*  532 */     mPath1.close();
/*      */ 
/*  534 */     return mPath1;
/*      */   }
/*      */ 
/*      */   public static Path getPath2()
/*      */   {
/*  539 */     Path mPath2 = new Path();
/*      */ 
/*  543 */     mPath2.moveTo(pta2.x, pta2.y);
/*  544 */     mPath2.lineTo(pta.x, pta.y);
/*      */ 
/*  546 */     mPath2.lineTo(ptb.x, ptb.y);
/*      */ 
/*  548 */     mPath2.quadTo(ptl.x, ptl.y, ptd.x, ptd.y);
/*      */ 
/*  550 */     mPath2.close();
/*  551 */     return mPath2;
/*      */   }
/*      */ 
/*      */   public static Path getPath3()
/*      */   {
/*  556 */     Path mPath3 = new Path();
/*      */ 
/*  558 */     mPath3.moveTo(pta2.x, pta2.y);
/*  559 */     mPath3.lineTo(pta.x, pta.y);
/*      */ 
/*  561 */     mPath3.lineTo(ptk.x, ptk.y);
/*      */ 
/*  563 */     mPath3.quadTo(ptm.x, ptm.y, pti.x, pti.y);
/*  564 */     mPath3.close();
/*  565 */     return mPath3;
/*      */   }
/*      */ 
/*      */   public static Path getPath4()
/*      */   {
/*  571 */     Path mPath4 = new Path();
/*      */ 
/*  573 */     mPath4.moveTo(pte.x, pte.y);
/*  574 */     mPath4.lineTo(ptc.x, ptc.y);
/*  575 */     mPath4.quadTo(ptd1.x, ptd1.y, ptd.x, ptd.y);
/*      */ 
/*  578 */     mPath4.lineTo(pti.x, pti.y);
/*  579 */     mPath4.quadTo(pti1.x, pti1.y, ptj.x, ptj.y);
/*  580 */     mPath4.lineTo(pth.x, pth.y);
/*      */ 
/*  582 */     mPath4.close();
/*  583 */     return mPath4;
/*      */   }
/*      */ 
/*      */   public static Path getPath5()
/*      */   {
/*  589 */     Path mPath5 = new Path();
/*      */ 
/*  591 */     mPath5.moveTo(ptc.x, ptc.y);
/*  592 */     mPath5.quadTo(pte.x, pte.y, ptb.x, ptb.y);
/*  593 */     mPath5.lineTo(pta.x, pta.y);
/*  594 */     mPath5.lineTo(ptk.x, ptk.y);
/*  595 */     mPath5.quadTo(pth.x, pth.y, ptj.x, ptj.y);
/*  596 */     mPath5.lineTo(ptf.x, ptf.y);
/*  597 */     mPath5.close();
/*  598 */     return mPath5;
/*      */   }
/*      */ 
/*      */   public static Path getPath6()
/*      */   {
/*  604 */     Path mPath6 = new Path();
/*      */ 
/*  606 */     mPath6.moveTo(pta2.x, pta2.y);
/*  607 */     mPath6.lineTo(pta.x, pta.y);
/*  608 */     mPath6.lineTo(pte.x, pte.y);
/*  609 */     mPath6.lineTo(ptc.x, ptc.y);
/*  610 */     mPath6.close();
/*  611 */     return mPath6;
/*      */   }
/*      */ 
/*      */   public static Path getPath7() {
/*  615 */     Path mPath7 = new Path();
/*      */ 
/*  617 */     mPath7.moveTo(pta2.x, pta2.y);
/*  618 */     mPath7.lineTo(pta.x, pta.y);
/*  619 */     mPath7.lineTo(pth.x, pth.y);
/*  620 */     mPath7.lineTo(ptj.x, ptj.y);
/*  621 */     mPath7.close();
/*  622 */     return mPath7;
/*      */   }
/*      */ 
/*      */   public static Path getPath8()
/*      */   {
/*  628 */     Path mPath8 = new Path();
/*      */ 
/*  630 */     mPath8.moveTo(ptc.x, ptc.y);
/*  631 */     mPath8.lineTo(ptd.x, ptd.y);
/*  632 */     mPath8.lineTo(pti.x, pti.y);
/*  633 */     mPath8.lineTo(ptj.x, ptj.y);
/*  634 */     mPath8.lineTo(ptf.x, ptf.y);
/*  635 */     mPath8.close();
/*      */ 
/*  637 */     return mPath8;
/*      */   }
/*      */ 
/*      */   public static Path getPath9()
/*      */   {
/*  642 */     Path mPath9 = new Path();
/*      */ 
/*  644 */     mPath9.moveTo(pti.x, pti.y);
/*  645 */     mPath9.lineTo(ptd.x, ptd.y);
/*  646 */     mPath9.lineTo(ptb.x, ptb.y);
/*  647 */     mPath9.lineTo(pta.x, pta.y);
/*  648 */     mPath9.lineTo(ptk.x, ptk.y);
/*  649 */     mPath9.close();
/*      */ 
/*  651 */     return mPath9;
/*      */   }
/*      */ 
/*      */   public static void workOut(PointF a, PointF f)
/*      */   {
/*  657 */     calcPoints(a, f);
/*  658 */     mPath0 = getPath0();
/*  659 */     mPath1 = getPath1();
/*      */ 
/*  663 */     mPath2 = getPath2();
/*  664 */     mPath3 = getPath3();
/*  665 */     mPath4 = getPath4();
/*      */ 
/*  667 */     mPath5 = getPath5();
/*  668 */     mPath6 = getPath6();
/*  669 */     mPath7 = getPath7();
/*      */ 
/*  671 */     mPath8 = getPath8();
/*      */ 
/*  673 */     mPath9 = getPath9();
/*      */   }
/*      */ 
/*      */   private static void findCorner1(float x, float y, float w, float h)
/*      */   {
/*  690 */     float cx = w / 2.0F;
/*  691 */     float cy = h / 2.0F;
/*      */ 
/*  693 */     if (x < cx) {
/*  694 */       if (y < cy) {
/*  695 */         dragCorner.x = 0.0F;
/*  696 */         dragCorner.y = 0.0F;
/*      */       }
/*      */       else
/*      */       {
/*  700 */         dragCorner.x = 0.0F;
/*  701 */         dragCorner.y = h;
/*      */       }
/*      */ 
/*      */     }
/*  705 */     else if (y < cy) {
/*  706 */       dragCorner.x = w;
/*  707 */       dragCorner.y = 0.0F;
/*      */     }
/*      */     else
/*      */     {
/*  711 */       dragCorner.x = w;
/*  712 */       dragCorner.y = h;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void findCorner2(float x, float y, float w, float h)
/*      */   {
/*  722 */     findCorner1(x, y, w, h);
/*      */ 
/*  724 */     if (dragCorner.x == 0.0F)
/*      */     {
/*  726 */       if (dragCorner.y == 0.0F)
/*      */       {
/*  728 */         lockCorner.x = w;
/*  729 */         lockCorner.y = 0.0F;
/*      */       }
/*      */       else
/*      */       {
/*  733 */         lockCorner.x = w;
/*  734 */         lockCorner.y = h;
/*      */       }
/*      */ 
/*      */     }
/*  739 */     else if (dragCorner.y == 0.0F)
/*      */     {
/*  741 */       lockCorner.x = 0.0F;
/*  742 */       lockCorner.y = 0.0F;
/*      */     }
/*      */     else
/*      */     {
/*  746 */       lockCorner.x = 0.0F;
/*  747 */       lockCorner.y = h;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void findCorner(PointF a, float w, float h)
/*      */   {
/*  761 */     W = w;
/*  762 */     H = h;
/*      */ 
/*  764 */     MaxLength = (float)Math.hypot(w, h);
/*      */ 
/*  767 */     findCorner2(a.x, a.y, w, h);
/*      */ 
/*  769 */     if (lockCorner.x > dragCorner.x)
/*  770 */       lockCorner.x *= 2.0F;
/*      */     else {
/*  772 */       targetCorner.x = (-dragCorner.x);
/*      */     }
/*  774 */     targetCorner.y = dragCorner.y;
/*      */   }
/*      */ 
/*      */   public static boolean isCornerLock(PointF a, PointF f, float w, float h)
/*      */   {
/*  779 */     PointF c = c(a, f);
/*  780 */     if (lockCorner.x == 0.0F)
/*      */     {
/*  782 */       if (lockCorner.y == 0.0F)
/*      */       {
/*  784 */         if (c.x < 0.0F) {
/*  785 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*  789 */       else if (c.x < 0.0F) {
/*  790 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*  795 */     else if (lockCorner.y == 0.0F)
/*      */     {
/*  797 */       if (c.x > w) {
/*  798 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*  802 */     else if (c.x > w) {
/*  803 */       return true;
/*      */     }
/*      */ 
/*  806 */     return false;
/*      */   }
/*      */ 
/*      */   public static void createDrawable()
/*      */   {
/*  828 */     int[] color = { 3355443, -1338821837 };
/*  829 */     mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
/*  830 */     mFolderShadowDrawableRL.setGradientType(0);
/*      */ 
/*  832 */     mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
/*  833 */     mFolderShadowDrawableLR.setGradientType(0);
/*      */ 
/*  835 */     mBackShadowColors = new int[] { -15658735, 1118481 };
/*  836 */     mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
/*  837 */     mBackShadowDrawableRL.setGradientType(0);
/*      */ 
/*  839 */     mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
/*  840 */     mBackShadowDrawableLR.setGradientType(0);
/*      */ 
/*  842 */     mFrontShadowColors = new int[] { -2146365167, 1118481 };
/*  843 */     mFrontShadowDrawableVLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
/*  844 */     mFrontShadowDrawableVLR.setGradientType(0);
/*  845 */     mFrontShadowDrawableVRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
/*  846 */     mFrontShadowDrawableVRL.setGradientType(0);
/*      */ 
/*  848 */     mFrontShadowDrawableHTB = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
/*  849 */     mFrontShadowDrawableHTB.setGradientType(0);
/*      */ 
/*  851 */     mFrontShadowDrawableHBT = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
/*  852 */     mFrontShadowDrawableHBT.setGradientType(0);
/*      */   }
/*      */ 
/*      */   public static void drawCurrentPageShadow(Canvas canvas)
/*      */   {
/*  860 */     int leftx = 0;
/*  861 */     int rightx = 0;
/*  862 */     GradientDrawable mCurrentPageShadow = null;
/*  863 */     float rotateDegrees = 0.0F;
/*      */ 
/*  865 */     if (dragCorner.x == 0.0F)
/*      */     {
/*  867 */       if (dragCorner.y == 0.0F)
/*      */       {
/*  869 */         leftx = (int)(pte.x - count);
/*  870 */         rightx = (int)pte.x + 1;
/*  871 */         mCurrentPageShadow = mFrontShadowDrawableVRL;
/*      */       }
/*      */       else
/*      */       {
/*  877 */         leftx = (int)pte.x;
/*  878 */         rightx = (int)((int)pte.x + count);
/*  879 */         mCurrentPageShadow = mFrontShadowDrawableVLR;
/*      */       }
/*      */ 
/*      */     }
/*  884 */     else if (dragCorner.y == 0.0F)
/*      */     {
/*  886 */       leftx = (int)pte.x;
/*  887 */       rightx = (int)((int)pte.x + count);
/*  888 */       mCurrentPageShadow = mFrontShadowDrawableVLR;
/*      */     }
/*      */     else
/*      */     {
/*  892 */       leftx = (int)(pte.x - count);
/*  893 */       rightx = (int)pte.x + 1;
/*  894 */       mCurrentPageShadow = mFrontShadowDrawableVRL;
/*      */     }
/*      */ 
/*  898 */     canvas.save();
/*      */ 
/*  900 */     canvas.clipPath(mPath5, Region.Op.XOR);
/*  901 */     canvas.clipPath(mPath6, Region.Op.INTERSECT);
/*  902 */     rotateDegrees = (float)Math.toDegrees(Math.atan2(pta.x - pte.x, pte.y - pta.y));
/*  903 */     canvas.rotate(rotateDegrees, pte.x, pte.y);
/*      */ 
/*  905 */     mCurrentPageShadow.setBounds(leftx, (int)(pte.y - MaxLength), rightx, (int)pte.y);
/*  906 */     mCurrentPageShadow.draw(canvas);
/*  907 */     canvas.restore();
/*      */ 
/*  909 */     if (dragCorner.x == 0.0F)
/*      */     {
/*  911 */       if (dragCorner.y == 0.0F)
/*      */       {
/*  913 */         leftx = (int)(pth.y - count);
/*  914 */         rightx = (int)(pth.y + 1.0F);
/*  915 */         mCurrentPageShadow = mFrontShadowDrawableHBT;
/*      */       }
/*      */       else
/*      */       {
/*  921 */         leftx = (int)pth.y;
/*  922 */         rightx = (int)(pth.y + count);
/*  923 */         mCurrentPageShadow = mFrontShadowDrawableHTB;
/*      */       }
/*      */ 
/*      */     }
/*  928 */     else if (dragCorner.y == 0.0F)
/*      */     {
/*  930 */       leftx = (int)pth.y;
/*  931 */       rightx = (int)(pth.y + count);
/*  932 */       mCurrentPageShadow = mFrontShadowDrawableHTB;
/*      */     }
/*      */     else
/*      */     {
/*  936 */       leftx = (int)(pth.y - count);
/*  937 */       rightx = (int)(pth.y + 1.0F);
/*  938 */       mCurrentPageShadow = mFrontShadowDrawableHBT;
/*      */     }
/*      */ 
/*  943 */     canvas.save();
/*  944 */     canvas.clipPath(mPath5, Region.Op.XOR);
/*  945 */     canvas.clipPath(mPath7, Region.Op.INTERSECT);
/*      */ 
/*  947 */     rotateDegrees = (float)Math.toDegrees(Math.atan2(pth.y - pta.y, pth.x - pta.x));
/*      */ 
/*  949 */     canvas.rotate(rotateDegrees, pth.x, pth.y);
/*      */ 
/*  951 */     float temp = 0.0F;
/*  952 */     if (pth.y < 0.0F)
/*  953 */       temp = pth.y - H;
/*      */     else {
/*  955 */       temp = pth.y;
/*      */     }
/*  957 */     int hmg = (int)Math.hypot(pth.x, temp);
/*  958 */     if (hmg > MaxLength)
/*      */     {
/*  961 */       mCurrentPageShadow.setBounds((int)(pth.x - count) - hmg, leftx, (int)(pth.x + MaxLength) - hmg, rightx);
/*      */     }
/*      */     else
/*      */     {
/*  967 */       mCurrentPageShadow.setBounds((int)(pth.x - MaxLength), leftx, (int)pth.x, rightx);
/*      */     }
/*      */ 
/*  972 */     mCurrentPageShadow.draw(canvas);
/*  973 */     canvas.restore();
/*      */   }
/*      */ 
/*      */   public static void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap)
/*      */   {
/*  979 */     int leftx = 0;
/*  980 */     int rightx = 0;
/*  981 */     GradientDrawable mBackShadowDrawable = null;
/*  982 */     float mDegrees = (float)Math.toDegrees(Math.atan2(pte.x - ptf.x, pth.y - ptf.y));
/*      */ 
/*  984 */     if (dragCorner.x == 0.0F)
/*      */     {
/*  986 */       if (dragCorner.y == 0.0F)
/*      */       {
/*  988 */         leftx = (int)(ptc.x - mTouchToCornerDis / 4.0F);
/*  989 */         rightx = (int)ptc.x;
/*  990 */         mBackShadowDrawable = mBackShadowDrawableRL;
/*      */       }
/*      */       else
/*      */       {
/*  996 */         leftx = (int)ptc.x;
/*  997 */         rightx = (int)(ptc.x + mTouchToCornerDis / 4.0F);
/*  998 */         mBackShadowDrawable = mBackShadowDrawableLR;
/*      */       }
/*      */ 
/*      */     }
/* 1003 */     else if (dragCorner.y == 0.0F)
/*      */     {
/* 1005 */       leftx = (int)ptc.x;
/* 1006 */       rightx = (int)(ptc.x + mTouchToCornerDis / 4.0F);
/* 1007 */       mBackShadowDrawable = mBackShadowDrawableLR;
/*      */     }
/*      */     else
/*      */     {
/* 1011 */       leftx = (int)(ptc.x - mTouchToCornerDis / 4.0F);
/* 1012 */       rightx = (int)ptc.x;
/* 1013 */       mBackShadowDrawable = mBackShadowDrawableRL;
/*      */     }
/*      */ 
/* 1020 */     canvas.save();
/* 1021 */     canvas.clipPath(mPath0);
/* 1022 */     canvas.clipPath(mPath8, Region.Op.INTERSECT);
/* 1023 */     canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
/* 1024 */     canvas.rotate(mDegrees, ptc.x, ptc.y);
/* 1025 */     mBackShadowDrawable.setBounds(leftx, (int)ptc.y, rightx, (int)(MaxLength + ptc.y));
/* 1026 */     mBackShadowDrawable.draw(canvas);
/* 1027 */     canvas.restore();
/*      */   }
/*      */ 
/*      */   public static void drawCurrentPageArea(Canvas canvas, Bitmap bitmap)
/*      */   {
/* 1036 */     canvas.save();
/* 1037 */     canvas.clipPath(mPath5, Region.Op.XOR);
/* 1038 */     canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
/* 1039 */     canvas.restore();
/*      */   }
/*      */ 
/*      */   public static void drawCurrentBackArea(Canvas canvas, Bitmap bitmap)
/*      */   {
/* 1054 */     int i = (int)(ptc.x + pte.x) / 2;
/* 1055 */     float f1 = Math.abs(i - pte.x);
/* 1056 */     int i1 = (int)(ptj.y + pth.y) / 2;
/* 1057 */     float f2 = Math.abs(i1 - pth.y);
/* 1058 */     float f3 = Math.min(f1, f2);
/* 1059 */     GradientDrawable mFolderShadowDrawable = null;
/* 1060 */     int left = 0;
/* 1061 */     int right = 0;
/*      */ 
/* 1067 */     cm.set(array);
/* 1068 */     ColorMatrixColorFilter mColorMatrixFilter = new ColorMatrixColorFilter(cm);
/*      */ 
/* 1070 */     Matrix mMatrix = new Matrix();
/* 1071 */     float[] mMatrixArray = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/*      */ 
/* 1073 */     float mDegrees = (float)Math.toDegrees(Math.atan2(pte.x - ptf.x, pth.y - ptf.y));
/*      */ 
/* 1075 */     if (dragCorner.x == 0.0F)
/*      */     {
/* 1077 */       if (dragCorner.y == 0.0F)
/*      */       {
/* 1079 */         left = (int)(ptc.x - f3 - 1.0F);
/* 1080 */         right = (int)(ptc.x + 1.0F);
/* 1081 */         mFolderShadowDrawable = mFolderShadowDrawableRL;
/*      */       }
/*      */       else
/*      */       {
/* 1087 */         left = (int)(ptc.x - 3.0F);
/* 1088 */         right = (int)(ptc.x + f3 + 4.0F);
/* 1089 */         mFolderShadowDrawable = mFolderShadowDrawableLR;
/*      */       }
/*      */ 
/*      */     }
/* 1094 */     else if (dragCorner.y == 0.0F)
/*      */     {
/* 1096 */       left = (int)(ptc.x - 1.0F);
/* 1097 */       right = (int)(ptc.x + f3 + 1.0F);
/* 1098 */       mFolderShadowDrawable = mFolderShadowDrawableLR;
/*      */     }
/*      */     else
/*      */     {
/* 1102 */       left = (int)(ptc.x - f3 - 1.0F);
/* 1103 */       right = (int)(ptc.x + 1.0F);
/* 1104 */       mFolderShadowDrawable = mFolderShadowDrawableRL;
/*      */     }
/*      */ 
/* 1110 */     canvas.save();
/* 1111 */     canvas.clipPath(mPath0);
/* 1112 */     canvas.clipPath(mPath9, Region.Op.INTERSECT);
/*      */ 
/* 1114 */     mPaint.setColorFilter(mColorMatrixFilter);
/*      */ 
/* 1116 */     mPaint.setFlags(1);
/*      */ 
/* 1118 */     float dis = (float)Math.hypot(ptf.x - pte.x, pth.y - ptf.y);
/* 1119 */     float f8 = (ptf.x - pte.x) / dis;
/* 1120 */     float f9 = (pth.y - ptf.y) / dis;
/* 1121 */     mMatrixArray[0] = (1.0F - 2.0F * f9 * f9);
/* 1122 */     mMatrixArray[1] = (2.0F * f8 * f9);
/* 1123 */     mMatrixArray[3] = mMatrixArray[1];
/* 1124 */     mMatrixArray[4] = (1.0F - 2.0F * f8 * f8);
/*      */ 
/* 1126 */     mMatrix.setValues(mMatrixArray);
/* 1127 */     mMatrix.preTranslate(-pte.x, -pte.y);
/* 1128 */     mMatrix.postTranslate(pte.x, pte.y);
/* 1129 */     canvas.drawBitmap(bitmap, mMatrix, mPaint);
/*      */ 
/* 1131 */     mPaint.setColorFilter(null);
/* 1132 */     canvas.rotate(mDegrees, ptc.x, ptc.y);
/* 1133 */     mFolderShadowDrawable.setBounds(left, (int)ptc.y, right, (int)(ptc.y + MaxLength));
/* 1134 */     mFolderShadowDrawable.draw(canvas);
/* 1135 */     canvas.restore();
/*      */   }
/*      */ }

/* Location:           I:\Workspaces\eclipse3.4\CCBookPro\lib\threedim.jar
 * Qualified Name:     com.chinachip.pageeffect.turnpage
 * JD-Core Version:    0.6.0
 */