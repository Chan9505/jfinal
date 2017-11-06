package com.jfinal.base;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Include the basic CRUD operation: save, list, get, page, update, delete
 * @param <T>
 */
public abstract class BaseActionController<T extends Model<T>> extends ControllerBase {
    final private static Logger log = Logger.getLogger(BaseActionController.class);

    protected abstract ServiceBase<T> getService();

    private T getDao() {
        return getService().getDao();
    }

    public void save() {
        try {
            T bean = (T) getBean(getDao().getClass(), "");
            updateBean(bean);
            bean.save();
            log.info("=== save model successfully.======");
            renderSucessResult();
        } catch (Exception e) {
        		e.printStackTrace();
            renderFailedResult("Failed to save");
            log.warn("=== save model successfully.======");
        }
    }

    public void list() {
        try {
            String columns = getPara("columns", "*");
            columns = escapeColumns(columns);
            String orderBy = StringUtils.replace(StringUtils.defaultString(getPara("orderBy"), ""), "_", "asc");
            Map<String, Object> params = genSqlParamMap();
            updateSqlParamMap(params);
            List<T> models = getService().findLoadColumns(columns, params, orderBy);
            updateSearchResult(models);
            log.info("=== list model successfully.======");
            renderSucessResult(models);
        } catch (Exception e) {
            renderFailedResult("failed to query, exception is " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void updateSearchResult(List<T> models) {
	}

	private String escapeColumns(String columns) {
        if (StringUtils.isBlank(columns) || StringUtils.equals("*", columns.trim())) {
            return columns;
        }
        List<String> escapedColumns = new ArrayList<>();
        for (String column : columns.split(",")) {
            System.out.println("======column:"+column);
            if (StringUtils.contains(column, ".")) {
                escapedColumns.add(column + " `" + column + "`");
            } else {
                escapedColumns.add("t." + column + " `" + column + "`");
            }
        }
        return StringUtils.join(escapedColumns, ",");
    }
    

    
    /**
     * update the sqlParams in child actions
     */
    protected void updateSqlParamMap(Map<String, Object> sqlParams) {
        
    }

    /**
     * update the sqlParams in child actions
     */
    protected void updateBean(T bean) {
    }
    
    public void page() {
        try {
            String columns = getPara("columns", "*");
            columns = escapeColumns(columns);
            String orderBy = StringUtils.replace(StringUtils.defaultString(getPara("orderBy"), ""), "_", "asc");
            Integer pageNumber = getParaToInt("pageNumber", 1);
            Integer pageSize = getParaToInt("pageSize", 20);
            Map<String, Object> params = genSqlParamMap();
            updateSqlParamMap(params);
            Page<T> page = getService().pageLoadColumns(columns, params, "and", orderBy, pageNumber, pageSize);
            renderSucessResult(page);
        } catch (Exception e) {
            renderFailedResult("failed to do pagination");
            e.printStackTrace();
        }
    }
    
    public void get() {
        try {
            String columns = getPara("columns", "*");
            columns = escapeColumns(columns);
            String orderBy = StringUtils.replace(StringUtils.defaultString(getPara("orderBy"), ""), "_", "asc");
            Map<String, Object> params = genSqlParamMap();
            updateSqlParamMap(params);
            T model = getService().findFirstLoadColumns(columns, params, orderBy);
            renderSucessResult(model);
        } catch (Exception e) {
            renderFailedResult("failed to query");
            e.printStackTrace();
        } 
    }
    
    public void update() {
        T bean = (T) getBean(getDao().getClass(), "");
        updateBean(bean);
        bean.update();
        renderSucessResult();
    }
    
    public void delete() {
        T bean = (T) getBean(getDao().getClass(), "");
        updateBean(bean);
        bean.delete();
        renderSucessResult();
    }

    protected Map<String, Object> genSqlParamMap() {
        Map<String, Object> paramMap = new HashMap<>();
        //Table mainTable = TableMapping.me().getTable(getDao().getClass());
        //Table table; = TableMapping.me().getTable(getDao().getClass());
        List<String> paramNames = getParams();
        for (String paramName : paramNames) {
            if (StringUtils.contains(paramName, ".")) {
                paramMap.put(paramName, getPara(paramName));
            } else {
                paramMap.put("t." + paramName, getPara(paramName));
            }
//            String tableAlias = "";
//            String columnName = paramName;
//            // column name like goods.name, goods_cat.name, goods.name_lk the str before "." is table alias, after "." is column with operator
//            if (paramName.contains(".")) {
//                tableAlias = StringUtils.split(paramName, ".")[0];
//                columnName = StringUtils.split(paramName, ".")[1];
//            }
//            Table table = getTable(tableAlias);
//            columnName = StringUtils.split(columnName, "_")[0];
//            Class<?> columnType = table.getColumnType(columnName);
//            if (columnType != null) {
//                if (StringUtils.endsWithIgnoreCase(paramName, "_in")
//                        && (StringUtils.equals(columnType.getSimpleName(), "String")
//                                || StringUtils.equals(columnType.getSimpleName(), "Timestamp")
//                                || StringUtils.equals(columnType.getSimpleName(), "Date"))) {
//                    String[] inValues = StringUtils.split(getPara(paramName), ",");
//                    String inStrValues = "";
//                    for (int i = 0; i < inValues.length; i++) {
//                        inStrValues = inStrValues + "'" + inValues[i] + "'";
//                        if (i < inValues.length - 1) {
//                            inStrValues = inStrValues + ",";
//                        }
//                    }
//                    paramMap.put(paramName, inStrValues);
//                } else if ((StringUtils.equals(columnType.getSimpleName(), "String")
//                        || StringUtils.equals(columnType.getSimpleName(), "Timestamp")
//                        || StringUtils.equals(columnType.getSimpleName(), "Date"))
//                        && !StringUtils.endsWithIgnoreCase(paramName, "_lk")) {
//                    paramMap.put(paramName, "'" + getPara(paramName) + "'");
//                } else {
//                    paramMap.put(paramName, getPara(paramName));
//                }
//            }
        }
        return paramMap;
    }

//    private Table getTable(String tableAlias) {
//        Table mainTable = TableMapping.me().getTable(getDao().getClass());
//        if (StringUtils.isEmpty(tableAlias) || StringUtils.equals(tableAlias, "t"))  {
//            return mainTable;
//        } else if (tableAlias.contains("_")) {
//            String tableAlias1 = tableAlias.split("_")[0];
//            String tableAlias2 = tableAlias.split("_")[1];
//            Table table1 = getForeignTableByAlias(mainTable, tableAlias1);
//            Table table2 = getForeignTableByAlias(table1, tableAlias2);
//            return table2;
//        } else {
//            return getForeignTableByAlias(mainTable, tableAlias);
//        }
//    }
//
//    private Table getForeignTableByAlias(Table mainTable, String tableAlias) {
//        String tableName = TableForeignMapping.me().get(mainTable.getName()).get(tableAlias)[1];
//        @SuppressWarnings("rawtypes")
//        Class<? extends Model> modelClass = TableModelMapping.me().getModel(tableName);
//        return TableMapping.me().getTable(modelClass);
//    }

    private List<String> getParams() {
        List<String> paramNames = new ArrayList<>();
        Enumeration<String> paraNames = getParaNames();
        while (paraNames.hasMoreElements()) {
            String name = paraNames.nextElement();
            if (!StringUtils.equals("action", name) && !StringUtils.equals("model", name)
                            && !StringUtils.equals("columns", name) && !StringUtils.equals("columns", name)
                            && !StringUtils.equals("pageNumber", name) && !StringUtils.equals("pageSize", name)
                            && !StringUtils.equals("orderBy", name)) {
                paramNames.add(name);
            }
        }
        return paramNames;
    }
    

    public void listJoin() {
//        String columns = getParam"quantity, goods.name, goods_cat.name";
//        String selectSql = "select ";
//        String fromSql = " from yk_order_detail t ";
        try {
            String columns = getPara("columns");
            String orderBy = StringUtils.replace(StringUtils.defaultString(getPara("orderBy"), ""), "_", "asc");
            Map<String, Object> params = genSqlParamMap();
            String selectSql = constructSelectSql(columns);
            log.info("=== selectSql:"+selectSql);
            List<Record> records = getService().findRecordList(selectSql, params, "and", orderBy);
            log.info("=== list record successfully.======");
            renderSucessResult(records);
        } catch (Exception e) {
            renderFailedResult("failed to query");
            e.printStackTrace();
        }
    }


    public void pageJoin() {
        try {
            String columns = getPara("columns");
            String orderBy = StringUtils.replace(StringUtils.defaultString(getPara("orderBy"), ""), "_", "asc");
            Integer pageNumber = getParaToInt("pageNumber");
            Integer pageSize = getParaToInt("pageSize");
            int pageNumberInt = pageNumber == null ? 1 : pageNumber.intValue();
            int pageSizeInt = pageSize == null ? 20 : pageSize.intValue();
            Map<String, Object> params = genSqlParamMap();
            String selectSql = constructSelectSql(columns);
            Page<Record> page = getService().pageRecords(selectSql, params, "and", orderBy, pageNumberInt, pageSizeInt);
            renderSucessResult(page);
        } catch (Exception e) {
            renderFailedResult("failed to do pagination");
            e.printStackTrace();
        }
    }
    
    public void getJoin() {
        try {
            String columns = getPara("columns");
            String orderBy = StringUtils.replace(StringUtils.defaultString(getPara("orderBy"), ""), "_", "asc");
            Map<String, Object> params = genSqlParamMap();
            String selectSql = constructSelectSql(columns);
            log.info("=== selectSql:"+selectSql);
            Record record = getService().findFirstRecord(selectSql, params, "and", orderBy);
            renderSucessResult(record);
        } catch (Exception e) {
            renderFailedResult("failed to query");
            e.printStackTrace();
        } 
    }
    
    private String constructSelectSql(String columns) {
        String mainTable = TableMapping.me().getTable(getService().getDao().getClass()).getName();

        StringBuilder selectSql = new StringBuilder("select ");
        
        
        Set<String> joinSqlSet = new LinkedHashSet<>();
        List<String> escapedColumns = new ArrayList<>();
        for (String column : columns.split(",")) {
            System.out.println("======column:"+column);
            if (StringUtils.contains(column, ".")) {
                genJoinSql(mainTable, StringUtils.trim(column), joinSqlSet);
                escapedColumns.add(column + " `" + column + "`");
            } else {
                escapedColumns.add("t." + column + " `" + column + "`");
            }
        }
        selectSql.append(StringUtils.join(escapedColumns, ", "));
        String joinSqls = StringUtils.join(joinSqlSet, "");
        System.out.println("=======111 joinSqls:" + joinSqls);
        String fromSql = " from " + mainTable + " t ";
        selectSql.append(fromSql);
        selectSql.append(joinSqls);
        return selectSql.toString();
    }

    private static void genJoinSql(String mainTable, String aliasWithColumn, Set<String> joinSqls) {
        System.out.println("=====aliasWithColumn:"+aliasWithColumn);
        String tables = aliasWithColumn.split("\\.")[0];
        String[] tableArr = tables.split("_");
        System.out.println("=====tableArr.length:"+tableArr.length);
        for (int i = 0; i < tableArr.length; i++) {
            if (StringUtils.equals("t", tableArr[i])) {
                continue;
            }
            String leftTableAlias;
            String leftTableName;
            String rightTableAlias ;
            if (i == 0) {
                leftTableAlias = "t";
                leftTableName = mainTable;
                rightTableAlias = tableArr[i];
            } else {
                leftTableAlias = tableArr[i - 1];
                leftTableName = TableForeignMapping.me().get(mainTable).get(leftTableAlias)[1];
                rightTableAlias = leftTableAlias + "_" + tableArr[i];
            }
            Map<String, String[]> aliasForeignKeyMapping = TableForeignMapping.me().get(leftTableName);
            String[] foreignKeyInfo = aliasForeignKeyMapping.get(tableArr[i]);
            System.out.println("=======aliasForeignKeyMapping:"+aliasForeignKeyMapping+" tableArr[i]:"+tableArr[i]);
            System.out.println("=======rightTableAlias:"+rightTableAlias+ " tableForeignKeyMap.get(leftTable):"+TableForeignMapping.me().get(leftTableName) + " foreignKeyInfo:" + foreignKeyInfo);
            String rightTableName = foreignKeyInfo[1];
            String leftColumn = foreignKeyInfo[0];
            String rightColumn = foreignKeyInfo[2];
            String left = leftTableAlias + "." + leftColumn;
            String right = rightTableAlias + "." + rightColumn;
            
            String joinSql = " left join " + rightTableName + " " + rightTableAlias + " on " + left + "=" + right;
            
            joinSqls.add(joinSql);
            System.out.println("joinSql:"+joinSql);
        }
        
    }
}
