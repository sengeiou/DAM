package whyq.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.handler.BoardHandler;
import whyq.handler.UserHandler;
import whyq.interfaces.Create_Board_delegate;
import whyq.interfaces.Get_Board_delegate;
import whyq.interfaces.Get_Perm_Delegate;
import whyq.interfaces.HttpAccess;
import whyq.interfaces.JoinPerm_Delegate;
import whyq.interfaces.Login_delegate;
import whyq.interfaces.LogoutDelegate;
import whyq.interfaces.MyDiary_Delegate;
import whyq.interfaces.WhyqList_Delegate;
import whyq.model.Store;
import whyq.model.User;
import whyq.model.WhyqBoard;
import whyq.model.WhyqImage;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class XMLParser implements HttpAccess {

	// private String TAG = "XML_PARSER";
	public final static int LOGIN = 0;
	public final static int LOGOUT = 8;
    public final static int MYDIARY  = 1;
    public final static int CREATE_BOARD = 2;
    public final static int JOIN_WHYQ= 3;
    public final static int PERMLIST = 4;
    public final static int GET_BOARD = 5;
    public final static int GET_PERMS_BY_DATE = 6;
    public final static int UPDATE_PROFILE = 7;
    public static final String PREFS_NAME = "UserPermping";
    public static final String STORE_USER_EMAIL = "UserEmail";
    public static final String STORE_USER_PASS = "UserPass";
    public static final String STORE_LAST_TIME_LOGIN = "LastTimeLogin";
    public static final long ACCOUNT_TIME_OUT = 3 * 60 * 60 * 1000;
    public static final String STORE_USER_TOKEN = "TOKEN";
    public static final String STORE_USER_ID = "STORE_USER_ID";
    public static final String STORE_TOTAL_CHECK_BILL = "total_check_bill";
    public static final String STORE_TOTAL_SAVING = "total_saving";
	public static final String STORE_USER_AVATAR = "userAvatar";
	public static final String STORE_USER_NAME = "userName";
	public static final String STORE_TOTAL_COMMENT = "totalComment";
	public static final String STORE_TOTAL_HISTORY = "totalHistory";
	private Document doc = null;
	private String xml = "<empty></empty>";
	public int type;
	HttpPermUtils httpPermUtils;
	public XMLParser(){
		type = -1;
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils();
	}

	public XMLParser(String document, Boolean DownloadFirst) {
		type = -1;
		this.setDoc(this.parseXMLFromUrl(document, DownloadFirst));
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils();
	}

	public String getXML(String url) {
		type = -1;
		String line = null;
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils();
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server: "+e.toString()+" </msg></results>";
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server: "+e.toString()+" </msg></results>";
		} catch (IOException e) {
			line = "<results status=\"error\"><msg>Can't connect to server: "+e.toString()+" </msg></results>";
		}

		String newString = line.replace(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		return newString;

	}
	
	
	public void getXMLAsync(final String url  ) {
		AsyncTask<Void, Void , Document > downloader = new AsyncTask<Void, Void, Document >() {
			
			protected void onPreExecute() {
				
			}
			
			
	        @Override
	        protected Document doInBackground(Void... params) {
	        	String line = null;

	    		try {

	    			DefaultHttpClient httpClient = new DefaultHttpClient();
	    			HttpPost httpPost = new HttpPost(url);

	    			HttpResponse httpResponse = httpClient.execute(httpPost);
	    			HttpEntity httpEntity = httpResponse.getEntity();
	    			line = EntityUtils.toString(httpEntity);

	    		} catch (UnsupportedEncodingException e) {
	    			line = "<results status=\"error\"><msg>Can't connect to server: "+e.toString()+" </msg></results>";
	    		} catch (MalformedURLException e) {
	    			line = "<results status=\"error\"><msg>Can't connect to server: "+e.toString()+" </msg></results>";
	    		} catch (IOException e) {
	    			line = "<results status=\"error\"><msg>Can't connect to server: "+e.toString()+" </msg></results>";
	    		}

	    		String newString = line.replace(
	    				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
	    		Document result = XMLfromString( newString );
	    		return result;
	        }

	        protected void onPostExecute(Document result) {
	        }
	    };
	    downloader.execute();
	}

	private Document parseXMLFromUrl(String url, Boolean DownloadFirst) {
		String xml = this.getXML(url);
		// String xml = API.xmlSample;
		if (xml != null) {
			this.xml = xml;
			return XMLfromString(xml);
		}
		return null;

	}

	public Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			if(is != null)
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
/*
	private Document parseXMLFromString(String document) {
		// TODO implement this
		return null;
	}
*/
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public ArrayList<Store> permListFromNodeList(String parentNode) {

		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();

			DataHandler dataHandler = new DataHandler();
			xr.setContentHandler(dataHandler);

			// xr.parse(new InputSource(new
			// StringReader("<data> <section id=\"1\">bla</section> <area>lala</area> </data> ")));
			xr.parse(new InputSource(new StringReader(this.xml)));

			ArrayList<Store> permList = dataHandler.getPermList();

			return permList;
		} catch (ParserConfigurationException pce) {
			//Log.e("SAX XML", "sax parse error", pce);
		} catch (SAXException se) {
			//Log.e("SAX XML", "sax error", se);
		} catch (IOException ioe) {
			//Log.e("SAX XML", "sax parse io error", ioe);
		}

		return null;
	}

	public class DataHandler extends DefaultHandler {

		// booleans that check whether it's in a specific tag or not
		// private boolean _inSection, _inArea;

		// this holds the data

		private Store currentPem = null;
		private User currentUser = null;
		private ArrayList<Store> permList = new ArrayList<Store>();

		private String currentElement = "";

		public ArrayList<Store> getPermList() {
			return this.permList;
		}

		/**
		 * This gets called when the xml document is first opened
		 * 
		 * @throws SAXException
		 */
		public void startDocument() throws SAXException {
		}

		/**
		 * Called when it's finished handling the document
		 * 
		 * @throws SAXException
		 */
		public void endDocument() throws SAXException {

		}

		/**
		 * This gets called at the start of an element. Here we're also setting
		 * the booleans to true if it's at that specific tag. (so we know where
		 * we are)
		 * 
		 * @param namespaceURI
		 * @param localName
		 * @param qName
		 * @param atts
		 * @throws SAXException
		 */
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			this.currentElement = localName;
			if (this.currentElement.equals("item")) {
				// Create new perm object
				this.currentPem = new Store();
			}

		}

		/**
		 * Called at the end of the element. Setting the booleans to false, so
		 * we know that we've just left that tag.
		 * 
		 * @param namespaceURI
		 * @param localName
		 * @param qName
		 * @throws SAXException
		 */
		public void endElement(String namespaceURI, String localName,
				String qName) throws SAXException {

			if (this.currentPem != null && localName.equals("item")) {
				this.permList.add(this.currentPem);
			}
			this.currentElement = null;

		}

		/**
		 * Calling when we're within an element. Here we're checking to see if
		 * there is any content in the tags that we're interested in and
		 * populating it in the Config object.
		 * 
		 * @param ch
		 * @param start
		 * @param length
		 */
		public void characters(char ch[], int start, int length) {
			String chars = new String(ch, start, length);
			chars = chars.trim();

			if (this.currentPem != null) {
				if (this.currentElement == "permId") {
					// Perm id
					this.currentPem.setId(chars);
				}else if (this.currentElement == "permUrl") {
//					this.currentPem.setPermUrl(chars);
				}else if (this.currentElement == "permAudio") {
//					this.currentPem.setPermAudio(chars);
				}else if (this.currentElement == "permImage") {
					WhyqImage imageObject = new WhyqImage(chars);
//					this.currentPem.setImage(imageObject);

					User permAuthor = new User("user");
					permAuthor.setName("Author ");
					permAuthor.setAvatar(imageObject);

//					this.currentPem.setAuthor(permAuthor);

				} else if (this.currentElement == "permCategory") {
					WhyqBoard whyqBoard = new WhyqBoard("Board ID ");
					whyqBoard.setName(chars);
//					this.currentPem.setBoard(whyqBoard);
				} else if (this.currentElement == "user") {
					this.currentUser = new User();
				} else if (this.currentElement == "userId") {
					this.currentUser.setId(chars);
				} else if (this.currentElement == "userName") {
					this.currentUser.setName(chars);
				} else if (this.currentElement == "userAvatar") {
					WhyqImage userAvatar = new WhyqImage(chars);
					this.currentUser.setAvatar(userAvatar);
//					this.currentPem.setAuthor(this.currentUser);
				}
			}

		}
	}
	
	
	
/** Linh added to work with User Profile. Should re-work */

	public String response;
	public Login_delegate loginDelegate;
	public MyDiary_Delegate myDiaryDelegate;
	public Object delegate;
	public Context context;
	List<NameValuePair> nameValuePairs;
	/**
	 * Initialize the parser with the url and params
	 * @param url
	 */
	public XMLParser(Context context, int type, Login_delegate delegate, String url, List<NameValuePair> nameValuePairs) {
		this.context = context;
		this.type = type;
		this.loginDelegate = delegate;
		this.nameValuePairs = nameValuePairs;
		getResponseFromURL(delegate, url, nameValuePairs);
	}
	
	public XMLParser(String url) {
		type = XMLParser.CREATE_BOARD;
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils(XMLParser.this);
		getResponseFromURL(url);
	}
//	public XMLParser(String url, int type) {
//		this.type = type;
//		if(httpPermUtils  == null)
//			httpPermUtils = new HttpPermUtils(XMLParser.this);
//		getResponseFromURL(url);
//	}
	public XMLParser(String url, Object delegate, int type) {
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils(XMLParser.this);
		this.type = type;
		this.delegate = delegate;
		getResponseFromURL(url);
	}
	
	public XMLParser(Context context, String url, Object delegate, int type) {
		this.context = context;
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils(XMLParser.this);
		this.type = type;
		this.delegate = delegate;
		getResponseFromURL(url);
	}

	public XMLParser(int type, Object delegate, String url, List<NameValuePair> nameValuePairs) {
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils(XMLParser.this);
		this.type = type;
		this.nameValuePairs = nameValuePairs;
		getResponseFromURL(type, delegate, url, nameValuePairs);

	}
	
	public XMLParser(int type, Object delegate, String url, List<NameValuePair> nameValuePairs, boolean isCallAsynTask) {
		if(httpPermUtils  == null)
			httpPermUtils = new HttpPermUtils(XMLParser.this);
		this.type = type;
		this.nameValuePairs = nameValuePairs;
		if(isCallAsynTask) {
			getAsynResponseFromURL(type, delegate, url, nameValuePairs);
		} else {
			getResponseFromURL(type, delegate, url, nameValuePairs);
		}		
	}
	public Document getResponseFromURL(int Type, Object delegate, String url, List<NameValuePair> nameValuePairs) {
		try {
			this.type= Type;
			this.nameValuePairs = nameValuePairs;
			if(delegate != null)
				this.delegate = delegate;		
			String response = httpPermUtils.sendRequest(url, nameValuePairs,false);
			if (response != null) {
				this.response = response;
				return parseResponse(response);
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public Document getAsynResponseFromURL(int Type, Object delegate, String url, List<NameValuePair> nameValuePairs) {
		try {
			this.type= Type;
			this.nameValuePairs = nameValuePairs;
			if(delegate != null)
				this.delegate = delegate;		
			String response = httpPermUtils.sendAsynRequest(url, nameValuePairs,false);
			if (response != null) {
				this.response = response;
				return parseResponse(response);
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public Document getResponseFromURL(int Type, Object delegate, String url, List<NameValuePair> nameValuePairs, String id) {
		this.type= Type;
		this.delegate = delegate;
		this.nameValuePairs = nameValuePairs;
		HttpPermUtils httpPermUtils = new HttpPermUtils(XMLParser.this);
		String response = httpPermUtils.sendRequest(url, nameValuePairs,id, false);
		if (response != null) {
			this.response = response;
			return parseResponse(response);
		}
		return null;
	}
	/**
	 * Get the response from URL, the result will be stored in the "response" variable.
	 * @param url
	 * @return
	 */
	public Document getResponseFromURL(Login_delegate delegate, String url, List<NameValuePair> nameValuePairs) {
		HttpPermUtils httpPermUtils = new HttpPermUtils(XMLParser.this);
		this.nameValuePairs = nameValuePairs;
		httpPermUtils.sendRequest( url, nameValuePairs, false);
		return null;
	}

	/**
	 * Get the response from the web service as Document object.
	 * @param url the url.
	 * @return the Document.
	 */
	private Document getResponseFromURL(String url) {
		HttpPermUtils httpPermUtils = new HttpPermUtils(XMLParser.this);
		String response = httpPermUtils.sendRequest(url, null, true);
		return null;
	}
	
	/**
	 * Prase the response returned from the web service.
	 * @param response the response.
	 * @return the Document object.
	 */
	public Document parseResponse(String response) {

//		new ParseResponseTask().execute(response);
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(response));
			doc = documentBuilder.parse(is);
			return doc;
			
		} catch (Exception e) {
			// TODO: handle exception
			//Logger.appendLog(e.toString(), "parseResponseLog");
			return null;
		}
		
	}
	class ParseResponseTask extends AsyncTask <String , String, Document >{
		
		public ParseResponseTask(){
			
		}
		protected Document doInBackground(String... urls) {
			try {
				String respond = urls[0];
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory
						.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(respond));
				doc = documentBuilder.parse(is);
				return doc;
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
	    protected void onPostExecute(Document result) {
	        // TODO: check this.exception 
	        // TODO: do something with the feed
	    	setDoc(result);

	    }
	}

	/**
	 * Initialize the XML Parser Reader
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private XMLReader initializeReader() throws ParserConfigurationException,
			SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Create a parser
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		return xmlReader;
	}

	/**
	 * Return the User object from response (API)
	 * @param url
	 * @return an User object
	 */
	public User getUser() {
		try {
			XMLReader xmlReader = initializeReader();
			UserHandler userHandler = new UserHandler();
			// assign the handler
			xmlReader.setContentHandler(userHandler);
			xmlReader.parse(new InputSource(new StringReader(response)));
			return userHandler.getUser();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Return a list of perms from response (API)
	 * @return a list of perms
	 */
	public List<Store> getPerms() {
		try {
			XMLReader xmlReader = initializeReader();
			BoardHandler boardHandler = new BoardHandler();
			xmlReader.setContentHandler(boardHandler);
			xmlReader.parse(new InputSource(new StringReader(response)));
			return boardHandler.getPerms();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String[]> getNoteListFromDoc(Document doc, String id){
		try {
//			List<HashMap<String, String>>;
			List<String[]> imagesList = new ArrayList<String[]>();
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("item");

			/** Assign textview array lenght by arraylist size */
			int length = nodeList.getLength();

			for (int i = 0; i < length; i++) {
				String []item =  new String[2];
				Node node = nodeList.item(i);
				String date = "";
				Element fstElmnt = (Element) node;
				NodeList permDate = fstElmnt.getElementsByTagName("permDate");
				Element dateElement = null;
				if(permDate != null){
					dateElement= (Element) permDate.item(0);
					permDate = dateElement.getChildNodes();
					date = ((Node) permDate.item(0)).getNodeValue();
	
				}
				date = getDateFromString(date);
				NodeList nameList = fstElmnt.getElementsByTagName("permImage");
				Element nameElement = null;
				if(nameList != null){
					nameElement= (Element) nameList.item(0);
					nameList = nameElement.getChildNodes();
					String link = ((Node) nameList.item(0)).getNodeValue();
					if(link!= null ){//&& date.equals(id)
						item[0] = date;
						item[1] = link;
 
					}
				}
				imagesList.add(item);
			}
			return imagesList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	public List<String> getNoteListFromDocDiary(Document doc){
		try {
			List<String> thumbList= new ArrayList<String>();
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("item");

			/** Assign textview array lenght by arraylist size */
			int length = nodeList.getLength();

			for (int i = 0; i < length; i++) {

				Node node = nodeList.item(i);
				Element fstElmnt = (Element) node;
				NodeList nameList = fstElmnt.getElementsByTagName("permImage");
				Element nameElement = null;
				if(nameList != null){
					nameElement= (Element) nameList.item(0);
					nameList = nameElement.getChildNodes();
					String link = ((Node) nameList.item(0)).getNodeValue();
					if(link!= null)
						thumbList.add(link);
				}

			}
			return thumbList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	/*public boolean isCreatedAccount() {
		// TODO: Should check the response
		return true;
	}*/

	public static String getDateFromString( String initDate){
		String date="";
		int k=0;
		for(int i=0; i< initDate.length();i++){
			if(k >= 1 && initDate.charAt(i) !=' '){
				if(initDate.charAt(i) != '-')date+= initDate.charAt(i) ;
			}else if(k >=1 && initDate.charAt(i) == ' '){
				return date;
			}else if(initDate.charAt(i) == '-'){
				k++;
			}
		
		}
		return date;
	}
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		//Log.d("", "ERror====>");
		switch (type) {
		case XMLParser.LOGIN:
			loginDelegate.on_error();
			break;
		case XMLParser.LOGOUT:
			LogoutDelegate logoutDelegate = (LogoutDelegate)delegate;
			logoutDelegate.on_error();
			break;
		case XMLParser.MYDIARY:
			//Log.d("delegate====:", "delegate ======>:");
			MyDiary_Delegate myDialyDelegate = (MyDiary_Delegate)delegate;
			myDialyDelegate.onError();
			break;
		case XMLParser.CREATE_BOARD:
			Create_Board_delegate createBoardDelegate = (Create_Board_delegate)delegate;
			createBoardDelegate.onError();
			break;
		case XMLParser.JOIN_WHYQ:
			JoinPerm_Delegate joinPerm_Delegate = (JoinPerm_Delegate)delegate;
			joinPerm_Delegate.onError();
			break;
		case XMLParser.PERMLIST:
			WhyqList_Delegate whyqList_Delegate = (WhyqList_Delegate)delegate;
			whyqList_Delegate.onError();
			break;
		case XMLParser.GET_PERMS_BY_DATE:
			Get_Perm_Delegate delegates = (Get_Perm_Delegate)delegate;
			delegates.onError();
			break;
		default:
			break;
		}
	}

	@Override
	public void onSeccess(String result, String id) {
		// TODO Auto-generated method stub
		String response = result;
		if (response != null) {
			this.response = response;
			Document doc =  parseResponse(response);
			if(doc != null)
				setDoc(doc);
			switch (type) {
			case XMLParser.LOGIN:
				exeLoginTask();
				break;
			case XMLParser.LOGOUT:
				exeLogoutTask(doc);
				break;
			case XMLParser.MYDIARY:
				exeMyDiary(doc, id);
				break;
			case XMLParser.CREATE_BOARD:
				exeCreateBoard(doc);
				break;
			case XMLParser.JOIN_WHYQ:
				exeJoinPerm(doc);
				break;
			case XMLParser.PERMLIST:
				exePermList(doc);
				break;
			case XMLParser.GET_BOARD:
				exeGetBoard(doc);
				break;
			case XMLParser.GET_PERMS_BY_DATE:
				exeGetPerm(doc);
				break;
			case XMLParser.UPDATE_PROFILE:
				User user = getUser();
				if(user != null && context != null) {
					WhyqApplication state = (WhyqApplication) context.getApplicationContext();
					if (state != null) {
						state.setUser(user);
						storeAccount(user);
					}
				}
			default:
				break;
			}
			
		}else{
			switch (type) {
			case XMLParser.LOGIN:
				loginDelegate.on_error();
				break;
			case XMLParser.LOGOUT:
				LogoutDelegate logoutDelegate = (LogoutDelegate)delegate;
				logoutDelegate.on_error();
				break;
			case XMLParser.MYDIARY:
				//Log.d("delegate====:", "delegate ======>:");
				MyDiary_Delegate myDialyDelegate = (MyDiary_Delegate)delegate;
				myDialyDelegate.onError();
				break;
			case XMLParser.CREATE_BOARD:
				Create_Board_delegate createBoardDelegate = (Create_Board_delegate)delegate;
				createBoardDelegate.onError();
				break;
			case XMLParser.JOIN_WHYQ:
				JoinPerm_Delegate joinPerm_Delegate = (JoinPerm_Delegate)delegate;
				joinPerm_Delegate.onError();
				break;
			case XMLParser.PERMLIST:
				WhyqList_Delegate whyqList_Delegate = (WhyqList_Delegate)delegate;
				whyqList_Delegate.onError();
				break;
			case XMLParser.GET_BOARD:
				Get_Board_delegate getBoardDelegate = (Get_Board_delegate)delegate;
				getBoardDelegate.onError();
				break;
			default:
				break;
			}
		}
	}
	
	private void exeLogoutTask(Document doc) {
		// TODO Auto-generated method stub
		boolean isSuccess = getLogoutStatus(doc);
		if(isSuccess){
			WhyqApplication state = (WhyqApplication)context.getApplicationContext();
			state.setUser(null);
			XMLParser.storeUserAccount(context, null);
			WhyqMain.back();
			WhyqMain.showLogin();
		}else{
			
		}

		
	}

	private boolean getLogoutStatus(Document doc ) {
		// TODO Auto-generated method stub
		try {
//			ArrayList<Whyq> boards = new ArrayList<Whyq>();
			NodeList boardNodeList = doc.getElementsByTagName("item");
			
			for( int i = 0; i < boardNodeList.getLength(); i ++ ){
				Element boardElement = (Element) boardNodeList.item(i);

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

	
	
	public static void storeUserAccount(Context context, User user) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = account.edit();
		if (user  == null) {
//			editor.putString(STORE_USER_ID, "");
//			editor.putString(STORE_USER_EMAIL, "");
//			editor.putString(STORE_USER_NAME, "");
			editor.putString(STORE_USER_TOKEN, "");
			editor.clear();
		} else {
			editor.putString(STORE_USER_ID, user.getId());
			editor.putString(STORE_USER_EMAIL, user.getEmail());
			editor.putString(STORE_USER_NAME, user.getFirstName());
			editor.putString(STORE_USER_TOKEN, user.getToken());
			editor.putString(STORE_TOTAL_CHECK_BILL, user.getTotalCheckBill());
			editor.putString(STORE_TOTAL_SAVING, user.getTotalSavingMoney());
			editor.putString(STORE_TOTAL_COMMENT, user.getTotalComment());
			editor.putString(STORE_TOTAL_HISTORY, user.getTotalCount());
			editor.putString(STORE_USER_AVATAR, user.getUrlAvatar());
			editor.putLong(STORE_LAST_TIME_LOGIN, System.currentTimeMillis());
		}
		editor.commit();
	}
	
	public static String getValue(Context context, String key) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		return account.getString(key, "");	
	}
	
	public static String getUserId(Context context) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		return account.getString(STORE_USER_ID, "");	
	}
	public static String getUserEmail(Context context) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		return account.getString(STORE_USER_EMAIL, "");		
	}
	
	public static String getUserPass(Context context) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		return account.getString(STORE_USER_PASS, "");		
	}
	public static String getToken(Context context) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		return account.getString(STORE_USER_TOKEN, "");		
	}
	public static boolean clearToken(Context context) {

		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);

			Editor editor = pref.edit();
			editor.remove("STORE_USER_TOKEN");
			return editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
	}
	public static long getLastTimeLogin(Context context) {
		SharedPreferences account = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		return account.getLong(STORE_LAST_TIME_LOGIN, System.currentTimeMillis());		
	}
	
	private void storeAccount(User user) {
//		if(nameValuePairs != null && nameValuePairs.size() > 0) {
//			String userEmail = "";
//			String userPass = "";
//			String token = null;
//			for(int i=0; i < nameValuePairs.size(); i++) {
//				BasicNameValuePair pair = (BasicNameValuePair) nameValuePairs.get(i);
//				if(pair != null && pair.getName().equals("email")) {
//					userEmail = pair.getValue();
//				}
//				if(pair != null && pair.getName().equals("password")) {
//					userPass = pair.getValue();
//				}
//				if(pair != null && pair.getName().equals("token")) {
//					token = pair.getValue();
//				}
//			}
			storeUserAccount(WhyqApplication.Instance().getApplicationContext(), user);
//		}
	}
	
	private void exeGetPerm(Document doc2) {
		// TODO Auto-generated method stub
		Get_Perm_Delegate delegates = (Get_Perm_Delegate)delegate;
		ArrayList<Store> boards = new ArrayList<Store>();
		NodeList boardNodeList = doc.getElementsByTagName("item");
		
		for( int i = 0; i < boardNodeList.getLength(); i ++ ){
			Element boardElement = (Element) boardNodeList.item(i);
			
			String boardId = getValue(boardElement, "permId");
			String boardName = getValue(boardElement, "permDesc");
			String boardDesc = getValue(boardElement, "permDesc");
			String boardDateMessage = getValue(boardElement, "permDateMessage");
			String boardImage = getValue(boardElement, "permImage");
			String permUrl = getValue(boardElement, "permUrl");
			String permAudio = getValue(boardElement, "permAudio");
			WhyqImage whyqImage = new WhyqImage(boardImage);
//			Whyq board = new Whyq(boardId, boardName, boardDesc, boardDateMessage, whyqImage,permUrl,permAudio);
//			boards.add(board);
		}
		delegates.onSuccess(boards);
	}

	public String getValue( Element e, String tag ){
		if( e != null )
		{
			Node node = e.getElementsByTagName(tag).item(0).getFirstChild();
			if( node != null ){
				return node.getNodeValue();
			}
		}
		return "";
	}
	

	private void exeGetBoard(Document doc) {
		// TODO Auto-generated method stub
		ArrayList<Store> boards = new ArrayList<Store>();
		NodeList boardNodeList = doc.getElementsByTagName("item");
		
		for( int i = 0; i < boardNodeList.getLength(); i ++ ){
			Element boardElement = (Element) boardNodeList.item(i);
			
			String boardId = getValue(boardElement, "permId");
			String boardName = getValue(boardElement, "permDesc");
			String boardDesc = getValue(boardElement, "permDesc");
			String boardDateMessage = getValue(boardElement, "permDateMessage");
			String boardImage = getValue(boardElement, "permImage");
			String boardUrl = getValue(boardElement, "permUrl");
			String permAudio = getValue(boardElement, "permAudio");
			WhyqImage whyqImage = new WhyqImage(boardImage);
//			Whyq board = new Whyq(boardId, boardName, boardDesc, boardDateMessage, whyqImage, boardUrl,permAudio);
//			boards.add(board);
		}
		Get_Board_delegate getBoard_delegate = (Get_Board_delegate)delegate;
		getBoard_delegate.onSuccess(boards);
	}

	private void exePermList(Document doc) {
		// TODO Auto-generated method stub
		WhyqList_Delegate whyqList_Delegate = (WhyqList_Delegate)delegate;
		whyqList_Delegate.onSuccess(doc);
	}

	private void exeJoinPerm(Document doc2) {
		// TODO Auto-generated method stub
		User user = getUser();
		JoinPerm_Delegate joinPerm_Delegate = (JoinPerm_Delegate)delegate;
		joinPerm_Delegate.onSuccess(user);
	}

	private void exeCreateBoard(Document doc) {
		// TODO Auto-generated method stub
		Create_Board_delegate createBoardDelegate = (Create_Board_delegate)delegate;
		if(createBoardDelegate != null) {
			createBoardDelegate.onSucess(doc);
		}
	}

	private void exeMyDiary(Document doc, String id) {
		// TODO Auto-generated method stub
		MyDiary_Delegate myDiary_Delegate = (MyDiary_Delegate)delegate;
		List<String[]> thumbList = new ArrayList<String[]>();
		if(doc != null)

		thumbList = getNoteListFromDoc(doc, id);

		if(myDiary_Delegate != null)
			myDiary_Delegate.onSuccess(thumbList, id);
	}

	private void exeLoginTask() {
		// TODO Auto-generated method stub
		User user = getUser();
		if (user != null && context != null && (context.getApplicationContext() instanceof WhyqApplication)) {
			// Store the user object to PermpingApplication
			WhyqApplication state = (WhyqApplication)context.getApplicationContext();
			state.setUser(user);
			storeAccount(user);			
			WhyqMain.UID = user.getId();			
			synchronized (this) {
				if(loginDelegate != null) {
					loginDelegate.on_success();
				}
			}
		} else {
			synchronized (this) {
				if(loginDelegate != null)
					loginDelegate.on_error();
			}
			
		}
	}
}
