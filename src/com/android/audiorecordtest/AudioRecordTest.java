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
	
    private void startRecording() {							//开始录音，对mRecorder进行初始化
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

    private void stopRecording() {							//录音结束，释放mRecorder资源
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {						//RecordButton类的定义，包括点击事件和构造函数				
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
    public void onCreate(Bundle icicle) {					//Activity的初始化，为界面添加mRecordButton和TextView
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
        final Runnable runnable=new Runnable() {			//后台线程实现声音数据的记录和转换为分贝
			@Override
			public void run() {
				if (mRecorder!=null) {	
					
					//通过getMaxAmplitude()方法获得当前声音的振幅，利用公式 dB = 20*Math.log10(获得的振幅) 得到声音的分贝
	            	double MaxAmplitude=mRecorder.getMaxAmplitude();
	            	
	            	//因为手机并非为专业的分贝仪，所以需要经过大幅度改变才能接近真实值 dB = 22*Math.log10((获得的振幅)^1.4)-23
	            	tv.setText(22*Math.log10(Math.pow(MaxAmplitude,1.4))-23+"");
	            	
	            	//这个是经过小幅度的公式 dB = 31*Math.log10(获得的振幅)-25
	            	tv.setText(tv.getText()+"\n\n"+(31*Math.log10(MaxAmplitude)-25+""));
				}
			}
		};
        mTimer = new Timer();  								//定时器，实现每0.5秒调用一次后台线程更新数据
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