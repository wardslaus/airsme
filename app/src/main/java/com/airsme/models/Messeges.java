package com.airsme.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M91p-04 on 1/29/2018.
 */

public class Messeges extends Model {
    String msgID;
    String businessid;
    String proxyid;
    String tenderno;

    List<MessegeItem> messages = new ArrayList<>();

    public Messeges() {
    }

    public Messeges(Tender tender, String proxyid) {
        this(tender.getPKeyValue(), tender.getTenderno(), proxyid);
    }

    public Messeges(Tender tender, Proxy proxy) {
        this(tender.getPKeyValue(), tender.getTenderno(), proxy.getUid());
    }
    public Messeges(String businessid, String tenderno, String proxyid) {
        this.businessid = businessid;
        this.proxyid = proxyid;
        this.tenderno = tenderno;

        this.msgID = businessid + "_" + tenderno + "_" + proxyid;
    }

    @Override
    public String getNode() {
        return "tender/" + businessid + "/" + tenderno + "/messeges/" + proxyid;
    }

    @Override
    public String getPKeyValue() {
        return msgID;
    }

    @Override
    public String getPKeyName() {
        return "msgID";
    }

    @Override
    public void setPKeyValue(String id) {
        msgID = id;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public List<MessegeItem> getMessages() {
        return messages;
    }

    public void setMessages(List<MessegeItem> messages) {
        this.messages = messages;
    }

    public void addMessege(String from, String to, String content) {
        this.messages.add(new MessegeItem(from, to, content));
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getProxyid() {
        return proxyid;
    }

    public void setProxyid(String proxyid) {
        this.proxyid = proxyid;
    }

    public String getTenderno() {
        return tenderno;
    }

    public void setTenderno(String tenderno) {
        this.tenderno = tenderno;
    }
}
