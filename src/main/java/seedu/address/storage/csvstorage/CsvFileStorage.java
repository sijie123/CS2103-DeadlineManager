package seedu.address.storage.csvstorage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.commons.util.FileUtil;

/**
 * Stores deadline manager data in an XML file
 */
public class CsvFileStorage {

    /**
     * Saves the given deadline manager data to the specified file.
     */
    public static void saveDataToFile(Path file, CsvSerializableTaskCollection taskCollection)
        throws IOException {
        try {
            FileUtil.writeToFile(file, taskCollection.toString());
        } catch (IOException e) {
            throw new IOException("Could not write data to file.", e);
        }
    }

}
