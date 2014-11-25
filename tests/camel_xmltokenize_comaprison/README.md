Apache Camel XML Tokenizer's performance comparisons
=================================================

This test shows how the xpath based tokenizer, the regex based classical tokenziers (xmltokenize and xmlpairtokenize)
and the newer StAX based xml tokenizer (xtokenize) perform with different input data.


Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Results
------------------------
See results/ for more details
| Sample | tokens  | wrap | xpath             | xmltokenize       | xmlpairtokenize   | xtokenize         |
|--------|--------:|:----:|------------------:|------------------:|------------------:|------------------:|
|        |         |      |t[ms]; r[tokens/ms]|t[ms]; r[tokens/ms]|t[ms]; r[tokens/ms]|t[ms]; r[tokens/ms]|
| rss    |   10000 | x    |           344; 29 |           474; 21 |           75; 133 |           64; 156 |
| rss    |   10000 | o    |                NA |           99; 101 |                NA |           22; 454 |
| rss    |  100000 | x    |                NA |          1052; 95 |          654; 152 |          185; 540 |
| rss    |  100000 | o    |                NA |          986; 101 |                NA |          201; 497 |
| rss    | 1000000 | x    |                NA |         9858; 101 |         6354; 157 |         1873; 533 |
| rss    | 1000000 | o    |                NA |         9710; 102 |                NA |         1925; 519 |
| parts  |   10000 | x    |           3557; 2 |           3096; 3 |           1169; 8 |           282; 35 |
| parts  |   10000 | o    |                NA |           3113; 3 |                NA |           437; 22 |
| parts  |  100000 | x    |                NA |          31165; 3 |          10702; 9 |          1970; 50 |
| parts  |  100000 | o    |                NA |          30838; 3 |                NA |          2047; 48 |
| parts  | 1000000 | x    |                NA |         306014; 3 |         109864; 9 |         19819; 50 |
| parts  | 1000000 | o    |                NA |         307699; 3 |                NA |         19812; 50 |

Each pair of numbers represents the total time[ms] taken to complete the tokenization and its throughput rate[tokens/ms], where OOM indicates the corresponding test resulted in OutOfMemroyError and NA indicates that the feature is not available.

The tests used two types of messages with various number of entries and were executed three times and each entry in the above table was taken from its best result. Sample rss is a rss feed xml file containing a series of rss item elements (about 200 characters each, corresponding to input data of size 1.7MB, 17MB, and 170MB, respectively) that are tokenized. Sample parts is an xml file containing a series of Part elements (about 3000 characters each, corresponding to input data of size 25MB, 250MB, and 2.5GB, respectively) that are tokenized.

The results show that the xpath based tokenizer can only be used in small data. The regex based tokenizers do not suffer
the memory issue, however, they are slow due to the complex regex processing required to recognize the basic xml structures. Between them, the original xmlpairtokeinze is a few factor faster than the xmltokenize. This difference appears to come from the fact that the regex used in the original xmlpairtokenize is a simpler regex that can only handle the normal start and end tags. In contrast, the regex used in the xmltokenize is a more complex regex that can also handle the self-closing tags. It is noted that these two regex based tokenizers have some inherent limitations in recognizing some xml structures and artifacts. The new StAX based xtokenize does not suffer from these limitations and appears to perform significantly better than the other tokenizers.
