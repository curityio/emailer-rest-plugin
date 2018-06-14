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

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.curity.emailer.rest.config.RestEmailProviderConfiguration.HttpMethod.POST;

/**
 * The RestEmailer is an example implementation of an EmailProvider plugin, that uses a configured
 * REST service to send use the email model to send an email message to a recipient email address.
 * <p>
 * It is not adviced to use this plugin in a production environment.
 * </p>
 */
public class RestEmailer implements Emailer
{
    private static final Logger _logger = LoggerFactory.getLogger(RestEmailer.class);

    private final Json _json;
    private final WebServiceClient _webServiceClient;

    private final RestEmailProviderConfiguration.HttpMethod _httpMethod;
    private final String _path;

    public RestEmailer(RestEmailProviderConfiguration configuration)
    {
        _json = configuration.json();
        _webServiceClient = configuration.webServiceClient();

        _httpMethod = configuration.getHttpMethod();
        _path = configuration.getPath();
    }

    @Override
    public void sendEmail(RenderableEmail renderableEmail, String recipient)
    {
        Map<String, Object> model = renderableEmail.getModel();

        // The REST mailer is only interested in the model, and will not let the email render its contents
        String modelAsJson = _json.toJson(model);

        String subject = String.valueOf(
                model.getOrDefault("_subject", "Message from Curity"));

        _logger.trace("Delivering email with subject '{}' for recipient '{}'", subject, recipient);

        HttpRequest.BodyProcessor requestBodyProcessor = HttpRequest.fromString(modelAsJson, StandardCharsets.UTF_8);

        HttpRequest.Builder builder = _webServiceClient.withPath(_path)
                .request()
                .contentType(ContentType.JSON.getContentType())
                .body(requestBodyProcessor);

        HttpRequest httpRequest = _httpMethod.equals(POST) ? builder.post() : builder.get();

        HttpResponse httpResponse = httpRequest.response();

        _logger.info("Request to delivering email responded with statuscode {}", httpResponse.statusCode());
    }
}
