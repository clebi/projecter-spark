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

package org.clebi.projecterspark.controllers;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.google.inject.Inject;

import org.clebi.projecterspark.authorization.AuthResponse;
import org.clebi.projecterspark.models.ErrorResponse;
import org.clebi.projecterspark.models.Project;
import org.clebi.projecterspark.models.serialize.JsonFactory;
import org.clebi.projecterspark.modules.annotations.OauthFilter;
import org.clebi.projecterspark.services.IProjectService;
import org.clebi.projecterspark.services.exceptions.AlreadyExistsException;
import org.clebi.projecterspark.services.exceptions.ServiceException;
import org.clebi.projecterspark.transformers.JsonResponseTransformer;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;

public class ProjecterController {

  private static final Logger logger = LoggerFactory.getLogger(ProjecterController.class);

  @Inject
  ProjecterController(IProjectService projectService, @OauthFilter Filter filter) {
    final Gson gson = JsonFactory.getGson();

    before(filter);

    post("/project/add-project", (request, response) -> {
      AuthResponse authResponse = request.attribute("authorization");
      System.out.println(authResponse);
      Project project = gson.fromJson(request.body(), Project.class);
      projectService.addProject(project, authResponse.getUsername());
      return project;
    }, new JsonResponseTransformer());

    exception(ServiceException.class, (exception, request, response) -> {
      logger.warn(exception.getMessage(), exception);
      response.status(HttpStatus.BAD_REQUEST_400);
      response.body(gson.toJson(new ErrorResponse("warning", exception.getMessage())));
    });

    exception(Exception.class, (exception, request, response) -> {
      logger.error(exception.getMessage(), exception);
      response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
      response.body(gson.toJson(new ErrorResponse("error", "unknown error")));
    });

    after(((request, response) -> {
      response.type("application/json");
    }));
  }

}
