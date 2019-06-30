# Loopover-Multi-Phase-God-s-Number
Calculates upper bound of God's number of a n x n Loopover board by extending a rectangle of solved tiles incrementally.

P.S. I know someone else has already implemented this same algorithm, since known upper bounds are at https://docs.google.com/spreadsheets/d/1gij270TVlA1sHmvvuyMXYKToGGpQKoBGbT2yYNwBe6I/edit#gid=0, but I couldn't find the code so I wrote my own version.

NOTE:
This algorithm gives a slightly weaker bound, since when it extends the rectangle of solved tiles it disallows any moves that temporarily break the rectangle.

ex. In extending a 2x2 to a 3x3, moving rows 1 and 2 and columns 1 and 2 are not allowed.
**-
**-
---

NOTE:
This version of the algorithm uses a modified Lehmer code for storing ordered subsets of integers and stores it in a 64-bit integer. Do not extend the rectangle by too many tiles, or else the algorithm risks integer overflow.
