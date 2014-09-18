package com.mn.tiger.service;

import com.mn.tiger.task.invoke.TGTaskParams;

import com.mn.tiger.task.result.TGTaskResult;


interface TGRemoteService
{
    void invoke(in TGTaskParams taskParams);
}