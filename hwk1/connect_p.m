function [] = connect_p( x,y_array )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
for i=1:size(y_array,1)
    y = y_array(i,:);
    line([x(1),y(1)],[x(2),y(2)]);
end

end

