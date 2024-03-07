MultiUserSetup;

gridDensity = 150;
pointSize = 2;
gridBound = max(abs(points)) * 1.5;
x = linspace(-gridBound,gridBound,gridDensity);
y = linspace(-gridBound,gridBound,gridDensity);
X = zeros(gridDensity^2,1);
Y = zeros(gridDensity^2,1);
closest = zeros(gridDensity^2,1);

% map decoding regions for source bits (0 or 1)
for i = 1:gridDensity
    for j = 1:gridDensity
        condProbVals = zeros(length(points),1);
        
        for k = 1:length(points)
           distance = (x(i) - points(k))^2; 
           condProbVals(k) = exp(-distance/N0);
        end
        
        weight0 = P0*(sum(pc0.*condProbVals));
        weight1 = P1*(sum(pc1.*condProbVals));
        
        [~, I] = max([weight0, weight1]);
        X((i-1)*gridDensity + j) = x(i);
        Y((i-1)*gridDensity + j) = y(j);
        closest((i-1)*gridDensity + j) = I;
    end
end

% plot regions
figure
hold on
scatter(X,Y,pointSize,closest,'filled');
scatter(points,points*0,20,'red','filled');
scatter(centerPoint,0,20,'green','filled');
scatter(0,0,20,'black','filled');
