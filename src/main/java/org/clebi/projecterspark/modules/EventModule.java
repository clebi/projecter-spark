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

package org.clebi.projecterspark.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.clebi.projecterspark.configuration.GlobalConfig;
import org.clebi.projecterspark.models.serialize.KafkaJsonSerializer;

import java.util.Properties;

public class EventModule extends AbstractModule {

  private GlobalConfig config;

  public EventModule(GlobalConfig config) {
    this.config = config;
  }

  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  KafkaProducer<String, Object> kafkaProvider() {
    Properties props = new Properties();
    StringBuilder brokersBuilder = new StringBuilder();
    for (String broker : config.getKafka().getBrokers()) {
      brokersBuilder.append(broker);
      brokersBuilder.append(',');
    }
    brokersBuilder.deleteCharAt(brokersBuilder.length() - 1);
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersBuilder.toString());
    props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());

    return new KafkaProducer<String, Object>(props);
  }

}
