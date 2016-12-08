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

package org.clebi.projecterspark.service.events;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.services.events.ProjectEventService;
import org.clebi.projecterspark.services.events.kafka.KafkaProjectEventService;
import org.clebi.projecterspark.utils.BaseTestCase;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProjectEventServiceTest extends BaseTestCase {

  @Mock
  private KafkaProducer<String, Object> producer;

  @Mock
  private Future<RecordMetadata> sendFuture;

  private static Project createTestProject() {
    String[] user_list = {"user1", "user2"};
    Map<String, String> fields = new HashMap<>();
    fields.put("field1", "value1");
    fields.put("field2", "value2");
    return new Project("test", new ArrayList<>(Arrays.asList(user_list)), fields);
  }

  @Test
  public void addProject() throws ExecutionException, InterruptedException {
    Project project = createTestProject();
    when(sendFuture.get()).thenReturn(null);
    when(producer.send(new ProducerRecord<>("projects", project.getName(), project))).thenReturn(sendFuture);
    ProjectEventService service = new KafkaProjectEventService(producer);
    service.addProject(project);
  }

  @Test(expected = ExecutionException.class)
  public void addProjectWithExecutionException() throws ExecutionException, InterruptedException {
    Project project = createTestProject();
    when(sendFuture.get()).thenThrow(new ExecutionException("mock", null));
    when(producer.send(any())).thenReturn(sendFuture);
    ProjectEventService service = new KafkaProjectEventService(producer);
    service.addProject(project);
  }

  @Test(expected = InterruptedException.class)
  public void addProjectWithInterruptedException() throws ExecutionException, InterruptedException {
    Project project = createTestProject();
    when(sendFuture.get()).thenThrow(new InterruptedException());
    when(producer.send(any())).thenReturn(sendFuture);
    ProjectEventService service = new KafkaProjectEventService(producer);
    service.addProject(project);
  }

}
