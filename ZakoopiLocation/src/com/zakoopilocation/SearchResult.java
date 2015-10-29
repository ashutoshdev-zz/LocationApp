// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.zakoopilocation;


public class SearchResult
{

    private String id;
    private String market_name;
    private String slug;
    private String store_name;

    public SearchResult()
    {
    }

    public String getId()
    {
        return id;
    }

    public String getMarket_name()
    {
        return market_name;
    }

    public String getSlug()
    {
        return slug;
    }

    public String getStore_name()
    {
        return store_name;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setMarket_name(String s)
    {
        market_name = s;
    }

    public void setSlug(String s)
    {
        slug = s;
    }

    public void setStore_name(String s)
    {
        store_name = s;
    }
}
