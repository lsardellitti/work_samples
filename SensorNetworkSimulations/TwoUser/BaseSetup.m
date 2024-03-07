% source probability
P1 = 0.3;
P0 = 1 - P1;

% crossover probabilities
Ew = 0.1;
Es = 0.15;

if exist('setupValsOverride','var') ~= 1 || setupValsOverride == false
    % noise power
    N0 = 1;
    
    % signal powers (expressed as square root of mean power)
    Pw = 1;
    Ps = 1;

    % constellation angle
    theta = pi/4;
end

% Noise standard deviation
noistdv = sqrt(N0/2);

% fading power, set to 0 for no fading
sigma = 0;
% if assuming knowledge of fading at receiver, set sigma to 0, and use this
% keep this at 1 otherwise
knownFade = 1;

% Case Type analysis
case1Thresh = (Ew*Es)/(1 - Ew - Es + 2*Ew*Es);
case2Thresh = (Ew - Ew*Es)/(Ew + Es - 2*Ew*Es);
if P1 <= case1Thresh
    caseType = 1;
elseif P1 <= case2Thresh
    caseType = 2;
else
    caseType = 3;
end

% constellation parmeters
Aw = sqrt(P0/P1);
As = sqrt(P0/P1);
Bw = -sqrt((1 - P1*(Aw^2)) / P0);
Bs = -sqrt((1 - P1*(As^2)) / P0);

% constellation points
w0 = [Bw*Pw, 0];
w1 = [Aw*Pw, 0];
s0 = [Bs*Ps*cos(theta), Bs*Ps*sin(theta)];
s1 = [As*Ps*cos(theta), As*Ps*sin(theta)];

% note the choice of a10 and a01 is determined by w0,w1,s0,s1
a00 = w0 + s0;
a10 = w1 + s0;
a01 = w0 + s1;
a11 = w1 + s1;

points = [a00; a10; a01; a11];
centerPoint = (a00 + a11) / 2;

% conditional probabilities of constellation points
p11g0 = Ew*Es;
p10g0 = Ew*(1-Es);
p01g0 = (1-Ew)*Es;
p00g0 = (1-Ew)*(1-Es);

p11g1 = (1-Ew)*(1-Es);
p10g1 = (1-Ew)*Es;
p01g1 = Ew*(1-Es);
p00g1 = Ew*Es;

% these must be in the same order as in points array
pc0 = [p00g0; p10g0; p01g0; p11g0];
pc1 = [p00g1; p10g1; p01g1; p11g1];
pc = P0*pc0 + P1*pc1;
