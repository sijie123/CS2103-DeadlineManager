package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalTasks.ALICE;

import org.junit.Test;

import seedu.address.model.task.Task;

public class ImportConflictResolverTest {

    @Test
    public void addDuplicate_added() {
        DuplicateImportConflictResolver resolver = new DuplicateImportConflictResolver();
        ResolverTester tester = new ResolverTester();
        resolver.resolve((task) -> tester.addTask(task), (task) -> tester.removeTask(task), ALICE);
        assertEquals(tester.didAddTask(), true);
        assertEquals(tester.didRemoveTask(), false);
        assertEquals(tester.didIgnoreTask(), false);
    }

    @Test
    public void addIgnore_ignored() {
        IgnoreImportConflictResolver resolver = new IgnoreImportConflictResolver();
        ResolverTester tester = new ResolverTester();
        resolver.resolve((task) -> tester.addTask(task), (task) -> tester.removeTask(task), ALICE);
        assertEquals(tester.didAddTask(), false);
        assertEquals(tester.didRemoveTask(), false);
        assertEquals(tester.didIgnoreTask(), true);
    }

    @Test
    public void addOverwrite_overwritten() {
        OverwriteImportConflictResolver resolver = new OverwriteImportConflictResolver();
        ResolverTester tester = new ResolverTester();
        resolver.resolve((task) -> tester.addTask(task), (task) -> tester.removeTask(task), ALICE);
        assertEquals(tester.didAddTask(), true);
        assertEquals(tester.didRemoveTask(), true);
        assertEquals(tester.didIgnoreTask(), false);
    }

    class ResolverTester {
        private boolean hasAdded;
        private boolean hasRemoved;

        public ResolverTester() {
            this.hasAdded = false;
            this.hasRemoved = false;
        }

        public void addTask(Task task) {
            this.hasAdded = true;
        }

        public void removeTask(Task task) {
            this.hasRemoved = true;
        }

        public boolean didAddTask() {
            return this.hasAdded;
        }

        public boolean didRemoveTask() throws AssertionError {
            return this.hasRemoved;
        }

        public boolean didIgnoreTask() throws AssertionError {
            return (!this.hasAdded) && (!this.hasRemoved);
        }
    }
}
