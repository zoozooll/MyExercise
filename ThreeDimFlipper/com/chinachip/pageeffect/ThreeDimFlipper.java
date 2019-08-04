/*     */ package com.chinachip.pageeffect;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.PointF;
/*     */ import android.graphics.Rect;
/*     */ import android.os.Handler;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.GestureDetector;
/*     */ import android.view.GestureDetector.OnGestureListener;
/*     */ import android.view.MotionEvent;
/*     */ import android.view.View;
/*     */ import android.widget.Toast;
/*     */ import com.ccbooks.fullscreen.content.TextThreeDim;
/*     */ import com.ccbooks.fullscreen.content.bitmapResource;
/*     */ import com.ccbooks.view.TextReader;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ThreeDimFlipper extends View
/*     */   implements GestureDetector.OnGestureListener
/*     */ {
/*  42 */   public Bitmap mCurPageBitmap = null;
/*  43 */   public Bitmap mNextPageBitmap = null;
/*     */ 
/*  46 */   public boolean isStart = true;
/*  47 */   public boolean isFinish = false;
/*     */ 
/*  49 */   public boolean isFirst = true;
/*     */   private ArrayList<Bitmap> b;
/*     */   public bitmapResource br;
/*  54 */   private long offset = 0L;
/*     */ 
/*  56 */   public int bgcolor = -723724;
/*     */ 
/*  58 */   private TextThreeDim ttd = null;
/*     */   public GestureDetector detector;
/* 127 */   private PointF autoDragPoint = new PointF();
/* 128 */   private PointF autoDragStart = new PointF();
/*     */ 
/* 130 */   private Rect pageRect = new Rect();
/* 131 */   private float w = 0.0F;
/* 132 */   private float h = 0.0F;
/*     */ 
/* 134 */   private boolean isTurning = false;
/* 135 */   private int autoDragStep = 0;
/* 136 */   private boolean turnR = false;
/*     */ 
/* 188 */   private PointF dragPoint = new PointF();
/* 189 */   private Paint paint = new Paint();
/*     */ 
/* 516 */   private Handler handler = new Handler();
/*     */ 
/* 518 */   private int delayMillis = 35;
/* 519 */   private Runnable runnable = new Runnable() {
/*     */     public void run() {
/* 521 */       if (ThreeDimFlipper.this.autoRun()) {
/* 522 */         ThreeDimFlipper.this.handler.postDelayed(this, ThreeDimFlipper.this.delayMillis);
/*     */       }
/*     */       else
/*     */       {
/* 526 */         ThreeDimFlipper.this.turnPageOff();
/*     */       }
/*     */     }
/* 519 */   };
/*     */ 
/*     */   public ThreeDimFlipper(Context context, TextThreeDim ttd, AttributeSet attrs, int defStyle)
/*     */   {
/*  61 */     super(context, attrs, defStyle);
/*  62 */     this.ttd = ttd;
/*  63 */     this.detector = new GestureDetector(this);
/*     */ 
/*  69 */     turnpage.createDrawable();
/*     */ 
/*  77 */     this.br = new bitmapResource(ttd);
/*     */   }
/*     */ 
/*     */   public ThreeDimFlipper(Context context, TextThreeDim ttd, AttributeSet attrs)
/*     */   {
/*  83 */     super(context, attrs);
/*  84 */     this.ttd = ttd;
/*     */ 
/*  86 */     turnpage.createDrawable();
/*  87 */     this.detector = new GestureDetector(this);
/*     */ 
/*  93 */     this.br = new bitmapResource(ttd);
/*     */   }
/*     */ 
/*     */   public ThreeDimFlipper(Context context, TextThreeDim ttd, long markPoint)
/*     */   {
/*  99 */     super(context);
/* 100 */     this.ttd = ttd;
/* 101 */     this.detector = new GestureDetector(this);
/*     */ 
/* 103 */     turnpage.createDrawable();
/*     */ 
/* 111 */     this.br = new bitmapResource(ttd);
/*     */   }
/*     */ 
/*     */   public void initResource()
/*     */   {
/* 117 */     this.br.getInitializationBitmap();
/*     */   }
/*     */ 
/*     */   public void CurrResource()
/*     */   {
/* 122 */     this.br.currInitializationBitmap();
/*     */ 
/* 124 */     invalidate();
/*     */   }
/*     */ 
/*     */   public boolean autoRun()
/*     */   {
/* 140 */     if (this.turnR) {
/* 141 */       this.dragPoint.x += this.autoDragStep;
/* 142 */       this.autoDragPoint = turnpage.targetTrack(this.autoDragStart, turnpage.targetCorner, this.dragPoint.x);
/*     */     } else {
/* 144 */       this.dragPoint.x -= this.autoDragStep;
/* 145 */       this.autoDragPoint = turnpage.targetTrack(this.autoDragStart, turnpage.targetCorner, this.dragPoint.x);
/*     */     }
/*     */ 
/* 148 */     this.autoDragStep += 20;
/*     */ 
/* 150 */     turnpage.workOut(this.autoDragPoint, turnpage.dragCorner);
/*     */ 
/* 152 */     invalidate();
/*     */ 
/* 161 */     this.isFirst = false;
/* 162 */     if (turnpage.isOver(turnpage.ptd, turnpage.pti, this.w, this.h).booleanValue()) {
/* 163 */       this.isTurning = false;
/*     */ 
/* 166 */       SwapViews();
/* 167 */       this.isStart = false;
/* 168 */       this.isFirst = true;
/*     */     }
/*     */ 
/* 172 */     return this.isTurning;
/*     */   }
/*     */ 
/*     */   private void autoPage() {
/* 176 */     this.autoDragStep = 1;
/* 177 */     this.autoDragPoint.x = this.dragPoint.x;
/* 178 */     this.autoDragPoint.y = this.dragPoint.y;
/* 179 */     this.autoDragStart.x = this.dragPoint.x;
/* 180 */     this.autoDragStart.y = this.dragPoint.y;
/* 181 */     if (turnpage.targetCorner.x > this.dragPoint.x)
/* 182 */       this.turnR = true;
/*     */     else
/* 184 */       this.turnR = false;
/*     */   }
/*     */ 
/*     */   protected void onDraw(Canvas canvas)
/*     */   {
/* 245 */     super.onDraw(canvas);
/* 246 */     this.bgcolor = this.ttd.actParent.bkColor;
/* 247 */     canvas.drawColor(this.bgcolor);
/* 248 */     if (this.isTurning)
/*     */     {
/* 253 */       turnpage.drawCurrentPageArea(canvas, this.mCurPageBitmap);
/*     */ 
/* 255 */       turnpage.drawNextPageAreaAndShadow(canvas, this.mNextPageBitmap);
/* 256 */       turnpage.drawCurrentPageShadow(canvas);
/* 257 */       turnpage.drawCurrentBackArea(canvas, this.mCurPageBitmap);
/*     */     }
/*     */     else
/*     */     {
/* 273 */       canvas.drawBitmap(this.mCurPageBitmap, 0.0F, 0.0F, null);
/* 274 */       this.isFirst = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private PointF adjustRange(PointF a, Rect r)
/*     */   {
/* 285 */     if (a.x < r.left)
/* 286 */       a.x = r.left;
/* 287 */     else if (a.x > r.right) {
/* 288 */       a.x = r.right;
/*     */     }
/* 290 */     if (a.y < r.top)
/* 291 */       a.y = r.top;
/* 292 */     else if (a.y > r.bottom) {
/* 293 */       a.y = r.bottom;
/*     */     }
/* 295 */     return a;
/*     */   }
/*     */ 
/*     */   public boolean doTouchEvent(MotionEvent event)
/*     */   {
/* 301 */     this.pageRect = new Rect();
/* 302 */     this.pageRect.left = 1;
/* 303 */     this.pageRect.top = 1;
/* 304 */     this.pageRect.right = (getWidth() - 1);
/* 305 */     this.pageRect.bottom = (getHeight() - 1);
/*     */ 
/* 307 */     this.w = getWidth();
/* 308 */     this.h = getHeight();
/*     */ 
/* 310 */     switch (event.getAction()) {
/*     */     case 0:
/* 312 */       if (this.isFirst)
/*     */       {
/* 314 */         this.dragPoint.x = (int)event.getX();
/* 315 */         this.dragPoint.y = (int)event.getY();
/* 316 */         PointF a = new PointF(this.dragPoint.x, this.dragPoint.y);
/*     */ 
/* 318 */         a = adjustRange(a, this.pageRect);
/*     */ 
/* 320 */         turnpage.findCorner(a, this.w, this.h);
/*     */ 
/* 323 */         PointF f = turnpage.dragCorner;
/* 324 */         if (turnpage.isCornerLock(a, f, this.w, this.h)) {
/* 325 */           a = turnpage.a1(a, f, turnpage.lockCorner);
/*     */         }
/*     */ 
/* 328 */         this.dragPoint.x = a.x;
/* 329 */         this.dragPoint.y = a.y;
/*     */ 
/* 333 */         turnpage.workOut(a, turnpage.dragCorner);
/*     */ 
/* 335 */         float halfx = this.w / 2.0F;
/* 336 */         float halfy = this.h / 2.0F;
/* 337 */         float thirdx = this.w / 4.0F;
/* 338 */         float thirdy = this.h / 4.0F;
/*     */ 
/* 340 */         if (((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= halfx) && (this.dragPoint.y >= 0.0F) && (this.dragPoint.y <= thirdy)) || ((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= thirdx) && (this.dragPoint.y > thirdy) && (this.dragPoint.y <= halfy)))
/*     */         {
/* 342 */           Bitmap temp = this.br.getbeforeBitmap();
/* 343 */           if (!this.br.isStart())
/*     */           {
/* 347 */             this.mNextPageBitmap = temp;
/* 348 */             this.isTurning = true;
/* 349 */             this.isStart = false;
/* 350 */             invalidate();
/*     */           }
/*     */           else
/*     */           {
/* 355 */             startToast();
/* 356 */             this.isTurning = false;
/* 357 */             this.isStart = true;
/*     */           }
/* 359 */           this.ttd.menuOff();
/*     */         }
/* 361 */         else if (((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= halfx) && (this.dragPoint.y >= thirdy * 3.0F) && (this.dragPoint.y <= this.h)) || ((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= thirdx) && (this.dragPoint.y > halfy) && (this.dragPoint.y <= thirdy * 3.0F)))
/*     */         {
/* 363 */           Bitmap temp = this.br.getbeforeBitmap();
/*     */ 
/* 365 */           if (!this.br.isStart())
/*     */           {
/* 368 */             this.mNextPageBitmap = temp;
/* 369 */             this.isTurning = true;
/* 370 */             this.isStart = false;
/* 371 */             invalidate();
/*     */           }
/*     */           else
/*     */           {
/* 376 */             startToast();
/* 377 */             this.isTurning = false;
/* 378 */             this.isStart = true;
/*     */           }
/*     */ 
/* 381 */           this.ttd.menuOff();
/*     */         }
/* 385 */         else if (((this.dragPoint.x > halfx) && (this.dragPoint.x <= this.w) && (this.dragPoint.y >= 0.0F) && (this.dragPoint.y < thirdy)) || ((this.dragPoint.x >= thirdx * 3.0F) && (this.dragPoint.x <= this.w) && (this.dragPoint.y > thirdy) && (this.dragPoint.y < halfy)))
/*     */         {
/* 388 */           Bitmap temp = this.br.getNextBitmap();
/* 389 */           if (!this.br.isFinish())
/*     */           {
/* 392 */             this.mNextPageBitmap = temp;
/* 393 */             this.isTurning = true;
/* 394 */             this.isStart = false;
/* 395 */             invalidate();
/*     */           }
/*     */           else
/*     */           {
/* 399 */             finishToast();
/* 400 */             this.isTurning = false;
/* 401 */             this.isStart = true;
/*     */           }
/* 403 */           this.ttd.menuOff();
/*     */         }
/* 405 */         else if (((this.dragPoint.x > halfx) && (this.dragPoint.x <= this.w) && (this.dragPoint.y >= thirdy * 3.0F) && (this.dragPoint.y <= this.h)) || ((this.dragPoint.x >= thirdx * 3.0F) && (this.dragPoint.x <= this.w) && (this.dragPoint.y > halfy) && (this.dragPoint.y <= thirdy * 3.0F)))
/*     */         {
/* 407 */           Bitmap temp = this.br.getNextBitmap();
/* 408 */           if (!this.br.isFinish())
/*     */           {
/* 411 */             this.mNextPageBitmap = temp;
/* 412 */             this.isTurning = true;
/* 413 */             this.isStart = false;
/* 414 */             invalidate();
/*     */           }
/*     */           else
/*     */           {
/* 418 */             finishToast();
/* 419 */             this.isTurning = false;
/* 420 */             this.isStart = true;
/*     */           }
/* 422 */           this.ttd.menuOff();
/*     */         }
/*     */ 
/* 430 */         this.isFirst = false;
/*     */       }
/*     */ 
/* 436 */       return true;
/*     */     case 2:
/* 439 */       this.dragPoint.x = (int)event.getX();
/* 440 */       this.dragPoint.y = (int)event.getY();
/*     */ 
/* 442 */       PointF a = new PointF(this.dragPoint.x, this.dragPoint.y);
/*     */ 
/* 444 */       a = adjustRange(a, this.pageRect);
/*     */ 
/* 446 */       PointF f = turnpage.dragCorner;
/*     */ 
/* 448 */       if (turnpage.isCornerLock(a, f, this.w, this.h)) {
/* 449 */         a = turnpage.a1(a, f, turnpage.lockCorner);
/*     */       }
/*     */ 
/* 452 */       this.dragPoint.x = a.x;
/* 453 */       this.dragPoint.y = a.y;
/* 454 */       turnpage.workOut(a, f);
/*     */ 
/* 456 */       invalidate();
/*     */ 
/* 458 */       return true;
/*     */     case 1:
/* 461 */       autoPage();
/* 462 */       return true;
/*     */     }
/* 464 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean onTouchEvent(MotionEvent event)
/*     */   {
/* 472 */     turnPageOff();
/* 473 */     doTouchEvent(event);
/*     */ 
/* 475 */     if (event.getAction() == 1)
/*     */     {
/* 477 */       turnPageOn(35);
/* 478 */       invalidate();
/*     */     }
/* 480 */     return this.detector.onTouchEvent(event);
/*     */   }
/*     */ 
/*     */   public void SwapViews()
/*     */   {
/* 485 */     Bitmap temp = this.mCurPageBitmap;
/* 486 */     this.mCurPageBitmap = this.mNextPageBitmap;
/* 487 */     this.mNextPageBitmap = temp;
/*     */   }
/*     */ 
/*     */   public long getOffset()
/*     */   {
/* 499 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void setOffset(long offset) {
/* 503 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   private void turnPageOn(int delayMillis) {
/* 507 */     this.delayMillis = delayMillis;
/* 508 */     this.handler.postDelayed(this.runnable, delayMillis);
/*     */   }
/*     */ 
/*     */   private void turnPageOff() {
/* 512 */     this.handler.removeCallbacks(this.runnable);
/*     */   }
/*     */ 
/*     */   private void startToast()
/*     */   {
/* 532 */     if (TextReader.file == this.ttd.actParent.headFile)
/* 533 */       Toast.makeText(this.ttd.actParent, 2131165194, 0).show();
/*     */   }
/*     */ 
/*     */   private void finishToast()
/*     */   {
/* 539 */     if (TextReader.file < this.ttd.actParent.endFile)
/*     */     {
/* 541 */       Toast.makeText(this.ttd.actParent, 2131165197, 0).show();
/*     */     }
/* 543 */     else if (TextReader.file == this.ttd.actParent.endFile)
/*     */     {
/* 545 */       Toast.makeText(this.ttd.actParent, 2131165196, 0).show();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean onDown(MotionEvent e)
/*     */   {
/* 553 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
/*     */   {
/* 560 */     return false;
/*     */   }
/*     */ 
/*     */   public void onLongPress(MotionEvent e)
/*     */   {
/* 567 */     this.dragPoint.x = (int)e.getX();
/* 568 */     this.dragPoint.y = (int)e.getY();
/* 569 */     PointF a = new PointF(this.dragPoint.x, this.dragPoint.y);
/*     */ 
/* 571 */     a = adjustRange(a, this.pageRect);
/*     */ 
/* 573 */     turnpage.findCorner(a, this.w, this.h);
/*     */ 
/* 576 */     PointF f = turnpage.dragCorner;
/* 577 */     if (turnpage.isCornerLock(a, f, this.w, this.h)) {
/* 578 */       a = turnpage.a1(a, f, turnpage.lockCorner);
/*     */     }
/*     */ 
/* 581 */     this.dragPoint.x = a.x;
/* 582 */     this.dragPoint.y = a.y;
/*     */ 
/* 586 */     turnpage.workOut(a, turnpage.dragCorner);
/*     */ 
/* 588 */     float halfx = this.w / 2.0F;
/* 589 */     float halfy = this.h / 2.0F;
/* 590 */     float thirdx = this.w / 4.0F;
/* 591 */     float thirdy = this.h / 4.0F;
/* 592 */     if (this.isFirst)
/*     */     {
/* 594 */       if (((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= halfx) && (this.dragPoint.y >= 0.0F) && (this.dragPoint.y <= thirdy)) || ((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= thirdx) && (this.dragPoint.y > thirdy) && (this.dragPoint.y <= halfy)))
/*     */       {
/* 596 */         return;
/*     */       }
/* 598 */       if (((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= halfx) && (this.dragPoint.y >= thirdy * 3.0F) && (this.dragPoint.y <= this.h)) || ((this.dragPoint.x >= 0.0F) && (this.dragPoint.x <= thirdx) && (this.dragPoint.y > halfy) && (this.dragPoint.y <= thirdy * 3.0F)))
/*     */       {
/* 600 */         return;
/*     */       }
/* 602 */       if (((this.dragPoint.x > halfx) && (this.dragPoint.x <= this.w) && (this.dragPoint.y >= 0.0F) && (this.dragPoint.y < thirdy)) || ((this.dragPoint.x >= thirdx * 3.0F) && (this.dragPoint.x <= this.w) && (this.dragPoint.y > thirdy) && (this.dragPoint.y < halfy)))
/*     */       {
/* 604 */         return;
/*     */       }
/* 606 */       if (((this.dragPoint.x > halfx) && (this.dragPoint.x <= this.w) && (this.dragPoint.y >= thirdy * 3.0F) && (this.dragPoint.y <= this.h)) || ((this.dragPoint.x >= thirdx * 3.0F) && (this.dragPoint.x <= this.w) && (this.dragPoint.y > halfy) && (this.dragPoint.y <= thirdy * 3.0F)))
/*     */       {
/* 608 */         return;
/*     */       }
/*     */ 
/* 612 */       this.ttd.menuOnOff();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
/*     */   {
/* 624 */     return false;
/*     */   }
/*     */ 
/*     */   public void onShowPress(MotionEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean onSingleTapUp(MotionEvent e)
/*     */   {
/* 636 */     return false;
/*     */   }
/*     */ }

/* Location:           I:\Workspaces\eclipse3.4\CCBookPro\lib\threedim.jar
 * Qualified Name:     com.chinachip.pageeffect.ThreeDimFlipper
 * JD-Core Version:    0.6.0
 */