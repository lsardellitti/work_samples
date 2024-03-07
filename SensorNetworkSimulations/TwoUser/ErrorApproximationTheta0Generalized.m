setupValsOverride = true; %#ok<NASGU>
theta = 0;
N0 = 1;
Pw = 1;
numXVals = 1000;
xSearchOffset = 10;

% Values range to test over
testVals = linspace(0.001,3,1000);

errorVals = zeros(length(testVals),1);

for testIndex = 1:length(testVals)
    % Comment out whichever parameter is being modified here in BaseSetup
    Ps = testVals(testIndex);

    BaseSetup;

    x = calculateXvals(points, P0, P1, pc0, pc1, N0, knownFade, -Ps-Pw-xSearchOffset, Ps+Pw+xSearchOffset, numXVals);
    
    errorVal = calculateErrorFromDR(x, points, P0, P1, pc0, pc1, noistdv);
    
    errorVals(testIndex) = errorVal;
end

figure
plot(testVals, errorVals)
ylabel('Error Probability')

setupValsOverride = false;
