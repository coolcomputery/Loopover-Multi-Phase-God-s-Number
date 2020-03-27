# Loopover-Multi-Phase-God-s-Number
**OFFICIALLY DEPRECATED:** Use the LoopoverBFS class at https://github.com/coolcomputery/Loopover-Brute-Force-and-Improvement instead.

Calculates upper bound of God's number of a n x n Loopover board by extending a rectangle of solved tiles incrementally.

P.S. I know someone else has already implemented this same algorithm, since known upper bounds are at https://docs.google.com/spreadsheets/d/1gij270TVlA1sHmvvuyMXYKToGGpQKoBGbT2yYNwBe6I/edit#gid=0, but I couldn't find the code so I wrote my own version.

NOTE:
This algorithm gives a slightly weaker bound, since when it extends the rectangle of solved tiles it disallows any moves that temporarily break the rectangle.

ex. In extending a 3x4 to a 4x4 (row amount first), moving rows 1 to 3 and columns 1 to 4 are not allowed.

NOTE:
This version of the algorithm uses a modified Lehmer code for storing ordered subsets of integers and stores it in a 64-bit integer. Do not extend the rectangle by too many tiles, or else the algorithm risks integer overflow.
