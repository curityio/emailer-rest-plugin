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

package io.curity.emailer.rest;

import io.curity.emailer.rest.config.RestEmailProviderConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.data.email.RenderableEmail;
import se.curity.identityserver.sdk.email.Emailer;
import se.curity.identityserver.sdk.http.ContentType;
import se.curity.identityserver.sdk.http.HttpRequest;
import se.curity.identityserver.sdk.http.HttpResponse;
import se.curity.identityserver.sdk.service.Json;
import se.curity.identityserver.sdk.service.WebServiceClient;

import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The RestEmailer is an example implementation of an EmailProvider plugin, that uses a configured
 * REST service to deliver the email message model and the recipient to.
 * <p>
 * It is not advised to use this plugin in a production environment.
 * </p>
 */
public class RestEmailer implements Emailer
{
    private static final Logger _logger = LoggerFactory.getLogger(RestEmailer.class);

    private final Json _json;
    private final WebServiceClient _webServiceClient;

    private final String _sender;
    private final Optional<String> _apiKey;

    public RestEmailer(RestEmailProviderConfiguration configuration)
    {
        _json = configuration.json();
        _webServiceClient = configuration.webServiceClient();

        _sender = configuration.getSender();
        _apiKey = configuration.getHttpBasicAuthnCredential();
    }

    @Override
    public void sendEmail(RenderableEmail renderableEmail, String recipient)
    {
        Map<String, Object> model = renderableEmail.getModel();

        // The REST mailer is only interested in the model, and will not let the email render its contents
        String modelAsJsonString = _json.toJson(model);

        HttpRequest.BodyProcessor requestBodyProcessor = HttpRequest.fromString(modelAsJsonString, UTF_8);

        String path = Paths.get(_sender, recipient).toString();

        _logger.trace("Delivering email with subject '{}' for recipient '{}' on path '{}'",
                renderableEmail.getSubject(), recipient, path);

        HttpRequest.Builder requestBuilder = _webServiceClient.withPath(path).request();

        if (_apiKey.isPresent())
        {
            String encodedApiKey = Base64.getEncoder().encodeToString(
                    _apiKey.get().getBytes(UTF_8));
            requestBuilder = requestBuilder.header("Authorization", "Basic " + encodedApiKey);
        }

        HttpRequest request = requestBuilder.contentType(ContentType.JSON.getContentType())
                .body(requestBodyProcessor)
                .post();

        HttpResponse httpResponse = request.response();

        _logger.debug("Request to delivering email responded with statuscode {}", httpResponse.statusCode());
    }
}
