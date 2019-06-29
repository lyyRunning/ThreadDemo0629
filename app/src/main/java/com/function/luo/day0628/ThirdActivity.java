package com.function.luo.day0628;

/**
 * Created by luo on 2019/6/28.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
public class ThirdActivity extends AppCompatActivity {

    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    private Handler handlerThreadHandler;
    private static final int HANDLER_THREAD = 3;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvMessage.setText("子线程处理Handler，正常用法");
        btn3.setText("Post发信息");
        createLooperHandler();
    }

    @OnClick({R.id.btn1, R.id.btn2,R.id.btn3})
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
                        message.obj = "发送的线程：" + Thread.currentThread().getName();
                        handlerThreadHandler.sendMessage(message);

                    }
                }).start();
                break;
            case R.id.btn3:
                postMessage();
                break;
            default:
        }
    }




    private void postMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvMessage.setText("用 Post 发送信息");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 创建一个可以包含looper的子线程，并开启
     */
    private void createLooperHandler() {
        HandlerThread handlerThread = new HandlerThread("handler_name1");
        handlerThread.start();

        handlerThreadHandler = new Handler(handlerThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
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
        Intent intent = new Intent(context, ThirdActivity.class);
        context.startActivity(intent);
    }


}
