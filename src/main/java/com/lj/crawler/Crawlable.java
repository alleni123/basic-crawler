package com.lj.crawler;

import java.util.Map;
import java.util.concurrent.Callable;


/**
 * 
 * @author Li Jing
 *
 * @param <T>
 * @param <R>
 */
public interface Crawlable<T,R> extends Callable<Map<T,R>>
{
	public void setTask(T task);
	
	public R crawl();
	
	/**
	 * Constants in interface are implicitly final and static
	 */
	 String CHARSET_GBK="gbk";
	 String CHARSET_UTF8="utf-8";
}