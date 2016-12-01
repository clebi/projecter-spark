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

package org.clebi.projecterspark;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import org.clebi.projecterspark.configuration.GlobalConfig;
import org.clebi.projecterspark.controllers.ProjecterController;
import org.clebi.projecterspark.modules.ConfigModule;
import org.clebi.projecterspark.modules.DaoModule;
import org.clebi.projecterspark.modules.FilterModule;
import org.clebi.projecterspark.modules.ServiceModule;
import org.clebi.projecterspark.modules.WsModule;
import org.clebi.projecterspark.modules.exceptions.ConfigurationException;
import org.clebi.projecterspark.modules.providers.ConfigCheckedProvider;
import spark.Spark;

public class App {

  /**
   * Main function.
   *
   * @param args program arguments
   */
  public static void main(String[] args) throws ConfigurationException {
    Injector injector = Guice.createInjector(
        new DaoModule(),
        new ServiceModule(),
        new ConfigModule(),
        new FilterModule(),
        new WsModule());
    final SparkConfig config = injector.getInstance(SparkConfig.class);
    final ProjecterController controller = injector.getInstance(ProjecterController.class);
  }

  private static class SparkConfig {

    @Inject
    public SparkConfig(ConfigCheckedProvider<GlobalConfig> configProvider) throws ConfigurationException {
      GlobalConfig config = configProvider.get();
      Spark.port(config.getProjecter().getPort());
    }
  }
}
