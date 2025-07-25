# Custom Dice Rows Feature - Issue Tracker

**Branch:** `feature/custom-dice-rows`  
**Project:** DnD Dice Roller Android  
**Created:** $(date +%Y-%m-%d)

## 📋 Issue Summary

This document tracks all GitHub issues created for the Custom Dice Rows feature implementation, organized by priority and dependencies.

## 🔴 High Priority Issues (Foundation Refactoring)

### 1. Presentation Layer Separation
- **Issue Template:** `.github/ISSUE_TEMPLATE/custom-dice-rows/01-presentation-layer-refactoring.md`
- **Title:** `[REFACTOR] Presentation Layer Separation - Extract ViewModels`
- **Labels:** `enhancement`, `architecture`, `high-priority`, `custom-dice-rows`
- **Effort:** Medium (2-3 days)
- **Dependencies:** None
- **Blocks:** Feature Module Architecture

**Objective:** Extract ViewModels and separate UI logic from business logic

### 2. Feature Module Architecture  
- **Issue Template:** `.github/ISSUE_TEMPLATE/custom-dice-rows/02-feature-module-architecture.md`
- **Title:** `[REFACTOR] Feature Module Architecture - Proper Boundaries`
- **Labels:** `enhancement`, `architecture`, `high-priority`, `custom-dice-rows`
- **Effort:** High (4-5 days)
- **Dependencies:** Presentation Layer Separation
- **Blocks:** Navigation Architecture, DI Structure

**Objective:** Reorganize features into proper module boundaries with clean separation

## 🟡 Medium Priority Issues (Infrastructure)

### 3. Dependency Injection Structure
- **Issue Template:** `.github/ISSUE_TEMPLATE/custom-dice-rows/03-dependency-injection-structure.md`
- **Title:** `[REFACTOR] Dependency Injection Structure - Layered Organization`
- **Labels:** `enhancement`, `architecture`, `medium-priority`, `custom-dice-rows`
- **Effort:** Medium (3 days)
- **Dependencies:** Feature Module Architecture
- **Blocks:** Custom Dice Rows Implementation

**Objective:** Organize DI modules by architectural layer and feature

## 🚀 Feature Implementation

### 4. Custom Dice Rows Implementation
- **Issue Template:** `.github/ISSUE_TEMPLATE/custom-dice-rows/04-custom-dice-rows-implementation.md`
- **Title:** `[FEATURE] Custom Dice Rows - Core Implementation`
- **Labels:** `enhancement`, `feature`, `custom-dice-rows`
- **Effort:** High (1-2 weeks)
- **Dependencies:** All previous refactoring issues
- **Blocks:** None (Final implementation)

**Objective:** Implement the core custom dice rows feature with drag-and-drop functionality

## 📊 Progress Tracking

| Issue | Status | Assignee | Due Date | Progress |
|-------|--------|----------|----------|----------|
| Presentation Layer Separation | 📋 To Do | - | - | 0% |
| Feature Module Architecture | 📋 To Do | - | - | 0% |
| Dependency Injection Structure | 📋 To Do | - | - | 0% |
| Custom Dice Rows Implementation | 📋 To Do | - | - | 0% |

## 🔄 Dependency Flow

```
Presentation Layer Separation
         ↓
Feature Module Architecture
         ↓
Dependency Injection Structure
         ↓
Custom Dice Rows Implementation
```

## 📝 Creating Issues

To create these issues in GitHub:

1. **Navigate to your repository**
2. **Go to Issues tab**
3. **Click "New Issue"**
4. **Select the appropriate template from the custom-dice-rows folder**
5. **Fill in any additional details**
6. **Assign labels and milestones as needed**

## 🏷️ Labels Used

- `enhancement` - General improvement
- `architecture` - Architecture-related changes
- `feature` - New feature implementation
- `high-priority` - Critical path items
- `medium-priority` - Important but not blocking
- `custom-dice-rows` - Feature-specific label

## 🎯 Milestones Suggestion

Create GitHub milestones for:
- **Foundation Refactoring** (Issues 1-2)
- **Infrastructure Setup** (Issue 3)
- **Feature Implementation** (Issue 4)

## 📚 Related Documentation

- [Architecture Audit](./architecture-audit-custom-dice-rows.md)
- [Development Guide](./development/DEVELOPMENT_GUIDE.md)
- [Quality Assurance](./QUALITY_ASSURANCE.md)

## 🤝 Team Contacts

- **Garrett Dillman:** garrett@sxc.codes, garrett.dillman@gmail.com
- **Tia:** tiatheone@protonmail.com

---

**Built by:** Garrett Dillman & Tia | **Organization:** ChaseWhiteRabbit NGO
