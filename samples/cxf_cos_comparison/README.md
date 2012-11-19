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

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running me.temp.samples.cxf.cos.test.CachedOutputStreamComparisonTest
    ### Supported Ciphers ###
    cipherName
    DES/CFB/PKCS5Padding
    DES/ECB/PKCS5Padding
    DES/CFB8/NoPadding
    DES/CTR/NoPadding
    BLOWFISH/CFB/NoPadding
    BLOWFISH/ECB/PKCS5Padding
    BLOWFISH/CFB8/NoPadding
    BLOWFISH/CTR/NoPadding
    AES/CBC/PKCS5Padding
    AES/ECB/PKCS5Padding
    AES/CFB/NoPadding
    AES/CFB8/NoPadding
    AES/CTR/NoPadding
    RC4
    ### WriteReadRoundTrip ###
    cipherName;terminate;size[KB];time[ms];rate[MB/s]
    none;false;4096;40;102
    DES/CFB/PKCS5Padding;false;4096;573;7
    DES/ECB/PKCS5Padding;false;4096;398;10
    DES/CFB8/NoPadding;false;4096;2753;1
    DES/CTR/NoPadding;false;4096;424;9
    BLOWFISH/CFB/NoPadding;false;4096;321;12
    BLOWFISH/ECB/PKCS5Padding;false;4096;232;17
    BLOWFISH/CFB8/NoPadding;false;4096;1306;3
    BLOWFISH/CTR/NoPadding;false;4096;242;16
    AES/CBC/PKCS5Padding;false;4096;294;13
    AES/ECB/PKCS5Padding;false;4096;177;23
    AES/CFB/NoPadding;false;4096;256;16
    AES/CFB8/NoPadding;false;4096;2054;1
    AES/CTR/NoPadding;false;4096;204;20
    RC4;false;4096;96;42
    none;false;16384;150;109
    DES/CFB/PKCS5Padding;false;16384;1555;10
    DES/ECB/PKCS5Padding;false;16384;1477;11
    DES/CFB8/NoPadding;false;16384;10741;1
    DES/CTR/NoPadding;false;16384;1576;10
    BLOWFISH/CFB/NoPadding;false;16384;877;18
    BLOWFISH/ECB/PKCS5Padding;false;16384;806;20
    BLOWFISH/CFB8/NoPadding;false;16384;5296;3
    BLOWFISH/CTR/NoPadding;false;16384;921;17
    AES/CBC/PKCS5Padding;false;16384;732;22
    AES/ECB/PKCS5Padding;false;16384;1024;16
    AES/CFB/NoPadding;false;16384;769;21
    AES/CFB8/NoPadding;false;16384;8167;2
    AES/CTR/NoPadding;false;16384;785;20
    RC4;false;16384;384;42
    ### WriteReadChunk ###
    cipherName;terminate
    none;false
    DES/CFB/PKCS5Padding;true
    DES/ECB/PKCS5Padding;true
    DES/CFB8/NoPadding;false
    DES/CTR/NoPadding;false
    BLOWFISH/CFB/NoPadding;true
    BLOWFISH/ECB/PKCS5Padding;true
    BLOWFISH/CFB8/NoPadding;false
    BLOWFISH/CTR/NoPadding;false
    AES/CBC/PKCS5Padding;true
    AES/ECB/PKCS5Padding;true
    AES/CFB/NoPadding;true
    AES/CFB8/NoPadding;false
    AES/CTR/NoPadding;false
    RC4;false
    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 45.894 sec
    
    Results :
    
    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
