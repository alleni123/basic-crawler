package com.lj.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lj.crawler.bean.Task;




/**
 * The class provides a skeletal implementation of the Crawlable interface to minimize the effort required to implement
 * that interface. 
 * To implement a crawler, user only needs to(and must) implement the method R crawl().
 * 
 * The interface Crawlable is implemented into the abstract class Manager to accomplish multiple-thread job.
 * 
 * 
 * 
 * @author Li Jing
 *
 * @see Manager
 *
 * @param <T> Task type which is set as paramter for a crawl job.
 * @param <R> Result crawled by a crawler at a time.
 */
public abstract  class AbstractCrawler<T extends Task,R>  implements Crawlable<T,R>
{
	protected boolean running;
	
	protected boolean waiting;
	
	
	protected long taskCounter;

	protected T task;
	
	protected AbstractCrawler()
	{
	}
	
	
	
	

	public boolean isRunning()
	{
		return running;
	}


	public void setRunning(boolean running)
	{
		this.running = running;
	}


	public boolean isWaiting()
	{
		return waiting;
	}


	public void setWaiting(boolean waiting)
	{
		this.waiting = waiting;
	}


	public long getTaskCounter()
	{
		return taskCounter;
	}


	public void setTaskCounter(long taskCounter)
	{
		this.taskCounter = taskCounter;
	}

	@Override
	public void setTask(T task) {
		this.taskCounter++;
		this.task=task;
		
		//这里暂且认为setTask之后就立即执行任务
		//We assume that a task is commited sequentially after invocation of method setTask.
		this.task.setStart(new Date());
	};
 

	@Override
	public Map<T,R> call() throws Exception
	{
		R r=crawl();
		this.task.setEnd(new Date());
		this.task.setFinished(0);
		Map<T,R>map=new HashMap<T,R>();
		map.put(task, r);
		return map;
	}
 
	
	
	
}
