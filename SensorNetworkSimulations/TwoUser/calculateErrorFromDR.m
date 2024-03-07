function [errorVal] = calculateErrorFromDR(x, points, P0, P1, pc0, pc1, noistdv)
    a11 = points(4,1);
    a01 = points(3,1);
    a10 = points(2,1);
    a00 = points(1,1);

    if isempty(x)
        errorVal = min(P0,P1);
        return
    elseif length(x) == 1
        p1g11 = normcdf((a11 - x(1))/noistdv);
        p1g01 = normcdf((a01 - x(1))/noistdv);
        p1g10 = normcdf((a10 - x(1))/noistdv);
        p1g00 = normcdf((a00 - x(1))/noistdv);
    elseif length(x) == 3
        p1g11 = normcdf((a11 - x(1))/noistdv) - normcdf((a11 - x(2))/noistdv) + normcdf((a11 - x(3))/noistdv);
        p1g01 = normcdf((a01 - x(1))/noistdv) - normcdf((a01 - x(2))/noistdv) + normcdf((a01 - x(3))/noistdv);
        p1g10 = normcdf((a10 - x(1))/noistdv) - normcdf((a10 - x(2))/noistdv) + normcdf((a10 - x(3))/noistdv);
        p1g00 = normcdf((a00 - x(1))/noistdv) - normcdf((a00 - x(2))/noistdv) + normcdf((a00 - x(3))/noistdv);
    else 
        error('Unexpected number of x vals found')
    end
    
    pg1 = [p1g00; p1g10; p1g01; p1g11];
    pg0 = 1 - pg1;

    e0 = sum(pc0.*pg1);
    e1 = sum(pc1.*pg0);
    
    errorVal = P0*e0 + P1*e1;
end
