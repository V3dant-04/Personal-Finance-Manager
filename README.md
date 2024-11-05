# Personal Finance Manager For Student

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/github/license/V3dant-04/Personal-Finance-Manager?style=for-the-badge)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)

Personal Finance Manager for Students is a Java desktop application designed to help students manage their finances effectively. This program allows users to track income, expenses, generate weekly and monthly reports, and visualize spending with charts.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Features
- **Add Income and Expenses**: Log incomes and expenses with details such as category, amount, date, and optional notes.
- **Balance Calculation**: Calculates current balance by subtracting total expenses from total income.
- **Weekly and Monthly Reports**: Generates weekly and monthly reports with cumulative balances.
- **Data Visualization**: Displays a pie chart showing expense breakdown by category.
- **Data Persistence**: Saves all financial data to a local file, which loads automatically on restart.

## Installation
### Prerequisites
- Java JDK (version 8 or later)
- Git

### Steps
1. Clone the repository:
    ```bash
    git clone https://github.com/V3dant-04/Personal-Finance-Manager.git
    ```
2. Navigate to the project directory:
    ```bash
    cd Personal-Finance-Manager
    ```
3. Compile and run the application:
    ```bash
    javac -d bin src/com/personalfinance/project/*.java
    java -cp bin com.personalfinance.project.FinanceManagerApp
    ```

## Usage
### Adding an Expense
1. Click **"Add Expense"**.
2. Enter the expense details: category, amount, date, and optional notes.
3. The expense will be saved, and the balance will be updated.

### Viewing Reports
- **Weekly Report**: Shows total income, expenses, and cumulative balance for each week.
- **Monthly Report**: Shows total income, expenses, and cumulative balance for each month.

### Generating Expense Chart
1. Click **"Generate Budget Chart"** to see a pie chart of expenses by category.
2. To close the chart, click **"Close Chart"**.

## Project Structure
Here is the directory structure of the project:

```plaintext
Personal-Finance-Manager/
├── src/
│   ├── main/
│   │   ├── java/personalfinance/
│   │   │   ├── FinanceManagerApp.java  # Main entry point of the application
└── README.md
└── .gitignore
└── pom.xml
```
## Contributing
1. Fork the repository.
2. Create a new branch for your feature:
    ```bash
    git checkout -b feature/YourFeature
    ```
3. Commit your changes:
    ```bash
    git commit -m "Add your feature"
    ```
4. Push the branch:
    ```bash
    git push origin feature/YourFeature
    ```
5. Submit a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

