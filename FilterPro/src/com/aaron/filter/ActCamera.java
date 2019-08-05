package com.aaron.filter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
 
/**
 * Camera��Matrix�ıȽϣ�<br/>
 * Camera��rotate()��ط�����ָ��ĳһά������תָ���ĽǶȡ�<br/>
 * Matrix��rotate()��ط���ʵ�ֵ�Ч����˳ʱ����תָ���ĽǶȣ���Cameraָ��Z����תЧ����ͬ���������෴��<br/>
 * 
 * Camera��translate()��������ĳһά�����ӵ��λ��ʵ��ͼ������ţ���Matrix��scale()��ط�������Ч�����ƣ�
 * ֻ��Matrix��scale()��ط�����ֱ��ָ�����ű�����<br/>
 * 
 * Camera��֧����б������Matrix����ֱ��ʵ����б������<br/>
 * 
 * @author Sodino E-mail:sodinoopen@hotmail.com
 * @version Time��2011-9-26 ����04:17:49
 */
public class ActCamera extends Activity implements OnSeekBarChangeListener {
    private Camera camera;
    // views
    private SeekBar seekbarXRotate;
    private SeekBar seekbarYRotate;
    private SeekBar seekbarZRotate;
    /*private TextView txtXRotate;
    private TextView txtYRotate;
    private TextView txtZRotate;*/
    private SeekBar seekbarXSkew;
    private SeekBar seekbarYSkew;
    private SeekBar seekbarZTranslate;
   /* private TextView txtXTranslate;
    private TextView txtYTranslate;
    private TextView txtZTranslate;*/
    private ImageView imgResult;
    // integer params
    private int rotateX, rotateY, rotateZ;
    private float skewX, skewY;
    private int translateZ;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camera);
        // camera
        camera = new Camera();
        // initViews
        // rotate
        seekbarXRotate = (SeekBar) findViewById(R.id.seekbarXRotate);
        seekbarXRotate.setOnSeekBarChangeListener(this);
        seekbarYRotate = (SeekBar) findViewById(R.id.seekbarYRotate);
        seekbarYRotate.setOnSeekBarChangeListener(this);
        seekbarZRotate = (SeekBar) findViewById(R.id.seekbarZRotate);
        seekbarZRotate.setOnSeekBarChangeListener(this);
        /*txtXRotate = (TextView) findViewById(R.id.txtXRotate);
        txtYRotate = (TextView) findViewById(R.id.txtYRotate);
        txtZRotate = (TextView) findViewById(R.id.txtZRotate);*/
        // translate
        seekbarXSkew = (SeekBar) findViewById(R.id.seekbarXSkew);
        seekbarXSkew.setOnSeekBarChangeListener(this);
        seekbarYSkew = (SeekBar) findViewById(R.id.seekbarYSkew);
        seekbarYSkew.setOnSeekBarChangeListener(this);
        seekbarZTranslate = (SeekBar) findViewById(R.id.seekbarZTranslate);
        seekbarZTranslate.setOnSeekBarChangeListener(this);
        /*txtXTranslate = (TextView) findViewById(R.id.txtXSkew);
        txtYTranslate = (TextView) findViewById(R.id.txtYSkew);
        txtZTranslate = (TextView) findViewById(R.id.txtZTranslate);*/
        imgResult = (ImageView) findViewById(R.id.imgResult);
        // refresh
        refreshImage();
    }
 
    private void refreshImage() {
        // ��ȡ�������ͼ��
        BitmapDrawable tmpBitDra = (BitmapDrawable) getResources().getDrawable(R.drawable.honeycomb);
        Bitmap tmpBit = tmpBitDra.getBitmap();
        // ��ʼ����ͼ��
        // 1.��ȡ�������
        // ��¼һ�³�ʼ״̬��save()��restore()���Խ�ͼ����ɵ����һЩ��
        // Each save should be balanced with a call to restore().
        camera.save();
        Matrix matrix = new Matrix();
        // rotate
        camera.rotateX(rotateX);
        camera.rotateY(rotateY);
        camera.rotateZ(rotateZ);
        // translate
        camera.translate(0, 0, translateZ);
        camera.getMatrix(matrix);
        // �ָ���֮ǰ�ĳ�ʼ״̬��
        camera.restore();
        // ����ͼ��������ĵ�
        matrix.preTranslate(tmpBit.getWidth() >> 1, tmpBit.getHeight() >> 1);
        matrix.preSkew(skewX, skewY);
        // matrix.postSkew(skewX, skewY);
        // ֱ��setSkew()����ǰ�洦���rotate()��translate()�ȵȶ�����Ч��
        // matrix.setSkew(skewX, skewY);
        // 2.ͨ������������ͼ��(��ֱ��������Canvas)
        Bitmap newBit = null;
        try {
          // ��������ת�����ͼ�����п��ܲ�����0����ʱ���׳�IllegalArgumentException
            newBit = Bitmap.createBitmap(tmpBit, 0, 0, tmpBit.getWidth(), tmpBit.getHeight(), matrix, true);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        if (newBit != null) {
            imgResult.setImageBitmap(newBit);
        }
    }
 
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seekbarXRotate) {
            //txtXRotate.setText(progress + "�b");
            rotateX = progress;
            Log.i("aaron", "rotateX "+rotateX);
        } else if (seekBar == seekbarYRotate) {
            //txtYRotate.setText(progress + "�b");
            rotateY = progress;
            Log.i("aaron", "rotateY "+rotateY);
        } else if (seekBar == seekbarZRotate) {
            //txtZRotate.setText(progress + "�b");
            rotateZ = progress;
            Log.i("aaron", "rotateZ "+rotateZ);
        } else if (seekBar == seekbarXSkew) {
            skewX = (progress - 100) * 1.0f / 100;
            Log.i("aaron", "skewX "+skewX);
            //txtXTranslate.setText(String.valueOf(skewX));
        } else if (seekBar == seekbarYSkew) {
            skewY = (progress - 100) * 1.0f / 100;
            Log.i("aaron", "skewY "+skewY);
            //txtYTranslate.setText(String.valueOf(skewY));
        } else if (seekBar == seekbarZTranslate) {
            translateZ = progress - 100;
            Log.i("aaron", "translateZ "+translateZ);
            //txtZTranslate.setText(String.valueOf(translateZ));
        }
        refreshImage();
    }
 
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
 
    }
 
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
 
    }
}