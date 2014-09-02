package com.lj.crawler;

import java.util.concurrent.Callable;


/**
 * 
 * @author Li Jing
 *
 * @param <T>
 * @param <R>
 */
public interface Crawlable<T,R> extends Callable<R>
{
	public void setTask(T task);
	
	public R crawl();
	
	
	public String CHARSET_GBK="gbk";
	
	public String CHARSET_UTF8="utf-8";
}