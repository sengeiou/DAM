package whyq.interfaces;

import java.util.List;

public interface MyDiary_Delegate {
	void onSuccess( List<String[]> thumbList, String id);
	void onError();
}
