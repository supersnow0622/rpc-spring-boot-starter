package com.wlx.middleware.rpc.network.future;

import com.wlx.middleware.rpc.network.msg.Response;

import java.util.HashMap;
import java.util.Map;

public class SyncWriteMap {

    public final static Map<String, WriteFuture<Response>> synKey = new HashMap<>();
}
