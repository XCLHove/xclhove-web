package top.xclhove.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xclhove.web.common.Result;
import top.xclhove.web.entity.DTO.Admin;
import top.xclhove.web.entity.SystemConfig;
import top.xclhove.web.entity.SystemStatus;
import top.xclhove.web.mapper.SystemConfigMapper;
import top.xclhove.web.mapper.SystemStatusMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> {
    @Autowired
    private SystemStatusServiceImpl systemStatusServiceImpl;
    @Autowired
    private SystemConfigMapper systemConfigMapper;
    @Autowired
    private SystemStatusMapper systemStatusMapper;

    public Result backLogin(Admin admin) {
        Map<String, Object> data = new HashMap<>();
        QueryWrapper<SystemConfig> queryWrapperSC;
        String adminToken = "";

        queryWrapperSC = new QueryWrapper<>();
        queryWrapperSC.lambda()
                .eq(SystemConfig::getValue, admin.getBackAccount());
        if (!systemConfigMapper.exists(queryWrapperSC)) {
            return Result.error("账号不存在！");
        }

        queryWrapperSC = new QueryWrapper<>();
        queryWrapperSC.lambda()
                .eq(SystemConfig::getValue, admin.getPassword());
        if (!systemConfigMapper.exists(queryWrapperSC)) {
            return Result.error("密码错误！");
        }

        data.put("token", adminToken);
        return Result.success("登录成功！", data);
    }

    public Result systemInit(Admin admin) {
        Map<String, Object> data = new HashMap<>();
        SystemConfig systemConfig = new SystemConfig();
        QueryWrapper<SystemConfig> queryWrapperSC;
        QueryWrapper<SystemStatus> queryWrapperSS;

        queryWrapperSS = new QueryWrapper<>();
        queryWrapperSS.lambda()
                .eq(SystemStatus::getName, "installed");
        List<SystemStatus> systemStatuses = systemStatusMapper.selectList(queryWrapperSS);
        systemStatuses = systemStatuses.stream()
                .filter(status -> status.getName().equals("installed"))
                .collect(Collectors.toList());
        if (systemStatuses.size() > 0 && systemStatuses.get(0).getValue() == 1) {
            data.put("installed", true);
            return Result.success("已初始化!", data);
        }

        queryWrapperSC = new QueryWrapper<>();
        queryWrapperSC.lambda()
                .eq(SystemConfig::getName, "backAccount");
        this.remove(queryWrapperSC);

        queryWrapperSC = new QueryWrapper<>();
        queryWrapperSC.lambda()
                .eq(SystemConfig::getName, "password");
        this.remove(queryWrapperSC);

        systemConfig.setName("backAccount");
        systemConfig.setValue(admin.getBackAccount());
        if (!this.save(systemConfig)) {
            return Result.error("初始化账号失败！");
        }

        systemConfig.setId(null);
        systemConfig.setName("password");
        systemConfig.setValue(admin.getPassword());
        if (!this.save(systemConfig)) {
            return Result.error("初始化密码失败！");
        }

        SystemStatus systemStatus = new SystemStatus(null, "installed", 1);
        boolean updateSuccess = systemStatusServiceImpl.updateSystemStatus(systemStatus);
        if (updateSuccess) {
            data.put("initSuccess", true);
            return Result.success("初始化成功！", data);
        }

        return Result.error("初始化失败！");
    }
}
