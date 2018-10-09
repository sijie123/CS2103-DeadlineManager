package seedu.address.model.attachment;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class AttachmentTest {

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


}
