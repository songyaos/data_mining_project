# -*- coding: utf-8 -*-
"""
Created on Tue Oct  7 18:33:13 2014

@author: mengyiyi
"""

import pprint
from itertools import chain
from collections import defaultdict
data = [(1,2,3),(2,3,4),(3,4,5),(4,5,6),(1,3,5),(2,4,6),(1,3,4),(2,4,5),(3,5,6),(1,2,4),(2,3,5),(3,4,6)]
data_chain = list(chain(*data))

singleitem =[data_chain.count(i+1) for i in range(6)]
print singleitem,len(singleitem)
pair =[(i,j) for i in range(1,7) for j in range(i+1,7)]
print pair,len(pair)

support_pair_list = defaultdict(int)
for item in pair:
    for tuple_item in data:
        if item[0] in tuple_item and item[1] in tuple_item:
            support_pair_list[item]+=1
for key, value in support_pair_list.items():
    print key,value

bucket_count = defaultdict(int)
bucket_pair = defaultdict(str)
for item in pair:
    bucket_count[item[0]*item[1]%11] += support_pair_list[item]
    bucket_pair[item[0]*item[1]%11] +=str(item) +'+'
for item1,item2 in zip(bucket_count.items(),bucket_pair.items()):
    print item1[0],':',item1[1],item2[1]

second_list = []    
for item in pair:
    if bucket_count[item[0]*item[1]%11] >= 4:
        second_list.append(item)
        
print second_list

