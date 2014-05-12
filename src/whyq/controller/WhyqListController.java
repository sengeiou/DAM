package whyq.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import whyq.interfaces.WhyqList_Delegate;
import whyq.model.Store;
import whyq.model.UserCheckBill;
import whyq.utils.API;
import whyq.utils.XMLParser;
import android.util.Log;


public class WhyqListController implements WhyqList_Delegate {


	/**
	 * MSA
	 */
	public static boolean isLoading = false;
	public static int selectedPos = 0;
	
	public static boolean isFooterAdded = false;
	/**
	 * Constructor
	 */
	public WhyqListController(){
		
	}
	
	/**
	 * @TODO simulate perm lists
	 */
	public ArrayList<Store> getBusinessList( String url ){

		
		ArrayList<Store> permList = new ArrayList<Store>();
		
		if( url == "" ) {
			url = API.popularBusinessListURL;
			//permList.add(new Perm());
		}/* else {
			if(PermpingMain.getCurrentTab() == 0) {
				//add this for follower header
				permList.add(new Perm());
			}
		}*/
		XMLParser parser = new XMLParser( url , true );
		Document doc = null;
		if(parser != null)
			doc = parser.getDoc();
		permList = parsePermListFromDoc(doc);
//		NodeList responseNodeList = doc.getElementsByTagName("response"); // TODO: actually, only 1 response in the list.
//		String nextItem = "-1";
//		for (int i = 0; i < responseNodeList.getLength(); i++) {
//			Element e = (Element) responseNodeList.item(i);
//			nextItem = getValue(e, "nextItem");
//		}
//		
//		String previousItem = "-1";
//		for (int i = 0; i < responseNodeList.getLength(); i++) {
//			Element e = (Element) responseNodeList.item(i);
//			previousItem = getValue(e, "previousItem");
//		}
//		
//		NodeList permNodeList =  doc.getElementsByTagName("item");
//		
//		for( int i = 0; i< permNodeList.getLength(); i++ ){
//			
//			//Create perm
//			Element permElement = (Element ) permNodeList.item(i);
//			String permId = getValue(permElement, "permId");
//			String permDesc = getValue(permElement, "permDesc");
//			String permDateMessage = getValue(permElement, "permDateMessage");
//			String permBoard = getValue(permElement, "permCategory");
//			String permImage = getValue(permElement, "permImage");
//			String permUrl = getValue(permElement, "permUrl");
//			String permAudio = getValue(permElement, "permAudio");
//			
//			//User 
//			Element permUser = (Element) permElement.getElementsByTagName("user").item(0);
//			String userId  = getValue(permUser, "userId");
//			String userName  = getValue(permUser, "userName");
//			String userAvatar  = getValue(permUser, "userAvatar");
//			
//			//user avatar
//			WhyqImage userAvatarImage = new WhyqImage(userAvatar);
//			//user object
//			User permAuthor = new User(userId);
//			permAuthor.setName(userName);
//			permAuthor.setAvatar(userAvatarImage);
//			
//			
//			//Comment
//			NodeList permComments = permElement.getElementsByTagName("comment");
//			
//			ArrayList<Comment> comments = new ArrayList<Comment>();
//			for( int j = 0; j < permComments.getLength(); j ++ ){
//				Element comment = (Element) permComments.item(j);
//				//Comment user
//				if( comment != null ){
//					String commentId = getValue(comment, "id");
//					String commentContent = getValue(comment, "content");
//					
//					Comment permComment = new Comment( commentId, commentContent );
//					Element commentUser = (Element) comment.getElementsByTagName("l_user").item(0);
//					String commentUserId = getValue(commentUser, "l_userId");
//					String commentUserName = getValue(commentUser, "l_userName");
//					String commentUserAvatar = getValue(commentUser, "l_userAvatar");
//					//commentUserAvatar = "http://www.lahiguera.net/cinemania/actores/jackie_chan/fotos/5635/jackie_chan.jpg";
//					
//					WhyqImage commentAvatar = new WhyqImage( commentUserAvatar );
//					User commentAuthor = new User(commentUserId );
//					commentAuthor.setName(commentUserName);
//					commentAuthor.setAvatar( commentAvatar );
//					
//					permComment.setAuthor( commentAuthor );
//					
//					comments.add(permComment);
//				}	
//			}
//			
//			//Perm Stat
//			String permRepinCount  = getValue(permElement, "permRepinCount");
//			String permLikeCount  = getValue(permElement, "permLikeCount");
//			String permCommentCount  = getValue(permElement, "permCommentCount");
//			String lat = getValue(permElement, "permLat");
//			String lon = getValue(permElement, "permLong");
//			String permUserLikeCount = getValue(permElement, Constants.PERM_USERLIKECOUNT);
//			
////			Whyq whyq = new Whyq(permId, new WhyqBoard("BoardID", permBoard), permDesc, permDateMessage, new WhyqImage(permImage), comments, permUrl,permAudio);
////			whyq.setAuthor(permAuthor);
////			whyq.setPermRepinCount(permRepinCount);
////			whyq.setPermLikeCount(permLikeCount);
////			whyq.setPermCommentCount(permCommentCount);
////			whyq.setPermUserLikeCount(permUserLikeCount);
////			whyq.setLat(Float.valueOf(lat).floatValue());
////			whyq.setLon(Float.valueOf(lon).floatValue());
////			whyq.setNextItem(nextItem);
////			whyq.setPreviousItem(previousItem);
//			
//			permList.add(whyq);
//			
//			
//			
//		}
//		//add for footer
//		if(Integer.parseInt(nextItem) == -1 && Integer.parseInt(previousItem) == -1) {
//			isFooterAdded = false;
//		} else {
//			permList.add(new Whyq());
//			isFooterAdded = true;
//		}
//		
//		//ArrayList <Perm> permList = parser.permListFromNodeList("popularPerms");
		return permList;
		
	}
	
	private ArrayList<Store> parsePermListFromDoc(Document doc) {
		// TODO Auto-generated method stub
		ArrayList<Store> permList = new ArrayList<Store>();
		NodeList permNodeList =  doc.getElementsByTagName("obj");
		
		for( int i = 0; i< permNodeList.getLength(); i++ ){
			try {

				Element permElement = (Element ) permNodeList.item(i);
				Store item = new Store();
				String id = getValue(permElement, "id");
				String storeId = getValue(permElement, "store_id");
				String address = getValue(permElement, "address");
				String longitude = getValue(permElement, "longitude");
				String latitude = getValue(permElement, "latitude");
				String status = getValue(permElement, "status");
				String radius = getValue(permElement, "radius_allow_deliver");
				String feeChargeOutRadiesDeliverPerKm = getValue(permElement, "fee_charge_out_radius_allow_deliver");
				String  createdate = getValue(permElement, "createdate");
				String updatedate = getValue(permElement, "updatedate");
				String cateId = getValue(permElement, "cate_id");
				String userId =getValue(permElement, "user_id");
				String nameStore =getValue(permElement, "name_store");
				String introStore = getValue(permElement, "intro_store");
				String phoneStore =getValue(permElement, "phone_store");
				String logi =getValue(permElement, "logo");;
				String style =getValue(permElement, "style");
				String startTime =getValue(permElement, "start_time");
				String endTime =getValue(permElement, "end_time");
				boolean isHomeDeliver = getValue(permElement, "is_home_deliver").equals("1")?true:false;
				boolean isHotelDeliver = getValue(permElement, "is_hotel_deliver").equals("1")?true:false;
				boolean isTakeAway = getValue(permElement, "is_take_away").equals("1")?true:false;
				boolean isPlace = getValue(permElement, "is_at_place").equals("1")?true:false;
				String minimunTimeDeliver = getValue(permElement, "minimun_time_deliver");
				String tableQuantity = getValue(permElement, "table_quantity");
				String countFavouriteMemebr = getValue(permElement, "count_favourite_member");
				String coutBill =getValue(permElement, "count_bill");
				String inCome = getValue(permElement, "income");
				String nameCate = getValue(permElement, "name_cate");
				
				NodeList userNode = (NodeList) permElement.getElementsByTagName("user_check_bill");
				int size  = userNode.getLength();
				for(int i2=0;i2< size;i2++){
					Element element = (Element)userNode.item(i2);
					UserCheckBill userCheckBill= new UserCheckBill();
					userCheckBill.setTotalFriend(getValue(element, "TotalFriend"));
					userCheckBill.setTotalMember(getValue(element, "TotalMember"));
					userCheckBill.setId(getValue(element, "id"));
					userCheckBill.setFirstName(getValue(element, "first_name"));
					userCheckBill.setLastName(getValue(element, "last_name"));
					userCheckBill.setAvatar(getValue(element, "avatar"));
					userCheckBill.setStoreId(getValue(element, "store_id"));
					userCheckBill.setStatusBill(getValue(element, "status_bill"));
					userCheckBill.setInteractionPoint(getValue(element, "interaction_point"));
					item.setUserCheckBill(userCheckBill);
				}
				item.setId(id);
				item.setStoreId(storeId);
				item.setAddress(address);
				item.setLongitude(longitude);
				item.setLatitude(latitude);
				item.setStatus(status);
				item.setRadius(radius);
				item.setFreeChargeOutRadiusDelieverPerKm(feeChargeOutRadiesDeliverPerKm);
				item.setCreatedate(createdate);
				item.setUpdatedate(updatedate);
				item.setCateid(cateId);
				item.setUserId(userId);
				item.setNameStore(nameStore);
				item.setIntroStore(introStore);
				item.setPhoneStore(phoneStore);
				item.setLogo(logi);
				item.setStyle(style);
				item.setStartTime(startTime);
				item.setEndTime(endTime);
				item.setHomeDeliver(isHomeDeliver);
				item.setHotelDeliver(isHotelDeliver);
				item.setTakeAway(isTakeAway);
				
				item.setTableQuantity(tableQuantity);
				item.setCountFavaouriteMember(countFavouriteMemebr);
				item.setCoutBill(coutBill);
				item.setIncome(inCome);
				item.setNameCate(nameCate);
				item.setMinimum(minimunTimeDeliver);
				item.setPlace(isPlace);
				
				permList.add(item);
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return permList;
	}

	/**
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public ArrayList<Store> getBusinessList(String url, List<NameValuePair> nameValuePairs){

		
		ArrayList<Store> permList = new ArrayList<Store>();
		
		if( url == "" ) {
			url = API.popularBusinessListURL;
			//permList.add(new Perm());
		}/* else {
			if(PermpingMain.getCurrentTab() == 0) {
				//add this for follower header
				permList.add(new Perm());
			}
		}*/
		XMLParser parser = new XMLParser(XMLParser.PERMLIST, WhyqListController.this, url, nameValuePairs, true);
		Document doc = parser.getDoc();
//		NodeList responseNodeList = doc.getElementsByTagName("response"); // TODO: actually, only 1 response in the list.
//		String nextItem = "-1";
//		for (int i = 0; i < responseNodeList.getLength(); i++) {
//			Element e = (Element) responseNodeList.item(i);
//			nextItem = getValue(e, "nextItem");
//		}
//		String previousItem = "-1";
//		for (int i = 0; i < responseNodeList.getLength(); i++) {
//			Element e = (Element) responseNodeList.item(i);
//			previousItem = getValue(e, "previousItem");
//		}
		NodeList permNodeList =  doc.getElementsByTagName("obj");
		
		for( int i = 0; i< permNodeList.getLength(); i++ ){
			try {

				Element permElement = (Element ) permNodeList.item(i);
				Store item = new Store();
				String id = getValue(permElement, "id");
				String storeId = getValue(permElement, "store_id");
				String address = getValue(permElement, "address");
				String longitude = getValue(permElement, "longitude");
				String latitude = getValue(permElement, "latitude");
				String status = getValue(permElement, "status");
				String radius = getValue(permElement, "radius_allow_deliver");
				String feeChargeOutRadiesDeliverPerKm = getValue(permElement, "fee_charge_out_radius_deliver_per_km");
				String  createdate = getValue(permElement, "createdate");
				String updatedate = getValue(permElement, "updatedate");
				String cateId = getValue(permElement, "cate_id");
				String userId =getValue(permElement, "user_id");
				String nameStore =getValue(permElement, "name_store");
				String introStore =getValue(permElement, "intro_store");
				String phoneStore = getValue(permElement, "phone_store");
				String logo =getValue(permElement, "logo");
				String style =getValue(permElement, "style");;
				String startTime =getValue(permElement, "start_time");
				String endTime =getValue(permElement, "end_time");
				boolean isHomeDeliver = getValue(permElement, "is_home_deliver").equals("1")?true:false;
				boolean isHotelDeliver = getValue(permElement, "is_hotel_deliver").equals("1")?true:false;
				boolean isTakeAway = getValue(permElement, "is_take_away").equals("1")?true:false;
				boolean isPlace = getValue(permElement, "is_at_place").equals("1")?true:false;
				boolean isFavourite = getValue(permElement, "is_favourite").equals("1")?true:false;
				String minimunTimeDeliver = getValue(permElement, "minimun_time_deliver");
				String tableQuantity = getValue(permElement, "table_quantity");
				String countFavouriteMemebr = getValue(permElement, "count_favourite_member");
				String coutBill =getValue(permElement, "count_bill");
				String inCome = getValue(permElement, "income");
				String nameCate = getValue(permElement, "name_cate");
				NodeList userNode = (NodeList) permElement.getElementsByTagName("user_check_bill");
				int size  = userNode.getLength();
				for(int i2=0;i2< size;i2++){
					Element element = (Element)userNode.item(i2);
					UserCheckBill userCheckBill= new UserCheckBill();
					Log.d("usercheckbill","totalfriend "+getValue(element, "TotalFriend"));
					userCheckBill.setTotalFriend(getValue(element, "TotalFriend"));
					userCheckBill.setTotalMember(getValue(element, "TotalMember"));
					userCheckBill.setId(getValue(element, "id"));
					userCheckBill.setFirstName(getValue(element, "first_name"));
					userCheckBill.setLastName(getValue(element, "last_name"));
					userCheckBill.setAvatar(getValue(element, "avatar"));
					userCheckBill.setStoreId(getValue(element, "store_id"));
					userCheckBill.setStatusBill(getValue(element, "status_bill"));
					userCheckBill.setInteractionPoint(getValue(element, "interaction_point"));
					item.setUserCheckBill(userCheckBill);
				}

				item.setId(id);
				item.setIsFavourite(isFavourite);
				item.setStoreId(storeId);
				item.setAddress(address);
				item.setLongitude(longitude);
				item.setLatitude(latitude);
				item.setStatus(status);
				item.setRadius(radius);
				item.setFreeChargeOutRadiusDelieverPerKm(feeChargeOutRadiesDeliverPerKm);
				item.setCreatedate(createdate);
				item.setUpdatedate(updatedate);
				item.setCateid(cateId);
				item.setUserId(userId);
				item.setNameStore(nameStore);
				item.setIntroStore(introStore);
				item.setPhoneStore(phoneStore);
				item.setLogo(logo);
				item.setStyle(style);
				item.setStartTime(startTime);
				item.setEndTime(endTime);
				item.setHomeDeliver(isHomeDeliver);
				item.setHotelDeliver(isHotelDeliver);
				item.setTakeAway(isTakeAway);
				
				item.setTableQuantity(tableQuantity);
				item.setCountFavaouriteMember(countFavouriteMemebr);
				item.setCoutBill(coutBill);
				item.setIncome(inCome);
				item.setNameCate(nameCate);
				item.setPlace(isPlace);
				item.setMinimum(minimunTimeDeliver);
				permList.add(item);
//				//Create perm
//				Element permElement = (Element ) permNodeList.item(i);
//				String permId = getValue(permElement, "permId");
//				String permDesc = getValue(permElement, "permDesc");
//				String permDateMessage = getValue(permElement, "permDateMessage");
//				String permBoard = getValue(permElement, "permCategory");
//				String permImage = getValue(permElement, "permImage");
//				String permUrl = getValue(permElement, "permUrl");
//				String permAudio = getValue(permElement, "permAudio");
//				//User 
//				Element permUser = (Element) permElement.getElementsByTagName("user").item(0);
//				String userId  = getValue(permUser, "userId");
//				String userName  = getValue(permUser, "userName");
//				String userAvatar  = getValue(permUser, "userAvatar");
//				
//				//user avatar
//				WhyqImage userAvatarImage = new WhyqImage(userAvatar);
//				//user object
//				User permAuthor = new User(userId);
//				permAuthor.setName(userName);
//				permAuthor.setAvatar(userAvatarImage);
//				
//				
//				//Comment
//				NodeList permComments = permElement.getElementsByTagName("comment");
//				
//				ArrayList<Comment> comments = new ArrayList<Comment>();
//				for( int j = 0; j < permComments.getLength(); j ++ ){
//					Element comment = (Element) permComments.item(j);
//					//Comment user
//					if( comment != null ){
//						String commentId = getValue(comment, "id");
//						String commentContent = getValue(comment, "content");
//						
//						Comment permComment = new Comment( commentId, commentContent );
//						Element commentUser = (Element) comment.getElementsByTagName("l_user").item(0);
//						String commentUserId = getValue(commentUser, "l_userId");
//						String commentUserName = getValue(commentUser, "l_userName");
//						String commentUserAvatar = getValue(commentUser, "l_userAvatar");
//						//commentUserAvatar = "http://www.lahiguera.net/cinemania/actores/jackie_chan/fotos/5635/jackie_chan.jpg";
//						
//						WhyqImage commentAvatar = new WhyqImage( commentUserAvatar );
//						User commentAuthor = new User(commentUserId );
//						commentAuthor.setName(commentUserName);
//						commentAuthor.setAvatar( commentAvatar );
//						
//						permComment.setAuthor( commentAuthor );
//						
//						comments.add(permComment);
//					}	
//				}
//				
//				//Perm Stat
//				String permRepinCount  = getValue(permElement, "permRepinCount");
//				String permLikeCount  = getValue(permElement, "permLikeCount");
//				String permCommentCount  = getValue(permElement, "permCommentCount");
//				String lat = getValue(permElement, "permLat");
//				String lon = getValue(permElement, "permLong");
//				String permUserLikeCount = getValue(permElement, Constants.PERM_USERLIKECOUNT);
//				
//				Whyq whyq = new Whyq(permId, new WhyqBoard("BoardID", permBoard), permDesc, permDateMessage, new WhyqImage(permImage), comments, permUrl,permAudio);
//				whyq.setAuthor(permAuthor);
//				whyq.setPermRepinCount(permRepinCount);
//				whyq.setPermLikeCount(permLikeCount);
//				whyq.setPermCommentCount(permCommentCount);
//				whyq.setPermUserLikeCount(permUserLikeCount);
//				whyq.setLat(Float.valueOf(lat).floatValue());
//				whyq.setLon(Float.valueOf(lon).floatValue());
//				whyq.setNextItem(nextItem);
//				whyq.setPreviousItem(previousItem);
				
				
				
				
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		//add for footer
//		if(Integer.parseInt(nextItem) == -1 && Integer.parseInt(previousItem) == -1) {
//			isFooterAdded = false;
//		} else {
//			permList.add(new Whyq());
//			isFooterAdded = true;
//		}
		//ArrayList <Perm> permList = parser.permListFromNodeList("popularPerms");
		return permList;
		
	}
	
	
	public String getValue( Element e, String tag ){
		if( e != null )
		{
			if(e.getElementsByTagName(tag).item(0) !=null){
				Node node = e.getElementsByTagName(tag).item(0).getFirstChild();
				if( node != null ){
					return node.getNodeValue();
				}
				
			}
		}
		return "";
	}

	@Override
	public void onSuccess(Document doc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}
}
