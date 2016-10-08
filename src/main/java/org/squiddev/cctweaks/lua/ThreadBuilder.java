package org.squiddev.cctweaks.lua;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper methods for various things
 */
public class ThreadBuilder {
	public static int THREAD_PRIORITY = Thread.MIN_PRIORITY + (Thread.NORM_PRIORITY - Thread.MIN_PRIORITY) / 2;

	public static ScheduledExecutorService createThread(String name, int threads) {
		return Executors.newScheduledThreadPool(threads, getFactory(name, THREAD_PRIORITY));
	}

	public static ThreadFactory getFactory(String name, final int priority) {
		final String prefix = "CCTweaks-" + name + "-";
		final AtomicInteger counter = new AtomicInteger(1);

		SecurityManager manager = System.getSecurityManager();
		final ThreadGroup group = manager == null ? Thread.currentThread().getThreadGroup() : manager.getThreadGroup();

		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = new Thread(group, runnable, prefix + counter.getAndIncrement());
				if (!thread.isDaemon()) thread.setDaemon(true);
				if (thread.getPriority() != priority) thread.setPriority(priority);

				return thread;
			}
		};
	}
}
