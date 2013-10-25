#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on Oct 3, 2013
@author: menotti
'''

import urllib, lxml.html, sqlite3

languages = [(u"Português", True, "http://www.escrivaworks.org.br/book/{book}-ponto-{point}.htm", [("caminho", 999), ("sulco", 1000), ("forja", 1055)]),
             (u"English", False, "http://www.escrivaworks.org/book/{book}-point-{point}.htm", [("the_way", 999), ("furrow", 1000), ("the_forge", 1055)]),
             (u"Español", False, "http://www.escrivaobras.org/book/{book}-punto-{point}.htm", [("camino", 999), ("surco", 1000), ("forja", 1055)])]

conn = sqlite3.connect('opus.db')

conn.execute('''CREATE TABLE android_metadata (locale TEXT);''');
conn.execute('''CREATE TABLE languages (_id INTEGER PRIMARY KEY NOT NULL, _name TEXT NOT NULL);''');
conn.execute('''CREATE TABLE books (_id INTEGER PRIMARY KEY NOT NULL, _language INT NOT NULL, _book INT NOT NULL, _title TEXT NOT NULL, _points INT NOT NULL);''');
conn.execute('''CREATE TABLE points (_id INTEGER PRIMARY KEY NOT NULL, _language INT NOT NULL, _book INT NOT NULL, _point INT NOT NULL, _text TEXT NOT NULL);''');

print "INSERT INTO android_metadata VALUES ('en_US');"
conn.execute("INSERT INTO android_metadata VALUES ('en_US');")

for lang in range(len(languages)):
    if languages[lang][1]:
        print "INSERT INTO languages VALUES (" + str(lang) + ", '" + languages[lang][0] + "');"
        conn.execute("INSERT INTO languages VALUES (?, ?);", (lang, languages[lang][0]))
        base_url = languages[lang][2]
        for book in range(len(languages[lang][3])):
            max_points = languages[lang][3][book][1]
            book_url = base_url.replace("{book}", languages[lang][3][book][0]);
            print "INSERT INTO books (_language, _book, _title, _points) VALUES (" + str(lang) + ", " + str(book) + ", " + languages[lang][3][book][0].replace("_", " ").title() + ", " + str(max_points) + ");"
            conn.execute("INSERT INTO books (_language, _book, _title, _points) VALUES (?,?,?,?)", (lang, book, languages[lang][3][book][0].replace("_", " ").title(), max_points))
            for point in range(max_points+1):
                point_url = book_url.replace("{point}", str(point))
                content = urllib.urlopen(point_url).read().decode("ISO-8859-1")
                doc = lxml.html.fromstring(content)
                for el in doc.find_class('punto'):
                    print "INSERT INTO points (_language, _book, _point, _text) VALUES (" + str(lang) + ", " + str(book) + ", " + str(point)  + ", " + el[0].text_content() + ");"
                    conn.execute("INSERT INTO points (_language, _book, _point, _text) VALUES (?,?,?,?);", (lang, book, point, el[0].text_content()))
        conn.commit()
conn.close()
