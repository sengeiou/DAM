
package whyq.controller;

import java.util.ArrayList;
import java.util.List;

import whyq.interfaces.Get_Board_delegate;
import whyq.model.Store;
import whyq.model.WhyqBoard;
import whyq.utils.API;
import whyq.utils.XMLParser;


public class BoardController {

	public BoardController() {
		
	}
	
	public ArrayList<WhyqBoard> getBoardList( String userId){
		
		ArrayList<WhyqBoard> boards = new ArrayList<WhyqBoard>();
		
		XMLParser parser = new XMLParser( API.getProfileURL + userId, true );	
		return boards;
	}
	
		
	/**
	 * Return the list of perms of selected board.
	 * @param boardId the board id.
	 * @return the list of perms.
	 */
	public List<Store> getPermsByBoardId(String boardId, Get_Board_delegate delegate) {
		if (boardId == null || "".equals(boardId))
			return null;
		XMLParser parser = new XMLParser(API.permListFromBoardUrl + boardId, delegate, XMLParser.GET_BOARD);
		return parser.getPerms();
	}
}