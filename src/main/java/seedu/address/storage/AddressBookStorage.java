package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.model.TaskCollection;

/**
 * Represents a storage for {@link TaskCollection}.
 */
public interface AddressBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getAddressBookFilePath();

    /**
     * Returns TaskCollection data as a {@link ReadOnlyTaskCollection}. Returns {@code Optional.empty()}
     * if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskCollection> readAddressBook() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyTaskCollection> readAddressBook(Path filePath)
        throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskCollection} to the storage.
     *
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyTaskCollection addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyTaskCollection)
     */
    void saveAddressBook(ReadOnlyTaskCollection addressBook, Path filePath) throws IOException;

}
