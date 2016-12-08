// Copyright 2016 Clément Bizeau
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.clebi.projecterspark.services.events.kafka;

import com.google.inject.Inject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.clebi.projecterspark.configuration.GlobalConfig;
import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.models.serialize.KafkaJsonSerializer;
import org.clebi.projecterspark.modules.exceptions.ConfigurationException;
import org.clebi.projecterspark.modules.providers.ConfigCheckedProvider;
import org.clebi.projecterspark.services.events.ProjectEventService;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProjectEventService implements ProjectEventService {

  private KafkaProducer<String, Project> producer;

  /**
   * Create kafka project event service.
   *
   * @param configCheckedProvider configuration provider
   * @throws ConfigurationException error getting configuration
   */
  @Inject
  public KafkaProjectEventService(ConfigCheckedProvider<GlobalConfig> configCheckedProvider)
      throws ConfigurationException {
    GlobalConfig config = configCheckedProvider.get();
    Properties props = new Properties();
    String brokers = "";
    for (String broker : config.getKafka().getBrokers()) {
      brokers += broker;
    }
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());

    this.producer = new KafkaProducer<>(props);
  }

  @Override
  public void addProject(Project project) throws ExecutionException, InterruptedException {
    producer.send(new ProducerRecord<>("projects", project.getName(), project)).get();
  }
}
