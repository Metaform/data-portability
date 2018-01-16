/*
 * Copyright 2018 The Data-Portability Project Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dataportabilityproject.spi.transfer;

import org.dataportabilityproject.datatransfer.types.auth.AuthData;
import org.dataportabilityproject.datatransfer.types.models.DataModel;

/**
 * Exports data from a source service.
 */
public interface Exporter<A extends AuthData, T extends DataModel> {

    /**
     * Performs an export operation.
     */
    ExportResult<T> export(A authData);

    /**
     * Performs an export operation, starting from the continuation.
     *
     * @param authData authentication data for the operation
     * @param continuationInformation continuation data
     */
    ExportResult<T> export(A authData, Object continuationInformation); // REVIEW: The original throws IOException. Continue to use checked exceptions or use unchecked?
}