/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.events.*;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(8)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
@State(Scope.Thread)
public class EmitterBenchmark {

  private Emitter emitter;

  public static void main(String[] args) throws RunnerException {
    new Runner(new OptionsBuilder().include(EmitterBenchmark.class.getSimpleName())
        .addProfiler(GCProfiler.class).build()).run();
  }

  @Setup
  public void setup(Blackhole blackhole) throws IOException {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
    emitter = new Emitter(new BlackholeWriter(blackhole), dumperOptions);
    // Begin a sequence so that the benchmark may emit a scalar each iteration.
    emitter.emit(new StreamStartEvent(null, null));
    emitter.emit(new DocumentStartEvent(null, null, false, null, null));
    emitter
        .emit(new SequenceStartEvent(null, null, true, null, null, DumperOptions.FlowStyle.AUTO));
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void emitScalar() throws IOException {
    emitter.emit(new ScalarEvent(null, null, new ImplicitTuple(true, false), "scalar", null, null,
        DumperOptions.ScalarStyle.PLAIN));
  }

  /**
   * Writer implementation which sends input to the {@link Blackhole} to prevent dead code
   * elimination.
   */
  private static final class BlackholeWriter extends Writer {
    private final Blackhole blackhole;

    BlackholeWriter(Blackhole blackhole) {
      this.blackhole = blackhole;
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
      blackhole.consume(cbuf);
      blackhole.consume(off);
      blackhole.consume(len);
    }

    @Override
    public void write(String str, int off, int len) {
      blackhole.consume(str);
      blackhole.consume(off);
      blackhole.consume(len);
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}
  }
}
