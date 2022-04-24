package ru.klimovitch.compmath;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.util.function.DoubleUnaryOperator;

public abstract class SecondOrderEquation {
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

    public SecondOrderEquation(DoubleUnaryOperator functionAtFirstOrderDerivative,
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

    public abstract double[] calculateSolution(int n);

    public double[] calculateDerivative(int n) {
        double[] gridFunction = calculateSolution(n);
        double h = (xN - x0) / n;
        double[] gridDerivative = new double[n + 1];
        for (int i = 0; i != n; i++) {
            gridDerivative[i] = (gridFunction[i + 1] - gridFunction[i]) / h;
        }
        gridDerivative[n] = gridDerivative[n - 1];
        return gridDerivative;
    }

    public void drawSolutionGraph(int n) {
        drawGraph(argumentDesignation, functionDesignation, calculateSolution(n));
    }

    public void drawDerivativeGraph(int n) {
        drawGraph(argumentDesignation, derivativeDesignation, calculateDerivative(n));
    }

    protected void drawGraph(String x, String y, double[] gridFunction) {
        drawGraph(x, y,
                new double[][] { gridFunction },
                new String[] { "" }
        );
    }

    protected void drawGraph(String x, String y, double[][] gridFunctions, String[] titles) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i != gridFunctions.length; i++) {
            XYSeries series = new XYSeries(titles[i]);
            int n = gridFunctions[i].length - 1;
            double h = (xN - x0) / n;
            for (int j = 0; j != n + 1; j++) {
                double xI = h * j;
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

        JFrame frame = new JFrame("JFreeChart");
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
