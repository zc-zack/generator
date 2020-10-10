package com.zkys.generator.config;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import lombok.Data;

/**
 * com.zkys.generator.config
 *
 * @author zhangc
 * @version 1.0
 * @create 2020/10/9 10:55
 */
@Data
public class MysqlStrategyConfig extends StrategyConfig {

    private boolean controllerImpl = false;

    private boolean mapper = false;
}
