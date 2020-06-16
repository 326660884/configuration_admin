package cn.cnic.security.configuration.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cnic.security.common.utils.PageUtils;
import cn.cnic.security.common.utils.Query;

import cn.cnic.security.configuration.dao.ComparingChartDao;
import cn.cnic.security.configuration.entity.ComparingChartEntity;
import cn.cnic.security.configuration.service.ComparingChartService;


@Service("comparingChartService")
public class ComparingChartServiceImpl extends ServiceImpl<ComparingChartDao, ComparingChartEntity> implements ComparingChartService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ComparingChartEntity> page = this.page(
                new Query<ComparingChartEntity>().getPage(params),
                new QueryWrapper<ComparingChartEntity>()
        );

        return new PageUtils(page);
    }

}