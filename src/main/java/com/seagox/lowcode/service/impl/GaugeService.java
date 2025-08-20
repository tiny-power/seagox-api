package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Gauge;
import com.seagox.lowcode.mapper.GaugeMapper;
import com.seagox.lowcode.service.IGaugeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GaugeService implements IGaugeService {

    @Autowired
    private GaugeMapper gaugeMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String name) {
    	PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<Gauge> qw = new LambdaQueryWrapper<>();
        qw.eq(Gauge::getCompanyId, companyId)
        .like(!StringUtils.isEmpty(name), Gauge::getName, name)
        .orderByDesc(Gauge::getCreateTime);
        List<Gauge> list = gaugeMapper.selectList(qw);
        PageInfo<Gauge> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    @Override
    public ResultData insert(Gauge gauge) {
    	String script = "export function mounted() {\r\n" +
    			"  console.log(`mounted`)\r\n" +
    			"}";
    	gauge.setScript(script);
        gaugeMapper.insert(gauge);
        return ResultData.success(null);
    }

    @Override
    public ResultData update(Gauge gauge) {
        gaugeMapper.updateById(gauge);
        return ResultData.success(null);
    }

    @Override
    public ResultData delete(Long id) {
        gaugeMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData queryById(Long id, Long userId) {
        Gauge gauge = gaugeMapper.selectById(id);
        return ResultData.success(gauge);
    }

    @Override
    public ResultData queryByCompanyId(Long companyId) {
        LambdaQueryWrapper<Gauge> qw = new LambdaQueryWrapper<>();
        qw.eq(Gauge::getCompanyId, companyId);
        List<Gauge> list = gaugeMapper.selectList(qw);
        return ResultData.success(list);
    }

    @Override
    public ResultData queryAll() {
        LambdaQueryWrapper<Gauge> qw = new LambdaQueryWrapper<>();
        List<Gauge> list = gaugeMapper.selectList(qw);
        return ResultData.success(list);
    }

	@Override
	public ResultData chartSql(String tableName, String dimension, String metrics, String filterData) {
		List<Map<String,Object>> result = new ArrayList<>();
		String sumSql = "";
		if(StringUtils.isEmpty(dimension)) {
			String[] metricsArray = metrics.split(",");
			Map<String, Object> metricsMap = new HashMap<String, Object>();
			for(int i=0;i<metricsArray.length;i++) {
				String metric = metricsArray[i];
				String name = metric.split("\\|")[0];
				String filed = metric.split("\\|")[1];
				sumSql = sumSql + "sum(" + filed + ") as " + "\"" + filed + "\"" + ",";
				metricsMap.put(filed, name);
			}
			sumSql = sumSql.substring(0, sumSql.length()-1);
			String whereSql = "";
			if(!StringUtils.isEmpty(filterData)) {
				whereSql = whereSql + " where " + filterData;
			}
			String sql =  "select " + sumSql + " from " + tableName + whereSql;
			System.out.println(sql);
			List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
			if (mapList.size() > 0) {
				Map<String, Object> data = mapList.get(0);
				data.forEach((key,value)-> {
					Map<String, Object> item = new HashMap<String, Object>();
		            item.put("name", metricsMap.get(key));
					item.put("value", value);
					result.add(item);
		        });
            }
		} else {
			String[] dimensionArray = dimension.split(",");
			String[] metricsArray = metrics.split(",");
			if(dimensionArray.length == 1 && metricsArray.length == 1) {
				String name = dimensionArray[0].split("\\|")[1];
				String value = metricsArray[0].split("\\|")[1];
				sumSql = sumSql + name + " AS \"name\"," + "sum(" + value + ") AS " + "\"value\"";
				String whereSql = "";
				if(!StringUtils.isEmpty(filterData)) {
					whereSql = whereSql + " where " + filterData;
				}
				String groupSql = " GROUP BY " + name;
				String sql = "select " + sumSql + " from " + tableName + whereSql + groupSql;
				System.out.println(sql);
				return ResultData.success(jdbcTemplate.queryForList(sql));
			}
		}
		return ResultData.success(result);
	}

}
