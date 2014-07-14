Apache CXF CachedOutputStream's encryption performance comparisons
=================================================

This test shows how CachedOutputStream's encryption option
affects its IO throughput.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Result with CXF 2.7.2, i7 2.3GHz, OSX
------------------------
See results/

|  CipherName                | close  |Data 4096KB [time;rate]|Data 16384KB [time;rate]|
|----------------------------|--------|--------------:|---------------:|
| none                       | false  | 38; 107       | 67; 244        |
| DES/CFB/PKCS5Padding       | true   | 360; 11       | 1024; 16       |
| DES/ECB/PKCS5Padding       | true   | 249; 16       | 905; 16        |
| DES/CFB8/NoPadding         | false  | 1767; 2       | 7150; 2        |
| DES/CTR/NoPadding          | false  | 261; 15       | 1009; 16       |
| BLOWFISH/CFB/NoPadding     | true   | 270; 15       | 638; 25        |
| BLOWFISH/ECB/PKCS5Padding  | true   | 173; 23       | 593; 27        |
| BLOWFISH/CFB8/NoPadding    | false  | 1060; 3       | 4197; 3        |
| BLOWFISH/CTR/NoPadding     | false  | 175; 23       | 646; 25        |
| AES/CBC/PKCS5Padding       | true   | 75; 54        | 217; 75        |
| AES/ECB/PKCS5Padding       | true   | 155; 26       | 387; 42        |
| AES/CFB/NoPadding          | true   | 163; 25       | 426; 38        |
| AES/CFB8/NoPadding         | false  | 1188; 3       | 4811; 3        |
| AES/CTR/NoPadding          | false  | 121; 33       | 465; 35        |
| RC4                        | false  | 58; 70        | 198; 82        |

Each pair of numbers represents the total time[ms] and throughput rate[MB/s].
close indicates whether the first read must wait until the cipher stream is closed.
All non 8-bit stream ciphers require the last block to be flushed before the first
read to occur, thus requiring the stream to be closed before the initial read to occur.


