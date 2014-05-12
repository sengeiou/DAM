/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import whyq.model.ActivityItem;
import whyq.model.BusinessInfo;

public class ActivityHandler extends BaseHandler {

	private static final String TAG_ID = "id";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_ACTIVE_ID = "active_id";
	private static final String TAG_OPTIONAL_ID = "optional_id";
	private static final String TAG_ACTIVITY_TYPE = "activity_type";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_IS_READ = "is_read";
	private static final String TAG_CREATEDATE = "createdate";
	private static final String TAG_UPDATEDATE = "updatedate";
	private static final String TAG_BUSINESS_INFO = "business_info";
	private static final String TAG_USER_NAME = "user_name";
	private static final String TAG_NAME_STORE = "name_store";
	private static final String ITEM = "obj";

	private List<ActivityItem> activities;
	private ActivityItem currentActivityItem;
	private BusinessInfo currentBusinessInfo;

	public List<ActivityItem> getActivities() {
		return activities;
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (this.currentBusinessInfo != null) {
			if (localName.equalsIgnoreCase(TAG_ID)) {
				currentBusinessInfo.setId(getString());
			} else if (localName.equalsIgnoreCase(TAG_NAME_STORE)) {
				currentBusinessInfo.setName_store(getString());
			}
		}
		if (this.currentActivityItem != null) {
			if (localName.equalsIgnoreCase(TAG_ACTIVE_ID)) {
				currentActivityItem.setActive_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_ACTIVITY_TYPE)) {
				currentActivityItem.setActivity_type(getString());
			} else if (localName.equalsIgnoreCase(TAG_BUSINESS_INFO)) {
				currentActivityItem.setBusiness_info(currentBusinessInfo);
			} else if (localName.equalsIgnoreCase(TAG_CREATEDATE)) {
				currentActivityItem.setCreatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_ID)) {
				currentActivityItem.setId(getString());
			} else if (localName.equalsIgnoreCase(TAG_IS_READ)) {
				currentActivityItem.setIs_read(getInt());
			} else if (localName.equalsIgnoreCase(TAG_MESSAGE)) {
				currentActivityItem.setMessage(getString());
			} else if (localName.equalsIgnoreCase(TAG_OPTIONAL_ID)) {
				currentActivityItem.setOptional_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_UPDATEDATE)) {
				currentActivityItem.setUpdatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_USER_ID)) {
				currentActivityItem.setUser_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_USER_NAME)) {
				currentActivityItem.setUser_name(getString());
			} else if (localName.equalsIgnoreCase(ITEM)) {
				activities.add(currentActivityItem);
			}
		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		activities = new ArrayList<ActivityItem>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(ITEM)) {
			this.currentActivityItem = new ActivityItem();
		} else if (localName.equalsIgnoreCase(TAG_BUSINESS_INFO)) {
			this.currentBusinessInfo = new BusinessInfo();
		}
	}

}
