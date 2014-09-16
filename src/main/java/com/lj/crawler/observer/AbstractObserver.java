package com.lj.crawler.observer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.lj.crawler.bean.Task;


/**
 * 
 * Added at 1.1
 * 
 * The fundamental implementation of observer.
 * The method update(String taskID, M map) obtains all the results and task marks sent from AbstractManager.
 * 
 * Users only need to override the method handleResult(T task,List<R>Result) to handle each result crawled by crawlers.
 * 
 * 
 * @author Lijing
 *
 * @param <T>
 * @param <R>
 * @param <M>
 */
public abstract class AbstractObserver<T extends Task,R,M extends Map<T,List<R>>> implements IManagerObserver<T,R,M>
{

	protected Set<T> tasks;
	
	protected List<R> results;
	
	protected String taskID;
	
	@Override
	public void update(String taskID, M map)
	{	
		this.taskID=taskID;
		tasks=map.keySet();
		
		for(Iterator<T>it=tasks.iterator();it.hasNext();){
			T task=it.next();
			List<R> results=map.get(task);
			handleResult(task, results);
		}
		
	}

	/**
	 * To handle each result crawled by crawlers. 
	 * The types of T and R  are both defined in the class AbstractCrawler<T,R>
	 * 
	 * @param task
	 * @param results
	 */
	protected abstract  void  handleResult(T  task,List<R> results);
	 
	
 

}
