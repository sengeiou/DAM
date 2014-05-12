package whyq.interfaces;

import java.util.List;
import whyq.model.FriendTwitter;
public interface FriendTwitterController {
	List<FriendTwitter> getListNotJoinWhyq();
	List<FriendTwitter> getListWhyq();
}
