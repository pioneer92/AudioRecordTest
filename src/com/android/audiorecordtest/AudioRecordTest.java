package com.android.audiorecordtest;

import android.app.Activity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecordTest extends Activity
{
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audiorecordtest.3gp";

    private RecordButton mRecordButton = null;
    private StopButton stopButton = null;
    private MediaRecorder mRecorder = null;
    private LisentButton lisentButton=null;
    
    double MaxAmplitude1=1;
    
    private TextView tv=null;
	private Timer mTimer=null;
	
	double a=22,b=1.2,c=0,d=28,e=0;
	
	boolean flag=true,lflag=false;
	
	EditText e1=null,e2=null,e3=null,e4=null,e5=null;
	
    private void startRecording() {							//��ʼ¼������mRecorder���г�ʼ��
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("AudioRecordTest", "prepare() failed");
        }
        mRecorder.start();   
    }

    private void stopRecording() {							//¼���������ͷ�mRecorder��Դ
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {						//RecordButton��Ķ��壬��������¼��͹��캯��				
        boolean mStartRecording = true;
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
            	a=Double.parseDouble(e1.getText().toString());
            	b=Double.parseDouble(e2.getText().toString());
            	c=Double.parseDouble(e3.getText().toString());
            	d=Double.parseDouble(e4.getText().toString());
            	e=Double.parseDouble(e5.getText().toString());
                if (mStartRecording) {
                	startRecording();
                    setText("Stop recording");
                } else {
                	stopRecording();
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };
        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
    
    class StopButton extends Button {	
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
            	
                if (flag) {
                	flag=false;
                    setText("Start");
                } else {
                	flag=true;
                    setText("Stop");
                }
            }
        };
        public StopButton(Context ctx) {
            super(ctx);
            setText("Stop");
            setOnClickListener(clicker);
        }
    }    
    
    class LisentButton extends Button {	
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
            	lflag=!lflag;
            	if (lflag) {
					MaxAmplitude1=0;
		            setText("����¼��������");
				}
            }
        };
        public LisentButton(Context ctx) {
            super(ctx);
            setText("��ʼ¼��������");
            setOnClickListener(clicker);
        }
    }

    @Override
    public void onCreate(Bundle icicle) {					//Activity�ĳ�ʼ����Ϊ�������mRecordButton��TextView
        super.onCreate(icicle);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        

        stopButton = new StopButton(this);
        ll.addView(stopButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        lisentButton=new LisentButton(this);
        ll.addView(lisentButton,
                new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    0));
        
        
        tv=new TextView(this);
        tv.setTextSize(30);
        ll.addView(tv,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        
        e1=new EditText(this);
        e2=new EditText(this);
        e3=new EditText(this);
        e4=new EditText(this);
        e5=new EditText(this);
        
        e1.setTextSize(25);
        e2.setTextSize(25);
        e3.setTextSize(25);
        e4.setTextSize(25);
        e5.setTextSize(25);
        
        e1.setText(a+"");
        e2.setText(b+"");
        e3.setText(c+"");
        e4.setText(d+"");
        e5.setText(e+"");
        

        ll.addView(e1,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        

        ll.addView(e2,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        

        ll.addView(e3,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        

        ll.addView(e4,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        

        ll.addView(e5,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        
        setContentView(ll);
        
        
    }

    @Override
	protected void onStart() {								
        final Runnable runnable=new Runnable() {			//��̨�߳�ʵ���������ݵļ�¼��ת��Ϊ�ֱ�
			@Override
			public void run() {
				if (mRecorder!=null) {	
					//ͨ��getMaxAmplitude()������õ�ǰ��������������ù�ʽ dB = 20*Math.log10(��õ����) �õ������ķֱ�
	            	double MaxAmplitude=mRecorder.getMaxAmplitude();

	            	//����Ǿ���С���ȵĹ�ʽ dB = 31*Math.log10(��õ����)-25
	            	tv.setText(20*Math.log10(MaxAmplitude)+"");
	            	
	            	//��Ϊ�ֻ�����Ϊרҵ�ķֱ��ǣ�������Ҫ��������ȸı���ܽӽ���ʵֵ dB = 22*Math.log10((��õ����)^1.4)-23
	            	tv.setText(tv.getText()+"\n\n"+(a*Math.log10(Math.pow(MaxAmplitude,b))+c)+"");
	            	
	            	//����Ǿ���С���ȵĹ�ʽ dB = 31*Math.log10(��õ����)-25
	            	tv.setText(tv.getText()+"\n\n"+(d*Math.log10(MaxAmplitude)+e+""));
	            	
	            	
	            	if(lflag){
	            		MaxAmplitude1=MaxAmplitude1>MaxAmplitude?MaxAmplitude1:MaxAmplitude;
	            	}
	            	else {
						lisentButton.setText("���ֱ�Ϊ��"+20*Math.log10(MaxAmplitude1));
					}
				}
			}
		};
        mTimer = new Timer();  								//��ʱ����ʵ��ÿ0.5�����һ�κ�̨�̸߳�������
		mTimer.schedule(new TimerTask() {    
            public void run() {
            	if (flag) {
                	runOnUiThread(runnable);
				}
            }
        }, 500,500);
		super.onStart();
	}

	@Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}