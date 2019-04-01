package com.lansosdk.videoeditor;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import com.lansosdk.box.LSLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.Calendar;

/**
 * 各种文件操作.
 *
 *
 * 杭州蓝松科技有限公司
 * www.lansongtech.com
 *
 */
public class LanSongFileUtil {

    public static final String TAG = LSLog.TAG;
    public static final boolean VERBOSE = false;
    private static final Object mLock = new Object();

    //可以修改这个路径;
    public static  String DEFAULT_DIR = "/sdcard/lansongBox/";
    public static  String TMP_DIR =DEFAULT_DIR;
    protected static String mTmpFileSubFix="";  //后缀,
    protected static String mTmpFilePreFix="";  //前缀;


    public static void setTempDIR(String dir){
        TMP_DIR=dir;
    }
    public static String getPath() {
        File file = new File(TMP_DIR);
        if (!file.exists()) {
            if(!file.mkdir()){
                TMP_DIR=DEFAULT_DIR;
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
        return TMP_DIR;
    }

    /**
     * 获取文件创建的所在的文件夹
     * @return
     */
    public static String getCreateFileDir() {
        return getPath();
    }


    /**
     * 返回文件大小, 单位M; 2位有效小数;
     *
     * @param filePath
     * @return
     */
    public static float getFileSize(String filePath) {
        if (filePath == null) {
            return 0.0f;
        } else {
            File file = new File(filePath);
            if (!file.exists()) {
                return 0.0f;
            } else {
                long size = file.length();
                float size2 = (float) size / (1024f * 1024f);

                int n = (int) (size2 * 100f);  //截断
                return (float) n / 100f;
            }
        }
    }



    /**
     * 在指定的文件夹里创建一个文件名字, 名字是当前时间,指定后缀.
     *
     * @return
     */
    public static String createFile(String dir, String suffix) {
        synchronized (mLock) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int second = c.get(Calendar.SECOND);
            int millisecond = c.get(Calendar.MILLISECOND);
            year = year - 2000;

            String dirPath = dir;
            File d = new File(dirPath);
            if (!d.exists())
                d.mkdirs();

            if (!dirPath.endsWith("/")) {
                dirPath += "/";
            }

            String name=mTmpFilePreFix;
            name += String.valueOf(year);
            name += String.valueOf(month);
            name += String.valueOf(day);
            name += String.valueOf(hour);
            name += String.valueOf(minute);
            name += String.valueOf(second);
            name += String.valueOf(millisecond);
            name+=mTmpFileSubFix;
            if (!suffix.startsWith(".")) {
                name += ".";
            }
            name += suffix;


            try {
                Thread.sleep(1); // 保持文件名的唯一性.
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String retPath=dirPath+name;
            File file = new File(retPath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return retPath;
        }
    }

    /**
     * 在box目录下生成一个mp4的文件,并返回名字的路径.
     *
     * @return
     */
    public static String createMp4FileInBox() {
        return createFile(TMP_DIR, ".mp4");
    }

    /**
     * 在box目录下生成一个aac的文件,并返回名字的路径.
     *
     * @return
     */
    public static String createAACFileInBox() {
        return createFile(TMP_DIR, ".aac");
    }

    public static String createM4AFileInBox() {
        return createFile(TMP_DIR, ".m4a");
    }

    public static String createMP3FileInBox() {
        return createFile(TMP_DIR, ".mp3");
    }


    /**
     * 创建wav格式的文件路径字符串
     * @return
     */
    public static String createWAVFileInBox() {
        return createFile(TMP_DIR, ".wav");
    }

    /**
     * 创建Gif文件路径字符串;
     * @return
     */
    public static String createGIFFileInBox() {
        return createFile(TMP_DIR, ".gif");
    }

    /**
     * 在box目录下生成一个指定后缀名的文件,并返回名字的路径.这里仅仅创建一个名字.
     *
     * @param suffix 指定的后缀名.
     * @return
     */
    public static String createFileInBox(String suffix) {
        return createFile(TMP_DIR, suffix);
    }

    /**
     * 只是在box目录生成一个路径字符串,但这个文件并不存在.
     *
     * @return
     */
    public static String newMp4PathInBox() {
        return newFilePath(TMP_DIR, ".mp4");
    }

    /**
     * 在指定的文件夹里 定义一个文件名字, 名字是当前时间,指定后缀.
     * 注意: 和 {@link #createFile(String, String)}的区别是,这里不生成文件,只是生成这个路径的字符串.
     *
     * @param suffix ".mp4"
     * @return
     */
    public static String newFilePath(String dir, String suffix) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int second = c.get(Calendar.SECOND);
        int millisecond = c.get(Calendar.MILLISECOND);
        year = year - 2000;
        String name = dir;
        File d = new File(name);

        // 如果目录不中存在，创建这个目录
        if (!d.exists())
            d.mkdir();
        name += "/";

        name += String.valueOf(year);
        name += String.valueOf(month);
        name += String.valueOf(day);
        name += String.valueOf(hour);
        name += String.valueOf(minute);
        name += String.valueOf(second);
        name += String.valueOf(millisecond);
        name += suffix;

        try {
            Thread.sleep(1);  //保持文件名的唯一性.
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//				File file=new File(name);
//				if(file.exists()==false)
//				{
//					try {
//						file.createNewFile();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
        return name;
    }

    /**
     * 使用终端的cp命令拷贝文件,拷贝成功,返回目标字符串,失败返回null;
     * <p>
     * 拷贝大文件,则有耗时,可以放到new Thread中进行.
     *
     * @param srcPath
     * @param suffix  后缀名,如".pcm"
     * @return
     */
    public static String copyFile(String srcPath, String suffix) {
        String dstPath = LanSongFileUtil.createFile(TMP_DIR, suffix);

//			 	String cmd="/system/bin/cp ";
//			 	cmd+=srcPath;
//			 	cmd+=" ";
//			 	cmd+=drawpadDstPath;
//				Runtime.getRuntime().exec(cmd).waitFor();

        File srcF = new File(srcPath);
        File dstF = new File(dstPath);

        copyFile(srcF, dstF);

        if (srcF.length() == dstF.length())
            return dstPath;
        else {
            Log.e(TAG, "fileCopy is failed! " + srcPath + " src size:" + srcF.length() + " dst size:" + dstF.length());
            LanSongFileUtil.deleteFile(dstPath);
            return null;
        }
    }

    /**
     * 删除指定的文件.
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }
    public static void  deleteNameFiles(String prefix,String subfix)
    {
        deleteNameFiles(getCreateFileDir(),prefix,subfix);
    }
    /**
     * 删除包含某些字符串名字的 文件;
     * 比如要删除: /sdcard/lansongBox/lansong*.bmp(所有开头是lansong,后缀是bmp)
     * 则prefix=lansong;  subfix=bmp;
     * @param dir
     * @param prefix
     * @param subfix
     */
    public static void  deleteNameFiles(String dir,String prefix,String subfix)
    {
        File file=new File(dir);
        for (File item : file.listFiles()){
            if(item.isDirectory()==false){
                String path=item.getAbsolutePath();
                String name=LanSongFileUtil.getFileNameFromPath(path);
                String subfix2=LanSongFileUtil.getFileSuffix(path);

                if(prefix!=null && subfix!=null){
                    if(name!=null && name.contains(prefix) && subfix2!=null && subfix2.equals(subfix)){
                        LanSongFileUtil.deleteFile(path);
                    }
                }else if(prefix!=null){
                    if(name!=null && name.contains(prefix)){
                        LanSongFileUtil.deleteFile(path);
                    }
                }else if (subfix!=null){
                    if(subfix2!=null && subfix2.equals(subfix)){
                        LanSongFileUtil.deleteFile(path);
                    }
                }else{
                    LSLog.e("删除指定文件失败,您设置的参数都是null");
                }
            }
        }
    }

    /**
     * 判断 两个文件大小相等.
     *
     * @param path1
     * @param path2
     * @return
     */
    public static boolean equalSize(String path1, String path2) {
        File srcF = new File(path1);
        File srcF2 = new File(path2);
        if (srcF.length() == srcF2.length())
            return true;
        else
            return false;
    }

    public static String getFileNameFromPath(String path) {
        if (path == null)
            return "";
        int index = path.lastIndexOf('/');
        if (index > -1)
            return path.substring(index + 1);
        else
            return path;
    }

    public static String getParent(String path) {
        if (TextUtils.equals("/", path))
            return path;
        String parentPath = path;
        if (parentPath.endsWith("/"))
            parentPath = parentPath.substring(0, parentPath.length() - 1);
        int index = parentPath.lastIndexOf('/');
        if (index > 0) {
            parentPath = parentPath.substring(0, index);
        } else if (index == 0)
            parentPath = "/";
        return parentPath;
    }

    public static boolean fileExist(String absolutePath) {
        if (absolutePath == null)
            return false;
        else {
            File file = new File(absolutePath);
            if (file.exists())
                return true;
        }
        return false;
    }

    public static boolean filesExist(String[] fileArray) {

        for (String file : fileArray) {
            if (!fileExist(file))
                return false;
        }
        return true;
    }

    /** 获取文件后缀*/
    public static String getFileSuffix(String path) {
        if (path == null)
            return "";
        int index = path.lastIndexOf('.');
        if (index > -1)
            return path.substring(index + 1);
        else
            return "";
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static boolean copyFile(File src, File dst) {
        boolean ret = true;
        if (src.isDirectory()) {
            File[] filesList = src.listFiles();
            dst.mkdirs();
            for (File file : filesList)
                ret &= copyFile(file, new File(dst, file.getName()));
        } else if (src.isFile()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src));
                out = new BufferedOutputStream(new FileOutputStream(dst));

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                return true;
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } finally {
                close(in);
                close(out);
            }
            return false;
        }
        return ret;
    }

    public static boolean close(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
            }
        return false;
    }


    //---------------------------------

    /**
     * 删除空目录
     *
     * @param dir 将要删除的目录路径
     */
    public static void deleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String saveIntBuffer(IntBuffer buffer,int width,int height)
    {
        Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        buffer.position(0);
        bmp.copyPixelsFromBuffer(buffer);
        return saveBitmap(bmp);
    }
    /**
     * LSNEW
     *
     * @param bmp
     */
    public static String saveBitmap(Bitmap bmp) {
        if (bmp != null) {
            try {
                BufferedOutputStream bos;
                String name = createFileInBox("png");
                bos = new BufferedOutputStream(new FileOutputStream(name));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bos.close();
                return name;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Log.i("saveBitmap", "error  bmp  is null");
        }
        return "save Bitmap ERROR";
    }
    /**
     * 读取图片的旋转的角度, 有些手机相册中的图片,有旋转角度,比如小米8拍的图片, 三星S9+拍的图片
     *  这里得到图片角度, 实际图片需要旋转这个角度后,才使用.
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        if(path==null || !LanSongFileUtil.fileExist(path)){
            LSLog.e("getBitmapDegree ERROR. file is null or not exist!");
            return 0;
        }
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // 定义矩阵
//    Matrix matrix = new Matrix();
//// 【缩放图像】
//matrix.postScale(0.8f, 0.9f);
//// 【向左旋转】
//matrix.postRotate(-90);
//// 【移动图像】
//matrix.postTranslate(100, 100);
//// 【裁减图像】
//Bitmap.createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)/**
     /** 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }


    /**
     * @param buffer
     * @param w
     * @param h
     * @return
     */
    public static Bitmap intBufferToBitmap(IntBuffer buffer, int w, int h){
        Bitmap stitchBmp = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        stitchBmp.copyPixelsFromBuffer(buffer);
        return stitchBmp;
    }

    /**
     * LSNEW
     *
     * @return
     */
    public static boolean deleteDefaultDir() {
        File file=new File(TMP_DIR);
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return file.delete();
    }
    /**
     *测试

     public static void main(String[] args) {
     doDeleteEmptyDir("new_dir1");
     String newDir2 = "new_dir2";
     boolean success = deleteDir(new File(newDir2));
     if (success) {
     System.out.println("Successfully deleted populated directory: " + newDir2);
     } else {
     System.out.println("Failed to delete populated directory: " + newDir2);
     }
     } */
}
