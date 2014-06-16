package whyq.service;

import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import whyq.handler.ActivityHandler;
import whyq.handler.BillHandler;
import whyq.handler.CommentHandler;
import whyq.handler.FriendFacebookHandler;
import whyq.handler.FriendTwitterHandler;
import whyq.handler.FriendWhyqHandler;
import whyq.handler.PhotoHandler;
import whyq.handler.UserHandler;
import whyq.handler.UserProfileHandler;
import whyq.model.Bill;
import whyq.model.BillItem;
import whyq.model.BillPushNotification;
import whyq.model.DeliveryFee;
import whyq.model.Distance;
import whyq.model.ExtraItem;
import whyq.model.Faq;
import whyq.model.Location;
import whyq.model.Menu;
import whyq.model.OptionItem;
import whyq.model.OrderCheckData;
import whyq.model.OrderSendResult;
import whyq.model.Photo;
import whyq.model.Product;
import whyq.model.ProductTypeInfo;
import whyq.model.Promotion;
import whyq.model.ResponseData;
import whyq.model.SearchFriend;
import whyq.model.SizeItem;
import whyq.model.StatusUser;
import whyq.model.Store;
import whyq.model.StoreInfo;
import whyq.model.User;
import whyq.model.UserCheckBill;
import whyq.model.UserProfile;
import whyq.model.WhyqImage;
import android.util.Log;

public class DataParser {

	public static String issueIntro = "";

	// private StoreIssueDataControler issueControler;
	public DataParser() {
		// issueController.resetStorySetData();
	}

	public Object parseRetaurentList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object parseLogout() {
		// TODO Auto-generated method stub
		// result.setSuccess(_root.optBoolean("success", false));
		// return null
		return null;
	}

	public Object parserLoginData(String inputString) {
		// TODO Auto-generated method stub
		try {
			ResponseData data = new ResponseData();
			Document doc = XMLfromString(inputString);
			ArrayList<Store> permList = new ArrayList<Store>();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				/*
				 * XMLReader xmlReader = initializeReader(); UserHandler
				 * userHandler = new UserHandler(); // assign the handler
				 * xmlReader.setContentHandler(userHandler); xmlReader.parse(new
				 * InputSource(new StringReader(inputString)));
				 */
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				// data.setData(userHandler.getUser());

				User user = new User();
				NodeList nodeList = doc.getElementsByTagName("obj");
				for (int i = 0; i < nodeList.getLength(); i++) {
					try {
						Element element = (Element) nodeList.item(i);

						user.setToken(getValue(element, "token"));
						user.setId(getValue(element, "id"));
						user.setEmail(getValue(element, "email"));
						user.setRoleId(getValue(element, "role_id"));
						user.setAcive(getValue(element, "is_active")
								.equals("1"));
						user.setTotalMoney(getValue(element,
								"total_saving_money"));
						user.setTotalSavingMoney(getValue(element,
								"total_saving_money"));
						user.setTotalComment(getValue(element, "total_comment"));
						user.setTotalFriend(getValue(element, "total_friend"));
						user.setTotalFavourite(getValue(element,
								"total_favourite"));
						user.setTotalCheckBill(getValue(element,
								"total_check_bill"));
						user.setTwitterId(getValue(element, "twitter_id"));
						user.setFacebookId(getValue(element, "facebook_id"));
						user.setStatus(getValue(element, "status"));
						user.setCreateDate(getValue(element, "createdate"));
						user.setUpdateDate(getValue(element, "updatedate"));
						user.setFirstName(getValue(element, "first_name"));
						user.setLastName(getValue(element, "last_name"));
						if (!getValue(element, "gender").equals(""))
							user.setGender(Integer.parseInt(getValue(element,
									"gender")));
						// user.seta(getValue(element, "avatar"));
						user.setCity(getValue(element, "city"));
						user.setAddress(getValue(element, "address"));
						user.setPhoneNumber(getValue(element, "phone_number"));
						user.setPaypalEmail(getValue(element, "paypal_email"));
						user.setStatusUser(getValue(element, "status_user"));
						user.setTotalCount(getValue(element, "total_count"));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

				data.setData(user);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public final Object parseActivities(String inputString) {
		try {
			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			if(doc.getElementsByTagName("Status") !=null){
				String statusResponse = doc.getElementsByTagName("Status").item(0)
						.getFirstChild().getNodeValue();
				
				if (statusResponse.equals("200")) {
					String totalPage = doc.getElementsByTagName("TotalPage").item(0)
							.getFirstChild().getNodeValue();
					XMLReader xmlReader = initializeReader();
					ActivityHandler handler = new ActivityHandler();
					xmlReader.setContentHandler(handler);
					xmlReader.parse(new InputSource(new StringReader(inputString)));

					final String mes = doc.getElementsByTagName("Message").item(0)
							.getFirstChild().getNodeValue();
					data.setStatus(statusResponse);
					data.setTotalPage(Integer.parseInt(totalPage));
					data.setData(handler.getActivities());
					data.setMessage(mes);
					return data;
				} else {
					final String mes = doc.getElementsByTagName("Message").item(0)
							.getFirstChild().getNodeValue();
					data.setStatus(statusResponse);
					data.setData(null);
					data.setMessage(mes);
					return data;

				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object parseBills(String inputString) {
		try {
			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				BillHandler handler = new BillHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(new StringReader(inputString)));

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(handler.getBills());
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final UserProfile parseUerProfiles(String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			UserProfileHandler handler = new UserProfileHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler.getUser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object parsePhotos(String inputString) {
		try {

			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			String statusResponse = "";
			if (doc != null)
				doc.getElementsByTagName("Status").item(0).getFirstChild()
						.getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				PhotoHandler handler = new PhotoHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(new StringReader(inputString)));

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(handler.getComments());
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object parseComments(String inputString) {
		try {
			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			String totalPage = doc.getElementsByTagName("TotalPage").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				CommentHandler handler = new CommentHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(new StringReader(inputString)));

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(handler.getComments());
				data.setMessage(mes);
				data.setTotalPage(Integer.parseInt(totalPage));
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setTotalPage(Integer.parseInt(totalPage));
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object parseFriendFacebook(String inputString) {
		try {
			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {

				XMLReader xmlReader = initializeReader();
				FriendFacebookHandler handler = new FriendFacebookHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(new StringReader(inputString)));

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(handler);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object parseFriendTwitter(String inputString) {
		try {
			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {

				XMLReader xmlReader = initializeReader();
				FriendTwitterHandler handler = new FriendTwitterHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(new StringReader(inputString)));

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(handler);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object parseFriendWhyq(String inputString) {
		try {
			Document doc = XMLfromString(inputString);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				FriendWhyqHandler handler = new FriendWhyqHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(new StringReader(inputString)));

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(handler.getFriends());
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static XMLReader initializeReader()
			throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Create a parser
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		return xmlReader;
	}

	public Object parseFavouriteFood(String input) {
		try {
			ArrayList<Product> productList = new ArrayList<Product>();
			Document doc = XMLfromString(input);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				NodeList nodeList = doc.getElementsByTagName("obj");

				for (int i = 0; i < nodeList.getLength(); i++) {
					try {
						Element permElement = (Element) nodeList.item(i);
						Product mProduct = new Product();
						mProduct.id = getValue(permElement, "id");
						mProduct.storeId = getValue(permElement, "store_id");
						mProduct.name = getValue(permElement, "name_product");
						mProduct.value = getValue(permElement, "value");
						mProduct.valuePromotion = getValue(permElement,
								"value_promotion");
						mProduct.description = getValue(permElement,
								"description");
						mProduct.imgProduct = getValue(permElement,
								"image_product");
						mProduct.imgThumb = getValue(permElement, "image_thumb");
						mProduct.status = getValue(permElement, "status");
						mProduct.productTypeId = getValue(permElement,
								"type_product_id");
						mProduct.createDate = getValue(permElement,
								"createdate");
						mProduct.updateDate = getValue(permElement,
								"updatedate");
						mProduct.sort = getValue(permElement, "sort");
						mProduct.likeCount = getValue(permElement, "count_like");
						mProduct.isLike = getValue(permElement, "is_like")
								.equals("1") ? true : false;

						NodeList groupItemInfo = (NodeList) permElement
								.getElementsByTagName("group_item_info");
						mProduct.groupItemId = getValue(
								((Element) groupItemInfo.item(0)), "id");
						mProduct.groupItemName = getValue(
								((Element) groupItemInfo.item(0)), "group_name");

						productList.add(mProduct);
					} catch (Exception e) {
					}
				}
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(productList);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(productList);
				data.setMessage(mes);
				return data;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return input;
	}

	public Object parseBusinessList(String result) {
		try {

			// TODO Auto-generated method stub
			ArrayList<Store> permList = new ArrayList<Store>();
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			String totalPage = doc.getElementsByTagName("TotalPage").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				NodeList nodeList = doc.getElementsByTagName("obj");

				for (int i = 0; i < nodeList.getLength(); i++) {
					try {

						Element permElement = (Element) nodeList.item(i);
						Store item = new Store();
						String id = getValue(permElement, "id");
						String storeId = getValue(permElement, "store_id");
						String address = getValue(permElement, "address");
						String longitude = getValue(permElement, "longitude");
						String latitude = getValue(permElement, "latitude");
						String status = getValue(permElement, "status");
						String radius = getValue(permElement,
								"radius_allow_deliver");
						String feeChargeOutRadiesDeliverPerKm = getValue(
								permElement,
								"fee_charge_out_radius_allow_deliver");
						String feeChargeOutRadiesDeliverPerOrder = getValue(
								permElement,
								"feeChargeOutRadiesDeliverPerOrder");
						String createdate = getValue(permElement, "createdate");
						String updatedate = getValue(permElement, "updatedate");
						String cateId = getValue(permElement, "cate_id");
						String userId = getValue(permElement, "user_id");
						String nameStore = getValue(permElement, "name_store");
						String introStore = getValue(permElement, "intro_store");
						String phoneStore = getValue(permElement, "phone_store");
						String merchant_number = getValue(permElement,
								"merchant_number");
						String logi = getValue(permElement, "logo");
						;
						String style = getValue(permElement, "style");
						String startTime = getValue(permElement, "start_time");
						String endTime = getValue(permElement, "end_time");
						boolean isHomeDeliver = getValue(permElement,
								"is_home_deliver").equals("1") ? true : false;
						boolean isHotelDeliver = getValue(permElement,
								"is_hotel_deliver").equals("1") ? true : false;
						boolean isTakeAway = getValue(permElement,
								"is_take_away").equals("1") ? true : false;
						boolean isPlace = getValue(permElement, "is_at_place")
								.equals("1") ? true : false;
						String minimunTimeDeliver = getValue(permElement,
								"minimun_time_deliver");
						String tableQuantity = getValue(permElement,
								"table_quantity");
						String countFavouriteMemebr = getValue(permElement,
								"count_favourite_member");
						boolean isFavourite = getValue(permElement,
								"is_favourite").equals("1") ? true : false;
						String coutBill = getValue(permElement, "count_bill");
						String inCome = getValue(permElement, "income");
						String nameCate = getValue(permElement, "name_cate");
						String description = getValue(permElement,
								"description");
						String distance = getValue(permElement, "distance");

						NodeList promotionNode = (NodeList) permElement
								.getElementsByTagName("promotion");
						int size1 = promotionNode.getLength();
						ArrayList<Promotion> promotionList = new ArrayList<Promotion>();
						for (int i2 = 0; i2 < size1; i2++) {
							try {
								Element element = (Element) promotionNode
										.item(i2);
								if (!getValue(element, "id").equals("")) {
									Promotion promotion = new Promotion();
									promotion.setId(getValue(element, "id"));
									promotion.setStoreId(getValue(element,
											"store_id"));
									promotion.setLocationId(getValue(element,
											"location_id"));
									promotion.setTypePromotionId(getValue(
											element, "type_promotion_id"));
									promotion.setTitlePromotion(getValue(
											element, "title_promotion"));
									promotion.setValuePromotion(getValue(
											element, "value_promotion"));
									promotion.setTmpData(getValue(element,
											"tmp_data"));
									promotion.setTypeValue(getValue(element,
											"type_value"));
									promotion.setImage(getValue(element,
											"image"));
									promotion
											.setText(getValue(element, "text"));
									promotion.setStatus(getValue(element,
											"status"));
									promotion.setConditionPromotion(getValue(
											element, "condition_promotion"));
									promotion.setDescriptionPromotion(getValue(
											element, "description_promotion"));
									promotion.setStartDate(getValue(element,
											"start_date"));
									promotion.setEndDate(getValue(element,
											"end_date"));
									promotion.setCreateDate(getValue(element,
											"createdate"));
									promotion.setUpdateTime(getValue(element,
											"updatedate"));
									promotion.setNameTypePromotion(getValue(
											element, "name_type_promotion"));
									promotion.setDescripton(getValue(element,
											"description"));
									promotionList.add(promotion);

								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
						item.setPromotionList(promotionList);

						NodeList userNode = (NodeList) permElement
								.getElementsByTagName("user_check_bill");
						int size = userNode.getLength();
						List<UserCheckBill> userCheckBillList = new ArrayList<UserCheckBill>();
						for (int i2 = 0; i2 < size; i2++) {
							Element element = (Element) userNode.item(i2);
							UserCheckBill userCheckBill = new UserCheckBill();
							userCheckBill.setTotalFriend(getValue(element,
									"TotalFriend"));
							userCheckBill.setTotalMember(getValue(element,
									"TotalMember"));
							userCheckBill.setId(getValue(element, "id"));
							userCheckBill.setFirstName(getValue(element,
									"first_name"));
							userCheckBill.setLastName(getValue(element,
									"last_name"));
							userCheckBill
									.setAvatar(getValue(element, "avatar"));
							userCheckBill.setStoreId(getValue(element,
									"store_id"));
							userCheckBill.setStatusBill(getValue(element,
									"status_bill"));
							userCheckBill.setInteractionPoint(getValue(element,
									"interaction_point"));
							item.setUserCheckBill(userCheckBill);
							userCheckBillList.add(userCheckBill);
							i2 = size;
						}
						item.setUserCheckBillList(userCheckBillList);
						item.setId(id);
						item.setStoreId(storeId);
						item.setAddress(address);
						item.setLongitude(longitude);
						item.setLatitude(latitude);
						item.setStatus(status);
						item.setRadius(radius);
						item.setIsFavourite(isFavourite);
						item.setFreeChargeOutRadiusDelieverPerKm(feeChargeOutRadiesDeliverPerKm);
						item.setFreeChargeOutRadiusDelieverPerOrder(feeChargeOutRadiesDeliverPerOrder);
						item.setCreatedate(createdate);
						item.setUpdatedate(updatedate);
						item.setCateid(cateId);
						item.setUserId(userId);
						item.setNameStore(nameStore);
						item.setIntroStore(introStore);
						item.setPhoneStore(phoneStore);
						item.setMerchantNumber(merchant_number);
						item.setLogo(logi);
						item.setStyle(style);
						item.setStartTime(startTime);
						item.setEndTime(endTime);
						item.setHomeDeliver(isHomeDeliver);
						item.setHotelDeliver(isHotelDeliver);
						item.setTakeAway(isTakeAway);
						item.setAtPlace(isPlace);

						item.setTableQuantity(tableQuantity);
						item.setCountFavaouriteMember(countFavouriteMemebr);
						item.setCoutBill(coutBill);
						item.setIncome(inCome);
						item.setNameCate(nameCate);
						item.setDistance(distance);
						item.setDiscription(description);
						item.setMinimum(minimunTimeDeliver);
						item.setPlace(isPlace);

						permList.add(item);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setTotalPage(Integer.parseInt(totalPage));
				data.setData(permList);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setTotalPage(Integer.parseInt(totalPage));
				data.setData(permList);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public Object parseBusinessDetail(String result) {
		try {

			// TODO Auto-generated method stub
			ArrayList<Store> permList = new ArrayList<Store>();
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				NodeList nodeList = doc.getElementsByTagName("obj");

				for (int i = 0; i < nodeList.getLength(); i++) {
					Element permElement = (Element) nodeList.item(i);
					Store item = new Store();
					String id = getValue(permElement, "id");
					String storeId = getValue(permElement, "store_id");
					String address = getValue(permElement, "address");
					String longitude = getValue(permElement, "longitude");
					String latitude = getValue(permElement, "latitude");
					String status = getValue(permElement, "status");
					String radius = getValue(permElement,
							"radius_allow_deliver");
					String feeChargeOutRadiesDeliverPerKm = getValue(
							permElement, "fee_charge_out_radius_deliver_per_km");
					String feeChargeOutRadiesDeliverPerOrder = getValue(
							permElement,
							"fee_charge_for_delivery_sub_product_for_every_order");
					String createdate = getValue(permElement, "createdate");
					String updatedate = getValue(permElement, "updatedate");
					String cateId = getValue(permElement, "cate_id");
					String userId = getValue(permElement, "user_id");
					String nameStore = getValue(permElement, "name_store");
					String introStore = getValue(permElement, "intro_store");
					String phoneStore = getValue(permElement, "phone_store");
					String merchant_number = getValue(permElement,
							"merchant_number");
					String logo = getValue(permElement, "logo");
					String style = getValue(permElement, "style");
					;
					String startTime = getValue(permElement, "start_time");
					String endTime = getValue(permElement, "end_time");
					boolean isHomeDeliver = getValue(permElement,
							"is_home_deliver").equals("1") ? true : false;
					boolean isHotelDeliver = getValue(permElement,
							"is_hotel_deliver").equals("1") ? true : false;
					boolean isTakeAway = getValue(permElement, "is_take_away")
							.equals("1") ? true : false;
					boolean isPlace = getValue(permElement, "is_at_place")
							.equals("1") ? true : false;
					String minimunTimeDeliver = getValue(permElement,
							"minimun_time_deliver");
					String tableQuantity = getValue(permElement,
							"table_quantity");
					String countFavouriteMemebr = getValue(permElement,
							"count_favourite_member");
					boolean isFavourite = getValue(permElement,
							"is_favourite").equals("1") ? true : false;
					String coutBill = getValue(permElement, "count_bill");
					String inCome = getValue(permElement, "income");
					String nameCate = getValue(permElement, "name_cate");
					String distance = getValue(permElement, "distance");
					String description = getValue(permElement, "description");
					String countComment = getValue(permElement, "count_comment");
					item.setId(id);
					item.setStoreId(storeId);
					item.setAddress(address);
					item.setLongitude(longitude);
					item.setLatitude(latitude);
					item.setStatus(status);
					item.setRadius(radius);
					item.setFreeChargeOutRadiusDelieverPerKm(feeChargeOutRadiesDeliverPerKm);
					item.setFreeChargeOutRadiusDelieverPerOrder(feeChargeOutRadiesDeliverPerOrder);
					item.setCreatedate(createdate);
					item.setUpdatedate(updatedate);
					item.setCateid(cateId);
					item.setUserId(userId);
					item.setNameStore(nameStore);
					item.setIntroStore(introStore);
					item.setPhoneStore(phoneStore);
					item.setMerchantNumber(merchant_number);
					item.setLogo(logo);
					item.setStyle(style);
					item.setStartTime(startTime);
					item.setEndTime(endTime);
					item.setHomeDeliver(isHomeDeliver);
					item.setHotelDeliver(isHotelDeliver);
					item.setTakeAway(isTakeAway);
					item.setAtPlace(isPlace);

					item.setTableQuantity(tableQuantity);
					item.setCountFavaouriteMember(countFavouriteMemebr);
					item.setIsFavourite(isFavourite);
					item.setCoutBill(coutBill);
					item.setIncome(inCome);
					item.setNameCate(nameCate);
					item.setDistance(distance);
					item.setDiscription(description);
					item.setPlace(isPlace);
					item.setCountComment(countComment);
					item.setMinimum(minimunTimeDeliver);
					NodeList nodes = permElement.getElementsByTagName("user");
					int lengthUser = nodes.getLength();
					ArrayList<User> userList = new ArrayList<User>();
					for (int j = 0; j < lengthUser; j++) {
						Element element = (Element) nodes.item(j);
						User user = new User();
						user.setId(getValue(element, "id"));
						user.setEmail(getValue(element, "email"));
						user.setRoleId(getValue(element, "role_id"));
						user.setAcive(getValue(element, "is_active")
								.equals("1") ? true : false);
						user.setTotalMoney(getValue(element,
								"total_saving_money"));
						user.setTotalSavingMoney(getValue(element,
								"total_comment"));
						user.setTotalComment(getValue(element, "total_comment"));
						user.setTotalCommentLike(getValue(element,
								"total_comment_like"));
						user.setTotalFriend(getValue(element, "total_friend"));
						user.setTotalFavourite(getValue(element,
								"total_favourite"));
						user.setTotalCheckBill(getValue(element,
								"total_check_bill"));
						user.setStatus(getValue(element, "status"));
						user.setCreateDate(getValue(element, "createdate"));
						user.setUpdateDate(getValue(element, "updatedate"));
						user.setFirstName(getValue(element, "first_name"));
						user.setLastName(getValue(element, "last_name"));
						user.setGender(Integer.parseInt(getValue(element,
								"gender")));
						WhyqImage image = new WhyqImage(getValue(element,
								"avatar"));
						user.setUrlAvatar(getValue(element, "avatar"));
						user.setAvatar(image);
						user.setAddress(getValue(element, "address"));
						userList.add(user);
					}
					item.setUserList(userList);

					NodeList menuNodes = permElement
							.getElementsByTagName("menu");
					int lengthMenuNode2 = menuNodes.getLength();
					ArrayList<Menu> menuList = new ArrayList<Menu>();

					for (int c2 = 0; c2 < lengthMenuNode2; c2++) {
						try {

							Element elementPr = (Element) menuNodes.item(c2);
							NodeList childList = elementPr.getChildNodes();
							int lengthMenuNode = childList.getLength();
							for (int c = 0; c < lengthMenuNode; c++) {
								Element menuElement = (Element) childList
										.item(c);

								NodeList productNodes = menuElement
										.getElementsByTagName("list_item");
								int lengthProductNode2 = productNodes
										.getLength();
								for (int c3 = 0; c3 < lengthProductNode2; c3++) {
									Element elementList = (Element) productNodes
											.item(c3);
									NodeList productsNoteList = elementList
											.getChildNodes();
									int lengthProductNode = productsNoteList
											.getLength();

									for (int c4 = 0; c4 < lengthProductNode; c4++) {
										Element element = (Element) productsNoteList
												.item(c4);
										Menu menu = new Menu();
										menu.setId(getValue(element, "id"));
										menu.setStoreId(getValue(element,
												"store_id"));
										menu.setGroupId(getValue(menuElement,
												"id"));
										menu.setGroupName(getValue(menuElement,
												"group_name"));
										menu.setNameProduct(getValue(element,
												"name_product"));
										menu.setValue(getValue(element, "value"));
										menu.setValuePromotion(getValue(
												element, "value_promotion"));
										menu.setImageProduct(getValue(element,
												"image_product"));
										menu.setImageThumb(getValue(element,
												"image_thumb"));
										menu.setStatus(getValue(element,
												"status"));
										menu.setTypeProductId(getValue(element,
												"type_product_id"));
										menu.setCreateDate(getValue(element,
												"createdate"));
										menu.setUpdateData(getValue(element,
												"updatedate"));
										menu.setSort(getValue(element, "sort"));

										menu.setFavorite(getValue(element, "is_like").equals("1")? true: false);
										menu.setCountFavorite(Integer.parseInt(getValue(element, "count_like")));
										
										NodeList productTypeInfoNodes = permElement
												.getElementsByTagName("product_type_info");
										int length = productTypeInfoNodes
												.getLength();
										ArrayList<ProductTypeInfo> productTypeInfoList = new ArrayList<ProductTypeInfo>();
										for (int y = 0; y < length; y++) {
											Element inElement = (Element) productTypeInfoNodes
													.item(y);
											ProductTypeInfo product = new ProductTypeInfo();
											product.setId(getValue(inElement,
													"id"));
											product.setNameProductType(getValue(
													inElement,
													"name_product_type"));
											product.setCreateDate(getValue(
													inElement, "createdate"));
											product.setSort(getValue(inElement,
													"sort"));
											productTypeInfoList.add(product);
										}
										menu.setProductTypeInfoList(productTypeInfoList);

										NodeList optionItem = element
												.getElementsByTagName("option_item");
										int lengthOption = optionItem
												.getLength();
										List<OptionItem> optionItemList = new ArrayList<OptionItem>();
										for (int yy = 0; yy < lengthOption; yy++) {
											Element in0Element = (Element) optionItem
													.item(yy);
											NodeList childMenu = in0Element
													.getChildNodes();
											for (int y = 0; y < childMenu
													.getLength(); y++) {
												Element inElement = (Element) childMenu
														.item(y);

												if (!getValue(inElement, "id")
														.equals("")) {
													OptionItem item2 = new OptionItem();
													item2.setId(getValue(
															inElement, "id"));
													item2.setProductId(getValue(
															inElement,
															"product_id"));
													item2.setName(getValue(
															inElement, "name"));
													item2.setSkue(getValue(
															inElement, "sku"));
													item2.setValue(getValue(
															inElement, "value"));
													item2.setStatus(getValue(
															inElement, "status"));
													item2.setType(getValue(
															inElement, "type"));
													item2.setNote(getValue(
															inElement, "note"));
													item2.setSort(getValue(
															inElement, "sort"));
													item2.setCreatedata(getValue(
															inElement,
															"createdate"));
													if (!getValue(inElement,
															"value").equals("")) {
														optionItemList
																.add(item2);
													}
												}
											}
										}
										menu.setOptionItemList(optionItemList);

										NodeList extraItem = element
												.getElementsByTagName("extra_item");
										int lengthExtra = extraItem.getLength();
										List<ExtraItem> extraItemList = new ArrayList<ExtraItem>();
										for (int yy = 0; yy < lengthExtra; yy++) {
											Element in0Element = (Element) extraItem
													.item(yy);
											NodeList childMenu = in0Element
													.getChildNodes();
											for (int y = 0; y < childMenu
													.getLength(); y++) {
												Element inElement = (Element) childMenu
														.item(y);
												if (!getValue(inElement, "id")
														.equals("")) {
													ExtraItem item2 = new ExtraItem();
													item2.setId(getValue(
															inElement, "id"));
													item2.setProductId(getValue(
															inElement,
															"product_id"));
													item2.setName(getValue(
															inElement, "name"));
													item2.setSkue(getValue(
															inElement, "sku"));
													item2.setValue(getValue(
															inElement, "value"));
													item2.setStatus(getValue(
															inElement, "status"));
													item2.setType(getValue(
															inElement, "type"));
													item2.setNote(getValue(
															inElement, "note"));
													item2.setSort(getValue(
															inElement, "sort"));
													item2.setCreatedata(getValue(
															inElement,
															"createdate"));
													if (!getValue(inElement,
															"value").equals("")) {
														extraItemList
																.add(item2);
													}
												}
											}

										}
										menu.setExtraItemList(extraItemList);

										NodeList sizeItem = element
												.getElementsByTagName("size_item");
										int lengthSize = sizeItem.getLength();
										List<SizeItem> sizeItemList = new ArrayList<SizeItem>();
										for (int yy = 0; yy < lengthSize; yy++) {
											Element in0Element = (Element) sizeItem
													.item(yy);
											NodeList childMenu = in0Element
													.getChildNodes();
											for (int y = 0; y < childMenu
													.getLength(); y++) {
												Element inElement = (Element) childMenu
														.item(y);
												if (!getValue(inElement, "id")
														.equals("")) {

													SizeItem item2 = new SizeItem();
													item2.setId(getValue(
															inElement, "id"));
													item2.setProductId(getValue(
															inElement,
															"product_id"));
													item2.setName(getValue(
															inElement, "name"));
													item2.setSkue(getValue(
															inElement, "sku"));
													item2.setValue(getValue(
															inElement, "value"));
													item2.setStatus(getValue(
															inElement, "status"));
													item2.setType(getValue(
															inElement, "type"));
													item2.setNote(getValue(
															inElement, "note"));
													item2.setSort(getValue(
															inElement, "sort"));
													item2.setCreatedata(getValue(
															inElement,
															"createdate"));
													if (!getValue(inElement,
															"createdate")
															.equals("")) {
												
														sizeItemList.add(item2);
													}

												}

											}

										}
										Log.d("test detail",
												""
														+ getValue(element,
																"name_product")
														+ "sizeItemList "
														+ sizeItemList);
										if (sizeItemList.size() > 0) {
											sizeItemList.get(0).setSelected(
													true);
										}
										menu.setSizeItemList(sizeItemList);

										NodeList storeInfoNodes = permElement
												.getElementsByTagName("store_info");
										int storeInfoLength = productTypeInfoNodes
												.getLength();
										for (int w = 0; w < storeInfoLength; w++) {
											Element infoElement = (Element) storeInfoNodes
													.item(w);
											StoreInfo storeInfo = new StoreInfo();
											storeInfo.setId(getValue(
													infoElement, "id"));
											storeInfo.setCateid(getValue(
													infoElement, "cate_id"));
											storeInfo.setUserId(getValue(
													infoElement, "user_id"));
											storeInfo.setNameSore(getValue(
													infoElement, "name_store"));
											storeInfo
													.setIntroStore(getValue(
															infoElement,
															"intro_store"));
											storeInfo
													.setPhoneStore(getValue(
															infoElement,
															"phone_store"));
											storeInfo
													.setMerchantNumber(getValue(
															infoElement,
															"merchant_number"));
											storeInfo.setLogo(getValue(
													infoElement, "logo"));
											storeInfo.setStyle(getValue(
													infoElement, "style"));
											storeInfo.setStartTime(getValue(
													infoElement, "start_time"));
											storeInfo.setEndTime(getValue(
													infoElement, "end_time"));
											storeInfo.setHomeDeliver(getValue(
													infoElement,
													"is_home_deliver").equals(
													"1") ? true : false);
											storeInfo.setHotelDeliver(getValue(
													infoElement,
													"is_hotel_deliver").equals(
													"1") ? true : false);
											storeInfo
													.setTakeAray(getValue(
															infoElement,
															"is_take_away")
															.equals("1") ? true
															: false);
											storeInfo
													.setAtPlace(getValue(
															infoElement,
															"is_at_place")
															.equals("1") ? true
															: false);
											storeInfo
													.setStartTimeDeliver(getValue(
															infoElement,
															"start_time_deliver"));
											storeInfo
													.setEndtimeDeliver(getValue(
															infoElement,
															"end_time_deliver"));
											storeInfo
													.setMinimunTimeDeliever(getValue(
															infoElement,
															"minimun_time_deliver"));
											storeInfo
													.setValueConditionDeliver(getValue(
															infoElement,
															"value_condition_deliver"));
											storeInfo
													.setTableWuantity(getValue(
															infoElement,
															"table_quantity"));
											storeInfo
													.setCountFavouriteMember(getValue(
															infoElement,
															"count_favourite_member"));
											storeInfo.setCountBill(getValue(
													infoElement, "count_bill"));
											storeInfo.setIncome(getValue(
													infoElement, "income"));
											storeInfo.setStatus(getValue(
													infoElement, "status"));
											storeInfo.setCreateDate(getValue(
													infoElement, "createdate"));
											storeInfo.setUpdateDate(getValue(
													infoElement, "updatedate"));
											menu.setStoreInfo(storeInfo);

										}
										menuList.add(menu);

									}

								}
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					item.setMenuList(menuList);

					NodeList promotionNode = (NodeList) permElement
							.getElementsByTagName("promotion");
					int size1 = promotionNode.getLength();
					ArrayList<Promotion> promotionList = new ArrayList<Promotion>();
					for (int i2 = 0; i2 < size1; i2++) {
						try {
							Element element = (Element) promotionNode.item(i2);
							if (!getValue(element, "id").equals("")) {
								Promotion promotion = new Promotion();
								promotion.setId(getValue(element, "id"));
								promotion.setStoreId(getValue(element,
										"store_id"));
								promotion.setLocationId(getValue(element,
										"location_id"));
								promotion.setTypePromotionId(getValue(element,
										"type_promotion_id"));
								promotion.setTitlePromotion(getValue(element,
										"title_promotion"));
								promotion.setValuePromotion(getValue(element,
										"value_promotion"));
								promotion.setTmpData(getValue(element,
										"tmp_data"));
								promotion.setTypeValue(getValue(element,
										"type_value"));
								promotion.setImage(getValue(element, "image"));
								promotion.setText(getValue(element, "text"));
								promotion
										.setStatus(getValue(element, "status"));
								promotion.setConditionPromotion(getValue(
										element, "condition_promotion"));
								promotion.setDescriptionPromotion(getValue(
										element, "description_promotion"));
								promotion.setStartDate(getValue(element,
										"start_date"));
								promotion.setEndDate(getValue(element,
										"end_date"));
								promotion.setCreateDate(getValue(element,
										"createdate"));
								promotion.setUpdateTime(getValue(element,
										"updatedate"));
								promotion.setNameTypePromotion(getValue(
										element, "name_type_promotion"));
								promotion.setDescripton(getValue(element,
										"description"));
								promotionList.add(promotion);

							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					item.setPromotionList(promotionList);

					/*
					 * Get Photos
					 */
					NodeList photoNode = (NodeList) permElement
							.getElementsByTagName("photos");
					int photoSizePr = photoNode.getLength();
					ArrayList<Photo> photos = new ArrayList<Photo>();
					for (int i2 = 0; i2 < photoSizePr; i2++) {
						try {

							Element elementPr = (Element) photoNode.item(i2);
							NodeList childrent = elementPr.getChildNodes();
							int photoSize = childrent.getLength();
							for (int i3 = 0; i3 < photoSize; i3++) {
								Element element = (Element) childrent.item(i3);
								Photo photoItem = new Photo();
								photoItem.setId(getValue(element, "id"));
								photoItem.setStore_id(getValue(element,
										"store_id"));
								photoItem.setUser_id(getValue(element,
										"user_id"));
								photoItem.setLocation_id(getValue(element,
										"location_id"));
								photoItem.setComment_id(getValue(element,
										"comment_id"));
								photoItem.setImage(getValue(element, "image"));
								photoItem.setThumb(getValue(element, "thumb"));
								photoItem
										.setStatus(getValue(element, "status"));
								photoItem.setType_photo(getValue(element,
										"type_photo"));
								photoItem.setCreatedate(getValue(element,
										"createdate"));
								photoItem.setUpdatedate(getValue(element,
										"updatedate"));
								photos.add(photoItem);
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					item.setPhotos(photos);

					NodeList userNode = (NodeList) permElement
							.getElementsByTagName("user_check_bill");
					int size = userNode.getLength();
					List<UserCheckBill> userCheckBillList = new ArrayList<UserCheckBill>();
					for (int i2 = 0; i2 < size; i2++) {
						Element element = (Element) userNode.item(i2);
						UserCheckBill userCheckBill = new UserCheckBill();
						userCheckBill.setTotalFriend(getValue(element,
								"TotalFriend"));
						userCheckBill.setTotalMember(getValue(element,
								"TotalMember"));
						userCheckBill.setId(getValue(element, "id"));
						userCheckBill.setFirstName(getValue(element,
								"first_name"));
						userCheckBill
								.setLastName(getValue(element, "last_name"));
						userCheckBill.setAvatar(getValue(element, "avatar"));
						userCheckBill.setStoreId(getValue(element, "store_id"));
						userCheckBill.setStatusBill(getValue(element,
								"status_bill"));
						userCheckBill.setInteractionPoint(getValue(element,
								"interaction_point"));
						item.setUserCheckBill(userCheckBill);
						userCheckBillList.add(userCheckBill);
						i2 = size;
					}
					item.setUserCheckBillList(userCheckBillList);
					NodeList tagNodes = permElement
							.getElementsByTagName("tags");
					int tagLenth = nodes.getLength();
					ArrayList<whyq.model.Tag> tagList = new ArrayList<whyq.model.Tag>();
					for (int k = 0; k < tagLenth; k++) {
						Element element2 = (Element) tagNodes.item(k);
						whyq.model.Tag tag = new whyq.model.Tag();
						tag.setId(getValue(element2, "id"));
						tag.setNameTag(getValue(element2, "name_tag"));
						if (!getValue(element2, "count_used").equals(""))
							tag.setCountUsed(Integer.parseInt(getValue(
									element2, "count_used")));
						tag.setStoreId(getValue(element2, "store_id"));
						tag.setCreateDate(getValue(element2, "createdate"));
						tagList.add(tag);
					}

					item.setTagList(tagList);

					data.setStatus(statusResponse);
					data.setData(item);
					return data;
				}
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(permList);
				data.setMessage(mes);
				return data;

			}

			return null;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public String getValue(Element e, String tag) {
		try {
			if (e != null) {
				Node node = e.getElementsByTagName(tag).item(0).getFirstChild();
				if (node != null) {
					return node.getNodeValue();
				}
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		return "";
	}

	public Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			if (is != null)
				doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}

	public Object parseCommentResult(String result) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object parseLCationResult(String result) {
		// TODO Auto-generated method stub

		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			ArrayList<Location> locationList = new ArrayList<Location>();
			String statusResponse = "200";// doc.getElementsByTagName("Status").item(0).getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				ArrayList<Store> permList = new ArrayList<Store>();
				NodeList nodeList = doc.getElementsByTagName("geoname");

				for (int i = 0; i < nodeList.getLength(); i++) {
					Location location = new Location();
					Element infoElement = (Element) nodeList.item(i);
					location.setLat(getValue(infoElement, "lat"));
					location.setLon(getValue(infoElement, "lng"));
					location.setName(getValue(infoElement, "name"));
					location.setCountry(getValue(infoElement, "countryName"));
					locationList.add(location);
				}

				// final String mes =
				// "Good";//doc.getElementsByTagName("Message").item(0).getFirstChild().getNodeValue();
				// data.setStatus(statusResponse);
				// data.setData(locationList);
				// data.setMessage(mes);
				return locationList;
			} else {
				// final String mes =
				// doc.getElementsByTagName("Message").item(0).getFirstChild().getNodeValue();
				// data.setStatus(statusResponse);
				// data.setData(null);
				// data.setMessage(mes);
				// return data;
				return locationList;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parseLFavouriteResult(String result) {
		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			data.setStatus(statusResponse);
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public Object parseLResetPasswordResult(String result) {
		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			String mes = doc.getElementsByTagName("Message").item(0)
					.getFirstChild().getNodeValue();
			data.setStatus(statusResponse);
			data.setMessage(mes);
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public Object parserSignupResult(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			ResponseData data = new ResponseData();
			Document doc = XMLfromString(result);
			ArrayList<Store> permList = new ArrayList<Store>();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				UserHandler userHandler = new UserHandler();
				// assign the handler
				xmlReader.setContentHandler(userHandler);
				xmlReader.parse(new InputSource(new StringReader(result)));
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(userHandler.getUser());
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parseFaqsResult(String result) {
		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				ArrayList<Store> permList = new ArrayList<Store>();
				NodeList nodeList = doc.getElementsByTagName("Faqs");
				ArrayList<Faq> faqList = new ArrayList<Faq>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Faq item = new Faq();
					Element infoElement = (Element) nodeList.item(i);
					item.setId(getValue(infoElement, "lat"));
					item.setContentQuestion(getValue(infoElement,
							"content_question"));
					item.setAnswerQuestion(getValue(infoElement,
							"answer_question"));
					item.setIsPublic(getValue(infoElement, "is_public"));
					item.setCreateDate(getValue(infoElement, "createdate"));
					item.setUpdateDate(getValue(infoElement, "updatedate"));
					faqList.add(item);
				}

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(faqList);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public Object parseLUserCheckedResult(String result) {
		// TODO Auto-generated method stub
		try {
			ResponseData data = new ResponseData();
			Document doc = XMLfromString(result);
			ArrayList<Store> permList = new ArrayList<Store>();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				UserHandler userHandler = new UserHandler();
				// assign the handler
				xmlReader.setContentHandler(userHandler);
				xmlReader.parse(new InputSource(new StringReader(result)));
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				// data.setData(userHandler.getUser());
				data.setMessage(mes);
				NodeList nodeList = doc.getElementsByTagName("obj");
				int size = nodeList.getLength();
				ArrayList<User> userList = new ArrayList<User>();
				for (int i = 0; i < size; i++) {
					try {

						Element element = (Element) nodeList.item(i);
						User user = new User();
						user.setId(getValue(element, "id"));
						user.setEmail(getValue(element, "email"));
						user.setRoleId(getValue(element, "role_id"));
						user.setAcive(getValue(element, "is_active")
								.equals("1") ? true : false);
						user.setTotalMoney(getValue(element,
								"total_saving_money"));
						user.setTotalSavingMoney(getValue(element,
								"total_comment"));
						user.setTotalComment(getValue(element, "total_comment"));
						user.setTotalCommentLike(getValue(element,
								"total_comment_like"));
						user.setTotalFriend(getValue(element, "total_friend"));
						user.setTotalFavourite(getValue(element,
								"total_favourite"));
						user.setTotalCheckBill(getValue(element,
								"total_check_bill"));
						user.setStatus(getValue(element, "status"));
						user.setCreateDate(getValue(element, "createdate"));
						user.setUpdateDate(getValue(element, "updatedate"));
						user.setFirstName(getValue(element, "first_name"));
						user.setLastName(getValue(element, "last_name"));
						if (!getValue(element, "gender").equals("")) {
							user.setGender(Integer.parseInt(getValue(element,
									"gender")));
						}
						WhyqImage image = new WhyqImage(getValue(element,
								"avatar"));
						user.setUrlAvatar(getValue(element, "avatar"));
						user.setAvatar(image);
						user.setAddress(getValue(element, "city"));
						user.setCity(getValue(element, "address"));
						user.setPhoneNumber(getValue(element, "phone_number"));
						user.setPaypalEmail(getValue(element, "paypal_email"));
						user.setFacebookId(getValue(element, "facebook_id"));
						user.setTwitterId(getValue(element, "twitter_id"));
						/*
						 * Get isFriend
						 */
						NodeList statusNodes = element
								.getElementsByTagName("status_user");
						int tagLenth = statusNodes.getLength();
						ArrayList<whyq.model.Tag> tagList = new ArrayList<whyq.model.Tag>();
						for (int k = 0; k < tagLenth; k++) {
							Element element2 = (Element) statusNodes.item(k);
							StatusUser item = new StatusUser();
							String isFriend = getValue(element2, "is_friend");
							if (isFriend != null) {
								user.setFriend(isFriend.equals("1") ? true
										: false);
								k = tagLenth;
							}
						}
						userList.add(user);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				data.setData(userList);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserSearchOnlFriend(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			ResponseData data = new ResponseData();
			Document doc = XMLfromString(result);
			ArrayList<Store> permList = new ArrayList<Store>();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				UserHandler userHandler = new UserHandler();
				// assign the handler
				xmlReader.setContentHandler(userHandler);
				xmlReader.parse(new InputSource(new StringReader(result)));
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				// data.setData(userHandler.getUser());
				data.setMessage(mes);
				NodeList nodeList = doc.getElementsByTagName("obj");
				int size = nodeList.getLength();
				ArrayList<SearchFriend> friendList = new ArrayList<SearchFriend>();
				for (int i = 0; i < size; i++) {
					try {

						Element element = (Element) nodeList.item(i);
						SearchFriend item = new SearchFriend();
						item.setId(getValue(element, "id"));
						item.setFacebookId(getValue(element, "id"));
						item.setTwitterId(getValue(element, "id"));
						item.setItuneUrl(getValue(element, "id"));
						item.setFirstName(getValue(element, "id"));
						item.setLastName(getValue(element, "id"));
						item.setIsJoin(getValue(element, "id"));
						item.setIsFriend(getValue(element, "id"));
						item.setAvatar(getValue(element, "id"));

						friendList.add(item);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				data.setData(friendList);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parseInvitationNotification(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				final String totalRecordInvited = doc
						.getElementsByTagName("TotalRecordInvited").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(totalRecordInvited);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserDeleteFriend(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData("1");
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserOrderSend(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);

				final String billId = doc.getElementsByTagName("BillID")
						.item(0).getFirstChild().getNodeValue();
				final String link = doc.getElementsByTagName("Link").item(0)
						.getFirstChild().getNodeValue();
				OrderSendResult dataResult = new OrderSendResult();
				dataResult.setBillId(billId);
				dataResult.setLink(link);
				data.setData(dataResult);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserBillDetailResultSend(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			ResponseData data = new ResponseData();
			Document doc = XMLfromString(result);
			ArrayList<Store> permList = new ArrayList<Store>();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				UserHandler userHandler = new UserHandler();
				// assign the handler
				xmlReader.setContentHandler(userHandler);
				xmlReader.parse(new InputSource(new StringReader(result)));
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				// data.setData(userHandler.getUser());
				data.setMessage(mes);

				String discount = "0";
				NodeList objNodeList = doc.getElementsByTagName("obj");
				int sizeObj = objNodeList.getLength();
				for (int ii = 0; ii < sizeObj; ii++) {
					Element element1 = (Element) objNodeList.item(ii);

					discount = getValue(element1, "discount_value");
					ArrayList<Bill> listBill = new ArrayList<Bill>();
					NodeList nodeList = doc.getElementsByTagName("detail");
					int size = nodeList.getLength();

					for (int i3 = 0; i3 < size; i3++) {
						try {

							Element element2 = (Element) nodeList.item(i3);

							NodeList billItemList = element2
									.getElementsByTagName("data");
							int sizeBillItem = billItemList.getLength();
							for (int i = 0; i < sizeBillItem; i++) {
								try {

									Element element = (Element) billItemList
											.item(i);
									Bill item = new Bill();
									item.setId(getValue(element, "id"));
									item.setPrice(getValue(element,
											"item_price"));
									item.setProductId(getValue(element,
											"product_id"));
									item.setUnit(getValue(element, "quantity"));
									item.setDiscount(discount);
									NodeList productes = element
											.getElementsByTagName("product");
									for (int j = 0; j < productes.getLength(); j++) {
										Element elementChild = (Element) productes
												.item(j);
										item.setProductName(getValue(element,
												"name_product"));
										item.setThumb(getValue(element,
												"image_thumb"));
										// item.setAmount(getValue(element,
										// "id"));
									}

									listBill.add(item);

								} catch (Exception e) {
									// TODO: handle exception
								}
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					data.setData(listBill);
				}

				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserDistanceResultSend(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("OK")) {
				data.setStatus("200");
				NodeList elementes = doc.getElementsByTagName("element");
				Distance dataResult = new Distance();
				if (elementes != null) {
					Element element = (Element) elementes.item(0);
					dataResult.setStatus(getValue(element, "status"));
					Log.d("parserDistanceResultSend",
							"" + getValue(element, "status"));
				} else {
					Log.d("parserDistanceResultSend", "null");
					dataResult.setValue(null);
				}
				data.setData(dataResult);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserCheckBillResult(String result) {
		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("OK")) {
				data.setStatus("200");
				NodeList elementes = doc.getElementsByTagName("element");
				Distance dataResult = new Distance();
				if (elementes != null) {
					Element element = (Element) elementes.item(0);
					dataResult.setStatus(getValue(element, "status"));
					Log.d("parserCheckBillResult",
							"" + getValue(element, "status"));
				} else {
					Log.d("parserCheckBillResult", "null");
					dataResult.setValue(null);
				}
				data.setData(dataResult);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public Object parseAcceptInvitation(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {

				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setMessage(mes);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * Parse Json Pushnotification data
	 */
	public BillPushNotification parserJsonPushnotification(String input) {
		try {
			JSONObject json = new JSONObject(input);
			BillPushNotification data = new BillPushNotification();
			data.setAlert(json.optString("alert"));
			data.setPushHash(json.optString("push_hash"));

			JSONObject objData = json.optJSONObject("customData");
			data.setStoryId(objData.optString("store_id"));
			data.setDeliverType(objData.optString("deliver_type"));
			data.setType(objData.optString("type"));
			data.setBillId(objData.optString("bill_id"));
			data.setTimeDeliver(objData.optString("time_deliver"));
			data.setNameStore(objData.optString("name_store"));
			return data;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public Object parseOrderCheck(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				data.setStatus("200");
				OrderCheckData data2 = new OrderCheckData();
				try {
					final String img = doc.getElementsByTagName("Image")
							.item(0).getFirstChild().getNodeValue();
					data2.setImage(img);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {
					final String link = doc.getElementsByTagName("Link")
							.item(0).getFirstChild().getNodeValue();
					data2.setLink(link);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				data.setData(data2);
				// NodeList elementes = doc.getElementsByTagName("element");
				// String dataResult = "";
				// if(elementes!=null){
				// Element element = (Element)elementes.item(0);
				// dataResult.setStatus(getValue(element, "image"));
				// Log.d("parserCheckBillResult",""+getValue(element,
				// "status"));
				// }else{
				// Log.d("parserCheckBillResult","null");
				// dataResult.setValue(null);
				// }
				// data.setData(dataResult);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parserPostOpenGraphResult(String input) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			Log.d("parserPostOpenGraphResult", "result " + input);
			JSONObject json = new JSONObject(input);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}

	public Object parseOrderEcoCash(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			Document doc = XMLfromString(result);
			ResponseData data = new ResponseData();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			data.setStatus(statusResponse);
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public Object parseLGetDeliveryFeeList(String result) {

		// TODO Auto-generated method stub
		try {
			ResponseData data = new ResponseData();
			Document doc = XMLfromString(result);
			List<DeliveryFee> list = new ArrayList<DeliveryFee>();
			String statusResponse = doc.getElementsByTagName("Status").item(0)
					.getFirstChild().getNodeValue();
			if (statusResponse.equals("200")) {
				XMLReader xmlReader = initializeReader();
				UserHandler userHandler = new UserHandler();
				// assign the handler
				xmlReader.setContentHandler(userHandler);
				xmlReader.parse(new InputSource(new StringReader(result)));
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				// data.setData(userHandler.getUser());
				data.setMessage(mes);

				String from = "0", to = "0", fee = "0";
				NodeList objNodeList = doc.getElementsByTagName("obj");
				int sizeObj = objNodeList.getLength();
				for (int ii = 0; ii < sizeObj; ii++) {
					Element element1 = (Element) objNodeList.item(ii);

					from = getValue(element1, "distance_from");
					to = getValue(element1, "distance_to");
					fee = getValue(element1, "fee");

					DeliveryFee item = new DeliveryFee();
					item.setTo(Integer.parseInt(to));
					item.setFrom(Integer.parseInt(from));
					item.setFee(Integer.parseInt(fee));

					list.add(item);

				}
				data.setData(list);
				return data;
			} else {
				final String mes = doc.getElementsByTagName("Message").item(0)
						.getFirstChild().getNodeValue();
				data.setStatus(statusResponse);
				data.setData(null);
				data.setMessage(mes);
				return data;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

}
