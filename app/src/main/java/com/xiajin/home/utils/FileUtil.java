package com.xiajin.home.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * @description 文件操作util类
 * @author shanyq
 * @time 1:31:58 PM May 23, 2013
 */
public class FileUtil {
	
	
	
	
	
	
	/** 接続タイムアウト　単位はms */
	private static final int CONNECTION_TIME_OUT = 5 * 1000;
	/** 読み込みタイムアウト　単位はms */
	private static final int READ_TIME_OUT = 60 * 1000;
	/** MD5変換後のファイルの拡張子 */
	private static final String MD5_FILE = ".jpg";
	/** コピー出力バッファサイズ。ファイルをコピーするときのバッファサイズ。 */
	private static final int COPY_BUFFER_SIZE = 65536;
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/xiajin/"+"/photo/xiajin";
	public static String SDPATH1 = Environment.getExternalStorageDirectory()
        + "/xiajin/"+"/photo/";
	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".jpg"); 
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			//System.out.println("createSDDir:" + dir.getAbsolutePath());
			//System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(path); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	/**
	 * ファイル書き込み
	 * 
	 * @param directoryPath
	 *            書き込み先ディレクトリ
	 * @param fileName
	 *            書き込み先ファイル名
	 * @param writeValue
	 *            書き込みデータ
	 * @param postscript
	 *            trueの場合は追記 falseの場合は上書き
	 */
	public static void writeFile(final String directoryPath,
			final String fileName, final String writeValue,
			final boolean postscript) {
		if (directoryPath == null || fileName == null || writeValue == null) {
		}

		File file = null;
		file = new File(directoryPath + fileName);
		file.getParentFile().mkdirs();

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {
			fos = new FileOutputStream(file, postscript);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			bw.write(writeValue);
		} catch (UnsupportedEncodingException e) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * ファイル読み込み
	 * 
	 * @param directoryPath
	 *            読み込み先ディレクトリ
	 * @param fileName
	 *            読み込み先ファイル名
	 * @return ファイルデータ
	 */
	public static String readFile(final String directoryPath,
			final String fileName) {
		if (directoryPath == null || fileName == null) {
		}

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			fis = new FileInputStream(directoryPath + fileName);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String tmpStr;
			while ((tmpStr = br.readLine()) != null) {
				sb.append(tmpStr);
				sb.append("\n");
			}
		} catch (UnsupportedEncodingException e) {

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {

				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {

				}
			}
		}
		return sb.toString();
	}

	/**
	 * ビットマップファイルを取得します
	 * 
	 * @param directoryPath
	 * @param fileName
	 * @param widthOfDisplay
	 *            ディスプレイ(幅)
	 * @param heightOfDisplay
	 *            ディスプレイ(高さ)
	 * @return ビットマップファイル
	 */
	public static Bitmap getBitmapFile(final String directoryPath,
			final String fileName, int widthOfDisplay, int heightOfDisplay) {

		// 読み込み用のオプションオブジェクトを生成
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 読み込み用のオプションオブジェクトに画像のサイズ情報だけを取得するフラグを設定
		options.inJustDecodeBounds = true;
		// 画像ファイル読み込み
		// (ここでは上記のオプションがtrueのため画像サイズを取得するだけで、実際の画像は読み込まれない)
		BitmapFactory.decodeFile(directoryPath + fileName, options);
		// 読み込んだサイズはoptions.outWidthとoptions.outHeightに格納されるので、
		// その値とディスプレイサイズから読み込む際の縮尺を計算する
		int scaleW = options.outWidth / widthOfDisplay;
		int scaleH = options.outHeight / heightOfDisplay;
		// 縮尺は整数値で、1ならオリジナルの画像サイズ、
		// 2なら画像の縦横のピクセル数を1/2にしたサイズ、
		// 3でも1/2にしたサイズ、
		// 4なら1/4にしたサイズで読み込まれる
		int scale = Math.max(scaleW, scaleH);
		// 縮尺が2倍までの場合
		if (scale <= 2) {
			// オリジナルの画像サイズ(縮尺は1)
			scale = 1;
		} else {
			// 縮尺の調整
			scale = scale - 1;
		}
		// 今度は画像を読み込みたいので、
		// 読み込み用のオプションオブジェクトに設定しているフラグをfalseに設定
		options.inJustDecodeBounds = false;
		// 先程計算した縮尺値を指定
		options.inSampleSize = scale;
		// システムメモリ上に再利用性の無いオブジェクトがある場合に勝手に解放する。
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(directoryPath + fileName,
				options);
		return bitmap;
	}

	/**
	 * ファイル削除
	 * 
	 * @param directoryPath
	 *            削除先ディレクトリ名
	 * @param fileName
	 *            ファイル名
	 */
	public static void deleteFile(final String directoryPath,
			final String fileName) {
		if (directoryPath == null || fileName == null) {
			return;
		}
		if (existsFile(directoryPath, fileName)) {
			File file = new File(directoryPath + fileName);
			if (file.delete()) {
			} else {
			}
		}
	}

	/**
	 * ディレクトリ削除
	 * 
	 * @param file
	 *            削除するデレクトリ名またはファイル名
	 */
	public static void deleteDirectory(File file) {
		if (file.exists() == false) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		}

		// 再帰的にサブディレクトリにさかのぼって、ファイルを消した上で、ディレクトリを消すという方法で、削除する
		if (file.isDirectory()) {
			File[] filesList = file.listFiles();
			for (int i = 0; i < filesList.length; i++) {
				deleteDirectory(filesList[i]);
			}
			file.delete();
		}

	}

	public static boolean existsFile(final String directoryPath,
			final String fileName) {
		boolean rv = false;
		if (directoryPath == null || fileName == null) {
			return rv;
		}
		File file = new File(directoryPath + fileName);

		if (file.exists()) {
			rv = true;
		} else {
		}
		return rv;
	}

	/**
	 * コピー処理(実処理)
	 * 
	 * @param in
	 *            コピー元
	 * @param out
	 *            コピー先
	 * @throws java.io.IOException
	 */
	private static void copyFile(BufferedInputStream in,
			BufferedOutputStream out) throws IOException {
		if (in == null || out == null) {
			return;
		}

		final byte[] buf = new byte[COPY_BUFFER_SIZE];
		int length = 0;
		while ((length = in.read(buf)) != -1) {
			out.write(buf, 0, length);
		}
		out.flush();

	}

	/**
	 * コピー処理
	 * 
	 * @param originDirectoryPath
	 *            コピー元のディレクトリ名
	 * @param originFileName
	 *            コピー元のファイル名
	 * @param destinationDirectoryPath
	 *            コピー先のディレクトリ名
	 * @param destinationFileName
	 *            コピー先のファイル名
	 */
	public static void copyFile(final String originDirectoryPath,
			final String originFileName, final String destinationDirectoryPath,
			final String destinationFileName) {
		try {
			copyFileWithThrows(originDirectoryPath, originFileName,
					destinationDirectoryPath, destinationFileName);
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Exception Throw付きコピー処理
	 * 
	 * @param originDirectoryPath
	 *            コピー元のディレクトリ名
	 * @param originFileName
	 *            コピー元のファイル名
	 * @param destinationDirectoryPath
	 *            コピー先のディレクトリ名
	 * @param destinationFileName
	 *            コピー先のファイル名
	 * @throws java.io.IOException
	 * @throws java.io.FileNotFoundException
	 */
	public static void copyFileWithThrows(final String originDirectoryPath,
			final String originFileName, final String destinationDirectoryPath,
			final String destinationFileName) throws IOException,
			FileNotFoundException {
		if (originDirectoryPath == null || originFileName == null
				|| destinationDirectoryPath == null
				|| destinationFileName == null) {
			return;
		}

		if (existsFile(originDirectoryPath, originFileName) == false) {
			return;
		}

		File destinationFile = null;
		destinationFile = new File(destinationDirectoryPath
				+ destinationFileName);
		destinationFile.getParentFile().mkdirs();

		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		final ByteBuffer byteBuff = ByteBuffer.allocateDirect(COPY_BUFFER_SIZE);
		try {
			inStream = new FileInputStream(originDirectoryPath + originFileName);
			inChannel = inStream.getChannel();
			outStream = new FileOutputStream(destinationFile);
			outChannel = outStream.getChannel();
			while (inChannel.read(byteBuff) != -1) {
				byteBuff.flip();
				outChannel.write(byteBuff);
				byteBuff.clear();
			}
		} finally {
			if (outChannel != null) {
				try {
					outChannel.close();
				} catch (IOException e) {
				}
			}
			if (inChannel != null) {
				try {
					inChannel.close();
				} catch (IOException e) {
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
				}
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Exception Throw付きファイルロード処理
	 * 
	 * @param in
	 *            コピー元のInputStream
	 * @param directoryPath
	 *            コピー先のディレクトリ名
	 * @param fileName
	 *            コピー先のファイル名
	 * @throws java.io.IOException
	 * @throws java.io.FileNotFoundException
	 */
	public static boolean copyAsset(InputStream in, final String directoryPath,
			final String fileName) {
		if (in == null || directoryPath == null || fileName == null) {
			return false;
		}
		boolean flag = true;
		File file = null;
		file = new File(directoryPath + fileName);
		file.getParentFile().mkdirs();
		FileOutputStream out = null;
		BufferedInputStream bfin = null;
		BufferedOutputStream bfout = null;
		try {
			out = new FileOutputStream(file);
			bfin = new BufferedInputStream(in);
			bfout = new BufferedOutputStream(out);
			copyFile(bfin, bfout);
		} catch (Exception ee) {
			flag = false;
			ee.printStackTrace();
		} finally {
			if (bfin != null) {
				try {
					bfin.close();
				} catch (IOException e) {
				}
			}
			if (bfout != null) {
				try {
					bfout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return flag;
	}

	/**
	 * 画像ファイル名にMD5をかけます
	 * 
	 * @param fileName
	 *            画像ファイル名
	 * @return MD5で変換された画像ファイル名＋jpgの拡張子 エラー時はnull
	 */
	public static String convertFileNm2Md5(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return null;
		}
		return convertFileNm2Md5NoExtension(fileName) + MD5_FILE;
	}

	/**
	 * ファイル名にMD5をかけます
	 * 
	 * @param fileName
	 *            画像ファイル名
	 * @return MD5で変換された画像ファイル名 エラー時はnull
	 */
	public static String convertFileNm2Md5NoExtension(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return null;
		}
		// MD5変換
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		md.update(fileName.getBytes());
		byte[] hashByte = md.digest();
		StringBuilder convertedFileNm = new StringBuilder();
		// 16進に変換する
		for (int i = 0; i < hashByte.length; i++) {
			if ((0xFF & hashByte[i]) < 16) {
				convertedFileNm.append("0");
			}
			convertedFileNm.append(Integer.toHexString(0xFF & hashByte[i]));
		}
		return convertedFileNm.toString();
	}

	/**
	 * ファイルダウンロード
	 * 
	 * @param downloadUrl
	 *            ダウンロード先URL
	 * @param directoryPath
	 *            格納先ディレクトリ名
	 * @param fileName
	 *            ダウンロードファイル名
	 */
	public static void downloadFile(final String downloadUrl,
			final String directoryPath, final String fileName) {
		if (downloadUrl == null || directoryPath == null || fileName == null) {
			return;
		}
		// ダウンロード処理
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParamsObj = httpClient.getParams();
		// 日本語に設定
		httpParamsObj.setParameter("Accept-Language", "ja;q=0.7,en;q=0.3");
		// 接続タイムアウトを5秒に(デフォルトでは無限)
		HttpConnectionParams.setConnectionTimeout(httpParamsObj,
				CONNECTION_TIME_OUT);
		// 読み込みタイムアウトを60秒に(デフォルトでは無限)
		HttpConnectionParams.setSoTimeout(httpParamsObj, READ_TIME_OUT);
		// 値の取得
		HttpGet request;
		HttpResponse httpResponse;
		try {
			request = new HttpGet(downloadUrl);
			httpResponse = httpClient.execute(request);
		} catch (IllegalArgumentException e) {
			return;
		} catch (ClientProtocolException e) {
			return;
		} catch (IOException e) {
			return;
		}

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			File file = new File(directoryPath + fileName);
			file.getParentFile().mkdirs();

			InputStream inStream = null;
			FileOutputStream outStream = null;
			ReadableByteChannel inChannel = null;
			FileChannel outChannel = null;
			final ByteBuffer byteBuff = ByteBuffer
					.allocateDirect(COPY_BUFFER_SIZE);

			try {
				inStream = httpResponse.getEntity().getContent();
				inChannel = Channels.newChannel(inStream);
				outStream = new FileOutputStream(file);
				outChannel = outStream.getChannel();
				while (inChannel.read(byteBuff) != -1) {
					byteBuff.flip();
					outChannel.write(byteBuff);
					byteBuff.clear();
				}
			} catch (FileNotFoundException e) {
				return;
			} catch (IOException e) {
				return;
			} finally {
				if (outChannel != null) {
					try {
						outChannel.close();
					} catch (IOException e) {
					}
				}
				if (inChannel != null) {
					try {
						inChannel.close();
					} catch (IOException e) {
					}
				}
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
					}
				}
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * Exception Throw付きファイルダウンロード
	 * 
	 * @param downloadUrl
	 *            ダウンロード先URL
	 * @param directoryPath
	 *            格納先ディレクトリ名
	 * @param fileName
	 *            ダウンロードファイル名
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static void downloadFileWithThrows(final String downloadUrl,
			final String directoryPath, final String fileName)
			throws FileNotFoundException, IOException {
		if (downloadUrl == null || directoryPath == null || fileName == null) {
			return;
		}

		// ダウンロード処理
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParamsObj = httpClient.getParams();

		// 日本語に設定
		httpParamsObj.setParameter("Accept-Language", "ja;q=0.7,en;q=0.3");

		// 接続タイムアウトを5秒に(デフォルトでは無限)
		HttpConnectionParams.setConnectionTimeout(httpParamsObj,
				CONNECTION_TIME_OUT);

		// 読み込みタイムアウトを60秒に(デフォルトでは無限)
		HttpConnectionParams.setSoTimeout(httpParamsObj, READ_TIME_OUT);

		// 値の取得
		HttpGet request = new HttpGet(downloadUrl);
		HttpResponse httpResponse = httpClient.execute(request);

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			File file = new File(directoryPath + fileName);
			file.getParentFile().mkdirs();

			InputStream inStream = null;
			FileOutputStream outStream = null;
			ReadableByteChannel inChannel = null;
			FileChannel outChannel = null;
			final ByteBuffer byteBuff = ByteBuffer
					.allocateDirect(COPY_BUFFER_SIZE);

			try {
				inStream = httpResponse.getEntity().getContent();
				inChannel = Channels.newChannel(inStream);
				outStream = new FileOutputStream(file);
				outChannel = outStream.getChannel();
				while (inChannel.read(byteBuff) != -1) {
					byteBuff.flip();
					outChannel.write(byteBuff);
					byteBuff.clear();
				}
			} finally {
				if (outChannel != null) {
					try {
						outChannel.close();
					} catch (IOException e) {
					}
				}
				if (inChannel != null) {
					try {
						inChannel.close();
					} catch (IOException e) {
					}
				}
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
					}
				}
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * http接続を行って画像をダウンロードする 画像が存在しない場合はnullを返す
	 * 
	 * @param downloadUrl
	 *            画像のurl
	 * @return 画像
	 * @throws java.net.URISyntaxException
	 *             URIの生成に失敗した場合
	 * @throws java.io.IOException
	 *             IOエラー
	 */
	public static Bitmap downloadBitmapFile(String downloadUrl)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParamsObj = httpClient.getParams();

		// 接続タイムアウトを5秒に(デフォルトでは無限)
		HttpConnectionParams.setConnectionTimeout(httpParamsObj,
				CONNECTION_TIME_OUT);

		// 読み込みタイムアウトを60秒に(デフォルトでは無限)
		HttpConnectionParams.setSoTimeout(httpParamsObj, READ_TIME_OUT);

		// 値の取得
		HttpGet request = new HttpGet(downloadUrl);
		HttpResponse httpResponse = httpClient.execute(request);

		if (httpResponse.getStatusLine() != null
				&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			InputStream is = null;

			Bitmap bit = null;
			try {
				is = httpResponse.getEntity().getContent();
				bit = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
			return bit;
		}
		return null;
	}

	/**
	 * ファイルリネーム
	 * 
	 * @param originDirectoryPath
	 *            リネーム元のディレクトリ名
	 * @param originFileName
	 *            リネーム元のファイル名
	 * @param renameDirectoryPath
	 *            リネーム先のディレクトリ名
	 * @param renameFileName
	 *            リネーム先のファイル名
	 */
	public static void renameFile(final String originDirectoryPath,
			final String originFileName, final String renameDirectoryPath,
			final String renameFileName) {

		if (originDirectoryPath == null || originFileName == null
				|| renameDirectoryPath == null || renameFileName == null) {
			return;
		}
		if (existsFile(originDirectoryPath, originFileName)) {
			File orgFile = new File(originDirectoryPath + originFileName);
			File newFile = new File(renameDirectoryPath + renameFileName);

			if (orgFile.renameTo(newFile)) {
			} else {
			}
		} else {
		}
	}

	/**
	 * ファイルを作成します。 作成できたら true 、作成できなかったら false を返します。
	 * 
	 * @param fileName
	 *            ファイル名
	 * @return 作成できたら true 、作成できなかったら false
	 * @throws java.io.IOException
	 */
	public static boolean createNewFile(final String fileName)
			throws IOException {
		File triggerFile = new File(fileName);
		boolean result = triggerFile.createNewFile();
		if (result) {
		} else {
		}
		return result;
	}
}
