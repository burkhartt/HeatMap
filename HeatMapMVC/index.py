#!/usr/bin/python

import csv
import MySQLdb
import json

#airport_file = open("MAR.csv",'r')
#for i,l in enumerate(airport_file):
#  pass
#file_len=i+1
#
#print file

def index(mo_str):
  #my_reader = csv.reader(airport_file,delimiter=',')
  db=MySQLdb.connect(user='guest123',passwd='guest123',db='traveldata')
  c=db.cursor()

  ex_str="select count(ticketid) as NumberOfTravelers, code,latitude,longitude from tickets inner join airports on airports.code=tickets.dest where arr_date<adddate('2011-" + mo_str + "-01',interval 1 month) and arr_date>='2011-" + mo_str + "-01' group by code"

  c.execute(ex_str)
  results = c.fetchall()
  
  for row in results:
    d = collections.OrderedDict()
    d['NumberOfTravelers'] = row.NumberOfTravelers
    d['DestinationLat'] = row.latitude
    d['DestinationLong'] = row.longitude    
    objects_list.append(d)

  return json.dumps(objects_list)
  
