/**
 * Copyright (c)  TMA Mobile Solutions, TMA Solutions company. All Rights Reserved.
 * This software is the confidential and proprietary information of TMA Solutions, 
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with TMA Solutions.
 *
 *
 * TMA SOLUTIONS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, 
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TMA SOLUTIONS SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES
 */
package whyq.service;

/** 
 * Class description: TODO
 * 
 * @created           Jan 18, 2011
 * @version 		  v2.0
 * @author 			  dvthien
 * @copyright         TMA Solutions, www.tmasolutions.com
 */

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.model.Location;
import android.util.Log;

public class ParseChangedLocationXMLFile extends DefaultHandler {

	StringBuilder sb = null;
	ArrayList<Location> locationes;
	boolean bStore = false;
	int howMany = 0;

	public ParseChangedLocationXMLFile() {
	}

	public ArrayList<Location> getResults() {

		return locationes;

	}

	@Override
	public void startDocument() throws SAXException {
		// initialize "list"
	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		try {

			Log.d("Parst1111", "222 " + namespaceURI + "1 " + localName + "2 "
					+ qName);

			if (localName.equals("geonames")) {
				this.sb = new StringBuilder("");
				bStore = true;
			}

			if (localName.equals("name")) {
				// bStore = false;
				this.sb = new StringBuilder("");
				bStore = true;
			}
			if (localName.equals("countryName")) {
				this.sb = new StringBuilder("");
				bStore = true;
			}

			Log.d("Parst1111", "3333333332");

		} catch (Exception ee) {

			Log.d("error in startElement", "exception...............");
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		Location location = new Location();
		if (bStore) {
			if (localName.equals("countryName")) {
				location.setCountry(sb.toString());
				sb = new StringBuilder("");
				return;

			}

			if (localName.equals("name")) {
				location.setCountry(sb.toString());
				sb = new StringBuilder("");
				return;
				// bStore = true;
			}
			if (localName.equals("lat")) {
				location.setLat(sb.toString());
				sb = new StringBuilder("");
				return;
			}
			if (localName.equals("lng")) {
				location.setLon(sb.toString());
				sb = new StringBuilder("");
				return;
			}

		}
		if (localName.equals("geoname")) {
			howMany++;
			bStore = false;
		}

	}

	@Override
	public void characters(char ch[], int start, int length) {

		if (bStore) {
			String theString = new String(ch, start, length);
			// Log.d("Result55555", theString);
			this.sb.append(theString);
		}

	}

}
