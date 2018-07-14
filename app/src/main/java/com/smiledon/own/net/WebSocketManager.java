package com.smiledon.own.net;

import android.util.Log;

import com.dhh.websocket.Config;
import com.dhh.websocket.RxWebSocket;
import com.dhh.websocket.WebSocketSubscriber;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smiledon.own.reflect.TypeBuilder;
import com.smiledon.own.utils.LogUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;

/**
 * Created by SmileDon on 2018/5/10.
 */

public class WebSocketManager {

    private static WebSocketManager mWebSocketManager;

    OkHttpClient client;
    String wsUrl = "wss://192.168.102.166:8882/";

    private WebSocketManager(){
        initMockWebServer();
        initClient();
        initRxWebSocket();
    }

    public static WebSocketManager geInstance(){
        if(mWebSocketManager == null){
            synchronized (WebSocketManager.class){
                if(mWebSocketManager == null){
                    mWebSocketManager = new WebSocketManager();
                }
            }
        }
        return mWebSocketManager;
    }

    private int msgCount = 0;

    private MockWebServer mockWebServer;

    private void initMockWebServer(){


        mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("server onOpen");
                System.out.println("server request header:" + response.request().headers());
                System.out.println("server response header:" + response.headers());
                System.out.println("server response:" + response);
            }
            @Override
            public void onMessage(WebSocket webSocket, String string) {
                System.out.println("server onMessage");
                System.out.println("message:" + string);
                //接受到5条信息后，关闭消息定时发送器
                if(msgCount == 5){
                    mTimer.cancel();
                    webSocket.close(1000, "close by server");
                    return;
                }
                webSocket.send("response-" + string);
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("server onClosing");
                System.out.println("code:" + code + " reason:" + reason);
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("server onClosed");
                System.out.println("code:" + code + " reason:" + reason);
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                //出现异常会进入此回调
                System.out.println("server onFailure");
                System.out.println("throwable:" + t);
                System.out.println("response:" + response);
            }
        }));
    }

    private WebSocket mWebSocket;

    private void initClient(){

//        String wsUrl = "ws://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort() + "/";
        System.out.println("wsUrl：" + wsUrl);

        //新建client

        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//允许失败重试
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();
    }

    private void initRxWebSocket(){

        //init config
        Config config = new Config.Builder()
                .setShowLog(true)           //show  log
                .setClient(client)   //if you want to set your okhttpClient
                .setShowLog(true, "your logTag")
                .setReconnectInterval(2, TimeUnit.SECONDS)  //set reconnect interval
                .build();
        RxWebSocket.setConfig(config);

        RxWebSocket.get(wsUrl)
                .subscribe(new WebSocketSubscriber() {
                    @Override
                    protected void onMessage(@NonNull String text) {
                        System.out.println("Response: " + text);


                        System.out.println(fromJsonObject(text, MarketState.class).getResult().toString());
                    }
                });


    }

    public static <T> BaseSocketResponse<T> fromJsonObject(String jsonObject, Class<T> tClass){
        Type type = TypeBuilder
                .newInstance(BaseSocketResponse.class)
                .addTypeParam(tClass)
                .build();

        return new Gson().fromJson(jsonObject, type);
    }


    public static <T> BaseSocketResponse<T> fromJsonAarry(String jsonObject, Class<T> tClass){
        Type type = TypeBuilder
                .newInstance(BaseSocketResponse.class)
                .beginSubType(List.class)
                .addTypeParam(tClass)
                .endSubType()
                .build();

        return new Gson().fromJson(jsonObject, type);
    }

    public static <T> BaseSocketResponse<T> GsonToList(String gsonString, Class<T> cls) {
        BaseSocketResponse<T> list = null;
        Gson gson = new Gson();
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<BaseSocketResponse<T>>() {
            }.getType());
        }
        return list;
    }

    public static <T> BaseSocketResponse<T> fromJson(String json) {
        Gson gson = new Gson();
        Type objectType = new TypeToken<BaseSocketResponse<T>>() {}.getType();
        return gson.fromJson(json, objectType);

    }

    private void connect(){

        //构造request对象
        Request request = new Request.Builder()
                .url(wsUrl)

                .build();
        //建立连接
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                mWebSocket = webSocket;
                System.out.println("client onOpen");
                System.out.println("client request header:" + response.request().headers());
                System.out.println("client response header:" + response.headers());
                System.out.println("client response:" + response);
                //开启消息定时发送
//                startTask();

            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("client onMessage");
                System.out.println("message:" + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("client onClosing");
                System.out.println("code:" + code + " reason:" + reason);
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("client onClosed");
                System.out.println("code:" + code + " reason:" + reason);
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                //出现异常会进入此回调
                System.out.println("client onFailure");
                System.out.println("throwable:" + t);
                System.out.println("response:" + response);

                mWebSocket.queueSize();
            }
        });
    }

    private Timer mTimer;
    //每秒发送一条消息
    private void startTask(){
        mTimer= new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mWebSocket == null) return;
                msgCount++;
                boolean isSuccessed = mWebSocket.send("msg" + msgCount + "-" + System.currentTimeMillis());
                //除了文本内容外，还可以将如图像，声音，视频等内容转为ByteString发送
                //boolean Reverse(ByteString bytes);
            }
        };
        mTimer.schedule(timerTask, 0, 1000);
    }


    public void sendRequest(){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "kline.query");
        jsonObject.addProperty("id", 1);

        long time = System.currentTimeMillis()/1000;

        JsonArray jsonArray = new JsonArray();
        jsonArray.add("NEWGBTC");
        jsonArray.add(time - 24*60*60);
        jsonArray.add(time);
        jsonArray.add(5*60);
        jsonObject.addProperty("params", jsonArray.toString());

        System.out.println("Request:" + jsonObject.toString());
        RxWebSocket.send(wsUrl, jsonObject.toString());
    }

    public void sendGsonRequest(){

        SocketModule socketModule = new SocketModule();
        socketModule.klineSubscribeModule();
        String request = new Gson().toJson(socketModule);
        System.out.println("Request:" + request);
        RxWebSocket.send(wsUrl,request);

    }

    class SocketModule{
        public String method;
        public int id;
        public Object[] params;

        public SocketModule(){

            long time = System.currentTimeMillis()/1000;

            method = "kline.query";
            id = 1;
            params = new Object[]{"NEWGBTC",time - 24*60*60,time, 30*60};
        }

        public void klineSubscribeModule(){

            method = "state.query";
            id = 1;
            params = new Object[]{"NEWGBTC", 86400};
        }
    }

    public void close(){
        if(mWebSocket != null)
            mWebSocket.close(1000, "退出应用了");
    }

}
