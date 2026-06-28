# 🛒 E-Commerce Testing Project

> A comprehensive quality assurance project covering **Manual Testing**, **Automation Testing**, and **API Testing** for an E-Commerce web application.

---

## 👥 Team

| Name | Role |
|------|------|
| **Abdelrahman Zaki** | Team Leader |
| Mohamed Sameh | QA Engineer |
| Zeyad Mohamed | QA Engineer |
| Bassant Amr | QA Engineer |
| Mariam Mohamed | QA Engineer |

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Testing Scope](#-testing-scope)
- [Tools & Technologies](#-tools--technologies)
- [Test Results & Statistics](#-test-results--statistics)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)

---

## 🔍 Project Overview

This project covers end-to-end quality assurance for an **E-Commerce web application**, including functional testing across all major modules such as Home, Login, Registration, Search, Cart, Wishlist, Customer Info, and Header navigation.

The testing strategy follows a three-layered approach:

- **Manual Testing** — exploratory and structured test case execution
- **Automation Testing** — regression and smoke testing using Selenium & TestNG
- **API Testing** — backend validation using Postman

---

## 🎯 Testing Scope

The following modules were covered across all testing types:

| Module | Description |
|--------|-------------|
| 🏠 Home | Landing page functionality and navigation |
| 🔐 Login | Authentication and session handling |
| 📝 Register | New user registration flow |
| 🔎 Search | Product search and filtering |
| 🛒 Cart | Add, update, and remove cart items |
| ❤️ Wishlist | Save and manage wishlisted products |
| 👤 Cust. Info | Customer profile and information management |
| 🔗 Header | Navigation links and UI elements |

---

## 🛠️ Tools & Technologies

### Manual Testing
![Excel](https://img.shields.io/badge/Test%20Cases-Excel-217346?style=flat&logo=microsoftexcel&logoColor=white)
![JIRA](https://img.shields.io/badge/Bug%20Tracking-JIRA-0052CC?style=flat&logo=jira&logoColor=white)

- Test case design and execution
- Bug reporting and tracking
- Exploratory testing sessions

### Automation Testing
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=flat&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-FF6C37?style=flat)

- **Selenium WebDriver** — browser automation and UI interaction
- **TestNG** — test execution, grouping, and reporting framework
- Page Object Model (POM) design pattern

### API Testing
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white)

- REST API endpoint validation
- Request/response verification
- Environment-based test collections

---

## 📊 Test Results & Statistics

### Overall Summary

| Metric | Value |
|--------|-------|
| ✅ Total Test Cases | **120** |
| ✔️ Passed | **98** |
| ❌ Failed | **22** |
| 📈 Overall Pass Rate | **82%** |
| 🐛 Total Bugs | **34** |

### Bug Severity Breakdown

| Severity | Count | Percentage |
|----------|-------|------------|
| 🔴 Critical | 8 | 24% |
| 🟠 Major | 14 | 41% |
| 🔵 Minor | 12 | 35% |

### Test Cases by Module

| Module | Total Test Cases | Passed |
|--------|-----------------|--------|
| Home | 12 | 10 |
| Login | 18 | 15 |
| Register | 16 | 13 |
| Search | 14 | 12 |
| Cart | 20 | 16 |
| Wishlist | 10 | 8 |
| Cust. Info | 14 | 12 |
| Header | 16 | 12 |

---

## 📁 Project Structure

```
📦 ecommerce-testing-project
 ┣ 📂 manual-testing
 ┃ ┣ 📂 test-cases
 ┃ ┣ 📂 bug-reports
 ┃ ┗ 📂 test-plan
 ┣ 📂 automation-testing
 ┃ ┣ 📂 src
 ┃ ┃ ┣ 📂 main
 ┃ ┃ ┃ ┗ 📂 java
 ┃ ┃ ┃   ┗ 📂 pages          # Page Object Model classes
 ┃ ┃ ┗ 📂 test
 ┃ ┃   ┗ 📂 java
 ┃ ┃     ┗ 📂 tests          # TestNG test classes
 ┃ ┣ 📜 testng.xml
 ┃ ┗ 📜 pom.xml
 ┣ 📂 api-testing
 ┃ ┣ 📂 collections          # Postman collections
 ┃ ┗ 📂 environments         # Postman environment files
 ┗ 📜 README.md
```

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 11+
- Maven 3.6+
- Chrome / Firefox browser
- Postman (for API testing)

### Running Automation Tests

```bash
# Clone the repository
git clone https://github.com/your-org/ecommerce-testing-project.git

# Navigate to the automation directory
cd automation-testing

# Install dependencies
mvn clean install

# Run all tests
mvn test

# Run a specific test group
mvn test -Dgroups="smoke"
```

### Running API Tests

1. Open **Postman**
2. Import the collection from `api-testing/collections/`
3. Import the environment from `api-testing/environments/`
4. Select the environment and run the collection

---

## 📌 Notes

- All test cases were executed against the staging environment
- Bug reports include steps to reproduce, expected vs. actual results, and screenshots
- Automation tests follow the **Page Object Model (POM)** design pattern for maintainability

---

<div align="center">

Made with ❤️ by the QA Team | Abdelrahman Zaki · Mohamed Sameh · Zeyad Mohamed · Bassant Amr · Mariam Mohamed

</div>
