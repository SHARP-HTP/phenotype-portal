package edu.mayo.phenoportal.server.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.google.common.net.MediaType;
import org.apache.tika.Tika;

public class MimeUtils {

	/* http://www.iana.org/assignments/media-types */
	public static final String[] XML_MIMES = { "application/xml", "text/xml" };
	public static final String[] HTML_MIMES = { "text/html" };
	public static final String[] XLS_MIMES = { "application/vnd.ms-excel",
	                                           "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };
	public static final String[] ZIP_MIMES = { "application/zip" };
	public static final String[] WORD_MIMES = { "application/msword",
	                                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" };
	public static final String[] PDF_MIMES = { "application/pdf" };

	public static String getMimeType(File file) throws IOException {
		Tika tika = new Tika();
		String type = tika.detect(file);
		MediaType mediaType = MediaType.parse(type);
		return mediaType.toString();
	}

	public static boolean isXmlFile(String mime) {
		return Arrays.asList(XML_MIMES).contains(mime);
	}

	public static boolean isXlsFile(String mime) {
		return Arrays.asList(XLS_MIMES).contains(mime);
	}

	public static boolean isHtmlFile(String mime) {
		return Arrays.asList(HTML_MIMES).contains(mime);
	}

	public static boolean isZipFile(String mime) {
		return Arrays.asList(ZIP_MIMES).contains(mime);
	}

	public static boolean isWordFile(String mime) {
		return Arrays.asList(WORD_MIMES).contains(mime);
	}

	public static boolean isPdfFile(String mime) {
		return Arrays.asList(PDF_MIMES).contains(mime);
	}

}
