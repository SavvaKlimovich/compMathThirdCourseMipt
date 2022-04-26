package com.klimovitch.compmath;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.util.function.DoubleUnaryOperator;

public abstract class SecondOrderODE {
    protected double x0;
    protected double xN;

    protected double u0;
    protected double uDerivative0;

    protected DoubleUnaryOperator functionAtFirstOrderDerivative;
    protected DoubleUnaryOperator functionAtZeroOrderDerivative;
    protected DoubleUnaryOperator rightHandSideFunction;

    protected String argumentDesignation;
    protected String functionDesignation;
    protected String derivativeDesignation;
    protected String secondDerivativeDesignation;

    protected String jFrameName = "JFreeChart";

    public SecondOrderODE(DoubleUnaryOperator functionAtFirstOrderDerivative,
                          DoubleUnaryOperator functionAtZeroOrderDerivative,
                          DoubleUnaryOperator rightHandSideFunction) {
        this.functionAtFirstOrderDerivative = functionAtFirstOrderDerivative;
        this.functionAtZeroOrderDerivative = functionAtZeroOrderDerivative;
        this.rightHandSideFunction = rightHandSideFunction;
    }

    public void setBoundaries(double x0, double xN) {
        this.x0 = x0;
        this.xN = xN;
    }

    public void setLeftBoundaryConditions(double u0, double uDerivative0) {
        setLeftBoundaryValue(u0);
        setLeftBoundaryDerivativeValue(uDerivative0);
    }

    public void setLeftBoundaryValue(double u0) {
        this.u0 = u0;
    }

    public void setLeftBoundaryDerivativeValue(double uDerivative0) {
        this.uDerivative0 = uDerivative0;
    }

    public void setDesignations(String argumentDesignation,
                                String functionDesignation) {
        String derivativeDesignation = functionDesignation + "'";
        String secondDerivativeDesignation = functionDesignation + "''";
        setDesignations(argumentDesignation, functionDesignation, derivativeDesignation, secondDerivativeDesignation);
    }

    public void setDesignations(String argumentDesignation,
                                String functionDesignation,
                                String derivativeDesignation) {
        String secondDerivativeDesignation = derivativeDesignation + "'";
        setDesignations(argumentDesignation, functionDesignation, derivativeDesignation, secondDerivativeDesignation);
    }

    public void setDesignations(String argumentDesignation,
                                String functionDesignation,
                                String derivativeDesignation,
                                String secondDerivativeDesignation) {
        this.argumentDesignation = argumentDesignation;
        this.functionDesignation = functionDesignation;
        this.derivativeDesignation = derivativeDesignation;
        this.secondDerivativeDesignation = secondDerivativeDesignation;
    }

    public void setJFrameName(String jFrameName) {
        this.jFrameName = jFrameName;
    }

    public abstract double[] calculateSolution(int numberOfPoints);

    public double[] calculateDerivative(int numberOfPoints) {
        double[] gridFunction = calculateSolution(numberOfPoints);
        double h = computeStep(numberOfPoints);
        double[] gridDerivative = new double[numberOfPoints];
        for (int i = 0; i != numberOfPoints - 1; i++) {
            gridDerivative[i] = (gridFunction[i + 1] - gridFunction[i]) / h;
        }
        gridDerivative[numberOfPoints - 1] = gridDerivative[numberOfPoints - 2];
        return gridDerivative;
    }

    public void drawSolutionGraph(int numberOfPoints) {
        drawGraph(argumentDesignation, functionDesignation, calculateSolution(numberOfPoints));
    }

    public void drawDerivativeGraph(int numberOfPoints) {
        drawGraph(argumentDesignation, derivativeDesignation, calculateDerivative(numberOfPoints));
    }

    protected void drawGraph(String x, String y, double[] gridFunction) {
        double[][] gridFunctions = new double[][] { gridFunction };
        String[] titles = new String[] { "" };
        drawGraph(x, y, gridFunctions, titles);
    }

    protected void drawGraph(String x, String y, double[][] gridFunctions, String[] titles) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i != gridFunctions.length; i++) {
            XYSeries series = new XYSeries(titles[i]);
            int numberOfPoints = gridFunctions[i].length;
            double h = computeStep(numberOfPoints);
            for (int j = 0; j != numberOfPoints; j++) {
                double xI = x0 + h * j;
                series.add(xI, gridFunctions[i][j]);
            }
            dataset.addSeries(series);
        }
        String chartTitle = String.format("%s(%s)", y, x);
        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle, x, y,
                dataset, PlotOrientation.VERTICAL,
                true, true, true
        );

        JFrame frame = new JFrame(jFrameName);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    protected double computeStep(int numberOfPoints) {
        return (xN - x0) / (numberOfPoints - 1);
    }
}
