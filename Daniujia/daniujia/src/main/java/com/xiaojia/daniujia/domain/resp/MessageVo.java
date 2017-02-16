package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class MessageVo {

    /**
     * eventId : 0
     * ok : true
     * data : [{"content":{"ctype":1,"msg":"aaa"},"to":"weaver","code":"1460874570929","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874574068,"type":"singleChat","messageId":32},{"content":{"ctype":1,"msg":"hxhxhxj"},"to":"weaver","code":"1460874544323","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874547461,"type":"singleChat","messageId":31},{"content":{"ctype":1,"msg":"gg"},"to":"weaver","code":"1460874316054","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532779,"type":"singleChat","messageId":30},{"content":{"ctype":1,"msg":"ggg"},"to":"weaver","code":"1460874090020","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532763,"type":"singleChat","messageId":29},{"content":{"ctype":1,"msg":"tgg"},"to":"weaver","code":"1460874206176","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532735,"type":"singleChat","messageId":28},{"content":{"ctype":1,"msg":"gg"},"to":"weaver","code":"1460874274812","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532732,"type":"singleChat","messageId":27},{"content":{"ctype":1,"msg":"hhh"},"to":"weaver","code":"1460873600639","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532698,"type":"singleChat","messageId":26},{"content":{"ctype":1,"msg":"uu"},"to":"weaver","code":"1460873977544","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532703,"type":"singleChat","messageId":25},{"content":{"ctype":1,"msg":"hhh"},"to":"weaver","code":"1460873585695","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874532706,"type":"singleChat","messageId":24},{"content":{"ctype":1,"msg":"tgg"},"to":"weaver","code":"1460874206176","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389789,"type":"singleChat","messageId":23},{"content":{"ctype":1,"msg":"gg"},"to":"weaver","code":"1460874274812","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389787,"type":"singleChat","messageId":22},{"content":{"ctype":1,"msg":"gg"},"to":"weaver","code":"1460874316054","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389761,"type":"singleChat","messageId":21},{"content":{"ctype":1,"msg":"hhh"},"to":"weaver","code":"1460873585695","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389758,"type":"singleChat","messageId":20},{"content":{"ctype":1,"msg":"hhh"},"to":"weaver","code":"1460873600639","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389756,"type":"singleChat","messageId":19},{"content":{"ctype":1,"msg":"uu"},"to":"weaver","code":"1460873977544","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389751,"type":"singleChat","messageId":18},{"content":{"ctype":1,"msg":"ggg"},"to":"weaver","code":"1460874090020","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874389754,"type":"singleChat","messageId":17},{"content":{"ctype":1,"msg":"gg"},"to":"weaver","code":"1460874316054","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874348920,"type":"singleChat","messageId":16},{"content":{"ctype":1,"msg":"gg"},"to":"weaver","code":"1460874274812","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874348946,"type":"singleChat","messageId":15},{"content":{"ctype":1,"msg":"ggg"},"to":"weaver","code":"1460874090020","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874348919,"type":"singleChat","messageId":14},{"content":{"ctype":1,"msg":"tgg"},"to":"weaver","code":"1460874206176","mtype":1,"from":"lisi","chatid":0,"dnjts":1460874348917,"type":"singleChat","messageId":13}]
     */

//    private int eventId;
    private boolean ok;
    /**
     * content : {"ctype":1,"msg":"aaa"}
     * to : weaver
     * code : 1460874570929
     * mtype : 1
     * from : lisi
     * chatid : 0
     * dnjts : 1460874574068
     * type : singleChat
     * messageId : 32
     */

    private List<MessageEntity> data;

//    public void setEventId(int eventId) {
//        this.eventId = eventId;
//    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setData(List<MessageEntity> data) {
        this.data = data;
    }

//    public int getEventId() {
//        return eventId;
//    }

    public boolean isOk() {
        return ok;
    }

    public List<MessageEntity> getData() {
        return data;
    }
}
