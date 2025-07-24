#!/bin/bash

echo "🏗️  Construction Quantities Interactive CLI"
echo "==========================================="
echo ""
echo "🔧 Rebuilding project to ensure latest changes are included..."
echo ""

# Clean and build the project to ensure latest changes
./gradlew clean build --refresh-dependencies

if [ $? -eq 0 ]; then
    echo "✅ Build successful! Starting interactive shell..."
    echo "Type 'help' for available commands, 'quit' or 'exit' to leave."
    echo ""
    
    # Run the application directly without Spring Boot
    java -cp build/classes/java/main com.akros.construction.InteractiveShell
else
    echo "❌ Build failed! Please fix the compilation errors and try again."
    exit 1
fi 