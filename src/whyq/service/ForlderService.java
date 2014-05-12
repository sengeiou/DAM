package whyq.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ForlderService {

	private File jsonFile;
	private File jsonOutputFile;

	public ForlderService() {

	}

	private void downloadJsonFile(String urlLink, String fileName) {
		try {
			jsonFile = new File(jsonOutputFile, fileName);
			URL url = new URL(urlLink);
			HttpURLConnection urlConnection;
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			FileOutputStream fileOutput = new FileOutputStream(jsonFile);
			InputStream inputStream = urlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new MalformedURLException(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
		} catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new IOException(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
		}
	}

	public String createDirectory(String path, String folderName) {

		try {
			final String meteoDirectory_path = path + "/" + folderName;
			Log.d("AAAAAAAAAAAAAAAAAA: DATA STORE AT:", "" + "===="
					+ meteoDirectory_path + "=====" + folderName);
			jsonOutputFile = new File(meteoDirectory_path, "/");
			if (jsonOutputFile.exists() == false)
				jsonOutputFile.mkdir();
			return meteoDirectory_path;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public String CreateWriteFile(String path, String fileName, String data) {

		try {

			File file = new File(path, fileName);
			if (file.exists() == false) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
				writer.write(data);
				writer.flush();
				writer.close();
			}

			return fileName;
		}

		catch (IOException e) {
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new IOException(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
			return null;
		}

	}

	// Read settings
	public String ReadFileData(Context context, String fileName) {
		FileInputStream fIn = null;
		InputStreamReader isr = null;

		char[] inputBuffer = new char[255];
		String data = null;

		try {
			fIn = context.openFileInput(fileName);
			isr = new InputStreamReader(fIn);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
			Toast.makeText(context, " read", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception(e.getMessage());
//			IssueListActivity.sendToBugsnag(th, metaData);
			// Toast.makeText(context, " not read", Toast.LENGTH_SHORT).show();
		} finally {
			try {
				isr.close();
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
				Map<String, String> metaData = new HashMap<String, String>();
				Throwable th = new IOException(e.getMessage());
//				IssueListActivity.sendToBugsnag(th, metaData);
			}
		}
		return data;
	}

}
