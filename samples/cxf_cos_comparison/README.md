Apache CXF CachedOutputStream's encryption performance comparisons
=================================================

This test shows how CachedOutputStream's encryption option
affects its IO througput.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Result with CXF 2.7.1-SNAPSHOT, i5 M540 2.53GHz, Win7
------------------------
Running me.temp.samples.cxf.cos.test.CachedOutputStreamComparisonTest
enc: none; roundtrip: 4096KB
  time=105ms; 39MB/s
enc: none; roundtrip: 16384KB
  time=166ms; 98MB/s
enc: none; roundtrip: 65536KB
  time=524ms; 125MB/s
enc: DES/CFB8/NoPadding; roundtrip: 4096KB
  time=3206ms; 1MB/s
enc: DES/CFB8/NoPadding; roundtrip: 16384KB
  time=10661ms; 1MB/s
enc: BLOWFISH; roundtrip: 4096KB
  time=301ms; 13MB/s
enc: BLOWFISH; roundtrip: 16384KB
  time=781ms; 20MB/s
enc: BLOWFISH; roundtrip: 65536KB
  time=3174ms; 20MB/s
enc: ARCFOUR; roundtrip: 4096KB
  time=99ms; 41MB/s
enc: ARCFOUR; roundtrip: 16384KB
  time=348ms; 47MB/s
enc: ARCFOUR; roundtrip: 65536KB
  time=1444ms; 45MB/s
enc: RC4; roundtrip: 4096KB
  time=97ms; 42MB/s
enc: RC4; roundtrip: 16384KB
  time=357ms; 45MB/s
enc: RC4; roundtrip: 65536KB
  time=1473ms; 44MB/s
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 23.015 sec






