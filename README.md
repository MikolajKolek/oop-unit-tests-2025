# oop-unit-tests
Repository for sharing unit tests for the OOP course

There is a `clone.sh` bash script available which clones the skeleton and tests for a task. Usage: `./clone.sh {task} {dest}` - creates a `{task}` directory in `{dest}`

# How to contribute with tests

## Git

1. If you don't have the repo on your machine:
    1. Fork the repository on Github
    2. Clone your repository into your machine
    3. Add a remote upstream `git remote add upstream https://github.com/mhorod/oop-unit-tests`


2. Sync local repository with remote
    - If you haven't commited your changes:
        1. Switch to `main` by running `git checkout main`
        2. Execute `git pull upstream main` to sync changes
    - If you commited your changes rebase instead of merging:
        1. `git fetch`
        2. `git rebase origin/main`
        
3. Create a branch for your changes and switch to it
    1. `git branch {branch_name}`
    2. `git checkout {branch_name}`

4. Add your tests to directory `oop-unit-tests/satori/{task}/`
    Don't create new directories there.

5. Make sure your tests compile by executing `bash check.sh`
    - note: you can build a single task by providing the task name as an argument, e.g. `bash check.sh A`
6. Commit your changes and push them
7. Go to your repository on Github and create a pull request

## Code style
This is recommended style guide for writing tests. While not necessary, following it will be highly appreciated.

### Classes
In a single java project there can exist only one class of a given name. In order to avoid name conflicts, prefix your test class with name and surname., e.g. `class JohnDoeTests {}`

With `JUnit` you can group your tests in nested classes with `RunWith(@Enclosed.class)`

Example test with nested classes:
```java
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class ExampleTest
{
    public static class ExampleGroupTest 
    {
        @Test
        public void example_test_method() {}
    }
}
```

### Methods
Use `snake_case` for naming test methods and use names that convey expected behavior.
e.g. `size` is a bad name for a test while `size_of_new_list_is_zero` is better.

Try to make your tests concise and focused on a single thing.
Test behavior rather than interface.
If you have many assertions consider splitting the test into more functions.

# Running tests in IntelliJ

Put files with unit tests in the `src/test/java/` directory in your solution project.

1. In the toolbar click between the `Build` and `Run` icons
2. `Edit Configurations`
3. `Add new configuration > JUnit`
4. `JUnit > Add new run configuration`
5. Select `Class` to run single test file
6. Select `All in directory` and select the directory to run all tests
7. Press the `Run` icon to run tests
