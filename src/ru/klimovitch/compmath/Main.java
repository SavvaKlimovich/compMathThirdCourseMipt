package ru.klimovitch.compmath;

import java.util.function.DoubleUnaryOperator;

public class Main {
    public static void main(String... args) {
        tenthTheme8_9();
        eleventhTheme9_1b();
        eleventhTheme9_3b();
    }

    private static void tenthTheme8_9() {
        double inductanceHenries = 1;
        double resistanceOhms = 1_000;
        double capacitanceFarads = 0.000_006_25;
        double electromotiveForceVolts = 24;

        SecondOrderEquation equation = new LinearSecondOrderEquation(
                resistanceOhms / inductanceHenries,
                1 / (inductanceHenries * capacitanceFarads),
                electromotiveForceVolts / inductanceHenries
        );
        equation.setBoundaries(0, 0.1);
        equation.setLeftBoundaryConditions(0, 5);
        equation.setDesignations("t", "Q", "I");

        int n = 100_000;
        double[] current = equation.calculateDerivative(n);
        System.out.println("I(0.1) = " + current[n] + " A");
        equation.drawDerivativeGraph(n);
    }

    private static void eleventhTheme9_1b() {
        LinearSecondOrderEquation equation = new LinearSecondOrderEquation(
                operand -> 0,
                operand -> 10 + operand * operand,
                operand -> operand * Math.exp(-operand)
        );
        equation.setBoundaries(0, 10);
        equation.setDesignations("x", "y");

        equation.drawGeneralSolution(100_000);
    }

    private static void eleventhTheme9_3b() {
        NonLinearSecondOrderEquation equation = new NonLinearSecondOrderEquation(
                operand -> 0, operand -> 0,
                operand -> -operand, Math::sqrt, operand -> 0
        );
        equation.setBoundaries(0, 1);
        equation.setLeftBoundaryValue(0);
        equation.setDesignations("x", "y");

        equation.drawSolutionGraphKnowingIntegral(100_000, 1);
    }
}
