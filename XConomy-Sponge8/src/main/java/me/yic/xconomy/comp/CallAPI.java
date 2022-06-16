package me.yic.xconomy.comp;

import me.yic.xconomy.info.RecordInfo;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("unused")
public class CallAPI {

    public static void CallPlayerAccountEvent(UUID u, String name, BigDecimal bal, BigDecimal amount, Boolean isAdd, RecordInfo ri){
    }

    public static void CallNonPlayerAccountEvent(String u, BigDecimal bal, BigDecimal amount, boolean isAdd, String type){
    }

}
