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

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayBooks extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SQLiteHelper db;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new SQLiteHelper(this);
		db.openDataBase();

		List<Book> books = new ArrayList<Book>();
		books = db.getBooks();
		db.close();

		ListView lv = (ListView) findViewById(android.R.id.list);

		lv.setAdapter(new ArrayAdapter<Book>
		(this, android.R.layout.simple_list_item_1, books));

		Toast.makeText(getApplicationContext(),
				(getString(R.string.display_aleatory_point)), Toast.LENGTH_SHORT).show();

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showPoints(position);
			}
		});		

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				SQLiteHelper db;
				db = new SQLiteHelper(getBaseContext());
				db.openDataBase();
				BookPoint bp = db.getBookPoint(position);
				DialogFragment newFragment = new DialogPoint(bp);
				newFragment.show(getSupportFragmentManager(), bp.toString());
				db.close();
				return true;
			}
		});		
	}

	public void showPoints(int book) {
		Intent intent = new Intent(this, DisplayChapters.class);
		intent.putExtra("book", book);
		startActivity(intent);
	}
}
