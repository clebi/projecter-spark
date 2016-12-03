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

import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.services.exceptions.AlreadyExistsException;

import java.util.concurrent.ExecutionException;

public interface IProjectService {

  /**
   * Add project.
   *
   * @param project     project to add
   * @param currentUser user adding the project
   * @throws AlreadyExistsException thrown when project already exists
   */
  Project addProject(Project project, String currentUser)
      throws AlreadyExistsException, ExecutionException, InterruptedException;

}
