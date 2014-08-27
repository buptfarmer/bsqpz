package buptfarmer.com.bsqpz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class HomeActivity extends ActionBarActivity {

    private ImageView mOriginalAvatar;
    private ImageView mPlusOne;
    private ImageView mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mOriginalAvatar = (ImageView) findViewById(R.id.headicon);
        mPlusOne = (ImageView) findViewById(R.id.add1);
        mSaveButton = (ImageView) findViewById(R.id.button);
        mOriginalAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getImageClipIntent();
//                intent.setAction(Intent.ACTION_PICK);
//                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "图片已经保存", Toast.LENGTH_LONG).show();

                Bitmap changedBitmap = createPlusOneBitmap(mOriginalAvatar);
                saveImageToGallery(HomeActivity.this, changedBitmap);
            }
        });
    }

    //设置裁剪图片的规格
    public Intent getImageClipIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        //实现对图片的裁剪，必须设置图片的属性
        intent.setType("image/*");                    //获取任意类型的图片
        intent.putExtra("crop", "true");           //滑动选中图片区域
        intent.putExtra("aspectX", 1);               //表示剪切框的比例为1:1
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80);           //指定输出图片的大小
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                // 得到图片的全路径
//                Uri uri = data.getData();
                // 通过路径加载图片
                //这里省去了图片缩放操作，如果图片过大，可能会导致内存泄漏
                //图片缩放的实现，请看：http://blog.csdn.net/reality_jie_blog/article/details/16891095
//                mOriginalAvatar.setImageURI(uri);

                Toast.makeText(HomeActivity.this, "点击“逼死强迫症” 保存头像", Toast.LENGTH_LONG).show();


                Bitmap uriBitmap = data.getParcelableExtra("data");
                mOriginalAvatar.setImageBitmap(uriBitmap);

//                Bitmap changedBitmap = createPlusOneBitmap(mOriginalAvatar);
//                saveImageToGallery(this,changedBitmap);

                String lastName = "last.jpg";
//                compressAndSaveBitmapToSDCard(changedBitmap, "plusone.jpg", 100);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap createPlusOneBitmap(ImageView originalView) {
        Drawable pulsoneDrawable = getResources().getDrawable(R.drawable.add1);
        Bitmap plusoneBitmap = ((BitmapDrawable) pulsoneDrawable).getBitmap();

        Bitmap bitmap = Bitmap.createBitmap(originalView.getWidth(), originalView.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas screenCanvas = new Canvas(bitmap);
        originalView.draw(screenCanvas);

        Paint paint = new Paint();
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffb6e0fb);

        float left = bitmap.getWidth() - plusoneBitmap.getWidth();
        screenCanvas.drawBitmap(plusoneBitmap, left,
                0, paint);
        Log.d("ccc", "left:" + left);
        return bitmap;
    }

    public Bitmap createScreenBitmap(ImageView originalView, ImageView plusOneView, Uri plusUri) {
        Bitmap bitmap = Bitmap.createBitmap(originalView.getWidth(), originalView.getHeight(),
                Bitmap.Config.RGB_565);
//        Bitmap plusoneBitmap = Bitmap.createBitmap(plusOneView.getWidth(),
// plusOneView.getHeight(),
//                Bitmap.Config.RGB_565);

        Drawable pulsoneDrawable = getResources().getDrawable(R.drawable.add1);
        Bitmap plusoneBitmap = ((BitmapDrawable) pulsoneDrawable).getBitmap();
        Canvas screenCanvas = new Canvas(bitmap);


        originalView.draw(screenCanvas);
//        plusOneView.draw(screenCanvas);
        Paint paint = new Paint();
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffb6e0fb);
//        screenCanvas.drawBitmap(bitmap, 0, 0, paint);
        float left = bitmap.getWidth() - plusoneBitmap.getWidth();
        screenCanvas.drawBitmap(plusoneBitmap, left,
                0, paint);
        Log.d("ccc", "left:" + left);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //获取SDCard的目录路径功能
    private String getSDCardPath() {
        String SDCardPath = null;
// 判断SDCard是否存在
        boolean IsSDcardExist = Environment.getExternalStorageState().equals(android.os
                .Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            SDCardPath = Environment.getExternalStorageDirectory().toString();
        }
        return SDCardPath;
    }

    private void compressAndSaveBitmapToSDCard(Bitmap rawBitmap, String fileName, int quality) {
        String saveFilePaht = Constants.APP_PATH + fileName;
        Log.d("ccc", "savePath :" + saveFilePaht);
        File saveFile = FileHelper.getSafeExternalFile(Constants.APP_PATH, fileName);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            if (fileOutputStream != null) {
//imageBitmap.compress(format, quality, stream);
//把位图的压缩信息写入到一个指定的输出流中
//第一个参数format为压缩的格式
//第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
//第三个参数stream为输出流
                rawBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private static Bitmap scalImage(Bitmap bgimage, double newWidth,
                                    double newHeight) {
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    //    private static Bitmap createBitmapWithMark(Context ctx, Bitmap src) {
//        Bitmap bmpWithMark = null;
//        if (src == null) {
//            return null;
//        }
//        int srcWidth = src.getWidth();
//        int srcHeight = src.getHeight();
//        Bitmap watermark = ((BitmapDrawable) ctx.getResources().getDrawable(R.drawable.watermark))
//                .getBitmap();
//        int watermarkWidth = watermark.getWidth();
//        int watermarkHeight = watermark.getHeight();
//        bmpWithMark = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//        Canvas cv = new Canvas(bmpWithMark);
//
//        cv.drawBitmap(src, 0, 0, null);
//        src.recycle();
//        cv.drawBitmap(watermark, srcWidth - watermarkWidth, srcHeight - watermarkHeight, null);
//        watermark.recycle();
//        cv.save();
//        cv.restore();
//        return bmpWithMark;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "last.jpg");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + file.getPath())));
    }
}
