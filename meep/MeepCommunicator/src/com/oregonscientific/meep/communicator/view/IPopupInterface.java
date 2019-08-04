package com.oregonscientific.meep.communicator.view;

public interface IPopupInterface {

	public void onSearchButtonPressed(PopUpFragment fragment, String message);

	public void onAddButtonPressed(PopUpFragment fragment, String message);

	public void onDeclineButtonPressed(PopUpFragment thisFragment,
			String meepTag);

	public void onAcceptButtonPressed(PopUpFragment thisFragment, String meepTag);
	
	public void onYesButtonPressed(PopUpFragment thisFragment);
	
	public void onNoButtonPressed(PopUpFragment thisFragment);
	
	public void onOkButtonPressed(PopUpFragment thisFragment, String action);
	
}
