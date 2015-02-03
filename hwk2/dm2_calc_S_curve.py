import math
def s_curve(s,r,b):
    return 1-(1-s**r)**b

def s_threshold(r,b):
    return math.exp( 1.0/r * math.log( 1 - math.exp(1.0/b*math.log(0.5)) ) )

def s_est(r,b):
    return math.exp((1.0/r)*math.log(1.0/b))

result = [{(0.1*s,r,b):s_curve(0.1*s,r,b)} for s in range(1,10) for (r,b) in zip([3,6,5],[10,20,50])]
for item in result:
    print item
print
result2 = [ {(r,b):('exact:',s_threshold(r,b),'estimated:',s_est(r,b))} for (r,b) in zip([3,6,5],[10,20,50]) ] 
for item in result2:
    print item



