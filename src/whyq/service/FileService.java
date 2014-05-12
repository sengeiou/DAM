package whyq.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class FileService {
	private static final String TAG = "UnCompressor";
	public static int SUCESS = 1;
	public static int ERROR = 0;
	public static int EXISTED = 2;

	public FileService() {

	}

	static public int downloadData(String path, String fileName, String urlName) {
		try {
			Log.v("a", "downloading data"+path+" filename"+fileName+" urlName"+urlName);
			File file = new File(path + "/" + fileName);
			if (file.exists())
				return EXISTED;
			else {
				URL url = new URL(urlName);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();

				Log.v("===", "lenghtOfFile = " + lenghtOfFile);

				

				try {
					InputStream is = url.openStream();
					FileOutputStream fos = new FileOutputStream(path + "/"
							+ fileName);

					byte data[] = new byte[1024];

					int count = 0;
					long total = 0;
					int progress = 0;

					while ((count = is.read(data)) != -1) {
						total += count;
						int progress_temp = (int) total * 100 / lenghtOfFile;
						if (progress_temp % 10 == 0 && progress != progress_temp) {
							progress = progress_temp;
							Log.v("=====>", "total = " + progress);
						}
						fos.write(data, 0, count);
					}

					is.close();
					fos.close();
					Log.v("finish", "downloading finished");
				} catch (FileNotFoundException e) {
					// TODO: handle exception
					e.printStackTrace();
					Map<String, String> metaData = new HashMap<String, String>();
					Throwable th = new FileNotFoundException(e.getMessage());
//					IssueListActivity.sendToBugsnag(th, metaData);
				}


				return SUCESS;
			}

		} catch (IOException e) {
			Log.v("eeeee", "exception in downloadData");
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new IOException(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
			return ERROR;

		}

	}

	static public boolean unpackZip(String path, String zipname) {
		InputStream is;
		ZipInputStream zis;
		try {
			is = new FileInputStream(path + zipname);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;

			while ((ze = zis.getNextEntry()) != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count;

				// zapis do souboru
				String filename = ze.getName();
				FileOutputStream fout = new FileOutputStream(path + filename);

				// cteni zipu a zapis
				while ((count = zis.read(buffer)) != -1) {
					baos.write(buffer, 0, count);
					byte[] bytes = baos.toByteArray();
					fout.write(bytes);
					baos.reset();
				}

				fout.close();
				zis.closeEntry();
			}

			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
			return false;
		}

		return true;
	}

	static public boolean unZip(String path, String fileName) {
		try {
			FileInputStream fin = new FileInputStream(path + "/" + fileName);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {

				if (ze.isDirectory()) {
					dirChecker(path, ze.getName());
				} else {
					FileOutputStream fout = new FileOutputStream(path + "/"
							+ ze.getName());
					for (int c = zin.read(); c != -1; c = zin.read()) {
						fout.write(c);
					}

					zin.closeEntry();
					fout.close();
				}

			}
			zin.close();
			return true;
		} catch (Exception e) {
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
			return false;
		}

	}

	public static void dirChecker(String path, String dir) {
		File f = new File(path + "/" + dir);

		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}

	static public String stripExtension(String str) {
		// Handle null case specially.

		if (str == null)
			return null;

		// Get position of last '.'.

		int pos = str.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.

		if (pos == -1)
			return str;

		// Otherwise return the string, up to the dot.

		return str.substring(0, pos);
	}

	// full text search
	public static boolean fullTextSearch(String textSearch, String filePath) {
		try {
			File file = new File(filePath);
			FileInputStream in = new FileInputStream(file);
			int len = 0;
			byte[] data1 = new byte[1024];
			try {
				while (-1 != (len = in.read(data1))) {
					if (new String(data1, 0, len).contains(textSearch))
						return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return false;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new FileNotFoundException(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
			return false;
		}
	}

	public static ArrayList<String> getAllFilesFromPath(String path) {
		ArrayList<String> filesList = null;
		File f = new File(path);
		if (f.isDirectory()) {
			String files[] = f.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i] != null)
					filesList.add(files[i]);
			}
		}
		return filesList;
	}

	static public boolean deleteDirectory(File path)
			throws NullPointerException, IllegalStateException,
			IndexOutOfBoundsException, RuntimeException {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}