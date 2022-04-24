package ru.klimovitch.compmath;

import java.util.function.DoubleUnaryOperator;

public class LinearSecondOrderEquation extends SecondOrderEquation {
    public LinearSecondOrderEquation(DoubleUnaryOperator functionAtFirstOrderDerivative,
                                     DoubleUnaryOperator functionAtZeroOrderDerivative,
                                     DoubleUnaryOperator rightHandSideFunction) {
        super(functionAtFirstOrderDerivative, functionAtZeroOrderDerivative, rightHandSideFunction);
    }

    public LinearSecondOrderEquation(double q, double p, double f) {
        this(operand -> q, operand -> p, operand -> f);
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
            gridSolution[i] = (fX + (2 / (h * h) - pX) * gridSolution[i - 1]
                    + (qX / (2 * h) - 1 / (h * h)) * gridSolution[i - 2])
                    / (1 / (h * h) + qX / (2 * h));
        }
        return gridSolution;
    }

    public void drawGeneralSolution(int n) {
        DoubleUnaryOperator f = this.rightHandSideFunction;
        this.rightHandSideFunction = operand -> 0;
        setLeftBoundaryConditions(1, 0);
        double[] firstGeneralSolution = calculateSolution(n);
        setLeftBoundaryConditions(0, 1);
        double[] secondGeneralSolution = calculateSolution(n);

        this.rightHandSideFunction = f;
        setLeftBoundaryConditions(0, 0);
        double[] particularSolution = calculateSolution(n);

        double[][] solutions = { firstGeneralSolution, secondGeneralSolution, particularSolution };
        String[] titles = { "firstGeneralSolution", "secondGeneralSolution", "particularSolution" };
        drawGraph(argumentDesignation, functionDesignation, solutions, titles);
    }
}
