/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.api.core.auth;

import com.datastax.oss.driver.api.core.metadata.EndPoint;
import com.datastax.oss.driver.internal.core.util.Strings;
import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple plaintext {@link AuthProvider} that receives the credentials programmatically instead of
 * pulling them from the configuration.
 *
 * <p>To use this class, create an instance with the appropriate credentials to use and pass it to
 * your session builder:
 *
 * <pre>
 * AuthProvider authProvider = new AtomicPlainTextAuthProvider("...", "...");
 * CqlSession session =
 *     CqlSession.builder()
 *         .addContactEndPoints(...)
 *         .withAuthProvider(authProvider)
 *         .build();
 * </pre>
 *
 * <p>Similar to {@link PlainTextAuthProviderBase}, this class allows for changing credentials
 * during runtime as shown below:
 *
 * <pre>
 *   authProvider.setCredentials("newUsername", "newPassword")
 * </pre>
 *
 * However, unlike {@link PlainTextAuthProviderBase}, username and password must be set together by
 * calling {@link #setCredentials} function to ensure that username and password always remain
 * consistent with each other.
 */
@ThreadSafe
public class AtomicPlainTextAuthProvider extends PlainTextAuthProviderBase {

  private volatile Credentials credentials;

  private static final Logger LOG = LoggerFactory.getLogger(AtomicPlainTextAuthProvider.class);

  public AtomicPlainTextAuthProvider(@NonNull String username, @NonNull String password) {
    super("");
    this.credentials = createNewCredentials(username, password);
  }

  public void setCredentials(@NonNull String username, @NonNull String password) {
    LOG.info("Setting credentials in auth provider");
    this.credentials = createNewCredentials(username, password);
  }

  @Override
  protected Credentials getCredentials(EndPoint endPoint, String serverAuthenticator) {
    LOG.info("Getting credentials from auth provider");
    return credentials;
  }

  private static Credentials createNewCredentials(String username, String password) {
    return new Credentials(
        Strings.requireNotEmpty(username, "username").toCharArray(),
        Strings.requireNotEmpty(password, "password").toCharArray());
  }
}
