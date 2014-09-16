package com.lj.crawler.observer;

import java.util.List;
import java.util.Map;

import com.lj.crawler.bean.Task;



/**
 * The root interface in the manager observer hierarchy. Observers can be added into a Manager instance, the method update of which will
 * be called when a few tasks are finished crawling(the exact amount of tasks can be set in Manager implementation).
 * 
 * The implementation of the interface can be JPA oriented,webservice,or just for Log. The main purpose of this is seperating the handling of 
 * crawled data from the implementation of task management and execution.
 * 
 * @author Li Jing
 *
 *
 * @param
 */
public interface IManagerObserver<T extends Task,R,M extends Map<T,List<R>>>
{
	public void update(String taskID,M map);
	
}

