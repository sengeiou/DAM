package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.model.Photo;
import whyq.service.img.UrlImageViewHelper;
import whyq.utils.Util;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.dam.R;

public class BasicImageListAdapter extends Fragment implements OnScrollListener {
	public static String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private static Photo photo;
	public static  HashMap<String, Photo> stories = new HashMap<String, Photo>();
//	private TextView tvAddresss;
//	private ImageView imgThumbnail;
//	private TextView tvNumberFavourtie;
	private ImageView imgView;
	private HashMap<String, View> viewList = new HashMap<String, View>();
//	public BasicImageListAdapter(ArrayList<Photo> photoList){
//		
//	}
	public BasicImageListAdapter() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String id = getArguments().getString(EXTRA_MESSAGE);
		photo = stories.get(id);
		View v = viewList.get(id);
		if(v!=null){
			return v;
		}else{
			

			v = inflater.inflate(R.layout.image_item, container, false);
			imgView = (ImageView)v.findViewById(R.id.imgView);
//			tvAddresss = (TextView) v.findViewById(R.id.tvAddress);
//			imgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
//			tvNumberFavourtie = (TextView) v.findViewById(R.id.tvNumberOfFavourite);
//			if(url !=null)
//				UrlImageViewHelper.setUrlDrawable(imgThumbnail, url);
//			tvAddresss.setText(ListDetailActivity.store.getAddress());
//			tvNumberFavourtie.setText(ListDetailActivity.store.getCountFavaouriteMember());


			if(photo !=null){
				String url = photo.getThumb();
				if(photo.getThumb() !=null)
//					UrlImageViewHelper.setUrlDrawable(imgView, photo.getThumb());
					WhyqApplication.Instance().getImageLoader().DisplayImage(photo.getThumb(), imgView);

			}
			return v;
		}

	}

	public static Fragment newInstance(Photo photo) {
		// TODO Auto-generated method stub
		BasicImageListAdapter f = new BasicImageListAdapter();
		Bundle bdl = new Bundle();
		bdl.putString(EXTRA_MESSAGE, "" + photo.getId());
		stories.put(photo.getId(), photo);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}