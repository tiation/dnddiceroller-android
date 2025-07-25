# D&D Dice Roller - Android

[![Build Status](https://img.shields.io/github/actions/workflow/status/tiation/dnddiceroller-android/ci.yml?branch=main)](https://github.com/tiation/dnddiceroller-android/actions)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Code Style](https://img.shields.io/badge/code%20style-ktlint-FF4081.svg)](https://ktlint.github.io/)

A modern, enterprise-grade Android application for rolling dice in Dungeons & Dragons campaigns. Built with Jetpack Compose, following Clean Architecture principles and modern Android development best practices.

## 🎯 Features

- **Dice Rolling Engine**: Roll standard D&D dice (d4, d6, d8, d10, d12, d20, d100)
- **Custom Dice Sets**: Create and save custom dice combinations
- **Roll History**: Track and review previous rolls
- **Campaign Management**: Organize dice sets by campaign
- **Offline Support**: Full functionality without internet connection
- **Accessibility**: Comprehensive support for screen readers and assistive technologies
- **Material Design 3**: Modern UI following Google's latest design guidelines

## 🏗️ Architecture

This project follows **Clean Architecture** principles with a modular structure:

```
dnddiceroller-android/
├── app/                    # Presentation layer (Activities, Fragments, ViewModels)
├── domain/                 # Business logic and use cases (pure Kotlin)
├── data/                   # Data layer (Room database, repositories)
├── core-ui/                # Design system and reusable UI components
├── common-test/            # Shared test utilities and fixtures
└── docs/                   # Architecture documentation
```

### Technology Stack

- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room with coroutines support
- **Async**: Kotlin Coroutines and Flow
- **Testing**: JUnit, MockK, Turbine, Compose Testing
- **Build**: Gradle with Kotlin DSL and Version Catalogs

## 🚀 Getting Started

### Prerequisites

- Android Studio Iguana | 2023.2.1 or later
- JDK 8 or later
- Android SDK with API level 24+

### Setup

1. **Clone the repository**
   ```bash
   git clone git@github.com:tiation/dnddiceroller-android.git
   cd dnddiceroller-android
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run tests**
   ```bash
   ./gradlew test
   ```

### Development Workflow

We follow GitFlow branching strategy:
- `main` - Production releases
- `develop` - Development integration
- `feature/*` - Feature development
- `release/*` - Release preparation
- `hotfix/*` - Critical fixes

## 📱 Screenshots

*Coming soon*

## 🧪 Testing

The project maintains high test coverage across all layers:

- **Unit Tests**: Domain logic and ViewModels
- **Integration Tests**: Repository and database operations
- **UI Tests**: Compose UI components and user flows

Run all tests:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📖 Documentation

- [Architecture Overview](docs/architecture.md)
- [API Documentation](docs/api.md)
- [Accessibility Guidelines](docs/accessibility.md)
- [Contributing Guide](CONTRIBUTING.md)

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) and [Code of Conduct](CODE_OF_CONDUCT.md).

### Quick Start for Contributors

1. Fork the repository
2. Create a feature branch
3. Make your changes following our coding standards
4. Add tests for new functionality
5. Run the full test suite
6. Submit a pull request

## 🔒 Security

For security vulnerabilities, please email security@tiation.net rather than opening a public issue.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🌟 Tiation Ecosystem

This project is part of the **Tiation ecosystem** of applications focused on creating accessible, high-quality software solutions. Connect with our other projects and tools designed to enhance user experiences across various platforms.

### Related Projects

- **ChaseWhiteRabbit NGO**: Social impact initiatives through technology
- **Development Tools**: Shared libraries and utilities
- **Enterprise Solutions**: Business-focused applications

## 👥 Team

**Built by:**
- **Garrett Dillman** - Lead Developer  
  - Email: garrett.dillman@gmail.com  
  - GitHub: garrett@sxc.codes

- **Tia** - Developer & Project Coordinator  
  - Email: tiatheone@protonmail.com  
  - Organization: ChaseWhiteRabbit NGO

## 📞 Support

- **Email**: support@tiation.net
- **Issues**: [GitHub Issues](https://github.com/tiation/dnddiceroller-android/issues)
- **Documentation**: [Project Wiki](https://github.com/tiation/dnddiceroller-android/wiki)

---

*Building accessible, enterprise-grade solutions for the D&D community.*
