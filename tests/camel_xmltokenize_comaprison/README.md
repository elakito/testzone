Apache Camel XML Tokenizer's performance comparisons
=================================================

This test shows how the xpath based tokenizer, the regex based classical tokenzier (xmltokenize)
and the newer StAX based xml tokenizer (xtokenize) perform with different input data.


Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Result with CAMEL 2.14-SNAPSHOT, i7 4-Core 2.3GHz, OSX 10.9
------------------------
See results/

| Sample | entries | |  xpath                | | xmltokenize           | | xtokenize             |
|        |         |Time[ms]|Rate[entries/ms]|Time[ms]|Rate[entries/ms]|Time[ms]|Rate[entries/ms]|
|--------|--------:|-------:|---------------:|-------:|---------------:|-------:|---------------:|
| rss    | 10000   | 151    | 66             | 517    | 19             | 74     | 135            |
| rss    | 100000  | NA     | NA             | 1072   | 93             | 194    | 515            |
| rss    | 1000000 | NA     | NA             | 10100  | 99             | 1998   | 500            |
| parts  | 10000   | 1322   | 7              | 3111   | 3              | 477    | 20             |
| parts  | 100000  | NA     | NA             | 30802  | 3              | 2087   | 47             |

Note the entry with NA indicates the test resulted in OutOfMemoryError.

