Apache CXF CachedOutputStream's encryption performance comparisons
=================================================

This test shows how CachedOutputStream's encryption option
affects its IO througput.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Result with CXF 2.7.2, i7 2.3GHz, OSX
------------------------

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running me.temp.samples.cxf.cos.test.CachedOutputStreamComparisonTest
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
    ### WriteReadRoundTrip ###
    cipherName;terminate;size[KB];time[ms];rate[MB/s]
    none;false;4096;40;102
    DES/CFB/PKCS5Padding;false;4096;361;11
    DES/ECB/PKCS5Padding;false;4096;239;17
    DES/CFB8/NoPadding;false;4096;1716;2
    DES/CTR/NoPadding;false;4096;257;15
    BLOWFISH/CFB/NoPadding;false;4096;269;15
    BLOWFISH/ECB/PKCS5Padding;false;4096;174;23
    BLOWFISH/CFB8/NoPadding;false;4096;1018;4
    BLOWFISH/CTR/NoPadding;false;4096;167;24
    AES/CBC/PKCS5Padding;false;4096;72;56
    AES/ECB/PKCS5Padding;false;4096;150;27
    AES/CFB/NoPadding;false;4096;152;26
    AES/CFB8/NoPadding;false;4096;1193;3
    AES/CTR/NoPadding;false;4096;116;35
    RC4;false;4096;56;73
    none;false;16384;67;244
    DES/CFB/PKCS5Padding;false;16384;952;17
    DES/ECB/PKCS5Padding;false;16384;887;18
    DES/CFB8/NoPadding;false;16384;6753;2
    DES/CTR/NoPadding;false;16384;961;17
    BLOWFISH/CFB/NoPadding;false;16384;617;26
    BLOWFISH/ECB/PKCS5Padding;false;16384;575;28
    BLOWFISH/CFB8/NoPadding;false;16384;4199;3
    BLOWFISH/CTR/NoPadding;false;16384;630;26
    AES/CBC/PKCS5Padding;false;16384;214;76
    AES/ECB/PKCS5Padding;false;16384;369;44
    AES/CFB/NoPadding;false;16384;431;38
    AES/CFB8/NoPadding;false;16384;4756;3
    AES/CTR/NoPadding;false;16384;456;35
    RC4;false;16384;205;79
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
    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 28.876 sec
    
    Results :
    
    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
