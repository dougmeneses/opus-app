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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayPoints extends ListActivity {
	private SQLiteHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		final int book = intent.getIntExtra("book", 0);
		int chapter = intent.getIntExtra("chapter", 0);
		final String search_key = intent.getStringExtra("search_key");
		
		db = new SQLiteHelper(this);
		db.openDataBase();
		final String book_name = db.getBook(book);

		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setFastScrollEnabled(true);

		if (search_key != null) {
			setTitle(getString(R.string.display_search) + ": " + search_key);
			List<BookPoint> points = new ArrayList<BookPoint>();
			points = db.getPoints(search_key);
			lv.setAdapter(new ArrayAdapter<BookPoint>
			(this, android.R.layout.simple_list_item_1, points));
		}
		else {
			setTitle(db.getBook(book) + ": " + db.getChapter(book, chapter));			
			List<Point> points = new ArrayList<Point>();
			points = db.getBookPoints(book, chapter);
			lv.setAdapter(new ArrayAdapter<Point>
			(this, android.R.layout.simple_list_item_1, points));
		}
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent sendIntent = new Intent();
				String text = (String)((TextView)view).getText() + " " + getText(R.string.display_url);
				if (search_key == null)
					text = book_name + " " + text;
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, text);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, getText(R.string.display_send)));
				return true;
			}
		});		

					
		db.close();
	}

}
