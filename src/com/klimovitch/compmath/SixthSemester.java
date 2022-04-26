package com.klimovitch.compmath;

public class SixthSemester {
    public static void tenthTheme8_9() {
        double inductanceHenries = 1;
        double resistanceOhms = 1_000;
        double capacitanceFarads = 0.000_006_25;
        double electromotiveForceVolts = 24;

        SecondOrderODE equation = new LinearSecondOrderODE(
                resistanceOhms / inductanceHenries,
                1 / (inductanceHenries * capacitanceFarads),
                electromotiveForceVolts / inductanceHenries
        );
        equation.setBoundaries(0, 0.1);
        equation.setLeftBoundaryConditions(0, 5);
        equation.setDesignations("t", "Q", "I");
        equation.setJFrameName("Тема X, 8.9");

        int numberOfPoints = 100_000;
        double[] current = equation.calculateDerivative(numberOfPoints);
        System.out.println("I(0.1) = " + current[numberOfPoints - 1] + " A");
        equation.drawDerivativeGraph(numberOfPoints);
    }

    public static void eleventhTheme9_1b() {
        LinearSecondOrderODE equation = new LinearSecondOrderODE(
                operand -> 0,
                operand -> 10 + operand * operand,
                operand -> operand * Math.exp(-operand)
        );
        equation.setBoundaries(0, 10);
        equation.setDesignations("x", "y");
        equation.setJFrameName("Тема XI, 9.1b");

        equation.drawGeneralSolution(100_000);
    }

    public static void eleventhTheme9_3b() {
        NonLinearSecondOrderODE equation = new NonLinearSecondOrderODE(
                operand -> 0, operand -> 0,
                operand -> -operand, Math::sqrt, operand -> 0
        );
        equation.setBoundaries(0, 1);
        equation.setLeftBoundaryValue(0);
        equation.setDesignations("x", "y");
        equation.setJFrameName("Тема XI, 9.3b");

        equation.drawSolutionGraphKnowingIntegral(100_000, 1);
    }
}
