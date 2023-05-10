package com.softwinner.dragonfire.aidl;

interface ComAIDLService {
	int sendCMDselect(String testname, String tip);
	String getMsgBack(int msgid);
    int sendTip(String testname, String tip);
}