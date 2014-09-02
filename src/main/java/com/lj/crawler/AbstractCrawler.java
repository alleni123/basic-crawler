package com.lj.crawler;

import com.lj.crawler.manager.Manager;



/**
 * The class provides a skeletal implementation of the Crawlable interface to minimize the effort required to implement
 * this interface. 
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
public abstract  class AbstractCrawler<T,R>  implements Crawlable<T,R>
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
	};
 

	@Override
	public R call() throws Exception
	{
		R r=crawl();
		return r;
	}
 
	
	
	
}
