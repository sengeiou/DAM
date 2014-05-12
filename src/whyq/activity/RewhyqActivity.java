/**
 * 
 */
package whyq.activity;

import whyq.WhyqMain;
import whyq.model.WhyqBoard;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.whyq.R;

/**
 * @author Linh Nguyen
 *
 */
public class RewhyqActivity extends Activity {
	
	private String p_permId;
	private String p_permDescription;
	private WhyqBoard p_currentBoard;
	
	private int selectedBoardId = -1;
	
	private EditText permDescription;
	private Spinner boardSpinner;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View contentView = LayoutInflater.from(getParent()).inflate(R.layout.reperm_layout, null);
        setContentView(contentView);
	}
	
	private class SpinnerSelectedItemListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			WhyqBoard board = (WhyqBoard) parent.getItemAtPosition(pos);
			selectedBoardId = Integer.parseInt(board.getId());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
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
}
