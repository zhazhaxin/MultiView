package cn.alien95.view.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by linlongxin on 2016/1/27.
 */
public class Util {

    private static Context mContext;

    private static Handler handler = new Handler();

    public static void init(Context context) {
        mContext = context;
    }

    public static void Toast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    public static int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取根目录下的cache地址
     *
     * @return
     */
    public static File getCacheDir(String fileName) {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName);
        }else
            return null;

    }


    /**
     * MD5加密，把字符串加密成32位乱码
     *
     * @param key 传入加密的字符串
     * @return 返回MD5加密后的字符串
     */
    public static String MD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节转换成16进制字符串
     *
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 下载图片保存到APP缓存根目录下，然后通知插入图库数据库，然后通知图库显示出来
     *
     * @param url  图片网络地址
     */
    public static void downloadImage(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) new URL(url).openConnection();
                        urlConnection.setRequestMethod("GET");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(10 * 1000);
                    urlConnection.setReadTimeout(10 * 1000);
                    //对HttpURLConnection对象的一切配置都必须要在connect()函数执行之前完成。
                    int respondCode;
                    urlConnection.connect();
                    final InputStream inputStream = urlConnection.getInputStream();
                    respondCode = urlConnection.getResponseCode();
                    if (respondCode == HttpURLConnection.HTTP_OK) {
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap == null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Util.Toast("保存失败");
                                }
                            });
                            return;
                        }
                        final File f = Util.getCacheDir(Util.MD5(url) + ".jpg");
                        if (f.exists()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Util.Toast("文件已存在");
                                }
                            });
                            return;
                        }
                        try {
                            FileOutputStream out = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();

                            // 最后通知图库更新
                            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getAbsolutePath())));
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Util.Toast("已保存在APP的缓存目录");

                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
