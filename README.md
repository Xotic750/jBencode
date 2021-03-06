[![Build Status](https://travis-ci.org/Xotic750/jBencode.svg?branch=master)](https://travis-ci.org/Xotic750/jBencode)
[![Coverage Status](https://coveralls.io/repos/github/Xotic750/jBencode/badge.svg?branch=master)](https://coveralls.io/github/Xotic750/jBencode?branch=master)

**Bencode** (pronounced like B encode) is the encoding used by the peer-to-peer file sharing system BitTorrent for storing and transmitting loosely structured data.

It supports four different types of values:

 1. byte strings
 2. integers
 3. lists
 4. dictionaries (associative arrays)

Bencoding is most commonly used in torrent files. These metadata files are simply bencoded dictionaries.

While less efficient than a pure binary encoding,[citation needed] bencoding is simple and (because numbers are encoded as text in decimal notation) is unaffected by endianness, which is important for a cross-platform application like BitTorrent. It is also fairly flexible, as long as applications ignore unexpected dictionary keys, so that new ones can be added without creating incompatibilities.

Encoding algorithm
------------------
Bencode uses ASCII characters as delimiters and digits.

An integer is encoded as **i< integer encoded in base ten ASCII >e**. Leading zeros are not allowed (although the number zero is still represented as "0"). Negative values are encoded by prefixing the number with a minus sign. The number 42 would thus be encoded as **i42e**, 0 as **i0e**, and -42 as **i-42e**. Negative zero is not permitted.

A byte string (a sequence of bytes, not necessarily characters) is encoded as **< length >:< contents >**. The length is encoded in base 10, like integers, but must be non-negative (zero is allowed); the contents are just the bytes that make up the string. The string "spam" would be encoded as **4:spam**. The specification does not deal with encoding of characters outside the ASCII set; to mitigate this, some BitTorrent applications explicitly communicate the encoding (most commonly UTF-8) in various non-standard ways. This is identical to how netstrings work, except that netstrings additionally append a comma suffix after the byte sequence.

A list of values is encoded as **l< contents >e** . The contents consist of the bencoded elements of the list, in order, concatenated. A list consisting of the string "spam" and the number 42 would be encoded as: **l4:spami42ee**. Note the absence of separators between elements.

A dictionary is encoded as **d< contents >e**. The elements of the dictionary are encoded each key immediately followed by its value. All keys must be byte strings and must appear in lexicographical order. A dictionary that associates the values 42 and "spam" with the keys "foo" and "bar", respectively (in other words, {"bar": "spam", "foo": 42}}), would be encoded as follows: **d3:bar4:spam3:fooi42ee**. (This might be easier to read by inserting some spaces: d 3:bar 4:spam 3:foo i42e e.)

There are no restrictions on what kind of values may be stored in lists and dictionaries; they may (and usually do) contain other lists and dictionaries. This allows for arbitrarily complex data structures to be encoded.
