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
import com.google.inject.Injector;

import org.clebi.projecterspark.configuration.GlobalConfig;
import org.clebi.projecterspark.configuration.YamlGlobalConfig;
import org.clebi.projecterspark.controllers.ProjecterController;
import org.clebi.projecterspark.modules.ConfigModule;
import org.clebi.projecterspark.modules.DaoModule;
import org.clebi.projecterspark.modules.EventModule;
import org.clebi.projecterspark.modules.FilterModule;
import org.clebi.projecterspark.modules.ServiceModule;
import org.clebi.projecterspark.modules.WsModule;
import org.clebi.projecterspark.modules.exceptions.ConfigurationException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import spark.Spark;

import java.io.File;
import java.io.FileInputStream;

public class App {

  /**
   * Main function.
   *
   * @param args program arguments
   */
  public static void main(String[] args) throws ConfigurationException {
    GlobalConfig config = getConfig();
    Injector injector = Guice.createInjector(
        new ConfigModule(config),
        new DaoModule(config),
        new EventModule(config),
        new ServiceModule(),
        new FilterModule(),
        new WsModule());
    Spark.port(config.getProjecter().getPort());
    injector.getInstance(ProjecterController.class);
  }

  private static GlobalConfig getConfig() throws ConfigurationException {
    String confPath = System.getProperty("global.config.path");
    if (confPath == null) {
      throw new ConfigurationException("missing config path");
    }
    try (FileInputStream configFile = new FileInputStream(new File(confPath))) {
      return (YamlGlobalConfig) new Yaml(
          new Constructor(YamlGlobalConfig.class)).load(configFile);
    } catch (Exception exc) {
      throw new ConfigurationException("unable to read configuration file", exc);
    }
  }

}
