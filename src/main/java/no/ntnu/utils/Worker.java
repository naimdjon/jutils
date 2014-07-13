package no.ntnu.utils;

/**
 * Created by NT
 * User: takhirov
 * Date: 5/11/12
 * Time: 12:25 PM
 */
public interface Worker extends Runnable{
    public boolean isDone();
}