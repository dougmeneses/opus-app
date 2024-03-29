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

import android.content.Context;
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

public class DisplayChapters extends FragmentActivity {

	int book;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SQLiteHelper db;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		book = intent.getIntExtra("book", 0);

		db = new SQLiteHelper(this);
		db.openDataBase();

		List<Chapter> chapters = new ArrayList<Chapter>();
		chapters = db.getChapters(book);
		setTitle(getString(R.string.display_chapters) + ": " + db.getBook(book));
		db.close();

		ListView lv = (ListView) findViewById(android.R.id.list);

		lv.setAdapter(new CustomAdapter<Chapter>
		(this, android.R.layout.simple_list_item_1, chapters));

		Toast.makeText(getApplicationContext(),
				(getString(R.string.display_aleatory_point)), Toast.LENGTH_SHORT).show();

		lv.setOnItemClickListener(new OnItemClickListener() {



			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				showPoints(book, (int)id);
			}
		});		

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				SQLiteHelper db;
				db = new SQLiteHelper(getBaseContext());
				db.openDataBase();
				BookPoint bp = db.getBookPoint(book, position);
				DialogFragment newFragment = new DialogPoint(bp);
				newFragment.show(getSupportFragmentManager(), bp.toString());
				db.close();
				return true;
			}
		});		
	}

	public void showPoints(int book, int id) {
		Intent intent = new Intent(this, DisplayPoints.class);
		intent.putExtra("book", book);
		intent.putExtra("chapter", id);
		startActivity(intent);
	}

	public class CustomAdapter<T> extends ArrayAdapter<T> {

		private ArrayList<T> innerClassBookArray;

		public CustomAdapter(Context context, int resource, List<T> objects) {
			super(context, resource, objects);
			this.innerClassBookArray = (ArrayList<T>) objects;
		}

		@Override
		public long getItemId(int position) {
			return ((Chapter) innerClassBookArray.get(position)).get_chapter();
		}
	}
}


