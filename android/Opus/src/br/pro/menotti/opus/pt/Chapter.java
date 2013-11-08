
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

public class Chapter {

	private long _id;
	private long _language;
	private long _book;
	private long _chapter;
	private String _title;
	
	public Chapter() {
	}
	
	public Chapter(long _language, long _book, long _points, long _chapter, String _title) {
		this._language = _language;
		this._book = _book;
		this._chapter = _chapter;
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

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}
	
	public long get_chapter() {
		return _chapter;
	}

	public void set_chapter(long _chapter) {
		this._chapter = _chapter;
	}
	
	@Override
	public String toString() {
		return _chapter + " - " + _title;
	}
}
