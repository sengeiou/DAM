package whyq.activity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.util.Log;

public class ConsumeServiceActivity extends NavigationActivity implements IServiceListener {

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		
	}
	
	protected Service getService() {
		return new Service(this);
	}
}
