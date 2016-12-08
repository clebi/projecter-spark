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
import org.apache.kafka.clients.producer.ProducerRecord;
import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.services.events.ProjectEventService;

import java.util.concurrent.ExecutionException;

/**
 * Project event service for kafka.
 */
public class KafkaProjectEventService implements ProjectEventService {

  private KafkaProducer<String, Object> producer;

  /**
   * Create kafka project event service.
   *
   * @param producer kafka event producer
   */
  @Inject
  public KafkaProjectEventService(KafkaProducer<String, Object> producer) {
    this.producer = producer;
  }

  @Override
  public void addProject(Project project) throws ExecutionException, InterruptedException {
    producer.send(new ProducerRecord<>("projects", project.getName(), project)).get();
  }
}
