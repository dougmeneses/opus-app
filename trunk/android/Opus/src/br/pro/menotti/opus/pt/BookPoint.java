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

public class BookPoint {

	private String _title;

	protected long _id;
	protected long _language;
	protected long _book;
	protected long _point;
	protected String _text;

	public BookPoint() {
		
	}
	
	public BookPoint(long _language, long _book, long _point, String _text, String _title) {
		this._language = _language;
		this._book = _book;
		this._point = _point;		
		this._text = _text;
		this._title = _title;
	}

	public long get_id() {
		return _id;
	}


	public void set_id(long _id) {
		this._id = _id;
	}


	public long get_language() {
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


	public long get_point() {
		return _point;
	}


	public void set_point(long _point) {
		this._point = _point;
	}


	public String get_text() {
		return _text;
	}


	public void set_text(String _text) {
		this._text = _text;
	}


	public void set_title(String _title) {
		this._title = _title;
	}


	public String get_title() {
		return _title;
	}

	@Override
	public String toString() {
		return _title + " " + _point + ": " + _text;
	}
}
