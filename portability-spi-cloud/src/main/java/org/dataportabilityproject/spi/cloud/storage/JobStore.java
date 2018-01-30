package org.dataportabilityproject.spi.cloud.storage;

import org.dataportabilityproject.spi.cloud.types.PortabilityJob;
import org.dataportabilityproject.types.transfer.models.DataModel;

import java.io.InputStream;

/**
 * Implementations handle storage and retrieval of transfer job data.
 *
 * This class is intended to be implemented by extensions that support storage in various back-end services.
 */
public interface JobStore {

    /**
     * Creates a new job.
     */
    void create(PortabilityJob job);

    /**
     * Updates and existing job.
     *
     * REVIEW: Maybe there should just be state transition-specific methods, e.g cancel(), complete(), etc.?
     */
    void update(PortabilityJob job);

    /**
     * Removes a job.
     *
     * REVIEW: Maybe there should just be state transition-specific methods, e.g cancel(), complete(), etc.?
     */
    void remove(String id);

    /**
     * Returns the job for the id or null if not found.
     *
     * @param id the job id
     */
    PortabilityJob find(String id);

    /**
     * Stores the given model instance associated with a job.
     */
    <T extends DataModel> void create(String jobId, T model);

    /**
     * Updates the given model instance associated with a job.
     */
    <T extends DataModel> void update(String jobId, T model);

    /**
     * Returns a model instance for the id of the given type or null if not found.
     */
    <T extends DataModel> T findData(Class<T> type, String id);

    /**
     * Removes ther data model instance.
     */
    void removeData(String id);

    /**
     * Stores a stream associated with a job using the given key.
     */
    void create(String jobId, String key, InputStream stream);

    /**
     * Returns stream data for the given key.
     */
    InputStream getStream(String jobId, String key);


}
