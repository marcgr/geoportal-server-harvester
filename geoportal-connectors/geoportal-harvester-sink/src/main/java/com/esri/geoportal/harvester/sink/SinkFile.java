/*
 * Copyright 2016 Esri, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esri.geoportal.harvester.sink;

import com.esri.geoportal.harvester.api.base.SimpleDataReference;
import com.esri.geoportal.commons.constants.MimeType;
import com.esri.geoportal.commons.constants.MimeTypeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import org.apache.commons.io.IOUtils;

/**
 * Sink file.
 */
/*package*/ class SinkFile {
  private final SinkBroker broker;
  private final Path file;

  /**
   * Creates instance of Sink file.
   * @param broker broker
   * @param file file
   */
  public SinkFile(SinkBroker broker, Path file) {
    this.broker = broker;
    this.file = file;
  }

  /**
   * Reads content.
   * @return content reference
   * @throws IOException if reading content fails
   * @throws URISyntaxException if file url is an invalid URI
   */
  public SimpleDataReference readContent() throws IOException, URISyntaxException {
    Date lastModifiedDate = readLastModifiedDate();
    MimeType contentType = readContentType();
    try (InputStream input = Files.newInputStream(file, StandardOpenOption.READ)) {
      SimpleDataReference ref = new SimpleDataReference(broker.getBrokerUri(), broker.getEntityDefinition().getLabel(), file.toAbsolutePath().toString(), lastModifiedDate, file.toUri(), broker.td.getSource().getRef(), broker.td.getRef());
      ref.addContext(contentType, IOUtils.toByteArray(input));
      return ref;
    }
  }
  
  /**
   * Deletes file.
   * @throws IOException if unable to delete file.
   */
  public void delete() throws IOException {
    file.toFile().delete();
  }

  /**
   * Reads last modified date.
   * @return last modified date
   */
  private Date readLastModifiedDate() throws IOException {
    return new Date(Files.getLastModifiedTime(file).toMillis());
  }
  
  /**
   * Reads content type.
   * @return content type or <code>null</code> if unable to read content type
   */
  private MimeType readContentType() {
    try {
      String strFileUrl = file.toAbsolutePath().toString();
      int lastDotIndex = strFileUrl.lastIndexOf(".");
      String ext = lastDotIndex>=0? strFileUrl.substring(lastDotIndex+1): "";
      return MimeTypeUtils.mapExtension(ext);
    } catch (Exception ex) {
      return null;
    }
  }
  
  @Override
  public String toString() {
    return file.toString();
  }
}
