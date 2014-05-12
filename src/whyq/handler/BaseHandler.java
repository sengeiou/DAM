package whyq.handler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BaseHandler extends DefaultHandler {
	protected StringBuilder builder;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
	}

	protected String getString() {
		return builder.toString();
	}

	protected int getInt() {
		try {
			return Integer.parseInt(builder.toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	protected float getFloat() {
		try {
			return Float.parseFloat(builder.toString());
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	protected boolean getBoolean() {
		return builder.toString().equals("1") ? true : false;
	}

	protected double getDouble() {
		try {
			return Double.parseDouble(builder.toString());
		} catch (NumberFormatException e) {
			return 0f;
		}
	}
}
