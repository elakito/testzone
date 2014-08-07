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

| Sample | tokens  | wrap | xpath                   | xmltokenize             | xmlpairtokenize         | xtokenize               |
|--------|--------:|:----:|------------------------:|------------------------:|------------------------:|------------------------:|
|        |         |      |time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|
| rss | 10000 | x | 350; 28 | 439; 22 | 76; 131 | 65; 153 |
| rss | 10000 | o | NA | 100; 100 | NA | 20; 500 |
| rss | 100000 | x | OOM | 1120; 89 | 644; 155 | 191; 523 |
| rss | 100000 | o | NA | 994; 100 | NA | 199; 502 |
| rss | 1000000 | x | OOM | 9962; 100 | 6393; 156 | 1914; 522 |
| rss | 1000000 | o | NA | 9966; 100 | NA | 1964; 509 |
| parts | 10000 | x | 3724; 2 | 3117; 3 | 1199; 8 | 298; 33 |
| parts | 10000 | o | NA | 3121; 3 | NA | 446; 22 |
| parts | 100000 | x | OOM | 31062; 3 | 11052; 9 | 1984; 50 |
| parts | 100000 | o | NA | 31214; 3 | NA | 2172; 46 |

Each pair of numbers represents the total time[ms] taken to complete the tokenization and its throughput rate[tokens/ms], where OOM indicates the corresponding test resulted in OutOfMemroyError and NA indicates that the feature is not available.

The tests used two types of messages with various number of entries and were executed three times and each entry in the above table was taken from its best result. Sample rss is a rss feed xml file containing a series of rss item elements (about 200 characters each) that are tokenized. Sample parts is an xml file containing a series of Part elements (about 3000 characters each) that are tokenized.

The results show that the xpath based tokenizer can only be used in small data. The regex based tokenizers do not suffer
the memory issue, however, they are slow due to the complex regex processing required to recognize the basic xml structures. Between them, the original xmlpairtokeinze is a few factor faster than the xmltokenize. This difference appears to come from the fact that the regex used in the original xmlpairtokenize is a simpler regex that can only handle the normal start and end tags. In contrast, the regex used in the xmltokenize is a more complex regex that can also handle the self-closing tags. It is noted that these two regex based tokenizers have some inherent limitations in recognizing some xml structures and artifacts. The new StAX based xtokenize does not suffer from these limitations and appears to perform significantly better than the other tokenizers.
