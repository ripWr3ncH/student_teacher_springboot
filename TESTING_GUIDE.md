# 🧪 Testing & CI/CD Workflow Guide

## 📋 Table of Contents
- [Overview](#overview)
- [Testing Strategy](#testing-strategy)
- [Git Workflow](#git-workflow)
- [Branch Protection Rules](#branch-protection-rules)
- [CI/CD Pipeline](#cicd-pipeline)
- [Running Tests Locally](#running-tests-locally)
- [Merge Conflict Resolution](#merge-conflict-resolution)

---

## 🎯 Overview

This project implements **enterprise-level testing** and **CI/CD best practices** following industry standards. The testing strategy covers:

- ✅ **Unit Tests** (Services with Mockito)
- ✅ **Integration Tests** (Controllers with MockMvc)
- ✅ **Repository Tests** (@DataJpaTest with H2)
- ✅ **Entity Tests** (POJOs validation)
- ✅ **GitHub Actions CI/CD Pipeline**
- ✅ **Branch Protection Rules**

---

## 🧪 Testing Strategy

### Test Coverage Overview

| Layer | Technology | Coverage |
|-------|-----------|----------|
| **Service Layer** | JUnit 5 + Mockito | StudentService, TeacherService |
| **Controller Layer** | SpringBootTest + MockMvc | StudentController, TeacherController |
| **Repository Layer** | @DataJpaTest + H2 | StudentRepository, TeacherRepository |
| **Entity Layer** | JUnit 5 + AssertJ | Student, Teacher |

### AAA Pattern (Arrange-Act-Assert)

All tests follow the **AAA Pattern**:

```java
@Test
void testExample() {
    // Arrange - Set up test data
    Student student = new Student();
    student.setName("Test");
    
    // Act - Execute the method under test
    Student result = studentService.save(student);
    
    // Assert - Verify the results
    assertThat(result).isNotNull();
}
```

---

## 🔀 Git Workflow

### Step 1: Create Testing Branch

```bash
# Checkout main branch
git checkout main

# Pull latest changes
git pull origin main

# Create and checkout testing branch
git checkout -b testing/unit-integration-tests

# Verify you're on the correct branch
git branch
```

### Step 2: Make Changes & Commit

Follow **Conventional Commits** format:

```bash
# Add all test files
git add .

# Commit with conventional format
git commit -m "test: add unit tests for StudentService"

# Or for multiple changes
git commit -m "test: add integration tests for StudentController

- Add GET /api/students tests
- Add POST /api/students tests
- Add authentication tests
- Cover edge cases and error scenarios"
```

**Conventional Commit Types:**
- `test:` - Adding or updating tests
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `refactor:` - Code refactoring
- `chore:` - Build process or tooling changes

### Step 3: Push Branch to Remote

```bash
# Push testing branch to GitHub
git push origin testing/unit-integration-tests

# Or set upstream and push
git push -u origin testing/unit-integration-tests
```

---

## 🛡️ Branch Protection Rules

### Configure on GitHub (Step-by-Step)

#### 1. Navigate to Repository Settings
1. Go to your GitHub repository
2. Click **Settings** tab
3. Select **Branches** from left sidebar

#### 2. Add Branch Protection Rule
1. Click **Add rule** button
2. Enter branch name pattern: `main`

#### 3. Configure Protection Settings

**✅ Required Settings:**

- [x] **Require a pull request before merging**
  - [x] Require approvals: **1**
  - [x] Dismiss stale pull request approvals when new commits are pushed
  - [x] Require review from Code Owners (optional)

- [x] **Require status checks to pass before merging**
  - [x] Require branches to be up to date before merging
  - **Required checks:** Add `test` (from GitHub Actions)

- [x] **Require conversation resolution before merging**

- [x] **Require signed commits** (optional but recommended)

- [x] **Require linear history** (prevents merge commits)

- [x] **Include administrators** (apply rules to admins too)

- [x] **Restrict who can push to matching branches**
  - No one can push directly to `main`

#### 4. Save Changes
Click **Create** or **Save changes**

### Visual Guide

```
Repository → Settings → Branches → Add rule

Branch name pattern: main

☑ Require pull request before merging
  ├─ Require approvals: 1
  └─ Dismiss stale reviews

☑ Require status checks to pass
  ├─ Require up-to-date branches
  └─ Status checks: test (Java CI)

☑ Require conversation resolution
☑ Include administrators
☑ Restrict pushes
```

---

## 🚀 CI/CD Pipeline

### GitHub Actions Workflow

Location: `.github/workflows/test.yml`

**Triggers:**
- ✅ Pull requests to `main`
- ✅ Push to `main`
- ✅ Manual workflow dispatch

**Pipeline Stages:**

```yaml
1️⃣ Checkout Code
   ↓
2️⃣ Setup JDK 17
   ↓
3️⃣ Clean & Compile
   ↓
4️⃣ Run Tests
   ↓
5️⃣ Package Application
   ↓
6️⃣ Upload Test Results
   ↓
7️⃣ ✅ Success / ❌ Failure
```

### Viewing CI Results

1. **On Pull Request:**
   - All checks must pass before merge
   - Red ❌ = Tests failed (cannot merge)
   - Green ✅ = Tests passed (can merge)

2. **Actions Tab:**
   - Go to **Actions** tab in GitHub
   - View workflow runs
   - Download test artifacts

---

## 🏃‍♂️ Running Tests Locally

### Run All Tests

```bash
# Using Maven Wrapper (Windows)
.\mvnw clean test

# Using Maven Wrapper (Linux/Mac)
./mvnw clean test

# Using Maven
mvn clean test
```

### Run Specific Test Classes

```bash
# Run StudentServiceTest only
mvn test -Dtest=StudentServiceTest

# Run all controller tests
mvn test -Dtest=*ControllerTest

# Run all repository tests
mvn test -Dtest=*RepositoryTest
```

### Run with Different Profiles

```bash
# Run with test profile
mvn test -Dspring.profiles.active=test

# Run with coverage
mvn clean test jacoco:report
```

### Test Reports Location

```
target/
  └── surefire-reports/
      ├── TEST-*.xml
      └── *.txt
```

---

## 🔧 Merge Conflict Resolution

### Scenario: Intentional Merge Conflict

#### Create Conflict:

1. **On `main` branch:**
```bash
git checkout main
# Edit StudentService.java
git add .
git commit -m "fix: update StudentService logic"
git push origin main
```

2. **On `testing/unit-integration-tests` branch:**
```bash
git checkout testing/unit-integration-tests
# Edit same lines in StudentService.java
git add .
git commit -m "test: add tests for StudentService"
git push origin testing/unit-integration-tests
```

3. **Create Pull Request** → GitHub shows conflict ⚠️

#### Resolve Locally:

```bash
# Checkout testing branch
git checkout testing/unit-integration-tests

# Fetch latest main
git fetch origin main

# Merge main into testing branch
git merge origin/main

# Git shows conflict:
# CONFLICT (content): Merge conflict in src/.../StudentService.java
```

**Edit conflicted file:**

```java
<<<<<<< HEAD
// Your changes in testing branch
private void myTestMethod() { ... }
=======
// Changes from main
private void myMainMethod() { ... }
>>>>>>> origin/main
```

**Choose resolution:**
- Keep both changes
- Keep only one
- Write new code

**Complete merge:**

```bash
git add src/.../StudentService.java
git commit -m "merge: resolve conflict in StudentService"
git push origin testing/unit-integration-tests
```

#### Resolve on GitHub:

1. Click **Resolve conflicts** button on PR
2. Edit file in GitHub editor
3. Mark as resolved
4. Commit merge

---

### Rebase vs Merge

| Strategy | Use Case | Command |
|----------|----------|---------|
| **Merge** | Keep full history | `git merge origin/main` |
| **Rebase** | Clean linear history | `git rebase origin/main` |

**Rebase Example:**

```bash
git checkout testing/unit-integration-tests
git fetch origin main
git rebase origin/main

# If conflicts occur:
# 1. Fix conflicts
# 2. git add <files>
# 3. git rebase --continue

# Force push after rebase
git push --force-with-lease origin testing/unit-integration-tests
```

**⚠️ Warning:** Never rebase public/shared branches!

---

## 📝 Pull Request Workflow

### Step 1: Create Pull Request

```bash
# Push your testing branch
git push origin testing/unit-integration-tests
```

On GitHub:
1. Go to **Pull requests** tab
2. Click **New pull request**
3. Base: `main` ← Compare: `testing/unit-integration-tests`
4. Fill in PR template:

```markdown
## Description
Add comprehensive unit and integration tests

## Changes
- ✅ Unit tests for StudentService
- ✅ Unit tests for TeacherService
- ✅ Integration tests for StudentController
- ✅ Integration tests for TeacherController
- ✅ Repository tests with @DataJpaTest
- ✅ Entity tests for validation

## Testing
- All tests pass locally
- CI pipeline: ✅ Passing

## Checklist
- [x] Tests added/updated
- [x] All tests pass
- [x] Code follows conventions
- [x] Documentation updated
```

5. Click **Create pull request**

### Step 2: Wait for CI Checks

GitHub Actions will automatically:
- ✅ Run all tests
- ✅ Build application
- ✅ Report results

### Step 3: Request Review

1. Assign reviewers
2. Add labels (e.g., `testing`, `ready-for-review`)
3. Link related issues

### Step 4: Address Review Comments

```bash
# Make changes based on feedback
git add .
git commit -m "test: address review comments"
git push origin testing/unit-integration-tests
```

### Step 5: Merge Pull Request

After approval:
1. **Squash and merge** (recommended for feature branches)
2. **Rebase and merge** (for clean history)
3. **Create a merge commit** (keep all commits)

**Final commit message:**
```
test: add comprehensive unit and integration tests (#1)

- Add unit tests for StudentService and TeacherService
- Add integration tests for controllers
- Add repository tests with @DataJpaTest
- Add entity validation tests
- Configure GitHub Actions CI pipeline
```

### Step 6: Delete Branch

```bash
# After successful merge
git checkout main
git pull origin main
git branch -d testing/unit-integration-tests
git push origin --delete testing/unit-integration-tests
```

---

## 🎓 Best Practices

### ✅ DO:
- Write tests before merging code
- Follow AAA pattern in tests
- Use meaningful test names
- Test edge cases and exceptions
- Keep tests independent
- Use conventional commits
- Request code reviews
- Resolve conflicts promptly

### ❌ DON'T:
- Push directly to `main`
- Merge without tests passing
- Ignore failing CI checks
- Skip code reviews
- Create huge PRs (keep them small)
- Force push to shared branches
- Leave conflicts unresolved

---

## 📊 Test Coverage Goals

| Metric | Target |
|--------|--------|
| Unit Test Coverage | > 80% |
| Integration Test Coverage | > 70% |
| Overall Code Coverage | > 75% |
| Passing Rate | 100% |

---

## 🆘 Troubleshooting

### Tests Fail Locally

```bash
# Clean Maven cache
mvn clean

# Delete target directory
rm -rf target/

# Re-run tests
mvn test
```

### CI Pipeline Fails

1. Check **Actions** tab for error logs
2. Run tests locally with same Java version
3. Verify H2 database configuration
4. Check application-test.properties

### Merge Conflicts

1. Always pull latest `main` before creating PR
2. Keep testing branch updated with `main`
3. Use `git mergetool` for complex conflicts

---

## 📚 Additional Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## 👨‍💻 Author

**Testing Framework Setup by:** GitHub Copilot  
**Date:** 2026-02-17  
**Version:** 1.0.0

---

## 📄 License

This testing framework is part of the SEPM Assignment project.

---

**Happy Testing! 🚀**

