package com.czstudio.czlibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by cheng on 2017/12/3.
 *
 * php 接口代码
 public function upload_image(){
 //路径自己定义
 $path='uploads/picture/'.date('Y-m-d',time()).'/';
 if(!is_dir($path)){
 mkdir($path);
 if(!is_dir($path)){
 $this->feedBackData('0','','create folder fail','');
 }
 }
 if ($_FILES['file']['error'] > 0){
 $this->feedBackData('0','','Return Code: ' . $_FILES['file']['error'],'');
 }else{
 if (file_exists($path . $_FILES['file']['name'])){
 $this->feedBackData('0','',$_FILES['file']['name'] . ' already exists. ','');
 }else{
 $filePath=$path . $_FILES['file']['name'];
 move_uploaded_file($_FILES['file']['tmp_name'],$filePath);
 $this->feedBackData('1', $filePath,json_encode($_POST),'');
 }
 }
 }
 */

public class CzSys_HTTP {

    /**
     * HTTP GET 请求
     *
     * @param address
     * @param paramsMap
     * @param httpListener
     */
    public static void requestGet(final Activity activity, final String address, final HashMap<String, String> paramsMap, final HttpListener httpListener) {
        new Thread() {
            public void run() {
                try {
                    //String address = "https://xxx.com/getUsers?";
                    StringBuilder tempParams = new StringBuilder();
                    int pos = 0;
                    for (String key : paramsMap.keySet()) {
                        if (pos > 0) {
                            tempParams.append("&");
                        }
                        tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                        pos++;
                    }
                    String requestUrl = address + "?" + tempParams.toString();
                    // 新建一个URL对象
                    URL url = new URL(requestUrl);
                    // 打开一个HttpURLConnection连接
                    final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    // 设置连接主机超时时间
                    urlConn.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    urlConn.setReadTimeout(5 * 1000);
                    // 设置是否使用缓存  默认是true
                    urlConn.setUseCaches(true);
                    // 设置为Post请求
                    urlConn.setRequestMethod("GET");
                    //urlConn设置请求头信息
                    //设置请求中的媒体类型信息。
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    //设置客户端与服务连接类型
                    urlConn.addRequestProperty("Connection", "Keep-Alive");
                    // 开始连接
                    urlConn.connect();
                    // 向Listener返回请求成败
                    final int responseCode = urlConn.getResponseCode();
                    if (200 == responseCode) {
                        // 获取返回的数据
                        final String result = streamToString(urlConn.getInputStream());
                        if (result != null) {
                            if (httpListener != null) {
                                activity.runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                httpListener.onHttpSuccess(result);
                                            }
                                        }
                                );
                            }
                            Log.e("requestGet", "GET Request SUCCESS，result--->" + result);
                        } else {
                            if (httpListener != null) {
                                activity.runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                httpListener.onHttpNull();
                                            }
                                        }
                                );
                            }
                            Log.e("requestGet", "GET Request get Null Data");
                        }

                    } else {
                        if (httpListener != null) {
                            activity.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            httpListener.onHttpFail("Fail!getResponseCode:" + responseCode);
                                        }
                                    }
                            );
                        }
                        Log.e("requestGet", "GET Request FAIL");
                    }
                    // 关闭连接
                    urlConn.disconnect();
                } catch (final Exception e) {
                    if (httpListener != null) {
                        activity.runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        httpListener.onHttpFail("Fail!Connect Exception:" + e);
                                    }
                                }
                        );
                    }
                    Log.e("requestGet", e.toString());
                }
            }
        }.start();
    }

    /**
     * HTTP POST 请求
     *
     * @param activity 用于获取主线程载体
     * @param address 接口地址
     * @param paramsMap POST 传参
     * @param httpListener 监听
     */
    public static void requestPost(final Activity activity, final String address, final HashMap<String, String> paramsMap, final HttpListener httpListener) {
        new Thread() {
            public void run() {
                try {
                    //合成参数
                    StringBuilder tempParams = new StringBuilder();
                    int pos = 0;
                    for (String key : paramsMap.keySet()) {
                        if (pos > 0) {
                            tempParams.append("&");
                        }
                        tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                        pos++;
                    }
                    String params = tempParams.toString();
                    // 请求的参数转换为byte数组
                    byte[] postData = params.getBytes();
                    // 新建一个URL对象
                    URL url = new URL(address);
                    // 打开一个HttpURLConnection连接
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    // 设置连接超时时间
                    urlConn.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    urlConn.setReadTimeout(5 * 1000);
                    // Post请求必须设置允许输出 默认false
                    urlConn.setDoOutput(true);
                    //设置请求允许输入 默认是true
                    urlConn.setDoInput(true);
                    // Post请求不能使用缓存
                    urlConn.setUseCaches(false);
                    // 设置为Post请求
                    urlConn.setRequestMethod("POST");
                    //设置本次连接是否自动处理重定向
                    urlConn.setInstanceFollowRedirects(true);
                    // 配置请求Content-Type
                    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    // 开始连接
                    urlConn.connect();
                    // 发送请求参数
                    DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                    dos.write(postData);
                    dos.flush();
                    dos.close();
                    // 向Listener返回请求成败
                    final int responseCode = urlConn.getResponseCode();
                    if (200 == responseCode) {
                        // 获取返回的数据
                        final String result = streamToString(urlConn.getInputStream());
                        if (result != null) {
                            if (httpListener != null) {
                                activity.runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                httpListener.onHttpSuccess(result);
                                            }
                                        }
                                );
                            }
                            Log.e("requestPOST", "POST Request SUCCESS，result--->" + result);
                        } else {
                            if (httpListener != null) {
                                activity.runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                httpListener.onHttpNull();
                                            }
                                        }
                                );
                            }
                            Log.e("requestPOST", "POST Request get Null Data");
                        }

                    } else {
                        if (httpListener != null) {
                            activity.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            httpListener.onHttpFail("Fail!getResponseCode:" + responseCode);
                                        }
                                    }
                            );
                        }
                        Log.e("requestPOST", "POST Request FAIL，code"+responseCode);
                    }
                    // 关闭连接
                    urlConn.disconnect();
                } catch (final Exception e) {
                    if (httpListener != null) {
                        activity.runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        httpListener.onHttpFail("Fail!Connect Exception:" + e);
                                    }
                                }
                        );
                    }
                    Log.e("requestPOST", e.toString());
                }
            }
        }.start();
    }

    /**
     * 图片上传
     * @param activity 用于获取主线程载体
     * @param address 接口地址
     * @param paramsMap POST 传参
     * @param httpListener 监听
     */
    public static void uploadImageByteArray(final Activity activity, final String address,
                                       final HashMap<String ,String> paramsMap, final byte[] bytes,
                                       final HttpListener httpListener) {
        if(bytes==null){
            Toast.makeText(activity.getApplicationContext(),"图片数据为空",Toast.LENGTH_SHORT).show();
            if (httpListener != null) {
                httpListener.onHttpFail("imageFullPath==nul");
            }
            return;
        }
        new Thread() {
            public void run() {
                String end = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                try {
                    URL url = new URL(address);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setChunkedStreamingMode(128 * 1024);// 128K

                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);

                    urlConn.setRequestMethod("POST");

                    urlConn.setRequestProperty("Connection", "Keep-Alive");
                    //urlConn.setRequestProperty("Charset", "UTF-8");
                    urlConn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    DataOutputStream ds = new DataOutputStream(urlConn.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    //加入 POST 参数 start
                    //tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    if (paramsMap != null) {
                        StringBuilder tempParams = new StringBuilder();
                        for (String key : paramsMap.keySet()) {
                            ds.writeBytes("Content-Disposition: form-data; name=\"" + key
                                    + "\"\r\n");
                            ds.writeBytes("\r\n" + paramsMap.get(key)
                                    + "\r\n");
                            // 分隔符
                            ds.writeBytes("--" + boundary + "\r\n");
                        }
                    }
                    //加入 POST 参数 end
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curDate =  new Date(System.currentTimeMillis());
                    ds.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                            + "img_byte_array"+formatter.format(curDate)
                            + "\"" + end);
                    ds.writeBytes(end);

                    //Log.e("CzLibrary", "filePath=" + imageFullPath);
//                    File testFile = new File(imageFullPath);
//                    if (testFile.exists()) {
//                        Log.e("CzLibrary", "file exist length=" + testFile.length());
//                    } else {
//                        Log.e("CzLibrary", "no file");
//                    }
//                    FileInputStream fStream = new FileInputStream(imageFullPath);

//                    byte[] buffer = new byte[8192];
//                    int size = 0;
//                    int length = 0;
//                    while ((length = fStream.read(buffer)) != -1) {
                        ds.write(bytes, 0, bytes.length);
//                        size += length;
//                    }
//                    fStream.close();
//                    Log.e("CzLibrary", "uploadImage fileLength=" + size);
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    ds.flush();

                    final String strJson = streamToString(urlConn.getInputStream());
                    Log.e("CzLibrary", "Upload ImageDataArray Get JSON:" + strJson);
                    if (httpListener != null) {
                        if (strJson != null) {
                            activity.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            httpListener.onHttpSuccess(strJson);
                                        }
                                    }
                            );
                        } else {
                            activity.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            httpListener.onHttpNull();
                                        }
                                    }
                            );
                        }
                    }
                } catch (final Exception e) {
                    activity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    httpListener.onHttpFail("Fail! Connect Exception:"+e);
                                }
                            }
                    );
                    Log.e("CzLibrary", "Fail! Connect Exception:"+e);
                }
            }
        }.start();


    }

    /**
     * 图片上传
     * @param activity 用于获取主线程载体
     * @param address 接口地址
     * @param paramsMap POST 传参
     * @param imageFullPath 图片路径
     * @param httpListener 监听
     *
     * 注意：如果涉及中文，则要在android端URL编码
     *                     URLEncoder.encode("XXXX"));
     *                     PHP端URL解码
     *                     $_POST['name']=urldecode($_POST['name']);
     */
    public static void uploadImageFile(final Activity activity, final String address,
                                                     final HashMap<String ,String> paramsMap, final String imageFullPath,
                                                     final HttpListener httpListener) {
        if(imageFullPath==null){
            Toast.makeText(activity.getApplicationContext(),"图片路径为空",Toast.LENGTH_SHORT).show();
            if (httpListener != null) {
                httpListener.onHttpFail("imageFullPath==nul");
            }
            return;
        }
        new Thread() {
            public void run() {
                String end = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                try {
                    URL url = new URL(address);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setChunkedStreamingMode(128 * 1024);// 128K

                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);

                    urlConn.setRequestMethod("POST");

                    urlConn.setRequestProperty("Connection", "Keep-Alive");
                    //urlConn.setRequestProperty("Charset", "UTF-8");
                    urlConn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    DataOutputStream ds = new DataOutputStream(urlConn.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    //加入 POST 参数 start
                    if (paramsMap != null) {
                        StringBuilder tempParams = new StringBuilder();
                        for (String key : paramsMap.keySet()) {
                            ds.writeBytes("Content-Disposition: form-data; name=\"" + key
                                    + "\"\r\n");
                            ds.writeBytes("\r\n" + paramsMap.get(key)
                                    + "\r\n");
                            // 分隔符
                            ds.writeBytes("--" + boundary + "\r\n");
                        }
                    }

                    //加入 POST 参数 end

                    ds.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                            + imageFullPath.substring(imageFullPath.lastIndexOf("/") + 1)
                            + "\"" + end);
                    ds.writeBytes(end);



                    Log.e("CzLibrary", "filePath=" + imageFullPath);
                    File testFile = new File(imageFullPath);
                    if (testFile.exists()) {
                        Log.e("CzLibrary", "file exist length=" + testFile.length());
                    } else {
                        Log.e("CzLibrary", "no file");
                    }
                    FileInputStream fStream = new FileInputStream(imageFullPath);

                    byte[] buffer = new byte[8192];
                    int size = 0;
                    int length = 0;
                    while ((length = fStream.read(buffer)) != -1) {
                        ds.write(buffer, 0, length);
                        size += length;
                    }
                    fStream.close();
                    Log.e("CzLibrary", "uploadImage fileLength=" + size);
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    ds.flush();

                    final String strJson = streamToString(urlConn.getInputStream());
                    Log.e("CzLibrary", "Upload ImageFile Get JSON:" + strJson);
                    if (httpListener != null) {
                        if (strJson != null) {
                            activity.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            httpListener.onHttpSuccess(strJson);
                                        }
                                    }
                            );
                        } else {
                            activity.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            httpListener.onHttpNull();
                                        }
                                    }
                            );
                        }
                    }
                } catch (final Exception e) {
                    activity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    httpListener.onHttpFail("Fail! Connect Exception:"+e);
                                }
                            }
                    );
                    Log.e("CzLibrary", "Fail! Connect Exception:"+e);
                }
            }
        }.start();


    }


    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e("streamToString", e.toString());
            return null;
        }
    }

    /**
     * 服务器返回监听
     */
    public interface HttpListener {
        public void onHttpSuccess(String data);

        public void onHttpFail(String failInfo);

        public void onHttpNull();
    }
}
