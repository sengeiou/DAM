package whyq.activity;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import whyq.WhyqMain;
import whyq.interfaces.Get_Perm_Delegate;
import whyq.interfaces.MyDiary_Delegate;
import whyq.model.Store;
import whyq.model.Transporter;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.custom.WebImageView;
import com.whyq.R;

public class FriendActivity extends Activity implements View.OnClickListener , MyDiary_Delegate, Get_Perm_Delegate {

	private static final String TAG = "MyDiaryActivity";
	private static int rowWidth = 0;
	private static int rowHeight = 0;
	//private ImageView calendarToJournalButton;
	private Button currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar calendar;
	private int month, year;
	private String monthGetData= "";
	private int maxMonth = 0;
	private boolean isGettingData = false;
	private HashMap<String, String> months = new HashMap<String, String>();
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	private List<String> imageList = new ArrayList<String>();
	private List<String> serviceList = new ArrayList<String>();
	private List<String> idList = new ArrayList<String>();
	RelativeLayout cellLayout;
	DisplayMetrics metrics;
	ProgressBar progressBar;
	private HashMap<String, List<WebImageView>> thumbListById = new HashMap<String, List<WebImageView>>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydiary_layout);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
		calendar = Calendar.getInstance(Locale.getDefault());
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		initMonths();
		/*Log.d(TAG, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);*/

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);
		prevMonth.setScaleType(ScaleType.FIT_XY);
		currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate,
				calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);
		nextMonth.setScaleType(ScaleType.FIT_XY);
		calendarView = (GridView) this.findViewById(R.id.calendar);
		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);



		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setPadding(0, 0, 0, 0);
		calendarView.setAdapter(adapter);
		
		WhyqUtils.clearViewHistory();

	}
//	@Override
//	public void onResume(){
//		Log.d("resume:","resume=======>");
//	}
	private void initMonths() {
		// TODO Auto-generated method stub
		months.put("January", "01");
		months.put("February", "02");
		months.put("March", "03");
		months.put("April", "04");
		months.put("May", "05");
		months.put("June", "06");
		months.put("July", "07");
		months.put("August", "08");
		months.put("September", "09");
		months.put("October", "10");
		months.put("November", "11");
		months.put("December", "12");
		
	}
	private void updateThumbnails(WebImageView thumb1, String url){
		synchronized (this) {
			if(thumb1 != null){
				thumb1.setImageUrl(url);
				thumb1.loadImage();
			}
		}
		
	}
	 private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
	     protected Long doInBackground(URL... urls) {
	         int count = urls.length;
	         long totalSize = 0;

	         return totalSize;
	     }

	     protected void onProgressUpdate(Integer... progress) {

	     }

	     protected void onPostExecute(Long result) {
	        
	     }
	 }
	 
	private List<String> getThumbnail(String url, String uid, String id){
		//Log.d(id+"====>", ">>>======>>>"+uid);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		XMLParser xmlParser = new XMLParser();
		xmlParser.getResponseFromURL(XMLParser.MYDIARY, FriendActivity.this, url, nameValuePairs,id);//+"2012-06-15"
		return null;

	}
	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		calendar.set(year, month - 1, calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(dateFormatter.format(dateTemplate,
				calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			maxMonth = 0;
			isGettingData = false; 
			
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			/*Log.d(TAG, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);*/
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			maxMonth = 0;
			isGettingData = false;
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			/*Log.d(TAG, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);*/
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		//Log.d(TAG, "Destroying View ...");
		super.onDestroy();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Inner Class
	public class GridCellAdapter extends BaseAdapter  {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private final int month, year;
		private int daysInMonth, prevMonthDays;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			this.month = month;
			this.year = year;

			/*Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);*/
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			/*Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());*/

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			//Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			// The number of days to leave blank at
			// the start of this month.
			int trailingSpaces = 0;
			int leadSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			/*Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");*/

			// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			//Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				/*Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);*/
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				/*Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);*/
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				/*Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);*/
			}

			// Compute how much to leave before before the first day of the
			// month.
			// getDay() returns 0 for Sunday.
			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			/*Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);*/

			if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
				++daysInMonth;
			}

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				/*Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));*/
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				/*Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);*/
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-#8b9096" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-#2c3642" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				//Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-#88b9f4" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap findNumberOfEventsPerMonth(int year, int month) {
			HashMap map = new HashMap<String, Integer>();
			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.calendar_day_gridcell, parent,
						false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
//			gridcell.setOnClickListener(this);
			cellLayout = (RelativeLayout)row.findViewById(R.id.layout);
			if(cellLayout != null){
				android.view.ViewGroup.LayoutParams params = cellLayout.getLayoutParams();
				params.height = (metrics.widthPixels-4)/7;
				
			}
				
			// ACCOUNT FOR SPACING

			//Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
					RelativeLayout layout = (RelativeLayout)row.findViewById(R.id.layout);
					layout.setBackgroundResource(R.drawable.date_bg_selected);
					num_events_per_day.setGravity(Gravity.LEFT|Gravity.TOP);
				}
			}

			// Set the Day GridCell
			gridcell.setGravity(Gravity.LEFT | Gravity.TOP);
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			
			if(theday.length() <2)
				theday = "0"+theday;
			String date = theyear+"-"+FriendActivity.this.months.get(themonth)+"-"+theday;
			row.setTag(date);
			//Log.d(tag, "Setting GridCell " +date);
			String monthString = FriendActivity.this.months.get(themonth);
			if(monthString.length() == 1){
				monthString = "0"+monthString;
			}
			String id = monthString+theday;
			if (day_color[1].equals("#88b9f4")) {
				gridcell.setTextColor(Color.parseColor("#e9eff3"));
			}
			if (day_color[1].equals("#2c3642")) {
				gridcell.setTextColor(Color.parseColor("#2c3642"));
			}
			if (day_color[1].equals("#8b9096")) {
				gridcell.setTextColor(Color.parseColor("#ccddf2"));
				row.setBackgroundDrawable(getResources().getDrawable(R.drawable.date_bg_selected));
//				row.setBackgroundColor(Color.parseColor("#7ea4bc"));
			}
			rowHeight = row.getHeight();
			rowWidth = row.getWidth();
//			row.setId(Integer.valueOf(theday+MyDiaryActivity.this.months.get(themonth)));
			if(theday.length()==1)
				theday="0"+theday;
			row.setId(Integer.valueOf(theday));
			// thumbs
			WebImageView thumb1;
			WebImageView thumb2; 
			WebImageView thumb3;
			thumb1 = (WebImageView)row.findViewById(R.id.thumbnail1);
			thumb2 = (WebImageView)row.findViewById(R.id.thumbnail2);
			thumb3 = (WebImageView)row.findViewById(R.id.thumbnail3);
			List<WebImageView> thumbList = new ArrayList<WebImageView>();
			thumbList.add(thumb1);
			thumbList.add(thumb2);
			thumbList.add(thumb3);
			String []paras = {API.getPermsByDateWithMonth+date, WhyqMain.UID ,theday};
			if(!isGettingData ){
				isGettingData = true;
				new getData().execute(paras);
				monthGetData = themonth;
				maxMonth++;
			}else if(maxMonth <2 &&!themonth.equals(monthGetData)){
				new getData().execute(paras);
				monthGetData = themonth;
				maxMonth++;
			}
				
			thumbListById.put(id, thumbList);
			idList.add(id);
			serviceList.add(id);
			row.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String date =(String)v.getTag();
					if(date != null){
//						onClicked(date);
						Intent myIntent = new Intent(v.getContext(), ListActivity.class);
						String boardUrl = API.getPermsByDate + date;
						myIntent.putExtra("permByDate", boardUrl);
						ListActivity.isLogin =false;
						View boardListView = FriendActivityGroup.group.getLocalActivityManager() .startActivity("BoardDeatalListActivity", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
						FriendActivityGroup.group.replaceView(boardListView);
					}
				}
			});
			return row;
		}


		public void onClicked(String date_) {
			String date_month_year = date_;
			try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
					nameValuePairs.add(new BasicNameValuePair("uid", WhyqMain.UID));
					String url = API.getPermsByDate + date_month_year;
					XMLParser xmlParser = new XMLParser();
					xmlParser.getResponseFromURL(XMLParser.GET_PERMS_BY_DATE, FriendActivity.this, url, nameValuePairs,null);
//					MyDiaryController myDiaryController = new MyDiaryController();
//					List<Perm> perms = myDiaryController.getPermsByDate(date_month_year, MyDiaryActivity.this);
//					Transporter transporter = new Transporter();
//					transporter.setPerms(perms);
//					
//					// Go to Perm detail screen
//
//					
//
//		            Intent myIntent = new Intent(MyDiaryActivity.this, BoardDetailActivity.class);
//		            myIntent.putExtra(Constants.TRANSPORTER, transporter);
//					View boardListView = MyDiaryActivityGroup.group.getLocalActivityManager() .startActivity("detail", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
//					MyDiaryActivityGroup.group.replaceView(boardListView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
		
		/**
		 * Convert the month as name to month as number.
		 * @param monthAsString the month as name.
		 * @return the month as number.
		 */
		private String convertMonth(String monthAsString) {
			String ret = null;
			if (monthAsString != null) {
				if (monthAsString.equals(months[0]))
					ret = "01";
				else if (monthAsString.equals(months[1])) 
					ret = "02";
				else if (monthAsString.equals(months[2])) 
					ret = "03";
				else if (monthAsString.equals(months[3])) 
					ret = "04";
				else if (monthAsString.equals(months[4])) 
					ret = "05";
				else if (monthAsString.equals(months[5])) 
					ret = "06";
				else if (monthAsString.equals(months[6])) 
					ret = "07";
				else if (monthAsString.equals(months[7])) 
					ret = "08";
				else if (monthAsString.equals(months[8])) 
					ret = "09";
				else if (monthAsString.equals(months[9])) 
					ret = "10";
				else if (monthAsString.equals(months[10])) 
					ret = "11";
				else if (monthAsString.equals(months[12])) 
					ret = "12";
			}
			
			return ret;
		}
	}
	
	public class getData extends AsyncTask<String, String, List<String>>{

		
        @Override
        protected List<String> doInBackground(String... msg) {
            // TODO Auto-generated method stub
        	
            List<String> result = getThumbnail( msg[0], msg[1],msg[2]);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> imageList) {
            // TODO Auto-generated method stub
        }
	}
	public class showData extends AsyncTask<String, String, String>{
		WebImageView thumb;
		public showData( WebImageView thumb) {
			// TODO Auto-generated constructor stub
			this.thumb = thumb;
			this.thumb.setScaleType(ScaleType.FIT_XY);
			this.thumb.setVisibility(View.VISIBLE);
		}
        @Override
        protected String doInBackground(String... msg) {
            // TODO Auto-generated method stub
        	String url = msg[0];
            return url;
        }

        @Override
        protected void onPostExecute(String url) {
            // TODO Auto-generated method stub
            //Log.d("====>", ">>>======>>>");
			if(thumb != null){
				thumb.setImageUrl(url);
				thumb.loadImage();
			}
               
        }
	}
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        WhyqMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onSuccess(ArrayList<Store> stores) {
		// TODO Auto-generated method stub
		Transporter transporter = new Transporter();
		transporter.setPerms(stores);

		
		// Go to the Board Detail screen
		Intent i = new Intent(FriendActivity.this, BoardDetailActivity.class);
		i.putExtra(Constants.TRANSPORTER, transporter);
		View boardDetail = ProfileActivityGroup.group.getLocalActivityManager() .startActivity("BoardDetailActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		ProfileActivityGroup.group.replaceView(boardDetail);
	}
	@Override
	public void onSuccess(List<String[]> imagesList, String idMonth) {
		// TODO Auto-generated method stub
		int length = imagesList.size();
		for(int i=0; i < length; i++){
//			String id = String.valueOf(i);
//			if(id.length()==1)
//				id="0"+id;
			String[] item = imagesList.get(i);
			if(item != null )
			{
				if(item.length > 0){
					String id = item[0];
					String thumbLink = item[1];
					int numberThumb = 0;
					WebImageView thumb = getThumbToLoad(id);
					if(thumb != null)
						new showData(thumb).execute(thumbLink);					 
				}
			}
//	        if(imageList != null){
//	        	if(id != null){
//
//	              		int max;
//	              		if(imageList.size() > 3){
//	              			max = 3;
//	              		}
//	              		else{
//	              			max = imageList.size();
//	              		}
//
//	          			for(int j=0;j< max;j++){
//	          				Log.d("===Update",id+">>>========"+imageList.get(j));
//	          				new showData(thumbListById.get(id).get(j)).execute(imageList.get(j));
//	          			}
//	        	}		
//	        }

		}
		progressBar.setVisibility(View.INVISIBLE);

	}
	private WebImageView getThumbToLoad(String id) {
		// TODO Auto-generated method stub
		if(thumbListById == null) {
			return null;
		}
		WebImageView result;
		for(int i=0;i<3;i++) {
			if(thumbListById.get(id) != null) {
				result = thumbListById.get(id).get(i);
				if(result != null && result.getVisibility() != View.VISIBLE)
					return result;
			}			
		}
		return null;
	}
}
