package com.personalfinance.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

public class FinanceManagerApp {
    private List<Expense> expenses = new ArrayList<>();
    private List<Income> incomes = new ArrayList<>();
    private Long nextExpenseId = 1L;
    private Long nextIncomeId = 1L;
    private static final String DATA_FILE = "financeData.dat";
    private JFrame frame;
    private JPanel chartPanel;

    public static void main(String[] args) {
        FinanceManagerApp app = new FinanceManagerApp();
        app.loadData();
        app.createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Personal Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(255, 250, 250));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));
        buttonPanel.setBackground(new Color(255, 230, 230));

        JButton addExpenseButton = createButton("Add Expense", e -> addExpense(textArea));
        JButton viewExpensesButton = createButton("View Expenses", e -> viewExpenses(textArea));
        JButton addIncomeButton = createButton("Add Income", e -> addIncome(textArea));
        JButton viewIncomesButton = createButton("View Incomes", e -> viewIncomes(textArea));
        JButton calculateBalanceButton = createButton("Calculate Balance", e -> calculateBalance(textArea));
        JButton generateChartButton = createButton("Generate Budget Chart", e -> generateBudgetChart(textArea));
        JButton closeChartButton = createButton("Close Chart", e -> closeChart());
        JButton generateMonthlyReportButton = createButton("Generate Monthly Report", e -> generateMonthlyReport(textArea));
        JButton generateWeeklyReportButton = createButton("Generate Weekly Report", e -> generateWeeklyReport(textArea));
        JButton exitButton = createButton("Exit", e -> {
            saveData();
            System.exit(0);
        });

        buttonPanel.add(addExpenseButton);
        buttonPanel.add(viewExpensesButton);
        buttonPanel.add(addIncomeButton);
        buttonPanel.add(viewIncomesButton);
        buttonPanel.add(calculateBalanceButton);
        buttonPanel.add(generateChartButton);
        buttonPanel.add(closeChartButton);
        buttonPanel.add(generateMonthlyReportButton);
        buttonPanel.add(generateWeeklyReportButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        chartPanel = new JPanel(new BorderLayout());
        frame.add(chartPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.addActionListener(actionListener);
        return button;
    }

    private void addExpense(JTextArea textArea) {
        String category = JOptionPane.showInputDialog(frame, "Enter category:");
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount:");
        String notes = JOptionPane.showInputDialog(frame, "Enter notes:");
        String dateStr = JOptionPane.showInputDialog(frame, "Enter date (yyyy-MM-dd, leave blank for today):");

        double amount = Double.parseDouble(amountStr);
        LocalDate date = (dateStr == null || dateStr.isEmpty()) ? LocalDate.now() : LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        Expense expense = new Expense(nextExpenseId++, category.toLowerCase(), amount, notes, date);
        expenses.add(expense);
        textArea.append("Expense added: " + expense + "\n");
        saveData();
    }

    private void viewExpenses(JTextArea textArea) {
        textArea.setText("--- Expenses ---\n");
        if (expenses.isEmpty()) {
            textArea.append("No expenses recorded.\n");
        } else {
            expenses.forEach(expense -> textArea.append(expense + "\n"));
        }
    }

    private void addIncome(JTextArea textArea) {
        String source = JOptionPane.showInputDialog(frame, "Enter source:");
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount:");
        String dateStr = JOptionPane.showInputDialog(frame, "Enter date (yyyy-MM-dd, leave blank for today):");

        double amount = Double.parseDouble(amountStr);
        LocalDate date = (dateStr == null || dateStr.isEmpty()) ? LocalDate.now() : LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        Income income = new Income(nextIncomeId++, source, amount, date);
        incomes.add(income);
        textArea.append("Income added: " + income + "\n");
        saveData();
    }

    private void viewIncomes(JTextArea textArea) {
        textArea.setText("--- Incomes ---\n");
        if (incomes.isEmpty()) {
            textArea.append("No incomes recorded.\n");
        } else {
            incomes.forEach(income -> textArea.append(income + "\n"));
        }
    }

    private void calculateBalance(JTextArea textArea) {
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalIncome - totalExpense;

        if (balance < 0) {
            textArea.append(String.format("You are over budget by: %.2f\n", -balance));
        } else {
            textArea.append(String.format("Current Balance: %.2f\n", balance));
        }
    }

    private void generateMonthlyReport(JTextArea textArea) {
        textArea.setText("--- Monthly Report ---\n");
        Map<String, Double> monthlyIncome = new HashMap<>();
        Map<String, Double> monthlyExpenses = new HashMap<>();
        
        incomes.forEach(income -> {
            String month = income.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyIncome.put(month, monthlyIncome.getOrDefault(month, 0.0) + income.getAmount());
        });
        
        expenses.forEach(expense -> {
            String month = expense.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0.0) + expense.getAmount());
        });

        double cumulativeBalance = 0;
        for (String month : monthlyIncome.keySet()) {
            double totalIncome = monthlyIncome.getOrDefault(month, 0.0);
            double totalExpense = monthlyExpenses.getOrDefault(month, 0.0);
            double balance = totalIncome - totalExpense;
            cumulativeBalance += balance;

            textArea.append(String.format("%s - Income: %.2f, Expenses: %.2f, Balance: %.2f\n", month, totalIncome, totalExpense, cumulativeBalance));
        }
    }

    private void generateWeeklyReport(JTextArea textArea) {
        textArea.setText("--- Weekly Report ---\n");
        Map<String, Double> weeklyIncome = new HashMap<>();
        Map<String, Double> weeklyExpenses = new HashMap<>();

        incomes.forEach(income -> {
            String week = income.getDate().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toString();
            weeklyIncome.put(week, weeklyIncome.getOrDefault(week, 0.0) + income.getAmount());
        });

        expenses.forEach(expense -> {
            String week = expense.getDate().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toString();
            weeklyExpenses.put(week, weeklyExpenses.getOrDefault(week, 0.0) + expense.getAmount());
        });

        double cumulativeBalance = 0;
        for (String week : weeklyIncome.keySet()) {
            double totalIncome = weeklyIncome.getOrDefault(week, 0.0);
            double totalExpense = weeklyExpenses.getOrDefault(week, 0.0);
            double balance = totalIncome - totalExpense;
            cumulativeBalance += balance;

            textArea.append(String.format("%s - Income: %.2f, Expenses: %.2f, Balance: %.2f\n", week, totalIncome, totalExpense, cumulativeBalance));
        }
    }

    private void generateBudgetChart(JTextArea textArea) {
        Map<String, Double> categoryTotal = expenses.stream().collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));

        PieChart chart = new PieChartBuilder().width(800).height(600).title("Expense Breakdown").build();
        categoryTotal.forEach(chart::addSeries);

        chartPanel.removeAll();
        chartPanel.add(new XChartPanel<>(chart), BorderLayout.CENTER);
        chartPanel.revalidate();
        frame.pack();
    }

    private void closeChart() {
        chartPanel.removeAll();
        chartPanel.revalidate();
        frame.pack();
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(expenses);
            oos.writeObject(incomes);
            oos.writeObject(nextExpenseId);
            oos.writeObject(nextIncomeId);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            expenses = (List<Expense>) ois.readObject();
            incomes = (List<Income>) ois.readObject();
            nextExpenseId = (Long) ois.readObject();
            nextIncomeId = (Long) ois.readObject();
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static class Expense implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String category;
        private double amount;
        private String notes;
        private LocalDate date;

        public Expense(Long id, String category, double amount, String notes, LocalDate date) {
            this.id = id;
            this.category = category;
            this.amount = amount;
            this.notes = notes;
            this.date = date;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public String toString() {
            return String.format("ID: %d, Category: %s, Amount: %.2f, Date: %s, Notes: %s", id, category, amount, date, notes);
        }
    }

    static class Income implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String source;
        private double amount;
        private LocalDate date;

        public Income(Long id, String source, double amount, LocalDate date) {
            this.id = id;
            this.source = source;
            this.amount = amount;
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public String toString() {
            return String.format("ID: %d, Source: %s, Amount: %.2f, Date: %s", id, source, amount, date);
        }
    }
}
