package com.jfinal.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

/**
 * @author lee
 * @date 2017年10月27日 下午5:03:54
 * 
 */
public class JfinalMetaBuilder extends MetaBuilder{
	 protected Set<String> includedTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	public JfinalMetaBuilder(DataSource dataSource) {
		super(dataSource);
	}
	
	public void addIncludedTable(String... includedTables) {
        if (includedTables != null) {
            for (String table : includedTables) {
                this.includedTables.add(table);
            }
        }
    }

    protected void buildTableNames(List<TableMeta> ret) throws SQLException {
        ResultSet rs = getTablesResultSet();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            if (includedTables != null && includedTables.size() != 0 && !includedTables.contains(tableName)) {
                System.out.println("Skip not included table :" + tableName);
                continue ;
            }
            if (excludedTables.contains(tableName)) {
                System.out.println("Skip excluded table :" + tableName);
                continue ;
            }
            if (isSkipTable(tableName)) {
                System.out.println("Skip table :" + tableName);
                continue ;
            }
            
            TableMeta tableMeta = new TableMeta();
            tableMeta.name = tableName;
            tableMeta.remarks = rs.getString("REMARKS");
            
            tableMeta.modelName = buildModelName(tableName);
            tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
            ret.add(tableMeta);
        }
        rs.close();
    }

}
