/* Copyright (c) 2013 Ricardo Menotti, All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its 
 * documentation for NON-COMERCIAL purposes and without fee is hereby granted 
 * provided that this copyright notice appears in all copies.
 *
 * RICARDO MENOTTI MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR 
 * NON-INFRINGEMENT. RICARDO MENOTTI SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS 
 * SOFTWARE OR ITS DERIVATIVES. 
 */

package br.pro.menotti.opus.pt;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	/*  language selection:
	 *  0 - Português (pt)
	 *  1 - English (en)
	 *  2 - Español (es) 
	 *  3 - Français (fr) 
	 */
	public static final int language = 0;
	private SQLiteHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new SQLiteHelper(getApplicationContext());

		String listStr[] = getResources().getStringArray(R.array.listString);

		ListView lv = (ListView) findViewById(android.R.id.list);

		lv.setAdapter(new ArrayAdapter<String>
		(this, android.R.layout.simple_list_item_1, listStr));

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SQLiteHelper db;
				switch (position) {
				case 0:
					showBooks();
					break;
				case 1:
					AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
					builder.setTitle(R.string.display_search);

					final EditText input = new EditText(view.getContext());
					builder.setView(input);

					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String search_key = input.getEditableText().toString().trim();
							if (!search_key.equals("")) {
								showPoints(search_key);
							}
						}
					});
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
					break;
				case 2:
					db = new SQLiteHelper(getBaseContext());
					db.openDataBase();
					showFavorites();
					db.close();
					break;
				case 3:
					db = new SQLiteHelper(getBaseContext());
					db.openDataBase();
					BookPoint bp = db.getBookPoint();
					DialogFragment newFragment = new DialogPoint(bp);
					newFragment.show(getSupportFragmentManager(), bp.toString());
					db.close();
					break;
				default:
					Toast.makeText(getApplicationContext(),	((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				}
			}

		});

		try {

			db.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			db.openDataBase();
			db.close();

		}catch(SQLException sqle){

			throw sqle;

		}
	}

	public void showBooks() {
		Intent intent = new Intent(this, DisplayBooks.class);
		startActivity(intent);
	}

	public void showPoints(String search_key) {
		Intent intent = new Intent(this, DisplayPoints.class);
		intent.putExtra("search_key", search_key);
		startActivity(intent);
	}

	public void showFavorites() {
		Intent intent = new Intent(this, DisplayPoints.class);
		intent.putExtra("favorites", true);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
