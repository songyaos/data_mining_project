%k means algorithm
%call dist2 to calculate distance btn points
clear all; close all;
data = [2,10; 2,5;8,4;5,8;7,5;6,4;1,2;4,9];
centroid  =[2,10;5,8;1,2];%initial centroid
distance_matrix = zeros(8,3);
iter= 1;
iter_max = 6;
cluster_matrix  = zeros(iter_max,8);
centroid_matrix  = zeros(iter_max,6); % 3 centroids
while iter<= iter_max
  for point=1:8
      for center=1:3
          distance_matrix(point,center) = dist2(data(point,:), centroid(center,:));
      end
  end
  %dlmwrite('myFile.txt',distance_matrix,'delimiter','\t','precision',3,'-append');
  [m_value,m_index] = min(distance_matrix,[],2);
  distance_matrix
  %figure;hold on;
%   scatter(data(:,1),data(:,2),'*');
%   cluster1 = [centroid(1,:)'  data(m_index ==1,:)'];
%   cluster2 = [centroid(2,:)'  data(m_index ==2,:)'];
%   cluster3 = [centroid(3,:)'  data(m_index ==3,:)'];
  figure;hold on;
  scatter(data(:,1),data(:,2),'*');
  scatter(centroid(1,1),centroid(1,2),'ro');
  scatter(centroid(2,1),centroid(2,2),'ro');
  scatter(centroid(3,1),centroid(3,2),'ro');
  connect_p(centroid(1,:),data(m_index ==1,:));
  connect_p(centroid(2,:),data(m_index ==2,:));
  connect_p(centroid(3,:),data(m_index ==3,:));
%   plot(cluster1(1,:),cluster1(2,:),'-*');
%   plot(cluster2(1,:),cluster2(2,:),'-x');
%   plot(cluster3(1,:),cluster3(2,:),'-o');
%   plot circle  
%   radius = [max(min(distance_matrix(m_index ==1,:),[],2)); ...
%       max(min(distance_matrix(m_index ==2,:),[],2));...
%       max(min(distance_matrix(m_index ==3,:),[],2))];
%   viscircles(centroid, radius);
  
  
  cluster_matrix(iter,:) = m_index;%assign cluster
  %update centroid for each cluster
  centroid = [mean(data(m_index ==1,:),1); mean(data(m_index ==2,:),1); mean(data(m_index ==3,:),1)];
  centroid_matrix(iter,:,:) =[mean(data(m_index ==1,:),1) mean(data(m_index ==2,:),1) mean(data(m_index ==3,:),1)]; % 3 centroids
  
  iter = iter +1;
end



