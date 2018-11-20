package staticPart;

import log.LoggerFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public enum ThreadPool {
	INSTANCE;
	private QueuedThreadPool threadPool = null;
	private int minThreads = 10;
	private int maxThreads = 100;
	public void init(int minThreads, int maxThreads, int timeout) {
		this.minThreads = minThreads;
		this.maxThreads = maxThreads;
		threadPool = new QueuedThreadPool(maxThreads, minThreads, timeout);
		threadPool.setDetailedDump(false);
	}
	public QueuedThreadPool getThreadPool() {
		return threadPool;
	}
	
	public void setMaxThreads(int num) {
		if(num >= minThreads && num <= maxThreads) {
			threadPool.setMaxThreads(num); 
			LoggerFactory.getLogger().error(String.format("设定maxThreads: %d 成功", num));
		} else {
			LoggerFactory.getLogger().error(String.format("无法设定maxthreads: %d, 范围是 %d ~ %d", num, minThreads, maxThreads));
		}
	}
}
