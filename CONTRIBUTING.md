# Contributing to D&D Dice Roller Android

Thank you for your interest in contributing to the D&D Dice Roller Android project! This document provides guidelines and information for contributors.

## 🌟 How to Contribute

We welcome contributions of all kinds:
- 🐛 Bug reports and fixes
- ✨ New features and enhancements
- 📚 Documentation improvements
- 🧪 Test coverage improvements
- 🎨 UI/UX improvements
- ♿ Accessibility enhancements

## 🚀 Getting Started

### Prerequisites

1. **Development Environment**
   - Android Studio Iguana | 2023.2.1 or later
   - JDK 17 or later
   - Git

2. **Fork and Clone**
   ```bash
   git clone git@github.com:yourusername/dnddiceroller-android.git
   cd dnddiceroller-android
   ```

3. **Setup Development**
   ```bash
   ./gradlew build
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

### Development Workflow

1. **Create a branch** from `develop`
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following our coding standards

3. **Test your changes**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ./gradlew ktlintCheck
   ```

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

5. **Push and create PR**
   ```bash
   git push origin feature/your-feature-name
   ```

## 📝 Coding Standards

### Kotlin Style Guide

We follow the [official Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html) with these additions:

- **Line length**: 120 characters maximum
- **Indentation**: 4 spaces (no tabs)
- **Import organization**: Android imports first, then third-party, then project imports
- **Naming**: Use descriptive names for variables, functions, and classes

### Architecture Guidelines

- **Clean Architecture**: Separate concerns across layers
- **MVVM Pattern**: Use ViewModels for UI state management
- **Repository Pattern**: Abstract data sources
- **Dependency Injection**: Use Hilt for all dependencies
- **Single Responsibility**: Each class should have one reason to change

### Code Quality Tools

We use several tools to maintain code quality:

```bash
# Kotlin linting
./gradlew ktlintCheck
./gradlew ktlintFormat

# Static analysis
./gradlew detekt

# Test coverage
./gradlew jacocoTestReport
```

## 🧪 Testing Guidelines

### Test Structure

- **Unit Tests**: Test business logic in isolation
- **Integration Tests**: Test component interactions
- **UI Tests**: Test user interactions and flows

### Test Requirements

- All new features must include tests
- Maintain minimum 80% code coverage
- Use meaningful test names that describe the scenario
- Follow the Given-When-Then pattern for test structure

### Example Test

```kotlin
@Test
fun `when rolling d20 dice then result should be between 1 and 20`() {
    // Given
    val diceRoller = DiceRoller()
    val diceType = DiceType.D20
    
    // When
    val result = diceRoller.roll(diceType)
    
    // Then
    assertTrue(result in 1..20)
}
```

## 📋 Pull Request Process

### Before Submitting

- [ ] Code follows our style guidelines
- [ ] All tests pass locally
- [ ] New tests cover your changes
- [ ] Documentation is updated if needed
- [ ] Commit messages follow convention

### PR Description Template

```markdown
## Description
Brief description of what this PR does.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Screenshots (if applicable)
Add screenshots here to show visual changes.

## Checklist
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code where necessary
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
```

### Review Process

1. **Automated Checks**: CI/CD pipeline runs tests
2. **Code Review**: Team members review code
3. **Testing**: Manual testing if needed
4. **Approval**: At least one maintainer approval required
5. **Merge**: Squash and merge to maintain clean history

## 🐛 Reporting Issues

### Bug Reports

Use our bug report template and include:

- **Environment**: Android version, device model, app version
- **Steps to reproduce**: Clear, numbered steps
- **Expected behavior**: What should happen
- **Actual behavior**: What actually happens
- **Screenshots**: If applicable
- **Logs**: Any relevant error messages

### Feature Requests

Include:

- **Use case**: Why is this feature needed?
- **Description**: Detailed description of the feature
- **Mockups**: Visual representation if applicable
- **Alternatives**: Other solutions you've considered

## 🏗️ Architecture Overview

Understanding our architecture helps with contributions:

```
├── app/                    # Presentation Layer
│   ├── ui/                # Activities, Fragments, Composables
│   ├── viewmodel/         # ViewModels
│   └── di/                # Dependency injection modules
├── domain/                 # Business Logic Layer
│   ├── usecase/           # Use cases
│   ├── model/             # Domain models
│   └── repository/        # Repository interfaces
├── data/                   # Data Layer
│   ├── repository/        # Repository implementations
│   ├── database/          # Room database
│   ├── network/           # API services
│   └── datastore/         # SharedPreferences alternative
├── core-ui/               # Design System
│   ├── theme/             # Colors, typography, shapes
│   ├── component/         # Reusable components
│   └── resources/         # Shared resources
└── common-test/           # Shared Test Utilities
    ├── fixture/           # Test data fixtures
    ├── rule/              # Custom test rules
    └── util/              # Test utilities
```

## 🎨 Design System

We maintain a consistent design system:

- **Material Design 3**: Follow latest Google guidelines
- **Accessibility**: WCAG 2.1 AA compliance
- **Dark Mode**: Support system dark mode
- **Responsive**: Support various screen sizes
- **Animations**: Meaningful and accessible animations

## ♿ Accessibility Guidelines

Accessibility is a priority:

- **Content Descriptions**: All interactive elements
- **Focus Management**: Proper focus order
- **Color Contrast**: Meet WCAG standards
- **Text Scaling**: Support large text sizes
- **Screen Readers**: Test with TalkBack
- **Touch Targets**: Minimum 48dp touch targets

## 📞 Getting Help

- **Discord**: [Join our community](https://discord.gg/tiation)
- **Email**: developers@tiation.net
- **Issues**: [GitHub Issues](https://github.com/tiation/dnddiceroller-android/issues)

## 🙏 Recognition

Contributors will be recognized in:
- Project README
- Release notes
- Hall of fame page
- Special contributor badges

## 📄 License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

## 👥 Core Team

- **Garrett Dillman** - garrett.dillman@gmail.com
- **Tia** - tiatheone@protonmail.com

Thank you for contributing to D&D Dice Roller Android! 🎲
