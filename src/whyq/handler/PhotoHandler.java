package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import whyq.model.Photo;

public class PhotoHandler extends BaseHandler {

	// TAG_PHOTO
	private static final String ITEMS = "obj";
	private static final String TAG_ID = "id";
	private static final String TAG_STORE_ID = "store_id";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_COMMENT_ID = "comment_id";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_THUMB = "thumb";
	private static final String TAG_STATUS = "status";
	private static final String TAG_TYPE_PHOTO = "type_photo";
	private static final String TAG_CREATEDATE = "createdate";
	private static final String TAG_UPDATEDATE = "updatedate";
	private static final String TAG_LOCATION_ID = "location_id";

	private List<Photo> photos;
	private Photo currentPhoto;

	public List<Photo> getComments() {
		return photos;
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (currentPhoto != null) {
			if (localName.equalsIgnoreCase(TAG_COMMENT_ID)) {
				currentPhoto.setComment_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_CREATEDATE)) {
				currentPhoto.setCreatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_ID)) {
				currentPhoto.setId(getString());
			} else if (localName.equalsIgnoreCase(TAG_IMAGE)) {
				currentPhoto.setImage(getString());
			} else if (localName.equalsIgnoreCase(TAG_STATUS)) {
				currentPhoto.setStatus(getString());
			} else if (localName.equalsIgnoreCase(TAG_STORE_ID)) {
				currentPhoto.setStore_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_THUMB)) {
				currentPhoto.setThumb(getString());
			} else if (localName.equalsIgnoreCase(TAG_TYPE_PHOTO)) {
				currentPhoto.setType_photo(getString());
			} else if (localName.equalsIgnoreCase(TAG_UPDATEDATE)) {
				currentPhoto.setUpdatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_USER_ID)) {
				currentPhoto.setUser_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_LOCATION_ID)) {
				currentPhoto.setLocation_id(getString());
			}
		}
		if (localName.equalsIgnoreCase(ITEMS)) {
			photos.add(currentPhoto);
		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		photos = new ArrayList<Photo>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(ITEMS)) {
			this.currentPhoto = new Photo();
		}
	}
}
