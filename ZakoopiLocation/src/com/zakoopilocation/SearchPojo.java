// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.zakoopilocation;


public class SearchPojo
{

    String id;
    String storeadd;
    String storecity;
    String storename;
    String storslug;

    public SearchPojo()
    {
    }

    public SearchPojo(String s, String s1, String s2, String s3, String s4)
    {
        id = s;
        storename = s1;
        storeadd = s2;
        storecity = s3;
        storslug = s4;
    }

    public String getId()
    {
        return id;
    }

    public String getStoreadd()
    {
        return storeadd;
    }

    public String getStorecity()
    {
        return storecity;
    }

    public String getStorename()
    {
        return storename;
    }

    public String getStorslug()
    {
        return storslug;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setStoreadd(String s)
    {
        storeadd = s;
    }

    public void setStorecity(String s)
    {
        storecity = s;
    }

    public void setStorename(String s)
    {
        storename = s;
    }

    public void setStorslug(String s)
    {
        storslug = s;
    }
}
