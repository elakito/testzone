Apache CXF CachedOutputStream's encryption performance comparisons
=================================================

This test shows how CachedOutputStream's encryption option
affects its IO throughput.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Results
------------------------
See results/ for more details

|  CipherName                | close  |Date 4096KB         |Data 16384KB        |
|----------------------------|--------|-------------------:|-------------------:|
|                            |        |time[ms]; rate[MB/s]|time[ms]; rate[MB/s]|
| none | False | 38; 107 | 69; 237 |
| DES/CFB/PKCS5Padding | True | 360; 11 | 956; 17 |
| DES/ECB/PKCS5Padding | True | 246; 16 | 898; 18 |
| DES/CFB8/NoPadding | False | 1767; 2 | 6745; 2 |
| DES/CTR/NoPadding | False | 258; 15 | 987; 16 |
| BLOWFISH/CFB/NoPadding | True | 242; 16 | 617; 26 |
| BLOWFISH/ECB/PKCS5Padding | True | 173; 23 | 578; 28 |
| BLOWFISH/CFB8/NoPadding | False | 1028; 3 | 4060; 4 |
| BLOWFISH/CTR/NoPadding | False | 170; 24 | 633; 25 |
| AES/CBC/PKCS5Padding | True | 75; 54 | 205; 79 |
| AES/ECB/PKCS5Padding | True | 148; 27 | 373; 43 |
| AES/CFB/NoPadding | True | 157; 26 | 426; 38 |
| AES/CFB8/NoPadding | False | 1188; 3 | 4755; 3 |
| AES/CTR/NoPadding | False | 114; 35 | 456; 35 |
| RC4 | False | 57; 71 | 197; 83 |

Each pair of numbers represents the total time[ms] taken to complete a write/read round trip and its throughput rate[MB/s].
close indicates whether the first read must wait until the cipher stream is closed.
All non 8-bit stream ciphers require the last block to be flushed before the first
read to occur, thus requiring the stream to be closed before the initial read to occur.


