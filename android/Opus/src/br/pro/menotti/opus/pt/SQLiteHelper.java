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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String KEY_BOOKS_ID = "_id";
	public static final String KEY_BOOKS_LANGUAGE = "_language";
	public static final String KEY_BOOKS_BOOK = "_book";
	public static final String KEY_BOOKS_NOME = "_title";
	public static final String KEY_BOOKS_POINTS = "_points";

	public static final String KEY_POINTS_ID = "_id";
	public static final String KEY_POINTS_LANGUAGE = "_language";
	public static final String KEY_POINTS_BOOK = "_book";
	public static final String KEY_POINTS_PONTO = "_point";
	public static final String KEY_POINTS_TEXTO = "_text";

	private static String DB_PATH = "/data/data/br.pro.menotti.opus.pt/databases/";
	private static String DB_NAME = "opus.db";
	//private static String DB_NAME_GZ = "obradb.db.gz";
	private static final int DB_VERSION = 1;
	private final Context myContext;
	private SQLiteDatabase myDataBase; 

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
	}

	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if (dbExist) {
			//do nothing - database already exist
		}else{

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){

			//database does't exist yet.

		}

		if(checkDB != null){

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException{

		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		
		//GZIPInputStream myInput = new GZIPInputStream(myContext.getAssets().open(DB_NAME_GZ));

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

	}


	@Override
	public synchronized void close() {

		if(myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	    

    public List<Book> getBooks(int language) {
    	List<Book> books = new ArrayList<Book>();
    	Cursor cursor = myDataBase.query("books", null, "_language=?", new String[] {Integer.toString(language)}, null, null, null);
    	if (cursor.moveToFirst()) {
    		do {
    			Book book = new Book();
    			book.set_id(Integer.parseInt(cursor.getString(0)));
    			book.set_language(Integer.parseInt(cursor.getString(1)));
    			book.set_book(Integer.parseInt(cursor.getString(2)));
    			book.set_title(cursor.getString(3));
    			book.set_points(Integer.parseInt(cursor.getString(4)));
    			// Adding contact to list
    			books.add(book);
    		} while (cursor.moveToNext());
    	}
		return books;
    }

    public List<Point> getBookPoints(int language, int book) {
    	List<Point> points = new ArrayList<Point>();
    	Cursor cursor = myDataBase.query("points", null, "_language=? and _book=?", new String[] {Integer.toString(language), Integer.toString(book)}, null, null, null);
    	if (cursor.moveToFirst()) {
    		do {
    			Point point = new Point();
    			point.set_id(Integer.parseInt(cursor.getString(0)));
    			point.set_language(Integer.parseInt(cursor.getString(1)));
    			point.set_book(Integer.parseInt(cursor.getString(2)));
    			point.set_point(Integer.parseInt(cursor.getString(3)));
    			point.set_text(cursor.getString(4));
    			// Adding contact to list
    			points.add(point);
    		} while (cursor.moveToNext());
    	}
		return points;
    }   
}
