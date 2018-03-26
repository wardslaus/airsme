package com.airsme.models;

import java.util.Date;

/**
 * Created by M91p-04 on 1/30/2018.
 */
public class MessegeItem {
        Date date=new Date();
        String from;
        String to;
        String content;

        public MessegeItem() {
        }

        public MessegeItem(String from, String to, String content) {
            this.from = from;
            this.to = to;
            this.content = content;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
