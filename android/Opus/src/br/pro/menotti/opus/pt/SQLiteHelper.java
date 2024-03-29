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
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class SQLiteHelper extends SQLiteOpenHelper {

//	public static final String KEY_BOOKS_ID = "_id";
//	public static final String KEY_BOOKS_LANGUAGE = "_language";
//	public static final String KEY_BOOKS_BOOK = "_book";
//	public static final String KEY_BOOKS_TITLE = "_title";
//	public static final String KEY_BOOKS_POINTS = "_points";
//
//	public static final String KEY_POINTS_ID = "_id";
//	public static final String KEY_POINTS_LANGUAGE = "_language";
//	public static final String KEY_POINTS_BOOK = "_book";
//	public static final String KEY_POINTS_PONTO = "_point";
//	public static final String KEY_POINTS_TEXTO = "_text";

	private static String DB_PATH = "/data/data/br.pro.menotti.opus.pt/databases/";
	private static String DB_NAME = "opus_pt_v7.db";
	//private static String DB_NAME_GZ = "obradb.db.gz";
	private static final int DB_VERSION = 1;
	private final Context myContext;
	private SQLiteDatabase myDataBase; 

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
	}


    @Override
	public void onCreate(SQLiteDatabase db) {

	}

    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}   
    
    public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

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

	@Override
	public synchronized void close() {

		if(myDataBase != null)
			myDataBase.close();

		super.close();

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

	/**
	 * Get a book by code
	 * @param book
	 * @return Book
	 */
	public Book getBook(int book) {
    	Cursor cursor = myDataBase.query("books", null, "_book=?", new String[] {Integer.toString(book)} , null, null, null);
    	Book b = new Book();
    	if (cursor.moveToFirst()) {
    		b.set_book(cursor.getLong(2));
    		b.set_title(cursor.getString(3));
    	}
		return b;
    }

	/**
	 * Get a random BookPoint
	 * @return BookPoint
	 */
	public BookPoint getBookPoint() {
    	Cursor cursor = myDataBase.rawQuery("select b._title, p._point, p._text, p._book from books b, points p where p._book=b._book and b._language=" + MainActivity.language + " order by random() limit 1", null);
    	BookPoint bp = new BookPoint();
    	if (cursor.moveToFirst()) {
    		bp.set_title(cursor.getString(0));
    		bp.set_point(cursor.getLong(1));
    		bp.set_text(cursor.getString(2));
    		bp.set_book(cursor.getLong(3));
    	}
		return bp;
    }
	 
	/**
	 * Get a random BookPoint from a book
	 * @param book
	 * @return BookPoint
	 */
    public BookPoint getBookPoint(int book) {
    	Cursor cursor = myDataBase.rawQuery("select b._title, p._point, p._text, p._book from books b, points p where p._book=b._book and b._book=" + book + " and b._language=" + MainActivity.language + " order by random() limit 1", null);
    	BookPoint bp = new BookPoint();
    	if (cursor.moveToFirst()) {
    		bp.set_title(cursor.getString(0));
    		bp.set_point(cursor.getLong(1));
    		bp.set_text(cursor.getString(2));
    		bp.set_book(cursor.getLong(3));
    	}
		return bp;
    }
    
    /**
     * Get a random BookPoint from a book chapter
     * @param book
     * @param chapter
     * @return BookPoint
     */
	public BookPoint getBookPoint(int book, int chapter) {
    	Cursor cursor = myDataBase.rawQuery("select b._title, p._point, p._text, p._book from books b, chapters c, chapter_points cp, points p where b._book=c._book and b._book=p._book and b._book=cp._book and c._chapter=cp._chapter and cp._point=p._point and p._book=" + book + " and c._chapter=" + chapter + " and b._language=" + MainActivity.language + " order by random() limit 1", null); 
    	BookPoint bp = new BookPoint();
    	if (cursor.moveToFirst()) {
    		bp.set_title(cursor.getString(0));
    		bp.set_point(cursor.getLong(1));
    		bp.set_text(cursor.getString(2));
    		bp.set_book(cursor.getLong(3));
    	}
		return bp;
    }      

	/**
	 * Get all BookPoints from a book chapter
	 * @param book
	 * @param chapter
	 * @return
	 */
    public List<BookPoint> getBookPoints(int book, int chapter) {
    	List<BookPoint> points = new ArrayList<BookPoint>();
    	Cursor cursor = myDataBase.rawQuery("select b._title, p._point, p._text, p._book from books b, chapters c, chapter_points cp, points p where b._book=c._book and b._book=p._book and b._book=cp._book and c._chapter=cp._chapter and cp._point=p._point and p._book=" + book + " and c._chapter=" + chapter + " and b._language=" + MainActivity.language, null); 
    	if (cursor.moveToFirst()) {
    		do {
    			BookPoint point = new BookPoint();
    			point.set_title(cursor.getString(0));
    			point.set_point(cursor.getLong(1));
    			point.set_text(cursor.getString(2));
    			point.set_book(cursor.getLong(3));
    			points.add(point);
    		} while (cursor.moveToNext());
    	}
		return points;
    }

    /**
     * Get all Books
     * @return
     */
    public List<Book> getBooks() {
    	List<Book> books = new ArrayList<Book>();
    	Cursor cursor = myDataBase.query("books", null, "_language=?", new String[] {Integer.toString(MainActivity.language)}, null, null, null);
    	if (cursor.moveToFirst()) {
    		do {
    			Book book = new Book();
    			book.set_id(Integer.parseInt(cursor.getString(0)));
    			book.set_language(Integer.parseInt(cursor.getString(1)));
    			book.set_book(Integer.parseInt(cursor.getString(2)));
    			book.set_title(cursor.getString(3));
    			book.set_points(Integer.parseInt(cursor.getString(4)));
    			books.add(book);
    		} while (cursor.moveToNext());
    	}
		return books;
    }

    /**
	 * Get a Chapter by code
     * @param book
     * @param chapter
     * @return Chapter
     */
    public Chapter getChapter(int book, int chapter) {
    	Cursor cursor = myDataBase.query("chapters", null, "_book=? and _chapter=?", new String[] {Integer.toString(book), Integer.toString(chapter)} , null, null, null);
    	Chapter c = new Chapter();
    	if (cursor.moveToFirst()) {
    		c.set_book(cursor.getLong(2));
    		c.set_chapter(cursor.getLong(3));
    		c.set_title(cursor.getString(4));
    	}
		return c;
    }   

    /**
     * Get all Chapter from a book
     * @param book
     * @return
     */
    public List<Chapter> getChapters(int book) {
		List<Chapter> chapters = new ArrayList<Chapter>();
		Cursor cursor = myDataBase.query("chapters", null, "_language=? and _book=?", new String[] {Integer.toString(MainActivity.language), Integer.toString(book)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Chapter chapter = new Chapter();
				chapter.set_chapter(cursor.getLong(3));
				chapter.set_title(cursor.getString(4));
				chapters.add(chapter);
			} while (cursor.moveToNext());
		}
		return chapters;
	}   

    /**
     * Get favorite points
     * @return
     */
    public List<BookPoint> getFavoritePoints() {
    	List<BookPoint> points = new ArrayList<BookPoint>();
    	Cursor cursor = myDataBase.rawQuery("select f._id, b._title, p._point, p._text, p._book from books b, points p, favorites f where b._book=p._book and b._book=f._book and p._point=f._point order by f._id", null); 
    	if (cursor.moveToFirst()) {
    		do {
    			BookPoint point = new BookPoint();
    			point.set_id(cursor.getLong(0));
    			point.set_title(cursor.getString(1));
    			point.set_point(cursor.getLong(2));
    			point.set_text(cursor.getString(3));
    			point.set_book(cursor.getLong(4));
    			points.add(point);
    		} while (cursor.moveToNext());
    	}
		return points;
    }   

    /**
     * Get points by search key
     * @param search_key
     * @return
     */
    public List<BookPoint> getPoints(String search_key) {
    	List<BookPoint> points = new ArrayList<BookPoint>();
    	String esc_search_key = DatabaseUtils.sqlEscapeString('%' + search_key + '%'); 
    	Cursor cursor = myDataBase.rawQuery("select b._title, p._point, p._text, p._book from books b, points p where b._book=p._book and (p._text like " + esc_search_key + " or p._point = " + esc_search_key.replace("%", "") + ")", null); 
    	if (cursor.moveToFirst()) {
    		do {
    			BookPoint point = new BookPoint();
    			point.set_title(cursor.getString(0));
    			point.set_point(cursor.getLong(1));
    			point.set_text(cursor.getString(2));
    			point.set_book(cursor.getLong(3));
    			points.add(point);
    		} while (cursor.moveToNext());
    	}
		return points;
    }   
    
	public void addFavorite(BookPoint bp) {
    	try {
    		myDataBase.execSQL("insert into favorites (_language, _book, _point) values (" + bp.get_language() + ", " + bp.get_book() + ", " + bp.get_point() + ")");
    	}	catch(SQLiteException e){
			Toast.makeText(myContext, myContext.getString(R.string.display_error), Toast.LENGTH_SHORT).show();
    	}
    	return;
    }

    public void removeFavorite(long id) {
    	try {
        	myDataBase.execSQL("delete from favorites where _id=" + id);
    	}	catch(SQLiteException e){
			Toast.makeText(myContext, myContext.getString(R.string.display_error), Toast.LENGTH_SHORT).show();
    	}
    	return;
    }
}
