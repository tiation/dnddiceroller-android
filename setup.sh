#!/bin/bash

# DnD Dice Roller Android Setup Script
# This script sets up the development environment for the project

set -e  # Exit on any error

echo "🎲 Setting up DnD Dice Roller Android development environment..."

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js first."
    echo "   Visit: https://nodejs.org/"
    exit 1
fi

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm is not installed. Please install npm first."
    exit 1
fi

# Install npm dependencies for Git hooks
echo "📦 Installing npm dependencies for Git hooks..."
npm install

# Initialize Husky
echo "🪝 Setting up Git hooks with Husky..."
npx husky install

# Create pre-commit hook
echo "📝 Creating pre-commit hook..."
npx husky add .husky/pre-commit "npx lint-staged"

# Create pre-push hook
echo "🚀 Creating pre-push hook..."
npx husky add .husky/pre-push "./gradlew test"

# Make gradlew executable
echo "🔧 Making gradlew executable..."
chmod +x gradlew

# Create local.properties template if it doesn't exist
if [ ! -f "local.properties" ]; then
    echo "📝 Creating local.properties template..."
    cat > local.properties << EOF
# This file contains local properties for the Android project
# Add your SDK path and any local configuration here

# SDK Path (update with your actual Android SDK path)
sdk.dir=/Users/\$USER/Library/Android/sdk

# Enable Compose compiler metrics for development (optional)
# composeCompilerReports=true
# composeCompilerMetrics=true

# Signing configuration (uncomment and configure for release builds)
# RELEASE_STORE_FILE=path/to/your/release.keystore
# RELEASE_STORE_PASSWORD=your_store_password
# RELEASE_KEY_ALIAS=your_key_alias
# RELEASE_KEY_PASSWORD=your_key_password
EOF
fi

# Run initial quality checks to ensure everything is set up correctly
echo "🔍 Running initial quality checks..."
./gradlew qualityCheck || {
    echo "⚠️  Quality checks failed. This is normal for a new project."
    echo "   Run './gradlew spotlessApply' to fix formatting issues."
}

echo ""
echo "✅ Setup complete! Your development environment is ready."
echo ""
echo "📋 Available commands:"
echo "   ./gradlew qualityCheck      - Run all quality checks"
echo "   ./gradlew spotlessApply     - Fix code formatting"
echo "   ./gradlew ktlintFormat      - Fix Kotlin style issues"
echo "   ./gradlew detekt            - Run static analysis"
echo "   ./gradlew assembleDevDebug  - Build dev debug APK"
echo "   npm run lint                - Run all linting tools"
echo "   npm run lint:fix            - Fix linting issues"
echo ""
echo "🎯 Build variants available:"
echo "   devDebug, devRelease        - Development environment"
echo "   stagingDebug, stagingRelease - Staging environment"
echo "   prodDebug, prodRelease      - Production environment"
echo ""
echo "🔧 To enable Compose compiler metrics (for performance analysis):"
echo "   ./gradlew assembleDebug -PcomposeCompilerMetrics=true -PcomposeCompilerReports=true"
echo ""
echo "Happy coding! 🎲✨"
