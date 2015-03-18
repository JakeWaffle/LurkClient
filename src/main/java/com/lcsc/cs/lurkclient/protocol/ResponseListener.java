package com.lcsc.cs.lurkclient.protocol;

import java.util.List;

/**
 * Created by Jake on 3/5/2015.
 */
public interface ResponseListener {
    public void notify(List<Response> responses);
}
