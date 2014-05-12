package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import whyq.model.BillItem;
import whyq.model.BusinessInfo;
import android.util.Log;

public class BillHandler extends BaseHandler {

	private static final String TAG_ID = "id";
	private static final String BILL_ID = "bill_id";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_STORE_ID = "store_id";
	private static final String TAG_TOTAL_VALUE = "total_value";
	private static final String TAG_TOTAL_REAL_VALUE = "total_real_value";
	private static final String TAG_DISCOUNT_VALUE = "discount_value";
	private static final String TAG_DELIVER_FEE_VALUE = "deliver_fee_value";
	private static final String TAG_CODE_BILL = "code_bill";
	private static final String TAG_DELIVER_TO = "deliver_to";
	private static final String TAG_DELIVER_TYPE = "deliver_type";
	private static final String TAG_STATUS_BILL = "status_bill";
	private static final String TAG_PARENT_BILL_ID = "parent_bill_id";
	private static final String TAG_CREATEDATE = "createdate";
	private static final String TAG_UPDATEDATE = "updatedate";
	private static final String TAG_BUSINESS_INFO = "business_info";
	private static final String TAG_TRANSACTION_ID = "transaction_id";
	private static final String TAG_STATUS_BILL_OLD = "status_bill_old";
	private static final String TAG_TEXT_STATUS_BILL = "text_status_bill";
	private static final String TAG_CREATEDATE_CV = "createdate_cv";
	private static final String TAG_TIME_DELIVER_CV = "time_deliver_cv";
	private static final String TAG_DETAIL = "detail";

	private static final String TAG_STORE_INFO = "store_info";

	private static final String ITEM = "obj";

	private static final String TAG_CITY = "city";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_ZIPCODE = "zipcode";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_STATUS = "status";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_RADIUS_ALLOW_DELIVER = "radius_allow_deliver";
	private static final String TAG_FEE_CHARGE_OUT_RADIUS_DELIVER_PER_KM = "fee_charge_out_radius_deliver_per_km";
	private static final String TAG_CATE_ID = "cate_id";
	private static final String TAG_NAME_STORE = "name_store";
	private static final String TAG_INTRO_STORE = "intro_store";
	private static final String TAG_PHONE_STORE = "phone_store";
	private static final String TAG_LOGO = "logo";
	private static final String TAG_STYLE = "style";
	private static final String TAG_START_TIME = "start_time";
	private static final String TAG_END_TIME = "end_time";
	private static final String TAG_IS_HOME_DELIVER = "is_home_deliver";
	private static final String TAG_IS_HOTEL_DELIVER = "is_hotel_deliver";
	private static final String TAG_IS_TAKE_AWAY = "is_take_away";
	private static final String TAG_IS_AT_PLACE = "is_at_place";
	private static final String TAG_START_TIME_DELIVER = "start_time_deliver";
	private static final String TAG_END_TIME_DELIVER = "end_time_deliver";
	private static final String TAG_MINIMUN_TIME_DELIVER = "minimun_time_deliver";
	private static final String TAG_VALUE_CONDITION_DELIVER = "value_condition_deliver";
	private static final String TAG_TABLE_QUANTITY = "table_quantity";
	private static final String TAG_COUNT_FAVOURITE_MEMBER = "count_favourite_member";
	private static final String TAG_COUNT_BILL = "count_bill";
	private static final String TAG_INCOME = "income";
	private static final String TAG_TIME_ZONE = "time_zone";
	private static final String TAG_NAME_CATE = "name_cate";
	private static final String TAG_IS_PAYMENT_VIA_PAYPAL = "is_payment_via_paypal";

	private List<BillItem> bills;
	private BillItem currentBill;
	private BusinessInfo currentBusinessInfo;

	public List<BillItem> getBills() {
		return bills;
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);

		if (this.currentBusinessInfo != null) {
			if (localName.equalsIgnoreCase(TAG_ADDRESS)) {
				currentBusinessInfo.setAddress(getString());
			} else if (localName.equalsIgnoreCase(TAG_CATE_ID)) {
				currentBusinessInfo.setCate_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_CITY)) {
				currentBusinessInfo.setCity(getString());
			} else if (localName.equalsIgnoreCase(TAG_COUNT_BILL)) {
				currentBusinessInfo.setCount_bill(getInt());
			} else if (localName.equalsIgnoreCase(TAG_CREATEDATE)) {
				currentBusinessInfo.setCreatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_END_TIME)) {
				currentBusinessInfo.setEnd_time(getString());
			} else if (localName.equalsIgnoreCase(TAG_END_TIME_DELIVER)) {
				currentBusinessInfo.setEnd_time_deliver(getString());
			} else if (localName
					.equalsIgnoreCase(TAG_FEE_CHARGE_OUT_RADIUS_DELIVER_PER_KM)) {
				currentBusinessInfo
						.setFee_charge_out_radius_deliver_per_km(getFloat());
			} else if (localName.equalsIgnoreCase(TAG_ID)) {
				currentBusinessInfo.setId(getString());
			} else if (localName.equalsIgnoreCase(TAG_INCOME)) {
				currentBusinessInfo.setIncome(getString());
			} else if (localName.equalsIgnoreCase(TAG_INTRO_STORE)) {
				currentBusinessInfo.setIntro_store(getString());
			} else if (localName.equalsIgnoreCase(TAG_IS_AT_PLACE)) {
				currentBusinessInfo.setIs_at_place(getBoolean());
			} else if (localName.equalsIgnoreCase(TAG_IS_HOME_DELIVER)) {
				currentBusinessInfo.setIs_home_deliver(getBoolean());
			} else if (localName.equalsIgnoreCase(TAG_IS_HOTEL_DELIVER)) {
				currentBusinessInfo.setIs_hotel_deliver(getBoolean());
			} else if (localName.equalsIgnoreCase(TAG_IS_TAKE_AWAY)) {
				currentBusinessInfo.setIs_take_away(getBoolean());
			} else if (localName.equalsIgnoreCase(TAG_LATITUDE)) {
				currentBusinessInfo.setLatitude(getDouble());
			} else if (localName.equalsIgnoreCase(TAG_LOGO)) {
				currentBusinessInfo.setLogo(getString());
			} else if (localName.equalsIgnoreCase(TAG_LONGITUDE)) {
				currentBusinessInfo.setLongitude(getDouble());
			} else if (localName.equalsIgnoreCase(TAG_MINIMUN_TIME_DELIVER)) {
				currentBusinessInfo.setMinimun_time_deliver(getString());
			} else if (localName.equalsIgnoreCase(TAG_NAME_CATE)) {
				currentBusinessInfo.setName_cate(getString());
			} else if (localName.equalsIgnoreCase(TAG_NAME_STORE)) {
				currentBusinessInfo.setName_store(getString());
			} else if (localName.equalsIgnoreCase(TAG_PHONE_STORE)) {
				currentBusinessInfo.setPhone_store(getString());
			} else if (localName.equalsIgnoreCase(TAG_RADIUS_ALLOW_DELIVER)) {
				currentBusinessInfo.setRadius_allow_deliver(getString());
			} else if (localName.equalsIgnoreCase(TAG_START_TIME)) {
				currentBusinessInfo.setStart_time(getString());
			} else if (localName.equalsIgnoreCase(TAG_START_TIME_DELIVER)) {
				currentBusinessInfo.setStart_time_deliver(getString());
			} else if (localName.equalsIgnoreCase(TAG_STATUS)) {
				currentBusinessInfo.setStatus(getString());
			} else if (localName.equalsIgnoreCase(TAG_STORE_ID)) {
				currentBusinessInfo.setStore_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_STYLE)) {
				currentBusinessInfo.setStyle(getString());
			} else if (localName.equalsIgnoreCase(TAG_TABLE_QUANTITY)) {
				currentBusinessInfo.setTable_quantity(getInt());
			} else if (localName.equalsIgnoreCase(TAG_TIME_ZONE)) {
				currentBusinessInfo.setTime_zone(getString());
			} else if (localName.equalsIgnoreCase(TAG_UPDATEDATE)) {
				currentBusinessInfo.setUpdatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_USER_ID)) {
				currentBusinessInfo.setUser_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_VALUE_CONDITION_DELIVER)) {
				currentBusinessInfo.setValue_condition_deliver(getString());
			} else if (localName.equalsIgnoreCase(TAG_ZIPCODE)) {
				currentBusinessInfo.setZipcode(getString());
			} else if (localName.equalsIgnoreCase(TAG_COUNT_FAVOURITE_MEMBER)) {
				currentBusinessInfo.setCount_favourite_member(getInt());
			} else if (localName.equalsIgnoreCase(TAG_IS_PAYMENT_VIA_PAYPAL)) {
				currentBusinessInfo.setIs_payment_via_paypal(getString());
			}
		}

		if (this.currentBill != null) {
			if (localName.equalsIgnoreCase(TAG_BUSINESS_INFO)
					|| localName.equalsIgnoreCase(TAG_STORE_INFO)) {
				currentBill.setBusiness_info(currentBusinessInfo);
			} else if (localName.equalsIgnoreCase(TAG_CREATEDATE)) {
				currentBill.setCreatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_CODE_BILL)) {
				currentBill.setCode_bill(getString());
			} else if (localName.equalsIgnoreCase(TAG_DELIVER_FEE_VALUE)) {
				currentBill.setDeliver_fee_value(getFloat());
			} else if (localName.equalsIgnoreCase(TAG_DELIVER_TO)) {
				currentBill.setDeliver_to(getString());
			} else if (localName.equalsIgnoreCase(TAG_DELIVER_TYPE)) {
				currentBill.setDeliver_type(getString());
			} else if (localName.equalsIgnoreCase(BILL_ID)) {
				currentBill.setId(getString());
			} else if (localName.equalsIgnoreCase(TAG_DISCOUNT_VALUE)) {
				currentBill.setDiscount_value(getFloat());
			} else if (localName.equalsIgnoreCase(TAG_PARENT_BILL_ID)) {
				currentBill.setParent_bill_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_STATUS_BILL)) {
				currentBill.setStatus_bill(getInt());
			} else if (localName.equalsIgnoreCase(TAG_STORE_ID)) {
				currentBill.setStore_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_TOTAL_REAL_VALUE)) {
				currentBill.setTotal_real_value(getFloat());
			} else if (localName.equalsIgnoreCase(TAG_TOTAL_VALUE)) {
				currentBill.setTotal_value(getFloat());
			} else if (localName.equalsIgnoreCase(TAG_TRANSACTION_ID)) {
				currentBill.setTransaction_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_UPDATEDATE)) {
				currentBill.setUpdatedate(getString());
			} else if (localName.equalsIgnoreCase(TAG_USER_ID)) {
				currentBill.setUser_id(getString());
			} else if (localName.equalsIgnoreCase(TAG_STATUS_BILL_OLD)) {
				currentBill.setStatus_bill_old(getString());
			} else if (localName.equalsIgnoreCase(TAG_TEXT_STATUS_BILL)) {
				currentBill.setText_status_bill(getString());
			} else if (localName.equalsIgnoreCase(TAG_CREATEDATE_CV)) {
				currentBill.setCreatedate_cv(getString());
			} else if (localName.equalsIgnoreCase(TAG_TIME_DELIVER_CV)) {
				currentBill.setTime_deliver_cv(getString());
			} else if (localName.equalsIgnoreCase(TAG_DETAIL)) {
				currentBill.setDetail(getString());
			} else if (localName.equalsIgnoreCase(ITEM)) {
				bills.add(currentBill);
			}

		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		bills = new ArrayList<BillItem>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(ITEM)) {
			Log.d(ITEM, "start obj");
			this.currentBill = new BillItem();
		} else if (localName.equalsIgnoreCase(TAG_BUSINESS_INFO)
				|| localName.equalsIgnoreCase(TAG_STORE_INFO)) {
			this.currentBusinessInfo = new BusinessInfo();
		}
	}
}
