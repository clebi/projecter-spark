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

package org.clebi.projecterspark.service;

import org.clebi.projecterspark.daos.ProjectDao;
import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.services.ProjectService;
import org.clebi.projecterspark.services.exceptions.AlreadyExistsException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProjectServiceTest {

  private static Project createTestProject() {
    String[] user_list = {"user1", "user2"};
    Map<String, String> fields = new HashMap<>();
    fields.put("field1", "value1");
    fields.put("field2", "value2");
    return new Project("test", new ArrayList<>(Arrays.asList(user_list)), fields);
  }

  @Test
  public void testAddProject() throws AlreadyExistsException {
    ProjectDao projectDao = Mockito.mock(ProjectDao.class);
    Project projectToAdd = createTestProject();
    ProjectService projectService = new ProjectService(projectDao);
    projectService.addProject(projectToAdd, projectToAdd.getMembers().get(0));
    Mockito.verify(projectDao).addProject(projectToAdd);
  }

  @Test
  public void testAddProjectWithNoUsers() throws AlreadyExistsException {
    String user = "missing_user";
    ProjectDao projectDao = Mockito.mock(ProjectDao.class);
    Project projectToAdd = createTestProject();
    ProjectService projectService = new ProjectService(projectDao);
    Project project = projectService.addProject(projectToAdd, user);
    Mockito.verify(projectDao).addProject(projectToAdd);
    Assert.assertThat(project.getMembers(), CoreMatchers.hasItem(user));
  }

}
