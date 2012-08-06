/*
 * 
 */
package org.holodeck.backend.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;

/**
 * The Class IOUtils.
 */
public class IOUtils extends org.apache.commons.io.IOUtils {
	
	/** The Constant TEMP_DIR_ATTEMPTS. */
	private static final int TEMP_DIR_ATTEMPTS = 10000;

	/**
	 * Creates the temp dir.
	 *
	 * @return the file
	 */
	public static File createTempDir() {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = System.currentTimeMillis() + "-";
		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within " + TEMP_DIR_ATTEMPTS + " attempts (tried "
				+ baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}

	/**
	 * To byte array.
	 *
	 * @param dataHandler the data handler
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] toByteArray(DataHandler dataHandler) throws IOException {
		final InputStream in = dataHandler.getInputStream();

		return toByteArray(in);
	}

	/**
	 * Removes the directory.
	 *
	 * @param directory the directory
	 * @return true, if successful
	 */
	public static boolean removeDirectory(File directory) {
		if (directory == null)
			return false;
		if (!directory.exists())
			return true;
		if (!directory.isDirectory())
			return false;
		String[] list = directory.list();
		// Some JVMs return null for File.list() when the
		// directory is empty.
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File entry = new File(directory, list[i]);
				if (entry.isDirectory()) {
					if (!removeDirectory(entry))
						return false;
				} else {
					if (!entry.delete())
						return false;
				}
			}
		}
		return directory.delete();
	}
}
