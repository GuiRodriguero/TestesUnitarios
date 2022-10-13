package br.com.gui.testes.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRunner extends BlockJUnit4ClassRunner {

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public ParallelRunner(Class<?> klass) throws InitializationError {
        super(klass);
        setScheduler(new ThreadPool());
    }

    private static class ThreadPool implements RunnerScheduler{

        private ExecutorService executor;

        public ThreadPool() {
            executor = Executors.newFixedThreadPool(2);
        }

        @Override
        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }

        @Override
        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
