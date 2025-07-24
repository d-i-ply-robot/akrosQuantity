#!/bin/bash

echo "🏗️  Construction Quantities CLI Application Demo"
echo "==============================================="
echo ""

echo "📋 Available Commands:"
echo "   - help: Show all available commands"
echo "   - create-project --name <name>: Create a new project"  
echo "   - list-projects: List all projects"
echo "   - delete-project --id <id>: Delete a project"
echo ""

echo "🧪 Testing CLI Commands..."
echo ""

echo "1️⃣  Testing help command:"
echo "----------------------------------------"
./gradlew bootRun --args='help' --console=plain | grep -A 20 "AVAILABLE COMMANDS"
echo ""

echo "2️⃣  Testing list-projects (should be empty initially):"
echo "----------------------------------------"
./gradlew bootRun --args='list-projects' --console=plain | grep -A 5 "Projects:"
echo ""

echo "3️⃣  Creating test projects:"
echo "----------------------------------------"
echo "Creating Project 1..."
RESULT1=$(./gradlew bootRun --args='create-project --name TestProject1' --console=plain 2>&1 | grep "Project created")
echo "$RESULT1"

echo "Creating Project 2..."
RESULT2=$(./gradlew bootRun --args='create-project --name AnotherProject' --console=plain 2>&1 | grep "Project created")
echo "$RESULT2"
echo ""

echo "✅ CLI Application Test Complete!"
echo ""
echo "💡 Note: Each command runs in a separate session with in-memory database,"
echo "   so projects don't persist between commands. In a real interactive session,"
echo "   all projects would be available within that session."
echo ""
echo "🚀 To run the interactive shell manually:"
echo "   ./gradlew bootRun --console=plain"
echo "   (Note: Interactive mode may have terminal compatibility issues in some environments)" 