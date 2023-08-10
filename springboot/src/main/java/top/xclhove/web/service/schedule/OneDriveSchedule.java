package top.xclhove.web.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.xclhove.web.service.impl.OneDriveItemServiceImpl;
import top.xclhove.web.service.impl.OneDriveServiceImpl;

@Configuration
@EnableScheduling
@Slf4j
public class OneDriveSchedule {
    @Autowired
    private OneDriveServiceImpl oneDriveService;
    @Autowired
    private OneDriveItemServiceImpl oneDriveItemService;

    /**
     * 项目启动 1 分钟后, 每 50 分钟执行一次刷新 OneDrive access_token 的定时任务.
     * 项目启动 1 分钟后, 每 50 分钟执行一次刷新所有 OneDriveItemDTO 的定时任务.
     */
    @Scheduled(fixedRate = 1000 * 60 * 50, initialDelay = 1000 * 60)
    private void refreshOneDriveAccessToken() {
        log.info("执行刷新 OneDrive access_token 的定时任务");
        oneDriveService.refreshAllOneDriveAccessToken();
        log.info("执行刷新所有 OneDriveItemDTO 的定时任务");
        oneDriveItemService.getAllItemInfo();

    }
}
