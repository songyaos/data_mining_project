#!/usr/bin/env python
import sys

myarg = sys.argv[1]
#arg2 = sys.argv[2]

for line in sys.stdin:
	line = line.strip()
	first, last = line.split()
	if first == myarg:
		print '%s \t %s \t 1' % (first, last)