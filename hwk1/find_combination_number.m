%find all combinations of numbers from 1 to 10 whose product is below 100;
data = 1:10;
limit = 100;
for i=1:10
    if i < limit;
        disp(i);
        dlmwrite('mynumber.txt',[i],'delimiter','\t','precision',3);
    end
end

for i=1:10
    for j=i+1:10
        if i*j < limit;
            disp([i,j]);
            dlmwrite('mynumber.txt',[i,j],'delimiter','\t','precision',3,'-append');
        end
    end
end   

for i=1:10
    for j=i+1:10
        for k = j+1:10
            if i*j*k < limit;
                disp([i,j,k]);
                dlmwrite('mynumber.txt',[i,j,k],'delimiter','\t','precision',3,'-append');
            end
        end
    end
end   

for i=1:10
    for j=i+1:10
        for k = j+1:10
            for l = k+1:10
                if i*j*k*l < limit;
                    disp([i,j,k,l]);
                    dlmwrite('mynumber.txt',[i,j,k,l],'delimiter','\t','precision',3,'-append');
                end
            end
        end
    end
end   
