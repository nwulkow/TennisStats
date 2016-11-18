function t = testJavaConnectionMethod(M)
%M = cellArrayToMatrix(M);
M
t = trace(M)*eig(M)
end