package seedu.address.storage.XmlStorage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores deadline manager data in an XML file
 */
public class XmlFileStorage {

    /**
     * Saves the given deadline manager data to the specified file.
     */
    public static void saveDataToFile(Path file, XmlSerializableTaskCollection taskCollection)
        throws IOException {
        try {
            XmlUtil.saveDataToFile(file, taskCollection);
        } catch (JAXBException e) {
            throw new IOException("Could not write data to file.", e);
        }
    }

    /**
     * Returns deadline manager in the file or an empty deadline manager
     */
    public static XmlSerializableTaskCollection loadDataFromSaveFile(Path file)
        throws DataConversionException,
        FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableTaskCollection.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
