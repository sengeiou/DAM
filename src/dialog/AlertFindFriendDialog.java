package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlertFindFriendDialog extends BaseDialogFragment {

	public interface OnDialogButtonClickListener {
		void onPositiveButtonClick();

		void onNegativeButtonClick();
	}

	private OnDialogButtonClickListener mListener;

	public void setOnDialogButtonClickListern(
			OnDialogButtonClickListener listener) {
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		return builder.setMessage("Do you want to find a friend")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) {
							mListener.onPositiveButtonClick();
						}
					}
				}).setNegativeButton(android.R.string.cancel, null).create();
	}
}
