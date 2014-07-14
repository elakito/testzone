Apache Camel XML Tokenizer's performance comparisons
=================================================

This test shows how the xpath based tokenizer, the regex based classical tokenzier (xmltokenize)
and the newer StAX based xml tokenizer (xtokenize) perform with different input data.


Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Results
------------------------
See results/ for more details

| Sample | tokens  | xpath                    | xmltokenize              | xtokenize                |
|--------|--------:|-------------------------:|-------------------------:|-------------------------:|
|        |         |time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|time[ms]; rate[tokens/ms]|
| rss    | 10000   | 151; 66            | 517; 19                 | 74; 135              |
| rss    | 100000  | NA                 | 1072; 93                | 194; 515             |
| rss    | 1000000 | NA                 | 10100; 99               | 1998; 500            |
| parts  | 10000   | 1322; 7            | 3111; 3                 | 477; 20              |
| parts  | 100000  | NA                 | 30802; 3                | 2087; 47             |

Each pair of numbers represents the total time[ms] taken to complete the tokenization and its throughput rate[tokens/ms], where NA indicates the corresponding test resulted in OutOfMemroyError.
The tests used two types of messages with various number of entries.


