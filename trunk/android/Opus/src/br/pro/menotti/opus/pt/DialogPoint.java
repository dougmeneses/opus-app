package br.pro.menotti.opus.pt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogPoint extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getTag());		
		builder.setPositiveButton(getText(R.string.display_send), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent sendIntent = new Intent();
				String text = getTag() + " " + getText(R.string.display_url);
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, text);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, getText(R.string.display_send)));
			}
		});	
		return builder.create();
	}	
}
