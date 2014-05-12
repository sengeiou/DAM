package whyq.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class SpannableUtils {

	public static Spannable stylistTextBold(String input, String key,
			int color) {
		Spannable result = new SpannableString(input);

		final int start, end;
		start = input.indexOf(key);
		end = start + key.length();
		result.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		result.setSpan(new StyleSpan(Typeface.BOLD), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return result;
	}

	public static Spannable stylistText(String input, String key, int color) {
		Spannable result = new SpannableString(input);

		final int start, end;
		start = input.indexOf(key);
		end = start + key.length();
		result.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		result.setSpan(new StyleSpan(Typeface.NORMAL), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return result;
	}

}