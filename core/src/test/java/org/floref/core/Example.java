/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.floref.core;

import org.floref.core.dsl.flow.Flows;

/**
 * @author Cristian Donoiu
 */
public class Example {
  // 1. Interface containing flows.
  interface MyFlows {
    String runJob(Job job);  // flow 1
    void recordJob(Job job); // flow 2
  }
  static class Job { }
  static class FooService {
    public Job validate(Job job) { return job; }
    public String sendTo3rdParty(Job job) {return "done";}
    public void record(Job job) {System.out.println("recorded");}
  }

  public static void main(String args[]) {
    // 2. Define and build.
    FooService service = new FooService();
    MyFlows flows = Flows.from(MyFlows::runJob)
        .to(service::validate)     // run instance::method or class::method (instance injectable)
        .fork(MyFlows::recordJob)  // run in separate thread, do not wait for result
        .retry(service::sendTo3rdParty).times(10).delay(1000)  // retry external calls
        .build();

    // Another flow used in the one above.
    Flows.from(MyFlows::recordJob)
        .to(service::record)
        .build();

    // 3. Run.
    String status = flows.runJob(new Job());
    System.out.println(status); // done
  }
}