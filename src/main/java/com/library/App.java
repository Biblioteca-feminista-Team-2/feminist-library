package com.library;

import com.library.config.DBManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       DBManager.getConnection();
    }
}
