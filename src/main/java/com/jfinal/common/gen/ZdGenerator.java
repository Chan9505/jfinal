package com.jfinal.common.gen;

import java.util.List;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

/**
 * 继承Jfinal generator，可以生成Service和Controller
 * @author lee
 *
 */
public class ZdGenerator extends Generator{
    protected ServiceGenerator serviceGenerator;
    protected ControllerGenerator controllerGenerator;
    
    /**
     * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
     * @param dataSource 数据源
     * @param baseModelPackageName base model 包名
     * @param baseModelOutputDir base mode 输出目录
     * @param modelPackageName model 包名
     * @param modelOutputDir model 输出目录
     */
    public ZdGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
        super(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir), new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir));
    }
    
    /**
     * 构造 Generator，只生成 baseModel
     * @param dataSource 数据源
     * @param baseModelPackageName base model 包名
     * @param baseModelOutputDir base mode 输出目录
     */
    public ZdGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
        super(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir));
    }

    /**
     * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
     * @param dataSource 数据源
     * @param baseModelPackageName base model 包名
     * @param baseModelOutputDir base mode 输出目录
     * @param modelPackageName model 包名
     * @param modelOutputDir model 输出目录
     * @param servicePackageName service 包名
     * @param serviceOutputDir service 输出目录
     */
    public ZdGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir,
                    String servicePackageName, String serviceOutputDir) {
        this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir),
                        new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir),
                        new ServiceGenerator(servicePackageName, modelPackageName, serviceOutputDir),
                        null);
    }

    /**
     * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
     * @param dataSource 数据源
     * @param baseModelPackageName base model 包名
     * @param baseModelOutputDir base mode 输出目录
     * @param modelPackageName model 包名
     * @param modelOutputDir model 输出目录
     * @param servicePackageName service 包名
     * @param serviceOutputDir service 输出目录
     * @param controllerPackageName controller 包名
     * @param controllerOutputDir controller 输出目录
     */
    public ZdGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir,
                    String servicePackageName, String serviceOutputDir, String controllerPackageName, String controllerOutputDir) {
        this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir),
                        new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir),
                        new ServiceGenerator(servicePackageName, modelPackageName, serviceOutputDir),
                        new ControllerGenerator(modelPackageName, servicePackageName, controllerPackageName, controllerOutputDir));
    }
    
    /**
     * 使用指定 BaseModelGenerator、ModelGenerator 构造 Generator 
     * 生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
     */
    public ZdGenerator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator,
                    ServiceGenerator serviceGenerator, ControllerGenerator controllerGenerator) {
        super(dataSource, baseModelGenerator, modelGenerator);
        this.serviceGenerator = serviceGenerator;
        this.controllerGenerator = controllerGenerator;
    }

    public void generate() {
        if (dialect != null) {
            metaBuilder.setDialect(dialect);
        }
        
        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = metaBuilder.build();
        if (tableMetas.size() == 0) {
            System.out.println("TableMeta 数量为 0，不生成任何文件");
            return ;
        }
        
        baseModelGenerator.generate(tableMetas);
        
        if (modelGenerator != null) {
            modelGenerator.generate(tableMetas);
        }
        
        if (mappingKitGenerator != null) {
            mappingKitGenerator.generate(tableMetas);
        }
        
        if (dataDictionaryGenerator != null && generateDataDictionary) {
            dataDictionaryGenerator.generate(tableMetas);
        }
        if (serviceGenerator != null) {
            serviceGenerator.generate(tableMetas);
        }
        if (controllerGenerator != null) {
            controllerGenerator.generate(tableMetas);
        }
        long usedTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Generate complete in " + usedTime + " seconds.");
    }
}
