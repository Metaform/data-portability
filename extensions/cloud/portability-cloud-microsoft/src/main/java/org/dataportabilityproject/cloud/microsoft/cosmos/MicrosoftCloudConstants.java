package org.dataportabilityproject.cloud.microsoft.cosmos;

/**
 * Constants used by the Microsoft cloud extension.
 */
public interface MicrosoftCloudConstants {

    String KEY_SPACE = "dataportability";

    String JOB_TABLE = KEY_SPACE + ".jobstore";

    String JOB_DATA_TABLE = KEY_SPACE + ".jobdatastore";
}
