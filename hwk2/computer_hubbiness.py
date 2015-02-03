# -*- coding: utf-8 -*-
"""
Created on Mon Nov 10 19:08:36 2014

@author: songyaos
"""
import numpy as np
#from fractions import Fraction
#hubbiness
L = np.matrix('0,1,1,1;1,0,0,1;1,0,0,0;0,1,1,0')
L1 = np.matrix('0,1,1,1,0;1,0,0,1,0;0,0,0,0,1;0,1,1,0,0;0,0,0,0,0')
#print L1
L_T = np.transpose(L)
L_T1 = np.transpose(L1)
#print L_T
h= np.matrix('1.0;1.0;1.0;1.0')
h1 = np.matrix('1.0;1.0;1.0;1.0;1.0')
#i=1
#while i<1000:
#    a = L_T1.dot(h1)/max(L_T1.dot(h1))
#    h1 = L1.dot(a)/max(L1.dot(a))
#    i+=1
    #break
i=1
while i<1000:
    a = L_T.dot(h)/max(L_T.dot(h))
    h = L.dot(a)/max(L.dot(a))
    i+=1    

print 'authority: \n',a  ,'\n','hub: \n',h