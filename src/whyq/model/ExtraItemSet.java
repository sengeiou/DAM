package whyq.model;

import java.util.ArrayList;
import java.util.List;

public class ExtraItemSet {

	private List<OptionItem> optionList = new ArrayList<OptionItem>();
	private List<SizeItem> sizeList = new ArrayList<SizeItem>();
	private List<ExtraItem> extraList = new ArrayList<ExtraItem>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the sizeList
	 */
	public List<SizeItem> getSizeList() {
		return sizeList;
	}

	/**
	 * @param sizeList the sizeList to set
	 */
	public void setSizeList(List<SizeItem> sizeList) {
		this.sizeList = sizeList;
	}

	/**
	 * @return the optionList
	 */
	public List<OptionItem> getOptionList() {
		return optionList;
	}

	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(List<OptionItem> optionList) {
		this.optionList = optionList;
	}

	/**
	 * @return the extraList
	 */
	public List<ExtraItem> getExtraList() {
		return extraList;
	}

	/**
	 * @param extraList the extraList to set
	 */
	public void setExtraList(List<ExtraItem> extraList) {
		this.extraList = extraList;
	}

}
