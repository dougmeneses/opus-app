#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on Oct 3, 2013
@author: menotti
'''

import sqlite3
import urllib
import lxml.html


languages = [(u"Português", False, "http://www.escrivaworks.org.br/book/{book}-ponto-{point}.htm", [("caminho", 999, 46), ("sulco", 1000, 32), ("forja", 1055, 13)], "http://www.escrivaworks.org.br/book/{book}-capitulo-{chapter}.htm"),
             (u"English", False, "http://www.escrivaworks.org/book/{book}-point-{point}.htm", [("the_way", 999, 46), ("furrow", 1000, 32), ("the_forge", 1055, 13)], "http://www.escrivaworks.org/book/{book}-chapter-{chapter}.htm"),
             (u"Español", False, "http://www.escrivaobras.org/book/{book}-punto-{point}.htm", [("camino", 999, 46), ("surco", 1000, 32), ("forja", 1055, 13)], "http://www.escrivaobras.org/book/{book}-capitulo-{chapter}.htm"),
             (u"Français", True, "http://fr.escrivaworks.org/book/{book}-point-{point}.htm", [("chemin", 999, 46), ("sillon", 1000, 32, False), ("forge", 1055, 14, False)], "http://fr.escrivaworks.org/book/{book}-chapitre-{chapter}.htm")]

conn = sqlite3.connect('opus_fr.db')

conn.execute('''CREATE TABLE android_metadata (locale TEXT);''');
conn.execute('''CREATE TABLE languages (_id INTEGER PRIMARY KEY NOT NULL, _name TEXT NOT NULL);''');
conn.execute('''CREATE TABLE books (_id INTEGER PRIMARY KEY NOT NULL, _language INT NOT NULL, _book INT NOT NULL, _title TEXT NOT NULL, _points INT NOT NULL);''');
conn.execute('''CREATE TABLE chapters (_id INTEGER PRIMARY KEY NOT NULL, _language INT NOT NULL, _book INT NOT NULL, _chapter INT NOT NULL, _title TEXT NOT NULL);''');
conn.execute('''CREATE TABLE chapter_points (_id INTEGER PRIMARY KEY NOT NULL, _language INT NOT NULL, _book INT NOT NULL, _chapter INT NOT NULL, _point INT NOT NULL);''');
conn.execute('''CREATE TABLE points (_id INTEGER PRIMARY KEY NOT NULL, _language INT NOT NULL, _book INT NOT NULL, _point INT NOT NULL, _text TEXT NOT NULL);''');

print "INSERT INTO android_metadata VALUES ('en_US');"
conn.execute("INSERT INTO android_metadata VALUES ('en_US');")

for lang in range(len(languages)):
    if languages[lang][1]:
        print "INSERT INTO languages VALUES (" + str(lang) + ", '" + languages[lang][0] + "');"
        conn.execute("INSERT INTO languages VALUES (?, ?);", (lang, languages[lang][0]))
        base_url = languages[lang][2]
        chap_url = languages[lang][4]
        for book in range(len(languages[lang][3])):
            max_points = languages[lang][3][book][1]
            max_chapters = languages[lang][3][book][2]
            book_url = base_url.replace("{book}", languages[lang][3][book][0]);
            book_chap_url = chap_url.replace("{book}", languages[lang][3][book][0]);
            print "INSERT INTO books (_language, _book, _title, _points) VALUES (" + str(lang) + ", " + str(book) + ", " + languages[lang][3][book][0].replace("_", " ").title() + ", " + str(max_points) + ");"
            conn.execute("INSERT INTO books (_language, _book, _title, _points) VALUES (?,?,?,?)", (lang, book, languages[lang][3][book][0].replace("_", " ").title(), max_points))
            for chapter in range(max_chapters+1):
                if chapter==0 and len(languages[lang][3][book])>3:
                    continue
                chap_book_url = book_chap_url.replace("{chapter}", str(chapter))
                content = urllib.urlopen(chap_book_url).read().decode("cp1252")
                doc = lxml.html.fromstring(content)
                for el in doc.find_class('path'):
                    if el.tag == 'a':
                        print "INSERT INTO chapters (_language, _book, _chapter, _title) VALUES (" + str(lang) + ", " + str(book) + ", " + str(chapter)  + ", " + el.text_content().lstrip() + ");"
                        conn.execute("INSERT INTO chapters (_language, _book, _chapter, _title) VALUES (?,?,?,?);", (lang, book, chapter, el.text_content().lstrip()))
                for el in doc.find_class('numero'):
                    print "INSERT INTO chapter_points (_language, _book, _chapter, _point) VALUES (" + str(lang) + ", " + str(book) + ", " + str(chapter)  + ", " + el.text_content() + ");"
                    conn.execute("INSERT INTO chapter_points (_language, _book, _chapter, _point) VALUES (?,?,?,?);", (lang, book, chapter, el.text_content()))
            for point in range(max_points+1):
                if point==0 and len(languages[lang][3][book])>3:
                    continue
                point_url = book_url.replace("{point}", str(point))
                content = urllib.urlopen(point_url).read().decode("cp1252")
                doc = lxml.html.fromstring(content)
                for el in doc.find_class('punto'):
                    print "INSERT INTO points (_language, _book, _point, _text) VALUES (" + str(lang) + ", " + str(book) + ", " + str(point)  + ", " + el[0].text_content() + ");"
                    conn.execute("INSERT INTO points (_language, _book, _point, _text) VALUES (?,?,?,?);", (lang, book, point, el[0].text_content()))
        conn.commit()
conn.close()
