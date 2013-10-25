
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

public class Book {

	private long _id;
	private long _language;
	private long _book;
	private String _title;
	private long _points;

	
	public Book() {
	}
	
	
	public Book(long _language, long _book, long _points, String _title) {
		this._language = _language;
		this._book = _book;
		this._points = _points;		
		this._title = _title;
	}

	
	public long get_id() {
		return _id;
	}


	public void set_id(long _id) {
		this._id = _id;
	}


	public long get_idioma() {
		return _language;
	}


	public void set_language(long _language) {
		this._language = _language;
	}


	public long get_book() {
		return _book;
	}


	public void set_book(long _book) {
		this._book = _book;
	}


	public long get_points() {
		return _points;
	}


	public void set_points(long _points) {
		this._points = _points;
	}


	public String get_title() {
		return _title;
	}


	public void set_title(String _title) {
		this._title = _title;
	}
	

	@Override
	public String toString() {
		return _title;
	}
}
