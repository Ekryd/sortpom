package sortpom.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class MultiThreadTest {
  private final AtomicInteger counter = new AtomicInteger(1);

  @Test
  void multipleSortingsShouldNotInterfereWithEachOther()
      throws InterruptedException, ExecutionException {
    ExecutorService executorService = new ScheduledThreadPoolExecutor(10);
    for (var a = 0; a < 10; a++) {
      testOneConcurrentLoop(executorService);
    }
  }

  private void testOneConcurrentLoop(ExecutorService executorService)
      throws InterruptedException, ExecutionException {
    var testThreads = getTestThreads();
    var futures = executorService.invokeAll(testThreads);
    assertAllThreadsReturnedTrue(futures);
  }

  private List<Callable<Boolean>> getTestThreads() {
    List<Callable<Boolean>> list = new ArrayList<>();
    list.add(
        new TestThread(
            "/full_unsorted_input.xml",
            "/sortOrderFiles/sorted_default_0_4_0.xml",
            "default_0_4_0"));
    list.add(
        new TestThread(
            "/full_unsorted_input.xml", "/sortOrderFiles/sorted_custom_1.xml", "custom_1"));
    list.add(
        new TestThread(
            "/full_unsorted_input.xml",
            "/sortOrderFiles/sorted_recommended_2008_06.xml",
            "recommended_2008_06"));
    list.add(
        new TestThread(
            "/full_unsorted_input.xml",
            "/sortOrderFiles/sorted_default_1_0_0.xml",
            "default_1_0_0"));
    return list;
  }

  private void assertAllThreadsReturnedTrue(List<Future<Boolean>> futures)
      throws InterruptedException, ExecutionException {
    for (var future : futures) {
      assertEquals(true, future.get());
    }
  }

  private class TestThread implements Callable<Boolean> {
    private final String inputResourceFileName;
    private final String expectedResourceFileName;
    private final String predefinedSortOrder;

    TestThread(
        final String inputResourceFileName,
        final String expectedResourceFileName,
        final String predefinedSortOrder) {
      this.inputResourceFileName = inputResourceFileName;
      this.expectedResourceFileName = expectedResourceFileName;
      this.predefinedSortOrder = predefinedSortOrder;
    }

    @Override
    public Boolean call() {
      try {
        SortPomImplUtil.create()
            .lineSeparator("\n")
            .testPomFileNameUniqueNumber(counter.getAndIncrement())
            .predefinedSortOrder(predefinedSortOrder)
            .testVerifyXmlIsOrdered(expectedResourceFileName);
        SortPomImplUtil.create()
            .lineSeparator("\n")
            .testPomFileNameUniqueNumber(counter.getAndIncrement())
            .predefinedSortOrder(predefinedSortOrder)
            .testVerifyXmlIsNotOrdered(
                inputResourceFileName,
                "The xml element <modelVersion> should be placed before <parent>");
      } catch (Exception e) {
        //noinspection CallToPrintStackTrace
        e.printStackTrace();
        return false;
      }
      return true; // To change body of implemented methods use File | Settings | File Templates.
    }
  }
}
