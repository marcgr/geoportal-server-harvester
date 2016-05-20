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
package com.esri.geoportal.harvester.gpt;

import com.esri.geoportal.commons.gpt.client.Client;
import com.esri.geoportal.harvester.api.defs.EntityDefinition;
import com.esri.geoportal.harvester.api.defs.UITemplate;
import com.esri.geoportal.harvester.api.ex.InvalidDefinitionException;
import com.esri.geoportal.harvester.api.specs.OutputConnector;
import static com.esri.geoportal.harvester.gpt.GptBrokerDefinitionAdaptor.P_HOST_URL;
import static com.esri.geoportal.harvester.gpt.GptBrokerDefinitionAdaptor.P_USER_NAME;
import static com.esri.geoportal.harvester.gpt.GptBrokerDefinitionAdaptor.P_USER_PASSWORD;
import java.util.ArrayList;
import java.util.List;

/**
 * GPT connector.
 */
public class GptConnector implements OutputConnector<GptBroker> {
  public static final String TYPE = "GPT";

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public UITemplate getTemplate() {
    List<UITemplate.Argument> arguments = new ArrayList<>();
    arguments.add(new UITemplate.StringArgument(P_HOST_URL, "URL", true));
    arguments.add(new UITemplate.StringArgument(P_USER_NAME, "User name"));
    arguments.add(new UITemplate.StringArgument(P_USER_PASSWORD, "User password") {
      public boolean isPassword() {
        return true;
      }
    });
    return new UITemplate("GPT", "Geoportal Server New Generation", arguments);
  }

  @Override
  public GptBroker createBroker(EntityDefinition definition) throws InvalidDefinitionException {
    GptBrokerDefinitionAdaptor adaptor = new GptBrokerDefinitionAdaptor(definition);
    Client client = new Client(adaptor.getHostUrl(), adaptor.getUserName(), adaptor.getUserName());
    return new GptBroker(this, adaptor, client);
  }
  
}
