# -*- coding: utf-8 -*-
"""
Created on Mon Nov 24 19:05:31 2014
compute similarity

@author: mengyiyi
"""
import numpy as np
import math

def simia(x,y):
    pset = [xx*yy for xx,yy in zip(x,y)]
    xp = [item for index,item in zip(pset,x) if index is not 0]
    xbar = float(sum(xp))/len(xp)
    xp = np.asarray(xp)
    yp = [item for index,item in zip(pset,y) if index is not 0]
    ybar = float(sum(yp))/len(yp)
    yp = np.asarray(yp)
    numerator = np.dot(xp-xbar,yp-ybar)
    denominator = np.sqrt(np.dot(xp-xbar,xp-xbar)) * np.sqrt(np.dot(yp-ybar,yp-ybar))
    print
    print xp,yp, xp-xbar,yp-ybar, numerator, denominator
    print
    return numerator/denominator

def simi(x,y):
    xp=x
    yp=y
    xp = np.asarray(xp)
    yp = np.asarray(yp)
    numerator = np.dot(xp,yp)
    denominator = np.sqrt(np.dot(xp,xp)) * np.sqrt(np.dot(yp,yp))
    return numerator/denominator


#alice = [5,0,4,4,0]
#u1 = [3,2,3,0,3]
#u2 = [4,0,4,3,5]
#u3 = [4,3,4,4,0]
#u4 = [1,5,0,2,1]

alice = [5,0,4,4]
u1 = [3,2,3,0]
u2 = [4,0,4,3]
u3 = [4,3,4,4]
u4 = [1,5,0,2]
#test case in ppt slide 26
a1 = [5,3,4,4,0]
t1=[3,1,2,3,3]
t2=[4,3,4,3,5]
t3=[3,3,1,5,4]
t4=[1,5,5,2,1]
a= [4,0,0,5,1,0,0]
b=[5,5,4,0,0,0,0]
c=[0,0,0,2,4,5,0]
d=[0,3,0,0,0,0,3]
rating = [4,2,5]
f1 = [3.06,2.68,2.92]
f2= [500,320,640]
f3=  [6,4,6]
#print simi(a1,t1),simi(a1,t2),simi(a1,t4),simi(a,b),simi(a,c)
print simi(alice,u1),simi(alice,u2),simi(alice,u3),simi(alice,u4)
print 'cf recursive:'
print simi(u3,u1),simi(u3,u2),simi(u3,u4)
print (4+3+4+4)/4.0 + (0.71*(3-11.0/4) + 0.72*(5-16.0/4) )/(0.71+0.72)
print '#################'
print math.acos(0.99088)/math.pi*180, math.acos(0.99155)/math.pi*180,math.acos(0.96177)/math.pi*180
print math.acos(0.99439)/math.pi*180, math.acos(0.99561)/math.pi*180,math.acos(0.98224)/math.pi*180
print math.acos(0.999997)/math.pi*180, math.acos(0.999995)/math.pi*180,math.acos(0.999987)/math.pi*180
