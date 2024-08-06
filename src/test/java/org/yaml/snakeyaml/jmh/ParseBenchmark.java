/*
 * Copyright (c) 2024, SnakeYAML
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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Fork(1)
@Warmup(iterations = 3, time = 10)
@Measurement(iterations = 3, time = 10)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ParseBenchmark {

  @Param({"1000", "100000"})
  private int entries;
  private String yamlString;
  private final Yaml yaml = createYaml();

  private static Yaml createYaml() {
    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
    return new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions);
  }

  public static void main(String[] args) throws RunnerException {
    new Runner(new OptionsBuilder().include(ParseBenchmark.class.getSimpleName())
            .build()).run();
  }

  @Setup
  public void setup() throws IOException {
    yamlString = yaml.dump(IntStream.range(0, entries).boxed()
            .collect(Collectors.toMap(i -> i, i -> Integer.toString(i))));
    System.out.printf("%nyaml bytes length: %d%n", yamlString.getBytes(StandardCharsets.UTF_8).length);
  }

  @Benchmark
  public int parse(Blackhole bh) throws IOException {
    int count = 0;
    for (Event event : yaml.parse(new StringReader(yamlString))) {
      bh.consume(event.getEventId());
      count++;
    }
    return count;
  }

  @Benchmark
  public Object load() throws IOException {
    return yaml.load(yamlString);
  }
}
