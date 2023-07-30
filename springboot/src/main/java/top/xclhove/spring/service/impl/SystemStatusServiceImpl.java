package top.xclhove.spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xclhove.spring.common.Result;
import top.xclhove.spring.entity.SystemStatus;
import top.xclhove.spring.mapper.SystemStatusMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemStatusServiceImpl extends ServiceImpl<SystemStatusMapper, SystemStatus> {
    @Autowired
    private SystemStatusMapper systemStatusMapper;

    public Result getSystemStatus() {
        Map<String, Object> data = new HashMap<>();
        List<SystemStatus> systemStatuses = systemStatusMapper.selectList(null);
        if (systemStatuses.size() == 0) {
            SystemStatus systemInstalled = new SystemStatus(null, "installed", 0);
            this.save(systemInstalled);
            systemStatuses = systemStatusMapper.selectList(null);
        }
        data.put("systemStatuses", systemStatuses);
        return Result.success(data);
    }

    public boolean updateSystemStatus(SystemStatus systemStatus) {
        UpdateWrapper<SystemStatus> updateWrapperSS;

        updateWrapperSS = new UpdateWrapper<>();
        updateWrapperSS.lambda()
                .set(SystemStatus::getValue, systemStatus.getValue())
                .eq(SystemStatus::getName, systemStatus.getName());
        if (!this.update(updateWrapperSS)) {
            this.save(systemStatus);
        }

        return true;
    }
}
