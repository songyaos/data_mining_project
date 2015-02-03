# -*- coding: utf-8 -*-
"""
Created on Tue Oct  7 19:33:03 2014

@author: mengyiyi

"""
import math
z= 1.15

def ucf(e,n):
    p = float(e)/n
    return (1.0/(1.0+1.0/n*(z*z))) * (p + 1.0/2/n*(z*z) + z*math.sqrt(1.0/n*p*(1.0-p) +1.0/4/n/n*(z*z) ))
    
#print ucf(11,123)

print 'adoption of budget resolution =n:'
print 'original subtree error: ',1*ucf(0,1)+ 9*ucf(0,9) + 6*ucf(0,6)
print 'replaced leaf error: ',16*ucf(1,16)
print
print 'physician fee freeze=n:'
print 'original subtree error: ', 16*ucf(1,16) + 1*ucf(0,1) + 151*ucf(0,151)
print 'replaced leaf error: ', 168*ucf(1,168)
print
print 'duty free exports = n: '
print 'original subtree error: ', 5*ucf(2,5)+ 13*ucf(2,13)+ 1*ucf(0,1)
print 'replaced leaf error: ', 19*ucf(6,19)
print
print 'synfuels corporation cutback =y:'
print 'original subtree error: ', 5*ucf(2,5)+ 13*ucf(2,13)+ 1*ucf(0,1) + 2*ucf(0,2)+1*ucf(0,1)
print 'replaced leaf error: ', 22*ucf(8,22)
print
print 'physician freeze=y:'
print 'original subtree error: ', 5*ucf(2,5)+ 13*ucf(2,13)+ 1*ucf(0,1) + 2*ucf(0,2)+1*ucf(0,1)+97*ucf(3,97)+4*ucf(0,4)
print 'replaced leaf error: ', 123*ucf(11,123)
print
print 'water project cost sharing =u:'
print 'original subtree error: ', 3*ucf(1,3) + 2*ucf(0,2)
print 'replaced leaf error: ', 5*ucf(2,5)
print
print 'physician freeze = u:'
print 'original subtree error: ', 4*ucf(0,4)+3*ucf(1,3) + 2*ucf(0,2)
print 'replaced leaf error: ', 9*ucf(3,9)



