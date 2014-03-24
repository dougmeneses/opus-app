package br.pro.menotti.opus.pt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

@SuppressLint("ValidFragment")
public class DialogPoint extends DialogFragment {

	private BookPoint bp;
	private boolean removing = false;
	
	public DialogPoint(BookPoint bp) {
		this.bp = bp;
	}

	public DialogPoint(BookPoint bp, boolean removing) {
		this.bp = bp;
		this.removing = removing;
	}

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
		if (removing)
		builder.setNeutralButton(getText(R.string.display_delete), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				SQLiteHelper db = new SQLiteHelper(getActivity().getBaseContext());
				db.openDataBase();
				db.removeFavorite(bp.get_id());
				db.close();
			}
		});	
		else
			builder.setNeutralButton(getText(R.string.display_favorites), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					SQLiteHelper db = new SQLiteHelper(getActivity().getBaseContext());
					db.openDataBase();
					db.addFavorite(bp);
					db.close();
				}
			});	
		return builder.create();
	}	
}
