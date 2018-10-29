package seedu.address.storage.xmlstorage;

import java.io.File;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attachment.Attachment;

/**
 * JAXB-friendly adapted version of Attachment.
 */
public class XmlAdaptedAttachment {

    @XmlValue
    private String filePath;

    /**
     * Constructs an XmlAdaptedAttachment. This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAttachment() {
    }

    /**
     * Constructs a {@code XmlAdaptedAttachment} with the given {@code filePath}.
     */
    public XmlAdaptedAttachment(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Converts a given Attachment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedAttachment(Attachment source) {
        filePath = source.file.getAbsolutePath();
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Tag object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *                               task
     */
    public Attachment toModelType() throws IllegalValueException {
        //TODO: Check if attachment file exists
        return new Attachment(new File(filePath));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAttachment)) {
            return false;
        }

        return filePath.equals(((XmlAdaptedAttachment) other).filePath);
    }
}
