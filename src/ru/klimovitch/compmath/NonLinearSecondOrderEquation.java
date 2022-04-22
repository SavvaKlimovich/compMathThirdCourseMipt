package ru.klimovitch.compmath;

import java.util.function.DoubleUnaryOperator;

public class NonLinearSecondOrderEquation extends SecondOrderEquation {
    private DoubleUnaryOperator firstOrderDerivativeFunction;
    private DoubleUnaryOperator zeroOrderDerivativeFunction;

    public NonLinearSecondOrderEquation(DoubleUnaryOperator functionAtFirstOrderDerivative,
                                        DoubleUnaryOperator firstOrderDerivativeFunction,
                                        DoubleUnaryOperator functionAtZeroOrderDerivative,
                                        DoubleUnaryOperator zeroOrderDerivativeFunction,
                                        DoubleUnaryOperator rightHandSideFunction) {
        super(functionAtFirstOrderDerivative, functionAtZeroOrderDerivative, rightHandSideFunction);
        this.firstOrderDerivativeFunction = firstOrderDerivativeFunction;
        this.zeroOrderDerivativeFunction = zeroOrderDerivativeFunction;
    }

    public double[] calculateSolutionKnowingIntegral(int n, double integral) {
        return calculateSolutionKnowingIntegral(n, integral, Math.PI / 4, Math.PI / 8);
    }

    private double[] calculateSolutionKnowingIntegral(int n, double integralValue, double angle, double term) {
        setLeftBoundaryDerivativeValue(Math.tan(angle));
        double[] solution = calculateSolution(n);
        double integral = calculateIntegral(solution, n);
        if (areEqual(integralValue, integral)) {
            return solution;
        } else if (integral > integralValue) {
            return calculateSolutionKnowingIntegral(n, integralValue, angle - term, term / 2);
        } else {
            return calculateSolutionKnowingIntegral(n, integralValue, angle + term, term / 2);
        }
    }

    @Override
    public double[] calculateSolution(int n) {
        double h = (xN - x0) / n;
        double[] gridSolution = new double[n + 1];
        gridSolution[0] = u0;
        gridSolution[1] = uDerivative0 * h + u0;
        for (int i = 2; i != n + 1; i++) {
            double x = x0 + i * h;
            double qX = functionAtFirstOrderDerivative.applyAsDouble(x);
            double pX = functionAtZeroOrderDerivative.applyAsDouble(x);
            double fX = rightHandSideFunction.applyAsDouble(x);

            double uI_1 = gridSolution[i - 2];
            double uI = gridSolution[i - 1];
            double f1 = firstOrderDerivativeFunction.applyAsDouble((uI - uI_1) / h);
            double f0 = zeroOrderDerivativeFunction.applyAsDouble(uI);
            gridSolution[i] = (h * h) * (fX - pX * f0 - qX * f1) + 2 * uI - uI_1;
        }
        return gridSolution;
    }

    private boolean areEqual(double x, double y) {
        return Math.abs(x - y) < Math.abs(x / 100);
    }

    private double calculateIntegral(double[] gridFunction, int n) {
        double h = (xN - x0) / n;
        double integral = h * (gridFunction[0] + gridFunction[n]) / 2;
        for (int i = 1; i != n + 1; i++) {
            integral += h * gridFunction[i];
        }
        return integral;
    }

    public void drawSolutionGraphKnowingIntegral(int n, double integral) {
        drawGraph(n, argumentDesignation, functionDesignation, 
                calculateSolutionKnowingIntegral(n, integral));
    }
}
