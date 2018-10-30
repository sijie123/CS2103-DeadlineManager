package seedu.address.model.attachment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.testutil.Assert;

public class AttachmentTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Attachment(null));
    }


    @Test
    public void equality_relativeAndRelativeFilePath() {
        File dummyFileRelativePath = new File("hello.txt");
        File dummyFileAbsolutePath = new File(dummyFileRelativePath.getAbsolutePath());

        Attachment relativeAttachment = new Attachment(dummyFileRelativePath);
        Attachment absoluteAttachment = new Attachment(dummyFileAbsolutePath);

        assertEquals(relativeAttachment, absoluteAttachment);
    }

    @Test
    public void isReadable_returnFalse() throws IOException {
        File testFolder = folder.newFolder();
        Attachment attachment = new Attachment(testFolder);
        assertFalse(attachment.isReadable());

    }

}
