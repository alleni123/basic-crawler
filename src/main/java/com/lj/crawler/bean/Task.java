package com.lj.crawler.bean;

import java.util.Date;


/**
 * Added at version 1.1
 * The base bean of crawl task.
 * 
 * @author Lijing
 *
 */
public class Task
{
	protected String tid;

	protected String url;

	protected int finished;

	protected Date start;

	protected Date end;

	public Task()
	{

	}

	public Task(String url)
	{
		this.url = url;
	}

	public int getFinished()
	{
		return finished;
	}

	public void setFinished(int finished)
	{
		this.finished = finished;
	}

	public Date getStart()
	{
		return start;
	}

	public void setStart(Date start)
	{
		this.start = start;
	}

	public Date getEnd()
	{
		return end;
	}

	public void setEnd(Date end)
	{
		this.end = end;
	}

	public String getTid()
	{
		return tid;
	}

	public void setTid(String tid)
	{
		this.tid = tid;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

}
