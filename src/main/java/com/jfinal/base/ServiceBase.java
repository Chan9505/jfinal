package com.jfinal.base;
/**
 * @author lee
 * @date 2017年9月27日 下午5:01:19
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * @Title: ServiceBase.java 
 * @Description: 服务类基础模版，包含一些基础方法
 * @author lee  
 * @date 2017年6月28日 下午8:35:04
 */
public abstract class ServiceBase<T extends Model<T>> {
    public String getTableName() {
        return TableMapping.me().getTable(getDao().getClass()).getName();
    }

    public abstract T getDao();
    
    /**
     * 根据id查找对象
     * @param id
     * @return
     */
    public T findById(Long id) {
    		return getDao().findById(id);
    }
    
    /**
     * 根据id删除对象
     * @param id
     * @return
     */
    public boolean deleteById(Long id) {
    		return getDao().deleteById(id);
    }

    private final static Map<String, String> restrictionMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("eq", " ='v' ");
            put("lt", " < 'v' ");
            put("le", " <= 'v' ");
            put("gt", " > 'v' ");
            put("ge", " >= 'v' ");
            put("nn", " is not null ");
            put("n", " is null ");
            put("lk", " like '%v%' ");
            put("in", " in (v) ");
        }
    };
    
	/**
	 * @Title: save 
	 * @Description: 创建对象 
	 * @param object
	 * @return
	 */
	public boolean save(T object) {
		return object.save();
	}
	
	/**
	 * @Title: batchSave 
	 * @Description: 批量保存对象 
	 * @param objects
	 * @return
	 */
	public int[] batchSave(List<T> objects) {
		return Db.batchSave(objects, 500);
	}
	
	/**
	 * @Title: update 
	 * @Description: 修改 
	 * @param object
	 * @return
	 */
	public boolean update(T object) {
		return object.update();
	}
	
	/**
	 * @Title: batchUpdate 
	 * @Description: 批量修改 
	 * @param objects
	 * @return
	 */
	public int[] batchUpdate(List<T> objects) {
		return Db.batchUpdate(objects, 500);
	}
	
	/**
	 * @Title: delete 
	 * @Description: 删除对象
	 * @param object
	 * @return
	 */
	public boolean delete(T object) {
		return object.delete();
	}

    public List<T> findLoadColumns(String columns, String propertyName, Object value) {
        String selectSql = "select " + columns + " from " + getTableName() + " t";
        return find(selectSql, propertyName, value);
    }

    public List<T> find(String propertyName, Object value) {
        String selectSql = "select * from " + getTableName() + " t";
        return find(selectSql, propertyName, value);
    }
    
    public List<T> find(String selectSql, String propertyName, Object value) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(propertyName) && value != null) {
            if (value instanceof String) {
                params.put(propertyName, "'" + value + "'");
            } else {
                params.put(propertyName, value);
            }
        }
        return find(selectSql, params);
    }

    public List<T> find(Map<String, Object> params) {
        String selectSql = "select * from " + getTableName() + " t";
        return find(selectSql, params, "and");
    }
    
    public List<T> findLoadColumns(String columns, Map<String, Object> params, String orderBy) {
        String sqlColumns = StringUtils.defaultString(columns, "*");
        String selectSql = "select " + sqlColumns + " from " + getTableName() + " t";
        return find(selectSql, params, "and", orderBy);
    }
    
    public List<T> find(String selectSql, Map<String, Object> params) {
        return find(selectSql, params, "and");
    }

    public List<T> find(String selectSql, Map<String, Object> params, String operator) {
        return find(selectSql, params, operator, "");
    }

    public List<T> find(String selectSql, Map<String, Object> params, String operator, String orderBy) {
        String fullSql = constructFullSql(selectSql, params, operator, orderBy);
        return getDao().find(fullSql);
    }

    public Page<T> pageLoadColumns(String columns, Map<String, Object> params, String operator, String orderBy,
                    int pageNumber, int pageSize) {
        String sqlColumns = StringUtils.defaultString(columns, "*");
        String selectSql = "select " + sqlColumns + " from " + getTableName() + " t";
        return page(selectSql, params, operator, orderBy, pageNumber, pageSize);
    }
    
    public Page<T> page(String selectSql, Map<String, Object> params, String operator, String orderBy,
                    int pageNumber, int pageSize) {
        String fullSelectSql = constructFullSql(selectSql, params, operator, orderBy);
        String totalRowSelectSql = "select count(1) from " + getTableName() + " t";
        String totalRowFullSql =  constructFullSql(totalRowSelectSql, params, operator, orderBy);
        return getDao().paginateByFullSql(pageNumber, pageSize, totalRowFullSql, fullSelectSql);
    }
    
	public Page<T> page(String selectSql, String totalRowSelectSql, Map<String, Object> params, String operator,
			String orderBy, int pageNumber, int pageSize) {
		String fullSelectSql = constructFullSql(selectSql, params, operator, orderBy);
		String totalRowFullSql = constructFullSql(totalRowSelectSql, params, operator, orderBy);
		return getDao().paginateByFullSql(pageNumber, pageSize, totalRowFullSql, fullSelectSql);
	}
    
    private String constructFullSql(String selectSql, Map<String, Object> params, String operator,
                    String orderBy) {
        StringBuilder sb = new StringBuilder(selectSql);
        if (params != null && params.size() > 0) {
            List<String> paramKeys = new ArrayList<>(params.keySet());
            if (selectSql.contains(" where ")) {
				sb.append(" ");
			} else {
				sb.append(" where 1 = 1 ");
			}
            for (int i = 0; i < paramKeys.size(); i++) {
                String key = paramKeys.get(i);
                Object value = params.get(key);
                if (StringUtils.isNotBlank(key) && value != null) {
                    sb.append(" " + operator + " ");
                    sb.append(genSearchCondition(key, "" + value));
                }
            }
        }
        if (StringUtils.isNotBlank(orderBy)) {
        		sb.append(" order by " + StringUtils.defaultString(orderBy, ""));
		}
        System.out.println("=======sb.toString():"+sb.toString());
        return sb.toString();
    }

    public T findFirst(String propertyName, Object value) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(propertyName) && value != null) {
            if (value instanceof String) {
                params.put(propertyName, "'" + value + "'");
            } else {
                params.put(propertyName, value);
            }
        }
        return findFirstLoadColumns("*", params, "");
    }

    public T findFirstLoadColumns(String columns, Map<String, Object> params, String orderBy) {
        String sqlColumns = StringUtils.defaultString(columns, "*");
        String selectSql = "select " + sqlColumns + " from " + getTableName() + " t";
        return findFirstLoadColumns(selectSql, params, "and", orderBy);
    }

    public T findFirstLoadColumns(String selectSql, Map<String, Object> params, String operator, String orderBy) {
        String fullSql = constructFullSql(selectSql, params, operator, orderBy);
        return getDao().findFirst(fullSql);
    }
    

    
    /**
     * The param name can include the tableAlias, column and restrictions，restrictions:=，lk:like,gt:>, lt:<,ge:>=,le:<=,in:in, example: goods.name_lk=phone
     * @param key
     * @param value
     * @return
     */
    private String genSearchCondition(String key, String value) {
        StringBuilder sb = new StringBuilder();
        String columnWithOperator;
        String tableAlias;
        if (StringUtils.contains(key, ".")) {
            tableAlias = key.split("\\.")[0] + ".";
            columnWithOperator = key.split("\\.")[1];
        } else {
            tableAlias = "";
            columnWithOperator = key;
        }

        String[] columnWithOperatorArr = StringUtils.split(columnWithOperator, "_");
        if (columnWithOperatorArr.length == 2 && restrictionMap.keySet().contains(columnWithOperatorArr[1])) {
            sb.append(tableAlias);
            sb.append(columnWithOperatorArr[0]);
            String restriction = restrictionMap.get(columnWithOperatorArr[1]);
            sb.append(StringUtils.replace(restriction, "v", value));
        } else {
            sb.append(tableAlias);
            sb.append("`").append(columnWithOperator);
            sb.append("` = '");
            sb.append(value).append("'");
        } 
        return sb.toString();
    }

    public Record findFirstRecord(String selectSql, Map<String, Object> params, String operator, String orderBy) {
        String fullSql = constructFullSql(selectSql, params, operator, orderBy);
        return Db.findFirst(fullSql);
    }
    
    public List<Record> findRecordList(String selectSql, Map<String, Object> params, String operator, String orderBy) {
        String fullSql = constructFullSql(selectSql, params, operator, orderBy);
        return Db.find(fullSql);
    }
    
    public Page<Record> pageRecords(String selectSql, Map<String, Object> params, String operator, String orderBy,
                    int pageNumber, int pageSize) {                        
        String fullSelectSql = constructFullSql(selectSql, params, operator, orderBy);
        String totalRowSelectSql = "select count(1) from " + getTableName() + " t";
        String totalRowFullSql =  constructFullSql(totalRowSelectSql, params, operator, orderBy);
        return Db.paginateByFullSql(pageNumber, pageSize, totalRowFullSql, fullSelectSql);
    }
    
    public List<Record> findRecordList(String sql, Object... paras) {
        return Db.find(sql, paras);
    }

    public boolean isExist(String sql, Object... paras) {
        Record record = findFirstRecord(sql, paras);
        return record != null;
    }
    
    public Record findFirstRecord(String sql, Object... paras) {
        return Db.findFirst(sql, paras);
    }

}
