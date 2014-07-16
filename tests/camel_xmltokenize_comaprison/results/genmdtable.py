#!/usr/bin/env python

#
# genmdtable.py
# generates the markdown table from a result output file. The input is read
# from the stdin and each line starting with "json:" with five entries
#   [language, wrap, test, repeat, performance]
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
    n = 2 * (len(str(obj[3])) - 5)
    if "parts" == obj[2]:
        n += 6
    if "o" == obj[1]:
        n += 1
    return n

def getcol(obj):
    "Calculate the row index"
    if "xpath" == obj[0]:
        return 3
    elif "xmlTokenize" == obj[0]:
        return 4
    elif "xmlPairTokenize" == obj[0]:
        return 5
    elif "xtokenize" == obj[0]:
        return 6

def makepair(t, r):
    "Create a time-rate pair"
    if t == 0:
        return "NA"
    else:
        return "%s; %s" % (t, r / t)


results = [["rss", 10000, "x", 0, 0, 0, 0], 
           ["rss", 10000, "o", 0, 0, 0, 0], 
           ["rss", 100000, "x", 0, 0, 0, 0], 
           ["rss", 100000, "o", 0, 0, 0, 0], 
           ["rss", 1000000, "x", 0, 0, 0, 0], 
           ["rss", 1000000, "o", 0, 0, 0, 0], 
           ["parts", 10000, "x", 0, 0, 0, 0], 
           ["parts", 10000, "o", 0, 0, 0, 0], 
           ["parts", 100000, "x", 0, 0, 0, 0], 
           ["parts", 100000, "o", 0, 0, 0, 0]]

for line in sys.stdin:
    if line.startswith("json:"):
        jsline = line[5:].rstrip()
        jsobj = json.loads(jsline)
        row = getrow(jsobj)
        col = getcol(jsobj)
        v = results[row][col]
        if v == 0 or jsobj[4] < v:
            results[row][col] = jsobj[4]


print "| Sample | tokens  | wrap | xpath                   | xmltokenize             | xmlpairtokenize         | xtokenize               |"
print "|--------|--------:|:----:|------------------------:|------------------------:|------------------------:|------------------------:|"
print "|        |         |      |time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|"
for item in results:
    print "| %s | %d | %s | %s | %s | %s | %s |" % (item[0], item[1], item[2], makepair(item[3], item[1]), makepair(item[4], item[1]), makepair(item[5], item[1]), makepair(item[6], item[1]))



    
