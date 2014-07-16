#!/usr/bin/env python

#
# genmdtable.py
# generates the markdown table from a result output file. The input is read
# from the stdin and each line starting with "json:" with two or four entries
#   [transform, close] or [transform, close, size, time] or 
#
# last update: 2014-07-16
#
# ay
#

import json
import sys

# 
#
#


def getrow(obj):
    "Calculate the row index"
    return ri

def getcol(obj):
    "Calculate the col index"
    if len(obj) == 4:
        if 16384 == obj[2]:
            return 3
        elif 4096 == obj[2]:
            return 2
    else:
        return 1

def makepair(t, r):
    "Create a time-rate pair"
    if t == 0:
        return "NA"
    else:
        return "%s; %s" % (t, r / t)

ri = 0
results = [[0] * 4 for i in range(15)]

for line in sys.stdin:
    if line.startswith("json:"):
        jsline = line[5:].rstrip()
        jsobj = json.loads(jsline)
        row = getrow(jsobj)
        col = getcol(jsobj)
        results[row][0] = jsobj[0]
        if len(jsobj) == 4:
            v = results[row][col]
            if v == 0 or jsobj[3] < v:
                results[row][col] = jsobj[3]
        else:
            results[row][col] = jsobj[1]

        ri = (ri + 1) % 15


print "|  CipherName                | close  |Date 4096KB         |Data 16384KB        |"
print "|----------------------------|--------|-------------------:|-------------------:|"
print "|                            |        |time[ms]; rate[MB/s]|time[ms]; rate[MB/s]|"
for item in results:
    print "| %s | %s | %s | %s |" % (item[0], item[1], makepair(item[2], 4096), makepair(item[3], 16384))



    
