package com.function.luo.day0628;

/**
 * Created by luo on 2019/6/28.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 子线程使用 Handler
 */
public class ThreadActivity extends AppCompatActivity {

    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    private Handler threadHandler;
    private static final int HANDLER_THREAD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvMessage.setText("子线程处理Handler，原始用法");
        createLooperHandler();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                break;
            case R.id.btn2:
                //子线程发送
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = HANDLER_THREAD;
                        message.obj = "子线程发送消息的线程名称：" + Thread.currentThread().getName();
                        threadHandler.sendMessage(message);

                    }
                }).start();
                break;
            case R.id.btn3:
                ThirdActivity.launch(ThreadActivity.this);
                break;

            default:
        }
    }


    /**
     * 创建一个可以包含looper的子线程，并开启
     */
    private void createLooperHandler() {
        MyThread myThread = new MyThread();
        myThread.start();

        //注释1
        SystemClock.sleep(100);
        threadHandler = new Handler(myThread.threadLooper) {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_THREAD:

                        //添加上当前线程名称
                        final String thrMsg = (String) msg.obj + "\n 收到的线程：" + Thread
                                .currentThread().getName();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                tvMessage.setText(thrMsg);
                            }
                        });

                        break;

                    default:
                        break;
                }
            }
        };
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, ThreadActivity.class);
        context.startActivity(intent);
    }


    private class MyThread extends Thread {

        private Looper threadLooper;

        @Override
        public void run() {
            Looper.prepare();
            threadLooper = Looper.myLooper();
            Looper.loop();
        }
    }
}
