package com.jfinal.base;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

/**
 * 因为我们数据库表没有定义外键，我们java后台要预先用mapping定义好表外键
 *  yk_order_detail: {
 *       "goods": ["goods_id", "yk_goods", "id"]
 *   }
 *   yk_goods: {
 *       "cat": ["cat_id", "yk_goods_cat", "id"]
 *   }
 * @author lee
 *
 */
public class TableForeignMapping {
    private static Map<String, Map<String, String[]>> tableForeignKeyMap = new HashMap<String, Map<String, String[]>>();

    private static TableForeignMapping me = new TableForeignMapping(); 
    
    public static TableForeignMapping me() {
        return me;
    }

    public void put(String tableName, Map<String, String[]> foreigInfo) {
        tableForeignKeyMap.put(tableName, foreigInfo);
    }

    public Map<String, String[]> get(String tableName) {
        return tableForeignKeyMap.get(tableName);
    }

}
