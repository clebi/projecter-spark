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
import com.mongodb.MongoClient;

import org.clebi.projecterspark.configuration.GlobalConfig;
import org.clebi.projecterspark.daos.ProjectDao;
import org.clebi.projecterspark.daos.mongo.MongoProjectDao;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;


public class DaoModule extends AbstractModule {

  private GlobalConfig config;

  public DaoModule(GlobalConfig config) {
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(ProjectDao.class).to(MongoProjectDao.class);
  }

  @Provides
  @Singleton
  Datastore mongoProvider() {
    final Morphia morphia = new Morphia();

    morphia.mapPackage("org.clebi.projecterspark.model");

    final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 27017), "projecter");
    datastore.ensureIndexes();
    return datastore;
  }

}
