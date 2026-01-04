@echo off
REM Gitlet Test Runner for Windows
echo ========================================
echo Gitlet Test Runner
echo ========================================
echo.

REM Check if Python is available
python --version >nul 2>&1
if errorlevel 1 (
    echo Error: Python is not installed or not in PATH
    echo Please install Python 3 and try again
    pause
    exit /b 1
)

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java and try again
    pause
    exit /b 1
)

REM Compile Java files if needed
echo Compiling Java files...
cd ..
javac -cp . gitlet/*.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!
echo.

REM Run tests
cd testing
echo Running tests...
echo.
python test_gitlet.py

echo.
echo Tests completed!
pause

