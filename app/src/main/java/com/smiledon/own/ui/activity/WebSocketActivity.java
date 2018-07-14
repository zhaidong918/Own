package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityWebsocketBinding;
import com.smiledon.own.net.SSLSocketClient;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 记录安卓系统的由来
 *
 * @author zhaidong
 * @date 2018/2/22 16:58
 */

public class WebSocketActivity extends BaseActivity{

    public static final String TAG = WebSocketActivity.class.getSimpleName();

    ActivityWebsocketBinding mBinding;


    public static final String BASE_URL = "wss://websocket-api.newifi.com";

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_websocket);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        initWebSocketListener();
        initOkhttpClient();
        initEvent();

        mBinding.logTv.setMovementMethod(new ScrollingMovementMethod());

    }

    OkHttpClient mOkHttpClient;

    WebSocketListener mWebSocketListener;
    WebSocket mWebSocket;
    private void initWebSocketListener() {

        mWebSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                addLog("Open");
                addLog(mWebSocket.toString());
                addLog(webSocket.toString());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                addLog("onMessage：" + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                addLog("onMessage" );
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                addLog("onClosing");
                addLog("code：" + code);
                addLog("reason：" + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                addLog("onClosed");
                addLog("code：" + code);
                addLog("reason：" + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                addLog("onFailure");
                addLog("t：" + t.getMessage());
            }
        };
    }


    private void initOkhttpClient() {

        //设置连接超时时间
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(9 * 10, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

    }

    private void initEvent() {

        mBinding.openBtn.setOnClickListener(v -> {
            Request request = new Request.Builder().url(BASE_URL).build();
            mWebSocket = mOkHttpClient.newWebSocket(request, mWebSocketListener);
        });

        mBinding.closeBtn.setOnClickListener(v -> {
            if (mWebSocket == null) {
                ToastUtils.showToast("请先开启");
                return;
            }
            addLog("close：" + mWebSocket.close(1000, "i want close"));
        });

        mBinding.logTv.setOnLongClickListener(v -> {
            stringBuilder.setLength(0);
            mBinding.logTv.setText(stringBuilder.toString());
            return false;
        });

    }

    StringBuilder stringBuilder = new StringBuilder();

    private void addLog(String content) {

        runOnUiThread(() -> {
            stringBuilder.append(content).append("\n");
            mBinding.logTv.setText(stringBuilder.toString());
        });

    }
}
