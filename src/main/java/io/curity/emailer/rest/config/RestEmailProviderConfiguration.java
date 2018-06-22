/*
 *  Copyright 2018 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.emailer.rest.config;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.DefaultEnum;
import se.curity.identityserver.sdk.config.annotation.DefaultString;
import se.curity.identityserver.sdk.config.annotation.Description;
import se.curity.identityserver.sdk.service.Json;
import se.curity.identityserver.sdk.service.WebServiceClient;

import java.util.Optional;

public interface RestEmailProviderConfiguration extends Configuration
{
    WebServiceClient webServiceClient();

    Json json();

    @Description("An RFC822 email address that will be used as the from name when sending emails")
    String getSender();

    @Description("Optional HTTP Basic Authentication credential to use with the HTTP request, " +
            "i.e. the value being provided as Authorization: Basic Base64Enc<HttpBasicAuthnCredential>")
    Optional<String> getHttpBasicAuthnCredential();
}
