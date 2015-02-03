# -*- coding: utf-8 -*-
"""
Created on Wed Nov  5 08:27:02 2014

@author: mengyiyi
"""
import numpy as np

def cosangle(x,y):
    return np.dot(x,y)/np.linalg.norm(x)/np.linalg.norm(y)

alpha = 3.0/(500+320+640) #1#0.01
beta = 3.0/16#1#0.5
a = np.array([3.06, alpha*500, beta*6])
b = np.array([2.68, alpha*320, beta*4])
c = np.array([2.92, alpha*640, beta*6])
print a,b,c
print 'cosine angles between vectors: \n',cosangle(a,b),cosangle(a,c),cosangle(b,c)

print '***************************************'
print 3.06**2, 2.68**2, 2.92**2
print 3.06*2.68, 3.06*2.92, 2.68*2.92
print 320**2, 640**2
print 320*500, 500*640, 320*640
print '***************************************'
norma = np.sqrt(9.3636 + 250000*alpha**2 + 36*beta**2)
normb = np.sqrt(7.1824 + 102400*alpha**2 + 16*beta**2)
normc = np.sqrt(8.5264 + 409600*alpha**2 + 36*beta**2)
dotab = 8.2008 + 160000*alpha**2 + 24*beta**2 
dotac = 8.9352 + 320000*alpha**2 + 36*beta**2 
dotbc = 7.8256 + 204800*alpha**2 + 24*beta**2 
print dotab/norma/normb, dotac/norma/normc, dotbc/normb/normc