package com.lj.crawler.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.lj.crawler.Crawlable;
import com.lj.crawler.observer.IManagerObserver;



/**
 * 
 * This class is the core part of the JLcrawler Framework.
 * When a user want a crawler to be working in multiple-thread conditon, he could implement a class extends this one.
 * Then simply call addMapsAndRun(Map<String, List<T>> tasks) to run the tasks.
 * 
 * There are still a lot of problems, such as I have no idea how singleton pattern can be used with inheritance.
 * Hopefully those problems can be solved in the future.
 * 
 * 
 * @author Li Jing
 *
 * @param <T>
 * @param <R>
 */
public abstract class Manager<T, R>
{

	 
	private static final Logger LOG = Logger.getLogger(Manager.class);

	@SuppressWarnings("rawtypes")
	protected Crawlable[] crawlers;

	protected Map<String, List<T>> taskMapQueue = Collections.synchronizedMap(new LinkedHashMap<String, List<T>>());
	
	
	
	protected List<IManagerObserver<R,List<R>>> observers = new ArrayList<IManagerObserver<R,List<R>>>();

	public final void addObserver(IManagerObserver<R,List<R>> observer)
	{
		observers.add(observer);
	}

	public final void removeObserver(IManagerObserver<R,List<R>> observer)
	{
		observers.remove(observer);
	}

	public final void notifyObservers(String taskID, List<R> list)
	{
		for (IManagerObserver<R,List<R>> observer : observers)
		{
			observer.update(taskID, list);
		}
	}

	protected Manager()
	{
		
	}

	
	protected final  static  int crawlerNum=5;
	
 
	

	protected Executor executor;

	
	
	/**
	 * String是任务id， List<T>是具体的任务集合
	 * @param tasks
	 */
	public synchronized void addMapsAndRun(Map<String, List<T>> tasks)
	{

		this.taskMapQueue.putAll(tasks);

		Set<String> keys = taskMapQueue.keySet();

		Iterator<String> it = keys.iterator();
		if (!executor.running)
		{
			if (it.hasNext())
			{
				String key = it.next();
				System.out.println("for key:keys " + key);
				executor.taskQueue.addAll(taskMapQueue.remove(key));
				executor.taskID = key;
				executor.runIt();
			}
		}
	}

	public synchronized void runAgain()
	{

		// 1. 首先提取刚才的信息， 给watcher们更新
		List<R> data = new ArrayList<R>(executor.results);

	//	Collections.copy(data, executor.results);
		notifyObservers(executor.taskID, data);

		Set<String> keys = taskMapQueue.keySet();

		Iterator<String> it = keys.iterator();
		if (it.hasNext())
		{
			String key = it.next();
			executor.taskQueue.addAll(taskMapQueue.remove(key));
			executor.taskID = key;
			executor.runIt();

		} else
		{
			executor.running = false;
		}
	}
	
	
	private synchronized void submitResults(){
	 	notifyObservers(executor.taskID, executor.results);
		executor.results=null;
		executor.results=new ArrayList<R>();
	//	data=null;
		
	}

	protected class Executor implements Runnable
	{
		protected boolean running;

		protected String taskID;

		protected Deque<T> taskQueue = new LinkedList<T>();

		protected List<R> results;
		
		private int taskNum=0;

		protected synchronized void runIt()
		{
			running = true;
			Thread t = new Thread(this);
			t.start();
		}

		
		
		/**
		 * 2014/08/26  由于JSONArray.fromObject出现了GC错误，这里准备改写一下。
		 * 				让程序每获取5000条或者更多的返回信息时，以及一个任务完成时，调用notifyObservers，执行一轮存储，
		 * 				这样就不会因为任务获取结果太多，而导致内存上的错误。
		 */
		@Override
		public void run()
		{
			results = new ArrayList<R>();

			while (!taskQueue.isEmpty() && running)
			{
				int taskSize = taskQueue.size();
				LOG.info("目前任务" + taskID + " " + Thread.currentThread().getId() + "数量为:" + taskSize);
				LOG.warn("总任务完成度"+taskNum+"/"+taskMapQueue.size());
				ExecutorService exec = Executors.newFixedThreadPool(crawlerNum);

				List<Future<List<R>>> futures = new ArrayList<Future<List<R>>>();

				for (int i = 0; i < (crawlerNum > taskSize ? taskSize : crawlerNum); i++)
				{
					@SuppressWarnings("unchecked")
					Crawlable<T, List<R>> c = crawlers[i];
					T t = taskQueue.pop();
					LOG.debug(t);
					c.setTask(t);
					
					futures.add(exec.submit(c));
				}

				for (Future<List<R>> future : futures)
				{
					try
					{
						// 这里是有可能为空的， 比如有个人没有个人主页，就会返回null。
						List<R> list = future.get();
						if (list != null&&list.size()>0)
						{
						//	System.out.println(list.get(0).getClass().getName());
							results.addAll(list);
						}
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					catch (ExecutionException e)
					{
						e.printStackTrace();
					}

				}
				if(results.size()>=1000){
				 submitResults();
				}

				exec.shutdownNow();

				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				if (taskQueue.isEmpty())
				{
					LOG.warn("目前任务" + taskID + " " + Thread.currentThread().getId() + "runagain:" + taskSize);
					runAgain();
					taskNum++;
					break; // 之前没有这个break，会导致runAgain里面加入taskQueue。
							// 结果虽然执行了这个if的内容，while却没有被终止。
							// 还有一种方法是把run里面的东西写出来，加入一个参数List<T>taskQueue,以这个taskQueue作为这里的局部变量，就没有这个问题了。

				}
			}

			System.out.println("hello C");
		}
	}

}
