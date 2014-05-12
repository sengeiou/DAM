package whyq.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	private static final String LOG_FILE = "log.txt";
	public static void appendLog(String text) {
		File dir = new File("sdcard/logger");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File logFile = new File("sdcard/logger/" + LOG_FILE);

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();

			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag

			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));

			buf.append(text);

			buf.newLine();

			buf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}
	}
}
