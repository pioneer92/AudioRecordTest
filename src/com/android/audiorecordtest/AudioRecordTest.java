package com.android.audiorecordtest;

import android.app.Activity;
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
    private MediaRecorder mRecorder = null;
    
    private TextView tv=null;
	private Timer mTimer=null;
	
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
        tv=new TextView(this);
        tv.setTextSize(35);
        ll.addView(tv,
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
	            	
	            	//��Ϊ�ֻ�����Ϊרҵ�ķֱ��ǣ�������Ҫ��������ȸı���ܽӽ���ʵֵ dB = 22*Math.log10((��õ����)^1.4)-23
	            	tv.setText(22*Math.log10(Math.pow(MaxAmplitude,1.4))-23+"");
	            	
	            	//����Ǿ���С���ȵĹ�ʽ dB = 31*Math.log10(��õ����)-25
	            	tv.setText(tv.getText()+"\n\n"+(31*Math.log10(MaxAmplitude)-25+""));
				}
			}
		};
        mTimer = new Timer();  								//��ʱ����ʵ��ÿ0.5�����һ�κ�̨�̸߳�������
		mTimer.schedule(new TimerTask() {    
            public void run() {
            	runOnUiThread(runnable);
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