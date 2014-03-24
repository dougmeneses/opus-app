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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayPoints extends FragmentActivity {
	private SQLiteHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		final int book = intent.getIntExtra("book", 0);
		int chapter = intent.getIntExtra("chapter", 0);
		final String search_key = intent.getStringExtra("search_key");
		final boolean favorites = intent.getBooleanExtra("favorites", false);
		
		db = new SQLiteHelper(this);
		db.openDataBase();
//		final String book_name = db.getBook(book).toString();

		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setFastScrollEnabled(true);

		if (favorites) {
			setTitle(getString(R.string.display_favorites));
			List<BookPoint> points = new ArrayList<BookPoint>();
			points = db.getFavoritePoints();
			lv.setAdapter(new ArrayAdapter<BookPoint>
			(this, android.R.layout.simple_list_item_1, points));			
		}
		else if (search_key != null) {
			setTitle(getString(R.string.display_search) + ": " + search_key);
			List<BookPoint> points = new ArrayList<BookPoint>();
			points = db.getPoints(search_key);
			lv.setAdapter(new ArrayAdapter<BookPoint>
			(this, android.R.layout.simple_list_item_1, points));
		}
		else {
			setTitle(db.getBook(book) + ": " + db.getChapter(book, chapter));			
			List<BookPoint> points = new ArrayList<BookPoint>();
			points = db.getBookPoints(book, chapter);
			lv.setAdapter(new ArrayAdapter<BookPoint>
			(this, android.R.layout.simple_list_item_1, points));
		}
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				db = new SQLiteHelper(getBaseContext());
				db.openDataBase();
				BookPoint bp = (BookPoint)parent.getItemAtPosition(position);
				DialogFragment newFragment = new DialogPoint(bp, favorites);
				newFragment.show(getSupportFragmentManager(), bp.toString());
				db.close();				
				return true;
			}
		});		

					
		db.close();
	}

}
