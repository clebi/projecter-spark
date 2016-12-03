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

package org.clebi.projecterspark.services;

import com.google.inject.Inject;

import org.clebi.projecterspark.daos.ProjectDao;
import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.services.events.ProjectEventService;
import org.clebi.projecterspark.services.exceptions.AlreadyExistsException;

import java.util.concurrent.ExecutionException;

public class ProjectService implements IProjectService {

  private ProjectDao projectDao;
  private ProjectEventService projectEventService;

  @Inject
  public ProjectService(ProjectDao projectDao, ProjectEventService projectEventService) {
    this.projectDao = projectDao;
    this.projectEventService = projectEventService;
  }

  @Override
  public Project addProject(Project project, String currentUser)
      throws AlreadyExistsException, ExecutionException, InterruptedException {
    if (!project.getMembers().contains(currentUser)) {
      project.getMembers().add(currentUser);
    }
    projectDao.addProject(project);
    projectEventService.addProject(project);
    return project;
  }
}
