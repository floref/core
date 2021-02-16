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

package org.floref.core.dsl.command.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.MethodReferenceCommand;
import org.floref.core.dsl.flow.impex.FlowStep;
import org.floref.core.exception.FlowTimeoutException;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.run.CommandContext;
import org.floref.core.flow.run.CommandRunner;
import org.floref.core.flow.run.FlowSession;
import org.floref.core.flow.run.FlowUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GroupCommandUtil extends ParentCommand {

  private static final Log LOG = LogFactory.getLog(GroupCommandUtil.class);

  protected MethodReferenceCommand aggregator;
  protected Long timeout;
  protected Boolean stopOnException;

  //public List defaultAggregator(Object currentResult, List results, Exception exception) {
  public static List defaultAggregator(Object currentResult, List results, Exception e) {
    if (e == null) {
      results.add(currentResult);
      LOG.debug("Intermediary result " + currentResult.toString());
    }
    return results;
  }

  public GroupCommandUtil() {
    // Set default aggregator.
    aggregator = new MethodReferenceCommand(GroupCommandUtil::defaultAggregator);
  }

  @Override
  public boolean isParent() {
    return true;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public void setAggregator(MethodReference methodReference) {
    aggregator = new MethodReferenceCommand(methodReference);
  }

  public void run(CommandContext commandContext, List<CommandCallable> callables) throws Exception {
    GroupCommandUtil me = this;
    //runChildren(commandContext);

    AtomicBoolean isTimeout = new AtomicBoolean(false);
    AtomicBoolean forceStop = new AtomicBoolean(false);
    AtomicInteger atomicInteger = new AtomicInteger(0);
    List<Future> futureList = new ArrayList();
    ScheduledFuture timeoutFuture = null;
    StringBuffer timeoutMessage = new StringBuffer();
    if (timeout != null) {
      timeoutFuture = CommandRunner.scheduleFuture(() -> {
        try {
          for (Future childFuture : futureList) {   // Cancel pending child tasks.
            childFuture.cancel(false);  // Do not interrupt running threads. May become configurable.
          }
        } catch (Exception e) {
          LOG.error(e.getMessage(), e);
        } finally {
          isTimeout.set(true);
          forceStop.set(true); // Stop the waiting parent thread.
          timeoutMessage.append("Timeout while running flow " + commandContext.getFlowDefinition().getId()
              + " " + getKeyword());
          synchronized (atomicInteger) {
            atomicInteger.notify();
          }
        }
      }, timeout);
    }

    List resultList = new CopyOnWriteArrayList();
    Map session = FlowSession.get();

    // Submit a task for each.
    for (CommandCallable commandCallable : callables) {

      // Reuse pool instead of creating new one (and use 'shutdown' and then 'awaitTermination').
      futureList.add(CommandRunner.submit(() -> {
        CommandContext childResult = null;
        try {
          if (isTimeout.get() || forceStop.get()) {
            LOG.error("Skipping " + commandCallable.getId());
            return;
          }
          FlowSession.set(session); // Copy session.
          CommandContext aggregatorContext = new CommandContext();

          // Run the runnable.
          childResult = commandCallable.getCallable().call();

          // Run the aggregator.
          try {
            aggregatorContext.setArguments(new Object[]{resultList, childResult.getResult()});
            //me.run(aggregatorContext, aggregator);
            aggregator.run(aggregatorContext);
          } catch (Exception e) {
            if (childResult == null || childResult.getException() == null) {  // Not already logged by MethodReferenceCommand.
              LOG.error(e.getMessage(), e);
            }
            if (Boolean.TRUE.equals(stopOnException)) {  // Should not fail the others if just the aggregator fails, unless stopOnException
              commandContext.setException(e);
              forceStop.set(true);
              FlowUtil.cancelFlow();  // Stops the others also.
            }
          }

        } catch (Exception e) {
          if (childResult == null || childResult.getException() == null) {
            LOG.error(e.getMessage(), e);
          }
          if (Boolean.TRUE.equals(stopOnException)) {
            commandContext.setException(e);
            forceStop.set(true);
            FlowUtil.cancelFlow();  // Stops the others also.
          }
        } finally {
          atomicInteger.incrementAndGet();
          synchronized (atomicInteger) {
            atomicInteger.notify();
          }
          FlowSession.clearSession(); // does not clear the map, only it sets the threadlocal on null for GC.
        }
      }));
    }
    // Do not go to next command after parallel until all children are done.
    synchronized (atomicInteger) {
      while (atomicInteger.get() < callables.size() && !forceStop.get()) { // spurious wakeup.
        atomicInteger.wait();
      }
    }
    // Cancel future timeout task if all was OK or not.
    if (timeoutFuture != null) {
      timeoutFuture.cancel(false);
    }

    if (timeoutMessage.length() != 0) {
      throw new FlowTimeoutException(timeoutMessage.toString());
    } else if (commandContext.getException() != null) {
      throw commandContext.getException();
    }

    commandContext.setResult(resultList);
    commandContext.setArguments(new Object[]{resultList});
  }

  public void stopOnException() {
    stopOnException = true;
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    flowStep.setType(getKeyword());
    if (aggregator != null) {
      flowStep.setAggregator(aggregator.getLambdaMeta().getActualMethodReferenceAsString());
    }
    if (timeout != null) {
      flowStep.setTimeout(timeout);
    }
    if (stopOnException != null) {
      flowStep.setStopOnException(stopOnException.booleanValue());
    }
    super.definitionExport(flowStep);
  }
}
