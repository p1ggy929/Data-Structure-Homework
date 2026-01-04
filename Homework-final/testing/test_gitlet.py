#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Simple Gitlet test script
Tests basic Gitlet functionality
"""
import os
import subprocess
import shutil
import sys
import tempfile

def run_command(cmd, cwd=None):
    """Run gitlet command and return output"""
    try:
        # Get the project root directory (parent of testing)
        project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        gitlet_dir = os.path.join(project_root, "gitlet")
        
        # Set classpath to include project root
        env = os.environ.copy()
        if 'CLASSPATH' in env:
            env['CLASSPATH'] = project_root + os.pathsep + env['CLASSPATH']
        else:
            env['CLASSPATH'] = project_root
        
        result = subprocess.run(
            ["java", "-cp", env['CLASSPATH'], "gitlet.Main"] + cmd.split(),
            cwd=cwd,
            capture_output=True,
            text=True,
            timeout=30,
            env=env
        )
        return result.returncode, result.stdout, result.stderr
    except subprocess.TimeoutExpired:
        return -1, "", "Command timeout"
    except Exception as e:
        return -1, "", str(e)

def test_init():
    """Test repository initialization"""
    print("=" * 60)
    print("Test 1: Initialize repository (init)")
    print("=" * 60)
    
    with tempfile.TemporaryDirectory() as tmpdir:
        test_repo = os.path.join(tmpdir, "test_repo")
        os.makedirs(test_repo)
        
        code, stdout, stderr = run_command("init", cwd=test_repo)
        
        print(f"Exit code: {code}")
        print(f"Output:\n{stdout}")
        if stderr:
            print(f"Error:\n{stderr}")
        
        gitlet_dir = os.path.join(test_repo, ".gitlet")
        if os.path.exists(gitlet_dir):
            print("? .gitlet directory created")
            if os.path.isdir(gitlet_dir):
                contents = os.listdir(gitlet_dir)
                print(f"  .gitlet contents: {contents}")
            return True
        else:
            print("? .gitlet directory not created")
            return False

def test_add_and_commit():
    """Test adding files and committing"""
    print("\n" + "=" * 60)
    print("Test 2: Add file and commit (add, commit)")
    print("=" * 60)
    
    with tempfile.TemporaryDirectory() as tmpdir:
        test_repo = os.path.join(tmpdir, "test_repo")
        os.makedirs(test_repo)
        
        code, _, _ = run_command("init", cwd=test_repo)
        if code != 0:
            print("? Initialization failed")
            return False
        
        test_file = os.path.join(test_repo, "test.txt")
        with open(test_file, "w") as f:
            f.write("Hello, Gitlet!\n")
        
        print("\nRunning: gitlet add test.txt")
        code, stdout, stderr = run_command("add test.txt", cwd=test_repo)
        print(f"Exit code: {code}")
        if stdout:
            print(f"Output: {stdout}")
        if stderr:
            print(f"Error: {stderr}")
        
        if code != 0:
            print("? Add file failed")
            return False
        
        print("? File added successfully")
        
        print("\nRunning: gitlet commit \"Initial commit\"")
        code, stdout, stderr = run_command('commit "Initial commit"', cwd=test_repo)
        print(f"Exit code: {code}")
        if stdout:
            print(f"Output: {stdout}")
        if stderr:
            print(f"Error: {stderr}")
        
        if code == 0:
            print("? Commit successful")
            
            print("\nRunning: gitlet log")
            code, stdout, stderr = run_command("log", cwd=test_repo)
            if code == 0 and stdout:
                print("? Log command successful")
                print(f"Output:\n{stdout}")
                return True
            else:
                print("? Log command failed")
                return False
        else:
            print("? Commit failed")
            return False

def test_status():
    """Test status command"""
    print("\n" + "=" * 60)
    print("Test 3: Check status (status)")
    print("=" * 60)
    
    with tempfile.TemporaryDirectory() as tmpdir:
        test_repo = os.path.join(tmpdir, "test_repo")
        os.makedirs(test_repo)
        
        code, _, _ = run_command("init", cwd=test_repo)
        if code != 0:
            print("? Initialization failed")
            return False
        
        print("\nRunning: gitlet status")
        code, stdout, stderr = run_command("status", cwd=test_repo)
        print(f"Exit code: {code}")
        if stdout:
            print(f"Output:\n{stdout}")
        if stderr:
            print(f"Error: {stderr}")
        
        if code == 0:
            print("? Status command successful")
            return True
        else:
            print("? Status command failed")
            return False

def test_branch():
    """Test branch command"""
    print("\n" + "=" * 60)
    print("Test 4: Branch operations (branch)")
    print("=" * 60)
    
    with tempfile.TemporaryDirectory() as tmpdir:
        test_repo = os.path.join(tmpdir, "test_repo")
        os.makedirs(test_repo)
        
        code, _, _ = run_command("init", cwd=test_repo)
        if code != 0:
            return False
        
        test_file = os.path.join(test_repo, "test.txt")
        with open(test_file, "w") as f:
            f.write("test\n")
        
        run_command("add test.txt", cwd=test_repo)
        run_command('commit "first commit"', cwd=test_repo)
        
        print("\nRunning: gitlet branch test-branch")
        code, stdout, stderr = run_command("branch test-branch", cwd=test_repo)
        print(f"Exit code: {code}")
        if stdout:
            print(f"Output: {stdout}")
        if stderr:
            print(f"Error: {stderr}")
        
        if code == 0:
            print("? Branch creation successful")
            return True
        else:
            print("? Branch creation failed")
            return False

def main():
    """Run all tests"""
    print("\n" + "=" * 60)
    print("Gitlet Test Suite")
    print("=" * 60 + "\n")
    
    print("Checking compilation status...")
    if not os.path.exists("gitlet/Main.class"):
        print("Warning: No compiled .class files found")
        print("Please compile first: javac -cp . gitlet/*.java")
    else:
        print("? Found compiled files")
    
    results = []
    
    results.append(("Initialization", test_init()))
    results.append(("Add and Commit", test_add_and_commit()))
    results.append(("Status", test_status()))
    results.append(("Branch", test_branch()))
    
    print("\n" + "=" * 60)
    print("Test Summary")
    print("=" * 60)
    passed = sum(1 for _, result in results if result)
    total = len(results)
    
    for name, result in results:
        status = "? PASS" if result else "? FAIL"
        print(f"{name}: {status}")
    
    print(f"\nTotal: {passed}/{total} tests passed")
    
    return passed == total

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)
