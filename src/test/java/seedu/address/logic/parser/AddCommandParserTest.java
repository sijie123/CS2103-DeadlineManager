package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.FREQUENCY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.FREQUENCY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_FREQUENCY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FREQUENCY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.task.Frequency.NO_FREQUENCY;
import static seedu.address.model.task.Priority.NO_PRIORITY;
import static seedu.address.testutil.TypicalTasks.AMY;
import static seedu.address.testutil.TypicalTasks.BOB;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Frequency;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

public class AddCommandParserTest {

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresentSomeFieldsDuplicate_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + NAME_DESC_BOB
                + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB + TAG_DESC_FRIEND, expectedMessage);

        // multiple priorities
        assertParseFailure(parser, NAME_DESC_BOB + PRIORITY_DESC_AMY + PRIORITY_DESC_BOB
                + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB + TAG_DESC_FRIEND, expectedMessage);

        // multiple frequencies
        assertParseFailure(parser, NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_AMY
                + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB + TAG_DESC_FRIEND, expectedMessage);

        // multiple deadlines
        assertParseFailure(parser, NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB
                + DEADLINE_DESC_AMY + DEADLINE_DESC_BOB + TAG_DESC_FRIEND, expectedMessage);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        Task expectedTask = new TaskBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB
                        + DEADLINE_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedTask));

        // multiple tags are ok - all accepted
        Task expectedTaskMultipleTags = new TaskBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB
                        + DEADLINE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedTaskMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags, no priority, no frequency
        Task expectedTask = new TaskBuilder(AMY)
                .withTags()
                .withPriority(NO_PRIORITY)
                .withFrequency(NO_FREQUENCY)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_AMY + DEADLINE_DESC_AMY,
                new AddCommand(expectedTask));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String
                .format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB,
                expectedMessage);

        // missing deadline prefix
        assertParseFailure(parser, NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB + VALID_DEADLINE_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PRIORITY_BOB + VALID_FREQUENCY_BOB
                + VALID_DEADLINE_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid priority
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_PRIORITY_DESC + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Priority.MESSAGE_PRIORITY_CONSTRAINTS);

        // invalid frequency
        assertParseFailure(parser,
                NAME_DESC_BOB + PRIORITY_DESC_BOB + INVALID_FREQUENCY_DESC + DEADLINE_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Frequency.MESSAGE_FREQUENCY_CONSTRAINTS);

        // invalid deadline
        assertParseFailure(parser,
                NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB + INVALID_DEADLINE_DESC
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Deadline.MESSAGE_DEADLINE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
                NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB
                        + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                INVALID_NAME_DESC + INVALID_PRIORITY_DESC + FREQUENCY_DESC_BOB + DEADLINE_DESC_BOB,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PRIORITY_DESC_BOB + FREQUENCY_DESC_BOB
                        + DEADLINE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
