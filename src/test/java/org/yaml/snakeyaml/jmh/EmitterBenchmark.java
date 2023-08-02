package org.yaml.snakeyaml.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;

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

  public static void main(String[] args) throws RunnerException {
    new Runner(new OptionsBuilder().include(EmitterBenchmark.class.getSimpleName())
        .addProfiler(GCProfiler.class).build()).run();
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
