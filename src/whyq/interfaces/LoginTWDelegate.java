package whyq.interfaces;

import twitter4j.http.AccessToken;

public interface LoginTWDelegate {

		void onLoginTWSuccess(AccessToken accessToken);
		void onLoginTWError();

}
