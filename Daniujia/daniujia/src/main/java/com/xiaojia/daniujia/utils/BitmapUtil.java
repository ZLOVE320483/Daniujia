package com.xiaojia.daniujia.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.ui.views.load.PhotoLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapUtil {
	private static final int SAMPLE_SIZE_WIDTH = 480;
	private static final int SAMPLE_SIZE_HEIGHT = 800;

	private static final int COMPRESS_FIRST_SIZE = 64 * 1024;
	private static final int COMPRESS_SECOND_SIZE = 128 * 1024;
	private static final int COMPRESS_THIRD_SIZE = 512 * 1024;
	private static final int COMPRESS_FOURTH_SIZE = 2 * 1024 * 1024;

	private static final int FACTOR_BEYOND_FIRST_SIZE = 80;
	private static final int FACTOR_BEYOND_SECOND_SIZE = 80;
	private static final int FACTOR_BEYOND_THIRD_SIZE = 80;
	private static final int FACTOR_BEYOND_FOURTH_SIZE = 80;

	private static final int MAX_HARDWARE_PIXS = 2048;

	public static Bitmap decodeFile(File f, int maxSize) {
		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(f.getPath(), options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(f.getPath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getCompressImageFile(File f, int outwidth, int outHeight) throws FileNotFoundException, OutOfMemoryError {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(f.getPath(), options);

		options.outWidth = outwidth;
		options.outHeight = outHeight;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(f.getPath(), options);
	}

	public static Bitmap decodeFile(byte[] data, int maxSize) throws OutOfMemoryError {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	@SuppressLint({ "FloatMath", "FloatMath" })
	public static Bitmap reSizeBitmap(String localPath, int maxWith, int maxHeight) {
		Bitmap bitmap = null;
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inDither = false;
			options.inJustDecodeBounds = true;
			// remove magic string we add in GalleryBrowser
			if (localPath.startsWith("file://")) {
				localPath = localPath.replace("file://", "");
			}
			bitmap = BitmapFactory.decodeFile(localPath, options);
			int width = options.outWidth;
			int height = options.outHeight;
			int ratio = 1;
			if (width > 0 && height / width > 3) {
				ratio = getRatio(width, height, 1024, false);
			} else {
				maxHeight = maxHeight > 1024 ? 1024 : maxHeight;
				maxWith = maxWith > 1024 ? 1024 : maxWith;
				int ratioX = 1;
				int ratioY = 1;
				if (width > maxWith) {
					ratioX = (int) Math.ceil((float) width / maxWith);
				}
				if (height > maxHeight) {
					ratioY = (int) Math.ceil((float) height / maxHeight);
				}
				ratio = Math.min(ratioX, ratioY);
			}

			options.inDither = false;
			options.inJustDecodeBounds = false;
			options.inSampleSize = ratio;
			bitmap = BitmapFactory.decodeFile(localPath, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			requtstLowMemory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 *
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	public static Bitmap adjustThePhotoOrientation(File sourceFile) throws IOException {
		Bitmap bitmap = null;
		ExifInterface exifInterface = new ExifInterface(sourceFile.getPath());
		int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
		int degree = 0;
		if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		if (degree != 0) {
			Matrix m = new Matrix();
			bitmap = BitmapUtil.reSizeBitmap(sourceFile.getPath(), 1024, 1024);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
		}
		return bitmap;
	}

	public static void requtstLowMemory() {
		PhotoLoader.getInstance().requestLowMemory();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static int getRatio(int width, int height, int maxWith, boolean supportHardwareAccelerated) {
		int ratio = 1;
		if (width > maxWith) {
			ratio = (int) Math.ceil((float) width / maxWith);
		}
		while (true) {
			float tHeight = (float) height / ratio;
			float tWidth = (float) width / ratio;
			float size = tHeight * tWidth * 4;
			if (size < PhotoLoader.getInstance().getMaxCacheSize() * 0.8) {
				break;
			}
			ratio++;
		}
		while (supportHardwareAccelerated) {
			float tHeight = (float) height / ratio;
			if (tHeight < MAX_HARDWARE_PIXS) {
				ratio = nextPowerOf2(ratio);
				break;
			}
			ratio++;
		}
		return ratio;
	}

	private static int nextPowerOf2(int n) {
		n -= 1;
		n |= n >>> 16;
		n |= n >>> 8;
		n |= n >>> 4;
		n |= n >>> 2;
		n |= n >>> 1;
		return n + 1;
	}

	public static Bitmap getSqureBitmap(Bitmap srcBmp) {
		if (srcBmp.getWidth() >= srcBmp.getHeight()) {
			return Bitmap.createBitmap(srcBmp, srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2, 0, srcBmp.getHeight(), srcBmp.getHeight());
		} else {
			return Bitmap.createBitmap(srcBmp, 0, srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2, srcBmp.getWidth(), srcBmp.getWidth());
		}
	}

	public static Bitmap getCircleBitmap(Bitmap bitmapimg) {
		bitmapimg = getSqureBitmap(bitmapimg);

		Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(), bitmapimg.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmapimg.getWidth(), bitmapimg.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmapimg.getWidth() / 2, bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmapimg, rect, rect, paint);
		return output;
	}

	public static boolean saveBitmap2file(Bitmap bmp, int quality, File destFile) {
		boolean successCompressed = false;
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(destFile);
			successCompressed = bmp.compress(format, quality, stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return successCompressed;
	}

	/**
	 * 类型JPEG
	 */
	public static boolean saveBitmap2file(Bitmap bmp, File path) {
		return saveBitmap2file(bmp, 100, path);
	}

	/**
	 * 根据路径获得图片，压缩返回bitmap用于显示
	 */
	public static Bitmap getSmallBitmapFromFile(File bmFile) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bmFile.getAbsolutePath(), options);// a must
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, SAMPLE_SIZE_WIDTH, SAMPLE_SIZE_HEIGHT);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap smallBm = BitmapFactory.decodeFile(bmFile.getAbsolutePath(), options);
		return toRGB565Bitmap(smallBm);
	}

	/**
	 * 由颜色数组重建Bitmap
	 */
	private static Bitmap toRGB565Bitmap(Bitmap srcBitmap) {
		int w = srcBitmap.getWidth();
		int h = srcBitmap.getHeight();
		int[] pix = new int[w * h];
		srcBitmap.getPixels(pix, 0, w, 0, 0, w, h);
		Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		img.setPixels(pix, 0, w, 0, 0, w, h);
		return img;
	}

	/**
	 * 计算图片的缩放值
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/*
	 * 获取压缩后的图片文件
	 */
	public static File getCompressedImgFile(File srcFile) {
		int compressFactor = 100;
		long picSize = srcFile.length();
		String tmpImgName = "tmp" + System.currentTimeMillis() + ".jpg";
		File tmpFile = new File(FileStorage.APP_IMG_DIR, WUtil.genAvatarFileName());
		if (picSize > COMPRESS_FIRST_SIZE) {// 压缩
			if (picSize < COMPRESS_SECOND_SIZE) {
				compressFactor = FACTOR_BEYOND_FIRST_SIZE;
			} else if (picSize >= COMPRESS_SECOND_SIZE && picSize < COMPRESS_THIRD_SIZE) {
				compressFactor = FACTOR_BEYOND_SECOND_SIZE;
			} else if (picSize >= COMPRESS_THIRD_SIZE && picSize < COMPRESS_FOURTH_SIZE) {
				compressFactor = FACTOR_BEYOND_THIRD_SIZE;
			} else {
				compressFactor = FACTOR_BEYOND_FOURTH_SIZE;
			}
			try {
				Bitmap albumPic = BitmapUtil.getSmallBitmapFromFile(srcFile);
				BitmapUtil.saveBitmap2file(albumPic, compressFactor, tmpFile);
				albumPic.recycle();
			} catch (Throwable e) {
				LogUtil.e("test", e);
			}
		} else {
			WUtil.copyFile(srcFile, tmpFile);
		}
		return tmpFile;
	}

	public static Bitmap getCompressedBitmapFromFile(File imgFile) {
		File f = getCompressedImgFile(imgFile);
		return getBitmapFromFile(f);
	}

	public static Bitmap getBitmapFromFile(String filePath) {
		Bitmap bm = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			bm = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			LogUtil.d("test", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	public static Bitmap getBitmapFromFile(File file) {
		Bitmap bm = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file.getAbsolutePath());
			bm = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			LogUtil.d("ZLOVE", "FileNotFoundException----" + e.toString());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LogUtil.d("ZLOVE", "IOException---" + e.toString());
				}
			}
		}
		return bm;
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {// Bitmap → byte[]
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bytes = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			LogUtil.d("test", e);
		}
		return bytes;
	}

	public static Bitmap bytes2Bimap(byte[] b) {// byte[] → Bitmap
		if (b != null && b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	private static Bitmap toRoundCorner(Bitmap bitmap, int corner) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = corner;// 圆角像素
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap toRoundCorner(Bitmap bitmap) { // 给图片加上圆角
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int corner = w / 2;
		if (w > h) {
			corner = h / 2;
		}
		return toRoundCorner(bitmap, corner);
	}

	public static Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN:这种模式，两个绘制的效果叠加后取交集展现后图
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param srcBm
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap srcBm, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = srcBm.getWidth();
		float height = srcBm.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(srcBm, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	public static Bitmap zoomImage(Bitmap srcBm, float scale) {
		float width = srcBm.getWidth();
		float height = srcBm.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap bitmap = Bitmap.createBitmap(srcBm, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	public static Bitmap zoomImageFitWidth(Bitmap bm) {
		int bmWidth = bm.getWidth();
		int bmHeight = bm.getHeight();

		int screenWidth = SysUtil.getDisplayMetrics().widthPixels;
		int screenHeight = SysUtil.getDisplayMetrics().heightPixels;

		float zoomScale = ((float) screenWidth) / bmWidth;
		if (zoomScale <= 1 && ((float) bmHeight) / zoomScale > screenHeight
				|| zoomScale > 1 && ((float) bmHeight) * zoomScale > screenHeight) {// 高比例太大
			zoomScale = ((float) screenHeight) / bmHeight;
		}
		bm = zoomImage(bm, zoomScale);
		return bm;
	}

	public static Bitmap rotateBitmap(Bitmap bm, int orientationDegree) {
		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		float targetX, targetY;
		if (orientationDegree == 90) {
			targetX = bm.getHeight();
			targetY = 0;
		} else {
			targetX = bm.getHeight();
			targetY = bm.getWidth();
		}
		final float[] values = new float[9];
		m.getValues(values);
		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];
		m.postTranslate(targetX - x1, targetY - y1);
		Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bm1);
		canvas.drawBitmap(bm, m, paint);
		return bm1;
	}

	/**
	 * 图片透明度处理
	 */
	public static Bitmap toTranslucent(Bitmap sourceImg) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		int number = 50;// number:0全透明-100不透明
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 修改最高2位的值
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Config.ARGB_8888);
		return sourceImg;
	}

	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
	    if (null != dst && dst.exists()) {
	        BitmapFactory.Options opts = null;
	        if (width > 0 && height > 0) {
	            opts = new BitmapFactory.Options();
	            opts.inJustDecodeBounds = true;
	            BitmapFactory.decodeFile(dst.getPath(), opts);
	            // 计算图片缩放比例
	            final int minSideLength = Math.min(width, height);
	            opts.inSampleSize = computeSampleSize(opts, minSideLength,
	                    width * height);
	            opts.inJustDecodeBounds = false;
	            opts.inInputShareable = true;
	            opts.inPurgeable = true;
	        }
	        try {
	            return BitmapFactory.decodeFile(dst.getPath(), opts);
	        } catch (OutOfMemoryError e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
	            .sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
	            .floor(w / minSideLength), Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}

	public static int[] calculatePicSize(int width, int height) {
			int[] arr = new int[2];
			if (width < 140 && height < 140) {
				arr[0] = width;
				arr[1] = height;
			}else {
				float simpleSize;
				if (width > height) {
					simpleSize = width/140;
					if (simpleSize < 1) {
						simpleSize = 1;
					}
					int newHeight = (int) (height/simpleSize);
					arr[0] = 140;
					arr[1] = newHeight;
				}else if(height > width){
					simpleSize = height/140;
					if (simpleSize < 1) {
						simpleSize = 1;
					}
					int newWidth = (int) (width/simpleSize);
					arr[0] = newWidth;
					arr[1] = 140;
				}else {
					arr[0] = 140;
					arr[1] = 140;
				}
			}
			return arr;
		}

	public static int[] calculatePicSizeOfScreen(int width, int height,float screenWidth) {

		int[] arr = new int[2];

		if (width < screenWidth && height < screenWidth) {
			/// FIXME: 2016/4/21 
		}
		return arr;
	}


	public static Bitmap base64ToBitmap(String base64Data) {
		int index = base64Data.indexOf(",");
		String result = base64Data.substring(index + 1);
		byte[] bytes = android.util.Base64.decode(result, android.util.Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
