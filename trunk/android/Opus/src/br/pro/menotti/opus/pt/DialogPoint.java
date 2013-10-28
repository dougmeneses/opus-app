package br.pro.menotti.opus.pt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogPoint extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getTag());
		return builder.create();
	}
	
}
