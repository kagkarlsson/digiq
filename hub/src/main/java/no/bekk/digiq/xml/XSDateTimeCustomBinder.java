package no.bekk.digiq.xml;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;

public class XSDateTimeCustomBinder {

	public static DateTime parseDateTime(final String s) {
		return new DateTime(DatatypeConverter.parseDate(s).getTime());
	}

	public static String printDateTime(final DateTime dt) {
		return dt.toString();
	}

}
