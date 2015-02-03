import numpy as np
from fractions import Fraction

#basic page rank 
v0 = [1.0/4 for _ in range(4)]
v0 = np.array([[0.25, 0.25, 0.25, 0.25]])
v0 = np.transpose(v0)
M = np.array([[0,1.0/3,1.0/3,1.0/3],[1.0/2,0,0,1.0/2],[1,0,0,0],[0,1.0/2,1.0/2,0]])
M = np.transpose(M)
print v0
v = M.dot(v0)
while not np.array_equal(v , M.dot(v)):
    #print v
    v = M.dot(v)
r = v    #page rank
print 'page rank: ','\n',r,'\n',[Fraction(item[0]).limit_denominator() for item in r]
#topic sensitive page rank
beta = 0.8
e_s = np.array([[0,1,0,0]])
v_s = (1-beta) *e_s/np.sum(e_s)
v_s = np.transpose(v_s)
M_b = beta*M
v = np.array([[0, 0.5, 0, 0.5]])
v =  np.transpose(v)
while not np.array_equal(v , M_b.dot(v) + v_s):
    #print v
    v = M_b.dot(v) + v_s
v = v.tolist()
print 'trust rank: ','\n',[Fraction(item[0]).limit_denominator() for item in v]
t=v #trusted rank

spam_mass = (r-t)/r
print 'spam mass: ','\n',[Fraction(item[0]).limit_denominator() for item in spam_mass],'\n',spam_mass