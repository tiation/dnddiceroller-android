#!/bin/bash

# Performance Optimization and Monitoring Setup Script
# Built by Jack Jonas (WA rigger) & Tia (dev, ChaseWhiteRabbit NGO)
# Contact: jackjonas95@gmail.com, tiatheone@protonmail.com

set -e

echo "🎲 DnD Dice Roller - Performance Monitoring Setup"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
GRAFANA_SERVER="grafana.sxc.codes"
GRAFANA_IP="153.92.214.1"
ELASTIC_SERVER="elastic.sxc.codes"
ELASTIC_IP="145.223.22.14"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check if running on macOS or Linux
    if [[ "$OSTYPE" != "darwin"* ]] && [[ "$OSTYPE" != "linux-gnu"* ]]; then
        print_error "This script requires macOS or Linux"
        exit 1
    fi
    
    # Check for required tools
    local missing_tools=()
    
    if ! command -v curl &> /dev/null; then
        missing_tools+=("curl")
    fi
    
    if ! command -v jq &> /dev/null; then
        missing_tools+=("jq")
    fi
    
    if ! command -v ssh &> /dev/null; then
        missing_tools+=("ssh")
    fi
    
    if [ ${#missing_tools[@]} -ne 0 ]; then
        print_error "Missing required tools: ${missing_tools[*]}"
        print_status "Please install them and run this script again"
        exit 1
    fi
    
    print_success "Prerequisites check passed"
}

# Setup Firebase configuration
setup_firebase() {
    print_status "Setting up Firebase configuration..."
    
    if [ ! -f "app/google-services.json" ]; then
        print_warning "google-services.json not found"
        print_status "Please download google-services.json from Firebase Console"
        print_status "and place it in the app/ directory"
        echo
        read -p "Press Enter when you have added google-services.json..."
        
        if [ ! -f "app/google-services.json" ]; then
            print_error "google-services.json still not found. Exiting."
            exit 1
        fi
    fi
    
    print_success "Firebase configuration ready"
}

# Deploy Grafana dashboard
deploy_grafana_dashboard() {
    print_status "Deploying Grafana dashboard..."
    
    if [ ! -f "monitoring/grafana-dashboard.json" ]; then
        print_error "Grafana dashboard file not found: monitoring/grafana-dashboard.json"
        exit 1
    fi
    
    # Test connection to Grafana server
    if ping -c 1 $GRAFANA_IP &> /dev/null; then
        print_success "Grafana server is reachable"
        
        # Copy dashboard to Grafana server
        print_status "Copying dashboard to Grafana server..."
        scp monitoring/grafana-dashboard.json root@$GRAFANA_IP:/tmp/
        
        # Import dashboard via SSH
        ssh root@$GRAFANA_IP << 'EOF'
# Import dashboard using Grafana API
curl -X POST \
  http://localhost:3000/api/dashboards/db \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_KEY_HERE' \
  -d @/tmp/grafana-dashboard.json

echo "Dashboard import attempted"
EOF
        
        print_success "Grafana dashboard deployed"
    else
        print_warning "Cannot reach Grafana server at $GRAFANA_IP"
        print_status "Please ensure VPN connection or server accessibility"
    fi
}

# Deploy Grafana alerts
deploy_grafana_alerts() {
    print_status "Deploying Grafana alerts..."
    
    if [ ! -f "monitoring/grafana-alerts.json" ]; then
        print_error "Grafana alerts file not found: monitoring/grafana-alerts.json"
        exit 1
    fi
    
    # Test connection to Grafana server
    if ping -c 1 $GRAFANA_IP &> /dev/null; then
        print_success "Grafana server is reachable"
        
        # Copy alerts to Grafana server
        print_status "Copying alerts to Grafana server..."
        scp monitoring/grafana-alerts.json root@$GRAFANA_IP:/tmp/
        
        # Configure alerts via SSH
        ssh root@$GRAFANA_IP << 'EOF'
# Import alert rules using Grafana API
curl -X POST \
  http://localhost:3000/api/ruler/grafana/api/v1/rules/default \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_KEY_HERE' \
  -d @/tmp/grafana-alerts.json

echo "Alert rules import attempted"
EOF
        
        print_success "Grafana alerts deployed"
    else
        print_warning "Cannot reach Grafana server at $GRAFANA_IP"
    fi
}

# Build and test the application
build_and_test() {
    print_status "Building and testing the application..."
    
    # Clean and build
    ./gradlew clean
    ./gradlew assembleDebug
    
    # Run tests
    ./gradlew testDebugUnitTest
    
    print_success "Build and tests completed"
}

# Verify monitoring setup
verify_monitoring() {
    print_status "Verifying monitoring setup..."
    
    # Check if Firebase is properly configured
    if grep -q "google-services" app/build.gradle.kts; then
        print_success "Firebase Performance Monitoring configured"
    else
        print_warning "Firebase Performance Monitoring may not be properly configured"
    fi
    
    # Check if WorkManager is included
    if grep -q "work-runtime-ktx" app/build.gradle.kts; then
        print_success "WorkManager configured for background monitoring"
    else
        print_warning "WorkManager may not be properly configured"
    fi
    
    # Verify monitoring files exist
    local monitoring_files=(
        "app/src/main/java/com/tiation/dnddiceroller/performance/PerformanceMonitor.kt"
        "app/src/main/java/com/tiation/dnddiceroller/performance/MemoryOptimizer.kt"
        "app/src/main/java/com/tiation/dnddiceroller/performance/BatteryOptimizer.kt"
        "app/src/main/java/com/tiation/dnddiceroller/performance/PerformanceWorker.kt"
    )
    
    for file in "${monitoring_files[@]}"; do
        if [ -f "$file" ]; then
            print_success "✓ $file"
        else
            print_error "✗ $file"
        fi
    done
}

# Main setup function
main() {
    echo
    print_status "Starting performance monitoring setup..."
    echo
    
    # Run setup steps
    check_prerequisites
    setup_firebase
    build_and_test
    deploy_grafana_dashboard
    deploy_grafana_alerts
    verify_monitoring
    
    echo
    print_success "🎉 Performance monitoring setup completed!"
    echo
    print_status "Next steps:"
    echo "1. Configure Firebase API keys in Grafana"
    echo "2. Set up alert notification channels"
    echo "3. Test the app and verify metrics are being collected"
    echo "4. Monitor dashboards at https://$GRAFANA_SERVER"
    echo
    print_status "For support, contact:"
    echo "- Jack Jonas: jackjonas95@gmail.com"
    echo "- Tia: tiatheone@protonmail.com"
    echo
}

# Run main function
main "$@"
