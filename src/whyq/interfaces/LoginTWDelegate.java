package whyq.interfaces;

import twitter4j.auth.AccessToken;

public interface LoginTWDelegate {

		void onLoginTWSuccess(AccessToken accessToken);
		void onLoginTWError();

}
