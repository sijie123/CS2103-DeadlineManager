package seedu.address.storage.CsvStorage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyTaskCollection;
import seedu.address.storage.TaskCollectionWriteStorage;

/**
 * A class to access TaskCollection data stored as an xml file on the hard disk.
 */
public class CsvTaskCollectionWriteStorage implements TaskCollectionWriteStorage {

    private static final Logger logger = LogsCenter.getLogger(CsvTaskCollectionWriteStorage.class);

    private Path filePath;

    public CsvTaskCollectionWriteStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getTaskCollectionFilePath() {
        return filePath;
    }


    @Override
    public void saveTaskCollection(ReadOnlyTaskCollection taskCollection) throws IOException {
        saveTaskCollection(taskCollection, filePath);
    }

    /**
     * Similar to {@link #saveTaskCollection(ReadOnlyTaskCollection)}
     *
     * @param filePath location of the data. Cannot be null
     */
    public void saveTaskCollection(ReadOnlyTaskCollection taskCollection, Path filePath) throws IOException {
        requireNonNull(taskCollection);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        CsvFileStorage.saveDataToFile(filePath, new CsvSerializableTaskCollection(taskCollection));
    }

}
