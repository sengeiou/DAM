package whyq.interfaces;

import org.w3c.dom.Document;

public interface WhyqList_Delegate {
	public void onSuccess(Document doc);
	public void onError();
}
