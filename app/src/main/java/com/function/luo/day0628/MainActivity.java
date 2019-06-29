package com.function.luo.day0628;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主线程使用 Handler
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.tv_message)
    TextView tvMessage;


    private static final int HANDLER_UI = 1;
    private static final int MAIN_UI = 2;


    /**
     * 运行在主线程的Handler：使用Android默认的UI线程中的Looper
     */
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
                    String strData = (String) msg.obj;
                    tvMessage.setText(strData);
                    break;
                case MAIN_UI:
                    String strData2 = (String) msg.obj;
                    tvMessage.setText(strData2);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tvMessage.setText("主线程发送 Handler");

    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                //主线程发送
                Message message = Message.obtain();
                message.what = MAIN_UI;
                message.obj = "main发送消息的线程名称：" + Thread.currentThread().getName();
                handlerUI.sendMessage(message);

                break;
            case R.id.btn2:
                //子线程发送
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = HANDLER_UI;
                        message.obj = "子线程发送消息的线程名称：" + Thread.currentThread().getName();
                        handlerUI.sendMessage(message);

                    }
                }).start();
                break;

            case R.id.btn3:
                ThreadActivity.launch(MainActivity.this);
                break;
            default:
        }
    }


}
