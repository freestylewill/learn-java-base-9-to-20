package com.flydean;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author wayne
 * @version LockOptimization,  2020/5/26 8:08 下午
 */
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1,
        jvmArgsPrepend = {
        "-XX:-UseBiasedLocking",
                "-XX:LoopUnrollLimit=1",
                "-XX:CompileCommand=print,com.flydean.LockOptimization::test"
//                "-XX:+PrintAssembly"
}
        )
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LockOptimization {

    int x;
    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void test() {
        for (int i = 0; i < 1000; i++) {
            synchronized (this) {
                x += 0x51;
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LockOptimization.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
