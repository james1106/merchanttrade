package ins.platform.aggpay.admin.common.listener;

import ins.platform.aggpay.admin.service.SysZuulRouteService;
import ins.platform.aggpay.common.constant.CommonConstant;
import ins.platform.aggpay.common.entity.SysZuulRoute;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xiaoleilu.hutool.collection.CollUtil;

/**
 * @author lengleng
 * @date 2018/5/16
 */
@Slf4j
@Component
public class RouteConfigInitListener{
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysZuulRouteService sysZuulRouteService;

    /**
     * Callback used to run the bean.
     * 初始化路由配置的数据，避免gateway 依赖业务模块
     *
     */
    @EventListener(value = {EmbeddedServletContainerInitializedEvent.class})
    public void init() {
        log.info("开始初始化路由配置数据");
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        List<SysZuulRoute> routeList = sysZuulRouteService.selectList(wrapper);
        if (CollUtil.isNotEmpty(routeList)) {
            redisTemplate.opsForValue().set(CommonConstant.ROUTE_KEY, routeList);
            log.info("更新Redis中路由配置数据：{}条", routeList.size());
        }
        log.info("初始化路由配置数据完毕");
    }
}