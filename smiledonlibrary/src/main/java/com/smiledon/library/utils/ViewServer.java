//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.smiledon.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViewServer implements Runnable {
    private static final int VIEW_SERVER_DEFAULT_PORT = 4939;
    private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;
    private static final String BUILD_TYPE_USER = "user";
    private static final String LOG_TAG = "ViewServer";
    private static final String VALUE_PROTOCOL_VERSION = "4";
    private static final String VALUE_SERVER_VERSION = "4";
    private static final String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
    private static final String COMMAND_SERVER_VERSION = "SERVER";
    private static final String COMMAND_WINDOW_MANAGER_LIST = "LIST";
    private static final String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
    private static final String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";
    private ServerSocket mServer;
    private final int mPort;
    private Thread mThread;
    private ExecutorService mThreadPool;
    private final List<ViewServer.WindowListener> mListeners;
    private final HashMap<View, String> mWindows;
    private final ReentrantReadWriteLock mWindowsLock;
    private View mFocusedWindow;
    private final ReentrantReadWriteLock mFocusLock;
    private static ViewServer sServer;

    public static ViewServer get(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        if("user".equals(Build.TYPE) && (info.flags & 2) != 0) {
            if(sServer == null) {
                sServer = new ViewServer(4939);
            }

            if(!sServer.isRunning()) {
                try {
                    sServer.start();
                } catch (IOException var3) {
                    Log.d("ViewServer", "Error:", var3);
                }
            }
        } else {
            sServer = new ViewServer.NoopViewServer();
        }

        return sServer;
    }

    private ViewServer() {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = -1;
    }

    private ViewServer(int port) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = port;
    }

    public boolean start() throws IOException {
        if(this.mThread != null) {
            return false;
        } else {
            this.mThread = new Thread(this, "Local View Server [port=" + this.mPort + "]");
            this.mThreadPool = Executors.newFixedThreadPool(10);
            this.mThread.start();
            return true;
        }
    }

    public boolean stop() {
        if(this.mThread != null) {
            this.mThread.interrupt();
            if(this.mThreadPool != null) {
                try {
                    this.mThreadPool.shutdownNow();
                } catch (SecurityException var14) {
                    Log.w("ViewServer", "Could not stop all view server threads");
                }
            }

            this.mThreadPool = null;
            this.mThread = null;

            try {
                this.mServer.close();
                this.mServer = null;
                return true;
            } catch (IOException var15) {
                Log.w("ViewServer", "Could not close the view server");
            }
        }

        this.mWindowsLock.writeLock().lock();

        try {
            this.mWindows.clear();
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }

        this.mFocusLock.writeLock().lock();

        try {
            this.mFocusedWindow = null;
        } finally {
            this.mFocusLock.writeLock().unlock();
        }

        return false;
    }

    public boolean isRunning() {
        return this.mThread != null && this.mThread.isAlive();
    }

    public void addWindow(Activity activity) {
        String name = activity.getTitle().toString();
        if(TextUtils.isEmpty(name)) {
            name = activity.getClass().getCanonicalName() + "/0x" + System.identityHashCode(activity);
        } else {
            name = name + "(" + activity.getClass().getCanonicalName() + ")";
        }

        this.addWindow(activity.getWindow().getDecorView(), name);
    }

    public void removeWindow(Activity activity) {
        this.removeWindow(activity.getWindow().getDecorView());
    }

    public void addWindow(View view, String name) {
        this.mWindowsLock.writeLock().lock();

        try {
            this.mWindows.put(view.getRootView(), name);
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }

        this.fireWindowsChangedEvent();
    }

    public void removeWindow(View view) {
        this.mWindowsLock.writeLock().lock();

        View rootView;
        try {
            rootView = view.getRootView();
            this.mWindows.remove(rootView);
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }

        this.mFocusLock.writeLock().lock();

        try {
            if(this.mFocusedWindow == rootView) {
                this.mFocusedWindow = null;
            }
        } finally {
            this.mFocusLock.writeLock().unlock();
        }

        this.fireWindowsChangedEvent();
    }

    public void setFocusedWindow(Activity activity) {
        this.setFocusedWindow(activity.getWindow().getDecorView());
    }

    public void setFocusedWindow(View view) {
        this.mFocusLock.writeLock().lock();

        try {
            this.mFocusedWindow = view == null?null:view.getRootView();
        } finally {
            this.mFocusLock.writeLock().unlock();
        }

        this.fireFocusChangedEvent();
    }

    public void run() {
        try {
            this.mServer = new ServerSocket(this.mPort, 10, InetAddress.getLocalHost());
        } catch (Exception var5) {
            Log.w("ViewServer", "Starting ServerSocket error: ", var5);
        }

        while(this.mServer != null && Thread.currentThread() == this.mThread) {
            try {
                Socket client = this.mServer.accept();
                if(this.mThreadPool != null) {
                    this.mThreadPool.submit(new ViewServer.ViewServerWorker(client));
                } else {
                    try {
                        client.close();
                    } catch (IOException var3) {
                        var3.printStackTrace();
                    }
                }
            } catch (Exception var4) {
                Log.w("ViewServer", "Connection error: ", var4);
            }
        }

    }

    private static boolean writeValue(Socket client, String value) {
        BufferedWriter out = null;

        boolean result;
        try {
            OutputStream clientStream = client.getOutputStream();
            out = new BufferedWriter(new OutputStreamWriter(clientStream), 8192);
            out.write(value);
            out.write("\n");
            out.flush();
            result = true;
        } catch (Exception var13) {
            result = false;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException var12) {
                    result = false;
                }
            }

        }

        return result;
    }

    private void fireWindowsChangedEvent() {
        Iterator i$ = this.mListeners.iterator();

        while(i$.hasNext()) {
            ViewServer.WindowListener listener = (ViewServer.WindowListener)i$.next();
            listener.windowsChanged();
        }

    }

    private void fireFocusChangedEvent() {
        Iterator i$ = this.mListeners.iterator();

        while(i$.hasNext()) {
            ViewServer.WindowListener listener = (ViewServer.WindowListener)i$.next();
            listener.focusChanged();
        }

    }

    private void addWindowListener(ViewServer.WindowListener listener) {
        if(!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }

    }

    private void removeWindowListener(ViewServer.WindowListener listener) {
        this.mListeners.remove(listener);
    }

    private class ViewServerWorker implements Runnable, ViewServer.WindowListener {
        private Socket mClient;
        private boolean mNeedWindowListUpdate;
        private boolean mNeedFocusedWindowUpdate;
        private final Object[] mLock = new Object[0];

        public ViewServerWorker(Socket client) {
            this.mClient = client;
            this.mNeedWindowListUpdate = false;
            this.mNeedFocusedWindowUpdate = false;
        }

        public void run() {
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(this.mClient.getInputStream()), 1024);
                String request = in.readLine();
                int index = request.indexOf(32);
                String command;
                String parameters;
                if(index == -1) {
                    command = request;
                    parameters = "";
                } else {
                    command = request.substring(0, index);
                    parameters = request.substring(index + 1);
                }

                boolean result;
                if("PROTOCOL".equalsIgnoreCase(command)) {
                    result = ViewServer.writeValue(this.mClient, "4");
                } else if("SERVER".equalsIgnoreCase(command)) {
                    result = ViewServer.writeValue(this.mClient, "4");
                } else if("LIST".equalsIgnoreCase(command)) {
                    result = this.listWindows(this.mClient);
                } else if("GET_FOCUS".equalsIgnoreCase(command)) {
                    result = this.getFocusedWindow(this.mClient);
                } else if("AUTOLIST".equalsIgnoreCase(command)) {
                    result = this.windowManagerAutolistLoop();
                } else {
                    result = this.windowCommand(this.mClient, command, parameters);
                }

                if(!result) {
                    Log.w("ViewServer", "An error occurred with the command: " + command);
                }
            } catch (IOException var19) {
                Log.w("ViewServer", "Connection error: ", var19);
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException var18) {
                        var18.printStackTrace();
                    }
                }

                if(this.mClient != null) {
                    try {
                        this.mClient.close();
                    } catch (IOException var17) {
                        var17.printStackTrace();
                    }
                }

            }

        }

        private boolean windowCommand(Socket client, String command, String parameters) {
            boolean success = true;
            BufferedWriter out = null;

            boolean var10;
            try {
                int index = parameters.indexOf(32);
                if(index == -1) {
                    index = parameters.length();
                }

                String code = parameters.substring(0, index);
                int hashCode = (int)Long.parseLong(code, 16);
                if(index < parameters.length()) {
                    parameters = parameters.substring(index + 1);
                } else {
                    parameters = "";
                }

                View window = this.findWindow(hashCode);
                if(window != null) {
                    Method dispatch = ViewDebug.class.getDeclaredMethod("dispatchCommand", new Class[]{View.class, String.class, String.class, OutputStream.class});
                    dispatch.setAccessible(true);
                    dispatch.invoke((Object)null, new Object[]{window, command, parameters, new ViewServer.UncloseableOutputStream(client.getOutputStream())});
                    if(!client.isOutputShutdown()) {
                        out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                        out.write("DONE\n");
                        out.flush();
                    }

                    return success;
                }

                var10 = false;
            } catch (Exception var21) {
                Log.w("ViewServer", "Could not send command " + command + " with parameters " + parameters, var21);
                success = false;
                return success;
            } finally {
                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException var20) {
                        success = false;
                    }
                }

            }

            return var10;
        }

        private View findWindow(int hashCode) {
            Iterator i$;
            if(hashCode == -1) {
                i$ = null;
                ViewServer.this.mWindowsLock.readLock().lock();

                View window;
                try {
                    window = ViewServer.this.mFocusedWindow;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }

                return window;
            } else {
                ViewServer.this.mWindowsLock.readLock().lock();

                try {
                    i$ = ViewServer.this.mWindows.entrySet().iterator();

                    while(i$.hasNext()) {
                        Entry<View, String> entry = (Entry)i$.next();
                        if(System.identityHashCode(entry.getKey()) == hashCode) {
                            View var4 = (View)entry.getKey();
                            return var4;
                        }
                    }
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }

                return null;
            }
        }

        private boolean listWindows(Socket client) {
            boolean result = true;
            BufferedWriter out = null;

            try {
                ViewServer.this.mWindowsLock.readLock().lock();
                OutputStream clientStream = client.getOutputStream();
                out = new BufferedWriter(new OutputStreamWriter(clientStream), 8192);
                Iterator i$ = ViewServer.this.mWindows.entrySet().iterator();

                while(i$.hasNext()) {
                    Entry<View, String> entry = (Entry)i$.next();
                    out.write(Integer.toHexString(System.identityHashCode(entry.getKey())));
                    out.write(32);
                    out.append((CharSequence)entry.getValue());
                    out.write(10);
                }

                out.write("DONE.\n");
                out.flush();
            } catch (Exception var15) {
                result = false;
            } finally {
                ViewServer.this.mWindowsLock.readLock().unlock();
                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException var14) {
                        result = false;
                    }
                }

            }

            return result;
        }

        private boolean getFocusedWindow(Socket client) {
            boolean result = true;
            String focusName = null;
            BufferedWriter out = null;

            try {
                OutputStream clientStream = client.getOutputStream();
                out = new BufferedWriter(new OutputStreamWriter(clientStream), 8192);
                View focusedWindow = null;
                ViewServer.this.mFocusLock.readLock().lock();

                try {
                    focusedWindow = ViewServer.this.mFocusedWindow;
                } finally {
                    ViewServer.this.mFocusLock.readLock().unlock();
                }

                if(focusedWindow != null) {
                    ViewServer.this.mWindowsLock.readLock().lock();

                    try {
                        focusName = (String)ViewServer.this.mWindows.get(ViewServer.this.mFocusedWindow);
                    } finally {
                        ViewServer.this.mWindowsLock.readLock().unlock();
                    }

                    out.write(Integer.toHexString(System.identityHashCode(focusedWindow)));
                    out.write(32);
                    out.append(focusName);
                }

                out.write(10);
                out.flush();
            } catch (Exception var31) {
                result = false;
            } finally {
                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException var28) {
                        result = false;
                    }
                }

            }

            return result;
        }

        public void windowsChanged() {
            Object[] var1 = this.mLock;
            synchronized(this.mLock) {
                this.mNeedWindowListUpdate = true;
                this.mLock.notifyAll();
            }
        }

        public void focusChanged() {
            Object[] var1 = this.mLock;
            synchronized(this.mLock) {
                this.mNeedFocusedWindowUpdate = true;
                this.mLock.notifyAll();
            }
        }

        private boolean windowManagerAutolistLoop() {
            ViewServer.this.addWindowListener(this);
            BufferedWriter out = null;

            try {
                out = new BufferedWriter(new OutputStreamWriter(this.mClient.getOutputStream()));

                while(!Thread.interrupted()) {
                    boolean needWindowListUpdate = false;
                    boolean needFocusedWindowUpdate = false;
                    Object[] var4 = this.mLock;
                    synchronized(this.mLock) {
                        while(!this.mNeedWindowListUpdate && !this.mNeedFocusedWindowUpdate) {
                            this.mLock.wait();
                        }

                        if(this.mNeedWindowListUpdate) {
                            this.mNeedWindowListUpdate = false;
                            needWindowListUpdate = true;
                        }

                        if(this.mNeedFocusedWindowUpdate) {
                            this.mNeedFocusedWindowUpdate = false;
                            needFocusedWindowUpdate = true;
                        }
                    }

                    if(needWindowListUpdate) {
                        out.write("LIST UPDATE\n");
                        out.flush();
                    }

                    if(needFocusedWindowUpdate) {
                        out.write("FOCUS UPDATE\n");
                        out.flush();
                    }
                }
            } catch (Exception var16) {
                Log.w("ViewServer", "Connection error: ", var16);
            } finally {
                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException var14) {
                        ;
                    }
                }

                ViewServer.this.removeWindowListener(this);
            }

            return true;
        }
    }

    private static class NoopViewServer extends ViewServer {
        private NoopViewServer() {
            super();
        }

        public boolean start() throws IOException {
            return false;
        }

        public boolean stop() {
            return false;
        }

        public boolean isRunning() {
            return false;
        }

        public void addWindow(Activity activity) {
        }

        public void removeWindow(Activity activity) {
        }

        public void addWindow(View view, String name) {
        }

        public void removeWindow(View view) {
        }

        public void setFocusedWindow(Activity activity) {
        }

        public void setFocusedWindow(View view) {
        }

        public void run() {
        }
    }

    private static class UncloseableOutputStream extends OutputStream {
        private final OutputStream mStream;

        UncloseableOutputStream(OutputStream stream) {
            this.mStream = stream;
        }

        public void close() throws IOException {
        }

        public boolean equals(Object o) {
            return this.mStream.equals(o);
        }

        public void flush() throws IOException {
            this.mStream.flush();
        }

        public int hashCode() {
            return this.mStream.hashCode();
        }

        public String toString() {
            return this.mStream.toString();
        }

        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.mStream.write(buffer, offset, count);
        }

        public void write(byte[] buffer) throws IOException {
            this.mStream.write(buffer);
        }

        public void write(int oneByte) throws IOException {
            this.mStream.write(oneByte);
        }
    }

    private interface WindowListener {
        void windowsChanged();

        void focusChanged();
    }
}
