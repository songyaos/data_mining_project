def compute_mm(N,M):
    tri_method = 16*10**6 + 12*M
    matrix_method =  4*10**6 + 2*N*N
    return min(matrix_method,tri_method)


mylist = [(50000,40000000),(30000,100000000),(100000,100000000),(50000,80000000)]

for item in mylist:
    print compute_mm(item[0],item[1])
    

    
    
